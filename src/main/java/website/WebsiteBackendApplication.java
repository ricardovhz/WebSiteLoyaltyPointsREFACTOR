package website;

import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WebsiteBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(WebsiteBackendApplication.class);

	static final String internalVersion = "0.0.1";

	@Value("${app.version}")
	String appVersion;

	@Autowired
	private Environment environment;

	public static void main(String[] args) {

		SpringApplication.run(WebsiteBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			
			logger.info("========================== Properties ==========================");
			logger.debug("Internal version: " + internalVersion);
			logger.debug("Config Version: " + appVersion);
			for (String profileName : environment.getActiveProfiles()) {
				logger.debug("Currently active profile - " + profileName);
			}

			Runtime runtime = Runtime.getRuntime();

			final NumberFormat format = NumberFormat.getInstance();

			final long maxMemory = runtime.maxMemory();
			final long allocatedMemory = runtime.totalMemory();
			final long freeMemory = runtime.freeMemory();
			final long mb = 1024 * 1024;
			final String mega = " MB";

			logger.info("========================== Memory Info ==========================");
			logger.info("Free memory: " + format.format(freeMemory / mb) + mega);
			logger.info("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
			logger.info("Max memory: " + format.format(maxMemory / mb) + mega);
			logger.info(
					"Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
			logger.info("=================================================================\n");

		};
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		System.out.println("creating RestTemplate");
		return new RestTemplate();
	}

}
