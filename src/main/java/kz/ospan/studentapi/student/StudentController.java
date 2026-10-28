package kz.ospan.studentapi.student;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** CRUD for students. Personal base path of Ramazan Ospan. */
@RestController
@RequestMapping(StudentController.BASE_PATH)
public class StudentController {

	public static final String BASE_PATH = "/api/ospan-ramazan/students";

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}

	@GetMapping
	public List<Student> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Student findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public ResponseEntity<Student> create(@Valid @RequestBody StudentRequest request) {
		Student created = service.create(request);
		return ResponseEntity.created(URI.create(BASE_PATH + "/" + created.getId())).body(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
