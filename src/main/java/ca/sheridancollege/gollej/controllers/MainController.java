package ca.sheridancollege.gollej.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.gollej.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import ca.sheridancollege.gollej.beans.Course;
import ca.sheridancollege.gollej.beans.Professor;
import ca.sheridancollege.gollej.beans.Student;
import ca.sheridancollege.gollej.repositories.CourseRepository;
import ca.sheridancollege.gollej.repositories.ProfessorRepository;

@Controller
@AllArgsConstructor
public class MainController {
	
	// ***********
	// Feel like I maybe should have used multiple controllers.
	// There's way too many methods in here, feels messy.
	// I'll try it out in the next assignment/exercise if it feels necessary.
	// Please let me know if this is incorrect or bad practice
	// ***********
	
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private ProfessorRepository professorRepository;

    // nav to home page
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    // nav to student page
    @GetMapping("/addStudent")
    public String addStudent(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "addStudent.html";
    }

    // process add student
    @GetMapping("/processAddStudent")
    public String processAddStudent(@ModelAttribute Student student) {
        if (student.getFirstName() == null && student.getLastName() == null) {
            return "redirect:/addStudent";
        }
        student.setCourses(new ArrayList<Course>());
        studentRepository.save(student);
        return "redirect:/addStudent";
    }

    // nav to course page
    @GetMapping("/addCourse")
    public String addCourse(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "addCourse.html";
    }

    // process add course
    @GetMapping("/processAddCourse")
    public String processAddCourse(@ModelAttribute Course course) {
        if (course.getCourseName() == null || course.getCourseCode() == null) {
            return "redirect:/addCourse";
        }
        course.setStudents(new ArrayList<Student>());
        course.setProfessor(null);
        courseRepository.save(course);
        return "redirect:/addCourse";
    }

    // nav to professor page
    @GetMapping("/addProfessor")
    public String addProfessor(Model model) {
        model.addAttribute("professors", professorRepository.findAll());
        return "addProfessor.html";
    }

    // process add professor
    @GetMapping("/processAddProfessor")
    public String processAddProfessor(@ModelAttribute Professor professor) {
        if (professor.getName() == null) {
            return "redirect:/addProfessor";
        }
        professor.setCourses(new ArrayList<Course>());
        professorRepository.save(professor);
        return "redirect:/addProfessor";
    }

    // nav to assign professor page
    @GetMapping("/assignProfessor")
    public String assignProfessor(Model model) {
        model.addAttribute("professors", professorRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "assignProfessor.html";
    }

    // process assign professor
    @GetMapping("/processAssignProfessor")
    public String processAssignProfessor(@RequestParam Long professorId, @RequestParam Long courseId) {
        Optional<Professor> professorOptional = professorRepository.findById(professorId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (!professorOptional.isPresent() || !courseOptional.isPresent()) {
            System.out.println("Did Not Assign Professor to Course");
            return "redirect:/assignProfessor";
        }
        Professor professor = professorRepository.findById(professorId).get();
        Course course = courseRepository.findById(courseId).get();
        if (course.getProfessor() != null) {

            return "redirect:/assignProfessor";
        }
        professor.getCourses().add(course);
        course.setProfessor(professor);
        courseRepository.save(course);
        professorRepository.save(professor);
        return "redirect:/assignProfessor";
    }

    // nav to assign student page
    @GetMapping("/assignStudent")
    public String assignStudent(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "assignStudent.html";
    }

    // process assign student
    @GetMapping("/processAssignStudent")
    public String processAssignStudent(@RequestParam Long studentId, @RequestParam Long courseId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (!studentOptional.isPresent() || !courseOptional.isPresent()) {
            System.out.println("Did Not Assign Student to Course");
            return "redirect:/assignStudent";
        }
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();
        if (course.getStudents().contains(student)) {
            return "redirect:/assignStudent";
        }
        course.getStudents().add(student);
        courseRepository.save(course);
        return "redirect:/assignStudent";
    }

    // nav to view course page
    @GetMapping("/viewCourse/{id}")
    public String viewCourse(Model model, @PathVariable Long id) {
        System.out.println("View Course " + id + "\n \n \n \n");
        model.addAttribute("course", courseRepository.findById(id).get());
        return "viewCourse.html";
    }

    // process dropping student from course
    @GetMapping("/processDropStudent/course/{courseId}/student/{studentId}")
    public String processDropStudent(Model model, @PathVariable Long courseId, @PathVariable Long studentId) {
        // check if the student and course exist
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        System.out.println("Process Drop Student Method Reached");
        if (!studentOptional.isPresent() || !courseOptional.isPresent()) {
            System.out.println("Course " + courseId + "or Student " + studentId + " Not Found");
            return "redirect:/viewCourse/{courseId}";
        }
        // check if the student is in the course
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();
        if (!course.getStudents().contains(student)) {
            System.out.println("Student " + studentId + " Not Found in Course " +
                    courseId);
            return "redirect:/viewCourse/{courseId}";
        }

        // remove student from course
        student.getCourses().remove(course);
        course.getStudents().remove(student);
        courseRepository.save(course);
        System.out.println("Student " + studentId + " Dropped from Course" + courseId);
        return "redirect:/viewCourse/" + courseId;
    }

    // view professor page
    @GetMapping("/viewProfessor/{id}")
    public String viewProfessor(Model model, @PathVariable Long id) {
        model.addAttribute("professor", professorRepository.findById(id).get());
        return "viewProfessor.html";
    }

    // drop professor from a course
    @GetMapping("/processDropProfessor/course/{courseId}/professor/{professorId}")
    public String dropProfessorFromCourse(Model model, @PathVariable Long courseId, @PathVariable Long professorId) {
        // check if the professor and course exist
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<Professor> professorOptional = professorRepository.findById(professorId);
        if (!professorOptional.isPresent() || !courseOptional.isPresent()) {
            System.out.println("Course " + courseId + "or Professor " + professorId + " Not Found");
            return "redirect:/viewCourse/{courseId}";
        }
        // check if the professor is in the course
        Professor professor = professorRepository.findById(professorId).get();
        Course course = courseRepository.findById(courseId).get();
        if (course.getProfessor() == null || !course.getProfessor().equals(professor)) {
            System.out.println("Professor " + professorId + " Not Found in Course " +
                    courseId);
            return "redirect:/viewCourse/{courseId}";
        }

        // remove professor from course
        professor.getCourses().remove(course);
        course.setProfessor(null);
        courseRepository.save(course);
        professorRepository.save(professor);
        System.out.println("Professor " + professorId + " Dropped from Course" + courseId);
        return "redirect:/viewCourse/{courseId}";
    }

    // view student page
    @GetMapping("/viewStudent/{id}")
    public String viewStudent(Model model, @PathVariable Long id) {
        model.addAttribute("student", studentRepository.findById(id).get());
        return "viewStudent.html";
    }
}
