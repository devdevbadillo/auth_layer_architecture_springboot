package com.david.auth_layer_architecture.infrestructure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.david.auth_layer_architecture.domain.entity.Credential;

@Repository
public interface CredentialRepository extends CrudRepository<Credential, Long>{
    Credential getCredentialByEmail(String email);
}
