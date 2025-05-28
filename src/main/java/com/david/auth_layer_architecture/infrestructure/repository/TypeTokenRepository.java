package com.david.auth_layer_architecture.infrestructure.repository;

import com.david.auth_layer_architecture.domain.entity.TypeToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeTokenRepository extends CrudRepository<TypeToken, Long> {
    TypeToken findByType(String type);
}
