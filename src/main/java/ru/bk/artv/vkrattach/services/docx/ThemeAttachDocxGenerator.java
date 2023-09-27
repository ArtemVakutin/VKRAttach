package ru.bk.artv.vkrattach.services.docx;

import lombok.AllArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.domain.ConfigData;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.services.ConfigDataService;
import ru.bk.artv.vkrattach.services.comparators.OrdersComparatorByDepartmentLecturerTheme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ThemeAttachDocxGenerator {

    ConfigDataService dataService;

    public ByteArrayOutputStream generateDocx(List<Order> orders) throws IOException {
        List<Order> noLecturers;
        List<Order> sortedOrders;
        Set<String> departments;

        noLecturers = orders.stream()
                .filter(order -> order.getLecturer() == null)
                .collect(Collectors.toList());

        orders.removeAll(noLecturers);

        sortedOrders = orders.stream()
                .sorted(new OrdersComparatorByDepartmentLecturerTheme())
                .collect(Collectors.toList());

        departments = sortedOrders.stream()
                .map(order -> order.getTheme().getDepartment())
                .collect(Collectors.toSet());


        XWPFDocument document = new XWPFDocument();

        departments.forEach(dep -> generateParagrathByDepartment(document, sortedOrders.stream()
                .filter(order -> order.getTheme().getDepartment().equals(dep))
                .collect(Collectors.toList())));


        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        document.write(bs);
        return bs;
    }

    private void generateParagrathByDepartment(XWPFDocument document, List<Order> orders) {
        generateTitleText(document.createParagraph(), orders.get(0));
        orders.forEach(order -> {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(14);
            run.setText(generateTextForOrder(order));
        });
    }

    private String generateTextForOrder(Order order) {
        SimpleUser user = order.getUser();
        Lecturer lecturer = order.getLecturer();
        Theme theme = order.getTheme();

        StringBuilder sb = new StringBuilder();
        sb.append(firstCharToUpperCase(user.getRank()) + " " + user.getRankType() + " ")
                .append(getSurnameWithIniziales(user) + ", ")
                .append(order.getUser().getPosition() + " " + order.getUser().getFaculty() + "-" + order.getUser().getGroup())
                .append(" учебной группы, " + "тема \"")
                .append(theme.getThemeName() + "\", ")
                .append("руководитель " + lecturer.getPosition() + " кафедры ")
                .append(dataService.getDataLabel(ConfigData.ConfigType.DEPARTMENT, lecturer.getDepartment()) + " ")
                .append(getSurnameWithIniziales(lecturer) + ", ")
                .append(dataService.getDataLabel(ConfigData.ConfigType.ACADEMIC_DEGREE, lecturer.getAcademicDegree()))
                .append(", ")
                .append(dataService.getDataLabel(ConfigData.ConfigType.ACADEMIC_TITLE, lecturer.getAcademicTitle()))
                .append(".");
        String text = sb.toString();
        return text.replaceAll("( , )|( {2})", " ").replaceAll(", \\.", ".").trim();
    }

    private String getSurnameWithIniziales(SimpleUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getSurname()).append(" ");
        sb.append(getFirstCharInUpperCase(user.getName())).append(".");
        if (!user.getPatronymic().isBlank()) {
            sb.append(getFirstCharInUpperCase(user.getPatronymic())).append(".");
        }
        return sb.toString();
    }

    private String getSurnameWithIniziales(Lecturer user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getSurname()).append(" ");
        sb.append(getFirstCharInUpperCase(user.getName())).append(".");
        if (!user.getPatronymic().isBlank()) {
            sb.append(getFirstCharInUpperCase(user.getPatronymic())).append(".");
        }
        return sb.toString();
    }

    private String getFirstCharInUpperCase(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }
        return text.substring(0, 1).toUpperCase();
    }

    private String firstCharToUpperCase(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);

    }

    private void generateTitleText(XWPFParagraph title, Order order) {
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setBold(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(14);
        titleRun.setText("Кафедра " + dataService
                .getDataLabel(ConfigData.ConfigType.DEPARTMENT, order.getTheme().getDepartment()));
        titleRun.addBreak();
    }
}
