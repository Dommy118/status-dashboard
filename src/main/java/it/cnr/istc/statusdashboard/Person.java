package it.cnr.istc.statusdashboard;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;

    // One person can own multiple cars
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();

    // One person can have multiple tasks
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Helper method to safely assign a car to this person
    public void addCar(Car car) {
        cars.add(car);
        car.setPerson(this);
    }

    // Helper method to safely assign a task to this person
    public void addTask(Task task) {
        tasks.add(task);
        task.setPerson(this);
    }
}