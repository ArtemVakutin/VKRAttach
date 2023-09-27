package ru.bk.artv.vkrattach.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vkr_config_data")
public class ConfigData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    @JsonIgnore
    private Long id;

    @Column(name = "config_type")
    @Enumerated(EnumType.STRING)
    private ConfigType type;

    @Column(name = "config_value")
    private String value;

    @Column(name = "config_label")
    private String label;

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
