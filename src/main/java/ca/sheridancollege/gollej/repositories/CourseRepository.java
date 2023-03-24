package ca.sheridancollege.gollej.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.gollej.beans.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

}
