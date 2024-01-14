package ru.bk.artv.vkrattach.config.security;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "vkr_deactivated_token")
public class DeactivatedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private UUID tokenId;
    @Column(name = "token_keep_until")
    private Date tokenKeepUntil;

    public DeactivatedToken() {
    }

    public DeactivatedToken(UUID tokenId, Date tokenKeepUntil) {
        this.tokenId = tokenId;
        this.tokenKeepUntil = tokenKeepUntil;
    }
}
