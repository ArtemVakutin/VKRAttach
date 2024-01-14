package ru.bk.artv.vkrattach.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UploadAnswer<T> {
    private String dataId;
    private List<T> objects = new ArrayList<>();
    private List<T> errorObjects = new ArrayList<>();
}
