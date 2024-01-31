package ru.bk.artv.vkrattach.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.bk.artv.vkrattach.services.model.ConfigData;

@Data
public class ConfigDataDto {

    public interface ValidationId {
        //validation marker interface
    }

    @NotNull(message = "id не может быть null", groups = {ValidationId.class})
    private Long id;
    @NotNull(message = "type не может быть null")
    private ConfigData.ConfigType type;
    @NotBlank(message = "value не может быть пустым")
    private String value;
    private String label = "";
}
