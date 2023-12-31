package com.jameshuynh.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	@Autowired
	public StudentService(StudentRepository studentRepository) {this.studentRepository = studentRepository;}

	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

	public void addNewStudent(Student student) {
		Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());

		if (studentByEmail.isPresent()) {
			throw new IllegalStateException("email taken");
		}

		studentRepository.save(student);
	}

	public void deleteStudent(Long studentId) {
		boolean exists = studentRepository.existsById(studentId);
		if (!exists) {
			throw new IllegalStateException("student with id " + studentId + " does not exist");
		}
		studentRepository.deleteById(studentId);
	}

	@Transactional
	public void updateStudent(Long studentId, String name, String email) {
		Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));

		if (!name.isBlank() && !student.getName().equals(name)) student.setName(name);

		if (!email.isBlank() && !student.getEmail().equals(email)) {
			if (studentRepository.findStudentByEmail(email).isPresent()) throw new IllegalStateException("email taken");
			student.setEmail(email);
		}
	}
}
