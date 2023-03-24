package ca.sheridancollege.gollej.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.gollej.beans.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>{

}
