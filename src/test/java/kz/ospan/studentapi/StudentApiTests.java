package kz.ospan.studentapi;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentApiTests {

	static final String URL = "/api/ospan-ramazan/students";

	@Autowired
	MockMvc mvc;

	String create(String email) throws Exception {
		String body = """
				{"firstName":"Ramazan","lastName":"Ospan","email":"%s","groupName":"IT2-2310"}""".formatted(email);
		return mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.lastName").value("Ospan"))
				.andReturn().getResponse().getHeader("Location");
	}

	@Test
	void infoShowsStudentName() throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.student").value("Ramazan Ospan"))
				.andExpect(jsonPath("$.group").value("IT2-9999"));
	}

	@Test
	void createAndReadStudent() throws Exception {
		String location = create("create@ospan.kz");
		mvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Ramazan"))
				.andExpect(jsonPath("$.email").value("create@ospan.kz"));
		mvc.perform(get(URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].email", org.hamcrest.Matchers.hasItem("create@ospan.kz")));
	}

	@Test
	void updateStudent() throws Exception {
		String location = create("update@ospan.kz");
		String body = """
				{"firstName":"Ramazan","lastName":"Ospan","email":"update@ospan.kz","groupName":"IT2-2310 (DevOps)"}""";
		mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.groupName").value("IT2-2310 (DevOps)"));
		mvc.perform(put(URL + "/999999").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteStudent() throws Exception {
		String location = create("delete@ospan.kz");
		mvc.perform(delete(location)).andExpect(status().isNoContent());
		mvc.perform(get(location)).andExpect(status().isNotFound());
	}

	@Test
	void missingStudentReturns404() throws Exception {
		mvc.perform(get(URL + "/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail", containsString("999999")));
	}

	@Test
	void invalidStudentIsRejected() throws Exception {
		mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content("{\"firstName\":\"\"}"))
				.andExpect(status().isBadRequest());
	}
}
