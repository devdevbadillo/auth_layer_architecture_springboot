package com.david.auth_layer_architecture.business.service.implementation;

import com.david.auth_layer_architecture.business.service.interfaces.ITypeTokenService;
import com.david.auth_layer_architecture.domain.entity.TypeToken;
import org.springframework.stereotype.Service;
import com.david.auth_layer_architecture.persistence.TypeTokenRepository;

@Service
public class TypeTokenServiceImpl implements ITypeTokenService {

    private final TypeTokenRepository typeTokenRepository;

    public TypeTokenServiceImpl(TypeTokenRepository typeTokenRepository) {
        this.typeTokenRepository = typeTokenRepository;
    }

    @Override
    public TypeToken getTypeToken(String typeToken) {
        return this.typeTokenRepository.findByType(typeToken);
    }
}
