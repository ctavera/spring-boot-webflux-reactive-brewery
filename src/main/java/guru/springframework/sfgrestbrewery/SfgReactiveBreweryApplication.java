package guru.springframework.sfgrestbrewery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SfgReactiveBreweryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SfgReactiveBreweryApplication.class, args);
	}

//	@Value("classpath:/schema.sql") //This block isn´t not necessary in spring boot 2.7
//	Resource resource;
//
//	@Bean
//	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//
//		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//		initializer.setConnectionFactory(connectionFactory);
//		initializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));
//
//		return initializer;
//	}
}
