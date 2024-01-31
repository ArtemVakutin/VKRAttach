package ru.bk.artv.vkrattach.services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Конфигурационная единица для отправки существующей модели на клиент. Впоследствие помещается в List-ы по ConfigType,
 * которые в свою очередь помещаются в Map и отправляются на клиент в качестве изначальной конфигурации модели.
 * Хранится в таблице vkr_config_data
 */
@Data
@Entity
@Table(name = "vkr_config_data")
public class ConfigData {


    /**
     * айдишник
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
//    @JsonIgnore
    private Long id;

    /**
     * тип данных
     */
    @Column(name = "config_type")
    @Enumerated(EnumType.STRING)
    private ConfigType type;

    /**
     * аббревиатура (например "НБФЗОП" - для хранения в обычных дата-объектах)
     */
    @Column(name = "config_value")
    private String value;

    /**
     * подпись (расшифровка) типа "40.05.01 Национальная безопасность
     */
    @Column(name = "config_label")
    private String label;

    /**
     * Виды значений, хранятся в отдельной таблице vkr_config_data
     */
    public enum ConfigType {
        DEPARTMENT,
        FACULTY,
        YEAR,
        RANK,
        LECTURER_RANK,
        RANK_TYPE,
        ACADEMIC_DEGREE,
        ACADEMIC_TITLE,
        LECTURER_POSITION,
        USER_POSITION
    }
}
