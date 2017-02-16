package com.justice.repository;

import com.justice.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aanu.oyeyemi on 1/4/17.
 * Project name -> demojustice
 */
public interface UserRepository extends CrudRepository<User,Long> {
}
