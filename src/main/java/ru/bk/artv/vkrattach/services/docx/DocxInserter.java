package ru.bk.artv.vkrattach.services.docx;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.DepartmentHeadGetter;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.ConfigDataService;
import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.services.model.Order;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocxInserter {

    //Паттерн ищет содержимое между скобок, но скобки непосредственно не включает
    private final Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
    private final ConfigDataService configDataService;
    private final DepartmentHeadGetter departmentsHeadGetter;


    public DocxInserter(ConfigDataService configDataService, DepartmentHeadGetter departmentsHeadGetter) {
        this.configDataService = configDataService;
        this.departmentsHeadGetter = departmentsHeadGetter;
    }

    /**
     * @param fields    названия полей. Если список включает большеш одного элемента, то первое - это название объекта,
     *                  а далее название String-а
     * @param attribute название аттрибута этого стринга (например, необходимо только label или инициалы для ФИО)
     */
    static record InsertionParameter(String text, String[] fields, Attribute attribute) {

        enum Attribute {
            NONE, LABEL, FIRST;
        }
    }


    /**
     * Метод вставляет в docx документ с помощью Apache POI необходимые данные. Документ должен содержать поля,
     * соответствующие названиям String переменных по отношению к order (если вложенные классы - то через точку).
     * Например {theme.department} вставит содержимое department заместо этого самого {theme.department}. Проверки на
     * null не производится, предполагается, что незаполненные значения содержат "".
     * <p>
     * Для установления содержания полей используется reflection, в связи с чем названия должны совпадать с названием
     * полей. В случае, если необходимо вставить не аббревиатуру, а расшифровку, необходимо в документ вставлять
     * {theme.department:label} через двоеточие. Если первый инициал - {theme.department:firstchar}
     * <p>
     * Любой косяк в исходном документе в classpath может привести к ошибке, при изменении названия полей объектов
     * модельки необходимо вносить соответствующие изменения в docx.
     * <p>
     * Начальник кафедры в необходимых случаях парсится через {$.name} и т.д. и запрашивается из базы данных для конкретной
     * кафедры (если кафедра заполнит в качестве должности
     *
     * @param order    заявка, которую надо заинсертить в docx
     * @param filePath файл, в который нужно заинсертить заявку
     * @return ByteArrayOutputStream, который нужно потом переделать в InputStream и отправить клиенту
     * @throws IOException todo разобраться с этим
     */
    public ByteArrayOutputStream insertIntoDocx(Order order, String filePath) throws IOException {

        try (InputStream inputStream = new FileInputStream(filePath)) {

            XWPFDocument doc = new XWPFDocument(inputStream);
            var text = doc.getParagraphs().stream().map(XWPFParagraph::getText).collect(Collectors.joining());
            log.info("TEXT BEFORE : " + text);

            Map<String, String> replacements = getAllReplacements(text, order);

            doc.getParagraphs().forEach(xwpfParagraph -> {
                mergeRunsBetweenBraces(xwpfParagraph);
                replacements.forEach((key, value) -> replaceTextInParagraph(xwpfParagraph,
                        key, value));
            });

            log.info("TEXT AFTER : " + doc.getParagraphs().stream().map(XWPFParagraph::getText).collect(Collectors.joining()));


            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            doc.write(bs);
            doc.close();
            return bs;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Получает список что и на что заменить исходя из Order, а также Lecturer (который запрашивается из базы
     * для данной кафедры и является ее начальником).
     *
     * @param text  Текст docx файла
     * @param order заявка
     * @return Map со значениями - что заменить / на что заменить
     */
    private Map<String, String> getAllReplacements(String text, Order order) {
        Lecturer departmentHead = departmentsHeadGetter.getDepartmentHead(order.getTheme().getDepartment());
        List<InsertionParameter> insertionParameters = getInsertionParametesList(text);

        log.info("insertionParametersList : " + insertionParameters.toString());

        List<InsertionParameter> insertionParametersForOrders = insertionParameters.stream()
                .filter(insertionParameter -> !insertionParameter.fields[0].equals("$"))
                .toList();

        log.info("insertionParametersForOrders size is : " + insertionParametersForOrders.size());
        //создает реплейсменты для заявки
        Map<String, String> replacements = getInsertionMap(insertionParametersForOrders, order);
        log.info("replacements size after insertionParametersForOrders  is : " + replacements.size());

        List<InsertionParameter> insertionParametersDepartmentHead = insertionParameters.stream()
                .filter(insertionParameter -> insertionParameter.fields[0].equals("$"))
                .map(insertionParameter -> new InsertionParameter(insertionParameter.text,
                        Arrays.copyOfRange(insertionParameter.fields, 1, insertionParameter.fields.length),
                        insertionParameter.attribute))
                .toList();
        log.info("insertionParametersForDepartmentHead size is : " + replacements.size());
        //добавляет реплейсменты для начальника кафедры
        replacements.putAll(getInsertionMap(insertionParametersDepartmentHead, departmentHead));
        return replacements;
    }

    /**
     * Если не объединить {theme.themeName}, то XMPFRuns будут содержать отдельно скобки и прочее друг за другом, соответственно
     * заменить уже не выйдет
     *
     * @param paragraph параграф
     */
    private void mergeRunsBetweenBraces(XWPFParagraph paragraph) {
        String startText = "{";
        String endText = "}";

        List<XWPFRun> runs = paragraph.getRuns();

        runs = new ArrayList<XWPFRun>(runs);

        Iterator<XWPFRun> iterator = runs.iterator();

        while (iterator.hasNext()) {
            XWPFRun run = iterator.next();
            String text = run.getText(0);

            //если текст содержит {
            if (text != null && text.contains(startText)) {
                // мы создаем СБ и добавляем в него текст
                StringBuilder mergedText = new StringBuilder(text);

                // Продолжаем объединение, пока не найдем конечный текст
                while (iterator.hasNext()) {
                    XWPFRun nextRun = iterator.next();
                    String nextText = nextRun.getText(0);
                                        //добавляем этот текст в mergedText
                    mergedText.append(nextText);
                    //если просто удалять, ничего не будет, придется все перезапихивать в параграф
                    nextRun.setText("", 0);
                    //Проверяем на наличие } и на отсутствие {. Если да, то прерываемся, если нет, то едем дальше
                    if (nextText != null && nextText.contains(endText) && !nextText.contains(startText)) {
                        //проверяем nextText на налилчие {
                        // Объединяем текст между { и }
                        run.setText(mergedText.toString(), 0);
                        break; // и прерываем цикл
                    }
                }
            }
        }
        log.info(runs.stream().map(xwpfRun -> xwpfRun.getText(0)).collect(Collectors.joining("|")));
//        log.info(paragraph.getRuns().stream().map(xwpfRun -> xwpfRun.getText(0)).collect(Collectors.joining("|")));
    }

    /**
     * Метод сперва извлекает список данных, находящийся между {} скобками, потом преобразует такие данные к
     * InsertionParameter.
     *
     * @param text Текст, который необходимо проверить на наличие встраиваемых параметров
     * @return список встраиваемых параметров (InsertionParameters)
     */
    private List<InsertionParameter> getInsertionParametesList(String text) {
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        List<String> list = pattern.matcher(text).results().map(matchResult -> matchResult.group()).toList();

        List<InsertionParameter> insertionParameters = list.stream().map(param -> {
            String[] split = param.split("\\."); //разделяем параметр на те, которые разделяются точками (или не разделяются)
            InsertionParameter.Attribute attribute = InsertionParameter.Attribute.NONE;
            int length = split.length;
            String[] splitWithAttr = split[length - 1].split("\\:"); //проверяем, имеется ли у последнего параметра аттрибут
            if (splitWithAttr.length > 1) {
                attribute = Enum.valueOf(InsertionParameter.Attribute.class, splitWithAttr[1].toUpperCase());
                split[length - 1] = splitWithAttr[0];
            }
            return new InsertionParameter(param, split, attribute); //сохраняем в InsertionParameter
        }).toList();

        log.debug(String.join("|", list));

        return insertionParameters;
    }

    /*Получение списка что заменять - на что заменять из объекта*/
    private Map<String, String> getInsertionMap(List<InsertionParameter> objectList, Object obj) {
        Map<String, String> insertionMap = new HashMap<>();

        List<Field> list = Arrays.stream(obj.getClass().getDeclaredFields()).toList();
        objectList.forEach(insertionParameter -> {
            String simpleInsertion = getSimpleInsertion(insertionParameter, list, obj);
            log.info("Adding to map key insertionParameter.text : {" + insertionParameter.text + "}" +
                    " and value : " + simpleInsertion);
            insertionMap.put("{" + insertionParameter.text + "}", simpleInsertion);
        });
        return insertionMap;
    }

    /*Получение одной замены из объекта*/
    private String getSimpleInsertion(InsertionParameter insertionParameter, List<Field> list, Object object) {
        String simpleInsertion = "";
        List<Field> insList = list;
        log.info("fields in object are : " + insList.stream().map(field -> field.getName()).collect(Collectors.joining("|")));
        Object obj = object;
        try {
            for (String fieldName : insertionParameter.fields) {
                log.info("Field name is : " + fieldName + " and obj.class is : " + obj.getClass().getSimpleName());
                Field insField = insList.stream().filter(field -> field.getName().equals(fieldName))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("No such field found : "
                                + fieldName));
                Class<?> fieldType = insField.getType();
                if (String.class.isAssignableFrom(fieldType)) {
                    insField.setAccessible(true);
                    simpleInsertion = insField.get(obj).toString();
                    insField.setAccessible(false);
                    break;
                } else {
                    insField.setAccessible(true);
                    obj = insField.get(obj);
                    insList = Arrays.asList(obj.getClass().getDeclaredFields());
                    insField.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException(e.getMessage(), e);
        }
        simpleInsertion = checkInsertionAttributes(insertionParameter, simpleInsertion);
        log.info("111");
        return simpleInsertion;
    }

    /*Проверка на наличие атрибутов после : . Возвращает либо замену, либо измененную замену в случае
     * наличия атрибутов отличных от NONE*/
    private String checkInsertionAttributes(InsertionParameter insertionParameter, String simpleInsertion) {
        simpleInsertion = switch (insertionParameter.attribute) {
            case NONE -> simpleInsertion;
            case LABEL -> configDataService.getDataLabel(insertionParameter
                    .fields[insertionParameter.fields.length - 1], simpleInsertion);
            case FIRST -> String.valueOf(simpleInsertion.charAt(0));
        };
        return simpleInsertion;
    }

    /*Непосредственно замена текста в параграфе*/
    private void replaceTextInParagraph(XWPFParagraph paragraph, String originalText, String updatedText) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            String text = run.getText(0);
            if (text != null && text.contains(originalText)) {
                String updatedRunText = text.replace(originalText, updatedText);
                run.setText(updatedRunText, 0);
            }
        }
    }

    /*Пока не удалять, замена текста во всем документе*/
    private void replaceText(XWPFDocument document, Map<String, String> replacements) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                log.info("BEFORE" + run.getText(0));
                String text = run.getText(0);
                if (text != null) {
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        text = text.replace(entry.getKey(), entry.getValue());
                    }
                    run.setText(text, 0);
                }
                log.info("AFTER" + run.getText(0));
            }
        }
    }
}
