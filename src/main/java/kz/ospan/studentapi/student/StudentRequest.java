package kz.ospan.studentapi.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Body of POST and PUT requests. */
public record StudentRequest(
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank @Email String email,
		@NotBlank String groupName) {

	Student toEntity() {
		return new Student(firstName, lastName, email, groupName);
	}
}
