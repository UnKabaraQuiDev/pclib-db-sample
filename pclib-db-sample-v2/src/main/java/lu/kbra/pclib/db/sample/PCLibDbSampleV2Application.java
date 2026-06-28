package lu.kbra.pclib.db.sample;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PCLibDbSampleV2Application {

	public static void main(final String[] args) {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(PCLibDbSampleV2Application.class)
				.web(WebApplicationType.NONE).run(args)) {
			System.exit(org.springframework.boot.SpringApplication.exit(context));
		}
	}
}
