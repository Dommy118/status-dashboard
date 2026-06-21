package it.cnr.istc.statusdashboard;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/persons") // This ensures all URLs start with /persons
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void insertSampleData() {
        // 1. Create all 5 people
        Person p1 = new Person("Balázs", 17);
        Person p2 = new Person("Regő", 17);
        Person p3 = new Person("Roland", 17);
        Person p4 = new Person("Hanna", 16);
        Person p5 = new Person("Dominik", 17);

        // 2. Give them some cars! (You can change these brands and models)
        p1.addCar(new Car("Toyota", "Supra", p1));

        p2.addCar(new Car("BMW", "M3", p2));

        p3.addCar(new Car("Audi", "RS6", p3));

        p4.addCar(new Car("Porsche", "911", p4));

        p5.addCar(new Car("Mitsubishi", "Lancer", p5));

        // 3. Save everyone to the database
        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
        repository.save(p4);
        repository.save(p5);

        System.out.println(">>> 5 Friends and their cars successfully saved to H2 Database! <<<");
    }

    // ==========================================
    // CRUD OPERATIONS
    // ==========================================

    // READ: Get all people (and their cars)
    @GetMapping
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    // CREATE: Add a new person
    @PostMapping
    public Person createPerson(@RequestBody Person newPerson) {
        return repository.save(newPerson);
    }

    // UPDATE: Change a person's name or age
    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person updatedPersonData) {
        return repository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setName(updatedPersonData.getName());
                    existingPerson.setAge(updatedPersonData.getAge());
                    return repository.save(existingPerson);
                })
                .orElseGet(() -> repository.save(updatedPersonData));
    }

    // DELETE: Remove a person (and their cars automatically)
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        repository.deleteById(id);
    }
}