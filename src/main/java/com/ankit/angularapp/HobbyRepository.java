package com.ankit.angularapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface HobbyRepository extends JpaRepository<Hobby, Long>{
	@Modifying
	@Query(" delete from Hobby hobby where hobby.user.id=:id ")
	void deleteHobbiesByUserId(@Param("id") Long id);
}
