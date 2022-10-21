package com.consultacns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.consultacns.model.Municipios;

@Repository
public interface MunicipiosRepository extends JpaRepository<Municipios, Long> {
	
	
	@Query(value = "SELECT m FROM Municipios m WHERE m.codigo LIKE %?1%")
	Municipios pegaMunicipio(String string);

}
