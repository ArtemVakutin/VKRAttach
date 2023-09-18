package ru.bk.artv.vkrattach.services.parsers;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DocxParser {

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

