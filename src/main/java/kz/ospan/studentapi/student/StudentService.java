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

	public Student update(Long id, StudentRequest request) {
		Student student = findById(id);
		student.setFirstName(request.firstName());
		student.setLastName(request.lastName());
		student.setEmail(request.email());
		student.setGroupName(request.groupName());
		return student;   // saved on commit: the entity is managed inside the transaction
	}

	public void delete(Long id) {
		repository.delete(findById(id));
	}
}
