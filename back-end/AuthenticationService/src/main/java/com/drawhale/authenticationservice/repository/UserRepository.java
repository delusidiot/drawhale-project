package com.drawhale.authenticationservice.repository;

import com.drawhale.authenticationservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
