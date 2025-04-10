package com.david.auth_layer_architecture.persistence;

import com.david.auth_layer_architecture.domain.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findRefreshTokenByRefreshTokenId(String refreshTokenId);

    RefreshToken findRefreshTokenByAccessTokenId(Long accessToken_id);

}
