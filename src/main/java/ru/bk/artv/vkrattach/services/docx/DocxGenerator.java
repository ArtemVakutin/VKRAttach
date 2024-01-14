package ru.bk.artv.vkrattach.services.docx;

import ru.bk.artv.vkrattach.services.model.Order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface DocxGenerator<T> {

    ByteArrayOutputStream generateDocx(T obj) throws IOException;

}
