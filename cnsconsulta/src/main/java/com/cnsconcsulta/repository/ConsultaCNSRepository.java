package com.cnsconcsulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnsconcsulta.model.ConsultaCNS;


@Repository
public interface ConsultaCNSRepository extends JpaRepository<ConsultaCNS, Long> {

}
