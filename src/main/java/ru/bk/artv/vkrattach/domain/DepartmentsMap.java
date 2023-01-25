package ru.bk.artv.vkrattach.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class DepartmentsMap {
    final Map<String, String> departments;
}
