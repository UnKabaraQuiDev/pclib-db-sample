package lu.kbra.pclib.db.sample.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

@Configuration
public class SampleSpringConfiguration {

	@Bean
	ConversionService conversionService() {
		return new ApplicationConversionService();
	}
}
