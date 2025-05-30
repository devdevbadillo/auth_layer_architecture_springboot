package com.david.auth_layer_architecture.business.services.impl.domain;

import com.david.auth_layer_architecture.business.services.interfaces.domain.ITypeTokenService;
import com.david.auth_layer_architecture.domain.entity.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.david.auth_layer_architecture.infrestructure.repository.TypeTokenRepository;

@AllArgsConstructor
@Service
public class TypeTokenServiceImpl implements ITypeTokenService {

    private final TypeTokenRepository typeTokenRepository;

    @Override
    public TypeToken getTypeToken(String typeToken) {
        return this.typeTokenRepository.findByType(typeToken);
    }
}
