package com.uniovi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.Mark;

/**
 * CrudRepository<Clase Entidad, Clase de la clave primaria>
 * 
 * @author igm1990
 *
 */
public interface MarksRepository extends CrudRepository<Mark, Long> {
}
