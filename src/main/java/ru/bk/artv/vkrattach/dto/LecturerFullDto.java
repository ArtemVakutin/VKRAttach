package ru.bk.artv.vkrattach.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerFullDto extends LecturerDto {

    private String email;

    private String telephone;

    private String academicDegree;

    private String academicTitle;

    private String rank;

    private String exception;

    //В случае, если запрос приходит с указанием факультета и года набора
    private int ordersCount;
}

