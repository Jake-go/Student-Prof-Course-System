package ca.sheridancollege.gollej.bootstrap;

import java.util.ArrayList;

import org.hibernate.mapping.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.sheridancollege.gollej.beans.*;

import ca.sheridancollege.gollej.repositories.CourseRepository;
import ca.sheridancollege.gollej.repositories.ProfessorRepository;
import ca.sheridancollege.gollej.repositories.StudentRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

	private StudentRepository studentRepository;
	private CourseRepository courseRepository;
	private ProfessorRepository professorRepository;

	@Override
	public void run(String... args) throws Exception {

		// create 30 Students
		for (int i = 0; i < 30; i++) {
			Student student = new Student();
			student.setFirstName("FirstNameStudent" + i);
			student.setLastName("LastName" + i);
			student.setCourses(new ArrayList<Course>());
			studentRepository.save(student);
		}

		// create 3 Professors
		for (int i = 0; i < 3; i++) {
			Professor professor = new Professor();
			professor.setName("Professor" + i);
			professor.setCourses(new ArrayList<Course>());
			professorRepository.save(professor);
		}

		// create 6 courses
		for (int i = 0; i < 6; i++) {
			Course course = new Course();
			course.setCourseName("Course" + i);
			course.setCourseCode("Course" + i);
			course.setStudents(new ArrayList<Student>());
			courseRepository.save(course);
		}

		// add 5 student to each course
		for (int i = 0; i < 30; i++) {
			Student student = studentRepository.findById((long) i + 1).get();
			Course course = courseRepository.findById((long) (7 - (i % 6) - 1)).get();
			student.getCourses().add(course);
			studentRepository.save(student);
		}

		// again, order flipped to mix up data
		for (int i = 0; i < 30; i++) {
			Student student = studentRepository.findById((long) i + 1).get();
			Course course = courseRepository.findById((long) (i % 6) + 1).get();
			student.getCourses().add(course);
			studentRepository.save(student);
		}

		// add 3 professors to each course
		for (int i = 0; i < 6; i++) {
			Professor professor = professorRepository.findById((long) (i / 2) + 1).get();
			Course course = courseRepository.findById((long) i + 1).get();
			professor.getCourses().add(course);
			course.setProfessor(professor);
			professorRepository.save(professor);
			courseRepository.save(course);
		}

	}
}
