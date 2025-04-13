package com.david.auth_layer_architecture.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_token")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token_id", unique = true, nullable = false)
    private String accessTokenId;

    @ManyToOne(targetEntity = Credential.class)
    @JoinColumn(name = "credential_id", referencedColumnName = "id", nullable = false)
    private Credential credential;

    @ManyToOne(targetEntity = TypeToken.class)
    @JoinColumn(name = "type_token_id", referencedColumnName = "id", nullable = false)
    private TypeToken typeToken;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
}
