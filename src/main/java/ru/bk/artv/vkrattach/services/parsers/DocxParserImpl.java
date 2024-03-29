package ru.bk.artv.vkrattach.services.parsers;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Парсит Docx в List по абзацам, разделенным знаком параграфа.
 */
@Slf4j
@Service
public class DocxParserImpl implements DocxParser {

    /**
     * @param file Docx файл.
     * @return коллекция, получаемая по абзацам.
     */
    @Override
    public List<String> parseDocx(MultipartFile file) {
        List<String> strings = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {

            XWPFDocument document = new XWPFDocument(file.getInputStream());

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            strings = paragraphs.stream().map(para -> para.getText().trim()).filter(text->text.length()>0).collect(Collectors.toList());
        } catch (IOException | IllegalArgumentException | EncryptedDocumentException e) {
            e.printStackTrace();
            throw new UploadResourceException(e.getMessage(), e);
        }
        return strings;
    }
}

