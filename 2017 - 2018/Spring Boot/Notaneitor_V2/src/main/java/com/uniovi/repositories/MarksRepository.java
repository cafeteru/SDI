package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.Mark;

/**
 * CrudRepository<Clase Entidad, Clase de la clave primaria>
 * 
 * @author igm1990
 *
 */
public interface MarksRepository extends CrudRepository<Mark, Long> {

	@Query("Select m from Mark m where m.description like %?1%")
	List<Mark> findByDescipcion(String descripcion);
}
