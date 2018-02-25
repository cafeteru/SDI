package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.uniovi.entities.Mark;
import com.uniovi.entities.User;

/**
 * CrudRepository<Clase Entidad, Clase de la clave primaria>
 * 
 * @author igm1990
 *
 */
public interface MarksRepository extends CrudRepository<Mark, Long> {

	@Query("Select m from Mark m where m.description like %?1%")
	List<Mark> findByDescipcion(String descripcion);

	@Modifying
	@Transactional
	@Query("UPDATE Mark SET resend = ?1 WHERE id = ?2")
	void updateResend(Boolean resend, Long id);

	@Query("SELECT r FROM Mark r WHERE r.user = ?1 ORDER BY r.id ASC ")
	List<Mark> findAllByUser(User user);

	@Query("SELECT r FROM Mark r WHERE r.user = ?1 AND r.score >= 5 ")
	List<Mark> findAllPassedByUser(User user);

	@Query("SELECT r FROM Mark r WHERE (LOWER(r.description) LIKE LOWER(?1) OR LOWER(r.user.name) LIKE LOWER(?1))")
	List<Mark> searchByDescriptionAndName(String seachtext);

	@Query("SELECT r FROM Mark r WHERE (LOWER(r.description) LIKE LOWER(?1) OR LOWER(r.user.name) LIKE LOWER(?1)) AND r.user = ?2 ")
	List<Mark> searchByDescriptionNameAndUser(String seachtext, User user);
}
