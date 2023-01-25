package ru.bk.artv.vkrattach.business;

import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.DomainDao;

@Service
public class DomainService {

    DomainDao domainDao;

    public DomainService(DomainDao domainDao) {
        this.domainDao = domainDao;
    }


}
