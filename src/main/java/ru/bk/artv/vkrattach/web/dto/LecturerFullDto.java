package ru.bk.artv.vkrattach.web.dto;

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

    private String rankType;

    private String exception;

    private String position;

    //В случае, если запрос приходит с указанием факультета и года набора
    private int ordersCount;


}

