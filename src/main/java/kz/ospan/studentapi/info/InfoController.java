package kz.ospan.studentapi.info;

import java.util.LinkedHashMap;
import java.util.Map;
import kz.ospan.studentapi.student.StudentController;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** GET / - who made the application and which version is running. */
@RestController
public class InfoController {

	private final ObjectProvider<BuildProperties> build;
	private final String student;
	private final String group;

	public InfoController(ObjectProvider<BuildProperties> build,
			@Value("${app.student.name}") String student,
			@Value("${app.student.group}") String group) {
		this.build = build;
		this.student = student;
		this.group = group;
	}

	@GetMapping("/")
	public Map<String, Object> info() {
		BuildProperties props = build.getIfAvailable();
		Map<String, Object> info = new LinkedHashMap<>();
		info.put("application", "Student API");
		info.put("student", student);
		info.put("group", group);
		info.put("version", props != null ? props.getVersion() : "dev");
		info.put("students", StudentController.BASE_PATH);
		return info;
	}
}
