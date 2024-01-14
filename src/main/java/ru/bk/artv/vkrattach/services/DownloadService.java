package ru.bk.artv.vkrattach.services;

import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.docx.DocxInserter;
import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.docx.ThemeAttachDocxGenerator;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;

import java.io.*;
import java.util.List;

/**
 * Сервис для генерации Docx файлов
 */
@Service
public class DownloadService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ThemeAttachDocxGenerator themeAttachDocxGenerator;
    private final DocxInserter docxInserter;

    public DownloadService(OrderDao orderDao, UserDao userDao, ThemeAttachDocxGenerator themeAttachDocxGenerator, DocxInserter docxInserter) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.themeAttachDocxGenerator = themeAttachDocxGenerator;
        this.docxInserter = docxInserter;
    }

    /**
     * @param spec спецификации (факультет и прочее), работает как фильтр для заявок, которые необходимо  поместить в
     *             docx-файл
     * @return стрим, отправляемый на клиент контроллером
     * @throws IOException ну бросает и бросает. Пока ничем не обрабатывается
     */
    public ByteArrayInputStream downloadAttachedOrders(Specification<Order> spec) throws IOException {
        List<Order> orders = orderDao.getOrders(spec);
        ByteArrayOutputStream outStream = themeAttachDocxGenerator.generateDocx(orders);
        return new ByteArrayInputStream(outStream.toByteArray());
    }

    @Transactional
    public ByteArrayInputStream downloadReport(Long id) {
        String filePath = getClass().getClassLoader()
                .getResource("report.docx")
                .getPath();

        if (userDao.findUserById(id) instanceof SimpleUser user) {
            var order = orderDao.getOrdersByUser(user).stream().filter(o -> o.getRequestStatus()
                            .equals(Order.RequestStatus.ACCEPTED))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No accepted orders found in user with id : " + id));
            Hibernate.initialize(order.getTheme());
            Hibernate.initialize(order.getUser());
            Hibernate.initialize(order.getLecturer());
            order.setTheme((Theme) Hibernate.unproxy(order.getTheme()));
            order.setLecturer((Lecturer) Hibernate.unproxy(order.getLecturer()));
            order.setUser((SimpleUser) Hibernate.unproxy(order.getUser()));

            try {
                return new ByteArrayInputStream(docxInserter.insertIntoDocx(order, filePath).toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        throw new ResourceNotFoundException("User is not SimpleUser");
    }

    // TODO: 08.12.2023 Добавить генерацию рапортов и прочей макулатуры после тестирования основного функционала
}
