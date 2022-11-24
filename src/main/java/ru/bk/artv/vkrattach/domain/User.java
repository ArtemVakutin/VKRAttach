package ru.bk.artv.vkrattach.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "vkr_user")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_surname")
    private String surname;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_patronymic")
    private String patronymic;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "user_telephone")
    private String telephone;

    @Column(name = "user_faculty")
    private String faculty;

    @Column(name = "user_group")
    private String group;

    @Column(name = "user_year_of_recruitment")
    private String yearOfRecruitment;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + role.name());
        return new ArrayList<SimpleGrantedAuthority>(Arrays.asList(sga));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    //проставил тру на всякий случай
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

