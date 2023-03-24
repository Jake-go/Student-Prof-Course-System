package ca.sheridancollege.gollej.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.gollej.beans.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
