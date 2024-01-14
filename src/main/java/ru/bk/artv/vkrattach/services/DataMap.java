package ru.bk.artv.vkrattach.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;


/** используется для временного хранения загружаемых коллекций. На выходе требует приведение класса.
 *  в качестве айдишки спользует LocalDateTime.now
 */

@Service
public class DataMap extends ConcurrentHashMap<String, Object> {
    public synchronized String putData(Object object) {
        String time = LocalDateTime.now().toString();
        put(time, object);
        return time;
    }

    // TODO: 08.12.2023 Сделать, чтобы раз в сутки все это дело чистилось, если айдишка по времени отстает на 10 минут
}
