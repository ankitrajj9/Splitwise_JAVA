package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ankit.angularapp.User;

public interface UserRepository extends JpaRepository<User, Long>{
	@Query("SELECT u FROM User u WHERE u.email = :email ")
    public User getStudentByMailId(@Param("email") String email);
	User findOneByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.cstatus=1 and (u.email like %:param% or u.name like %:param%) ")
	public List<User> searchUser(@Param("param") String param);
}

