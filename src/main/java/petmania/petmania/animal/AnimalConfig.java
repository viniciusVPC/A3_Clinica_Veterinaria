package petmania.petmania.animal;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnimalConfig {
    @Bean
    CommandLineRunner commandLineRunner(AnimalRepository repository) {
        return args -> {
            Animal baumi = new Animal(
                    1L,
                    "Baumi",
                    LocalDate.of(2018, Month.FEBRUARY, 5),
                    "gato",
                    "vira-lata",
                    1L);

            Animal toby = new Animal(
                    "Toby",
                    LocalDate.of(2021, Month.FEBRUARY, 5),
                    "cachorro",
                    "vira-lata",
                    2L);

            repository.saveAll(
                    List.of(baumi, toby));
        };
    }
}
