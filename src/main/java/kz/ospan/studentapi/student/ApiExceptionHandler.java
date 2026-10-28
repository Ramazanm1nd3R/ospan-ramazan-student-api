package kz.ospan.studentapi.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** A request for a nonexistent student returns 404 with a JSON body (RFC 9457 problem detail). */
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	public ProblemDetail notFound(StudentNotFoundException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
		problem.setTitle("Student not found");
		return problem;
	}
}
