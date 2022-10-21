package com.consultacns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.consultacns.model.Raca;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Long> {

	@Query(value = "SELECT r FROM Raca r WHERE r.code = ?1")
	Raca pegaRaca(String string);

}
