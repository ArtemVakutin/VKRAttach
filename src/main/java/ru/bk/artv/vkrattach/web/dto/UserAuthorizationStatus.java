package ru.bk.artv.vkrattach.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorizationStatus {
    boolean userAuthorized;
}
