package petmania.petmania;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import petmania.petmania.animal.Animal;

import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@RestController
public class PetmaniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetmaniaApplication.class, args);
	}

	@GetMapping
	public List<Animal> hello() {
		return List.of(
				new Animal(
						1L,
						"Baumi",
						6,
						"gato",
						"vira-lata",
						1L));
	}
}
