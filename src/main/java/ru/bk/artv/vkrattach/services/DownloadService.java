package ru.bk.artv.vkrattach.services;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.services.docx.ThemeAttachDocxGenerator;

import java.io.*;
import java.util.List;

@Service
@AllArgsConstructor
public class DownloadService {

    OrderDao orderDao;
    ThemeAttachDocxGenerator themeAttachDocxGenerator;

    public ByteArrayInputStream downloadAttachedOrders(Specification<Order> spec) throws IOException {
        List<Order> orders = orderDao.getOrders(spec);
        ByteArrayOutputStream outStream = themeAttachDocxGenerator.generateDocx(orders);
        return new ByteArrayInputStream(outStream.toByteArray());
    }
}
