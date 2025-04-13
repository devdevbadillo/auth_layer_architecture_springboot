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
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = AccessToken.class, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "access_token_id", referencedColumnName = "id", nullable = false)
    private AccessToken accessToken;

    @Column(name = "refresh_token_id", unique = true, nullable = false)
    private String refreshTokenId;

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
