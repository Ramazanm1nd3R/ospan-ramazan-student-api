package kz.ospan.studentapi.student;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

	private final StudentRepository repository;

	public StudentService(StudentRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<Student> findAll() {
		return repository.findAll(Sort.by("id"));
	}

	@Transactional(readOnly = true)
	public Student findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
	}

	public Student create(StudentRequest request) {
		return repository.save(request.toEntity());
	}

	public void delete(Long id) {
		repository.delete(findById(id));
	}
}
