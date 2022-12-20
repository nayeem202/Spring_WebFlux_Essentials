package devDojoAcademy.WebFluxEssentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;



@SpringBootApplication
public class WebFluxEssentialsApplication {

/*	static {
		BlockHound.install(
				builder -> builder.allowBlockingCallsInside("java.io.FilterInputStream", "read")
		);
}*/


	public static void main(String[] args) {
		System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("devdojo"));
		SpringApplication.run(WebFluxEssentialsApplication.class, args);

	}


}
