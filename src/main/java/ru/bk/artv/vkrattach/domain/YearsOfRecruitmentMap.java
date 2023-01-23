package ru.bk.artv.vkrattach.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class YearsOfRecruitment {
    Map<String, String> years;
}
