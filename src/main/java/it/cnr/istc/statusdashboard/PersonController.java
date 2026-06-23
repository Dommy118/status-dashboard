package it.cnr.istc.statusdashboard;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository repository;

    // Spring automatically injects the PersonRepository
    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void insertSampleData() {
        // 1. Create people
        Person p1 = new Person("Balázs", 17);
        Person p2 = new Person("Regő", 17);
        Person p3 = new Person("Roland", 17);
        Person p4 = new Person("Hanna", 16);
        Person p5 = new Person("Dominik", 17);

        // 2. Add exactly one car per person
        p1.addCar(new Car("Toyota", "Supra", p1));
        p2.addCar(new Car("BMW", "M3", p2));
        p3.addCar(new Car("Audi", "RS6", p3));
        p4.addCar(new Car("Honda", "Civic", p4));
        p5.addCar(new Car("Porsche", "911", p5));

        // 3. Assign tasks to the team
        p1.addTask(new Task("Coding", "Finish Spring Boot backend development", "IN_PROGRESS", p1));
        p2.addTask(new Task("Database Test", "Verify MySQL tables and connections", "DONE", p2));
        p3.addTask(new Task("Documentation", "Create project description and documentation", "TODO", p3));
        p4.addTask(new Task("UI Design", "Draw and design the frontend layout", "IN_PROGRESS", p4));
        p5.addTask(new Task("Debugging", "Review log files and fix errors", "DONE", p5));

        // 4. Save everything to the MySQL database
        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
        repository.save(p4);
        repository.save(p5);

        System.out.println(">>> All persons, cars, and tasks successfully saved! <<<");
    }

    // CRUD - READ: Get all data
    @GetMapping
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    // CRUD - CREATE: Add a new person
    @PostMapping
    public Person createPerson(@RequestBody Person newPerson) {
        return repository.save(newPerson);
    }

    // CRUD - UPDATE: Update person data
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

    // CRUD - DELETE: Delete a person (automatically removes their car and tasks)
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        repository.deleteById(id);
    }
}