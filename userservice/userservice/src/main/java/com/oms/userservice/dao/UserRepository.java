package com.oms.userservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oms.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
