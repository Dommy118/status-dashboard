package it.cnr.istc.statusdashboard;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PersonController {

    private final PersonRepository repository;

    // Spring injects the database repository here
    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    // This runs automatically when the app starts up and adds some dummy data
    @PostConstruct
    public void insertSampleData() {
        repository.save(new Person("Balázs", 17));
        repository.save(new Person("Regő", 17));
        repository.save(new Person("Roland", 17));
        repository.save(new Person("Hanna", 16));
        repository.save(new Person("Dominik", 17));

        System.out.println(">>> Sample data successfully saved to H2 Database! <<<");
    }

    // This creates your web URL endpoint
    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return repository.findAll();
    }
}