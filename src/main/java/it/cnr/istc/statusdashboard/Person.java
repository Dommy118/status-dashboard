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

    // One Person has Many Cars. CascadeType.ALL means if you save/delete the Person, it saves/deletes their cars too!
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Helper method to safely assign a car to this person
    public void addCar(Car car) {
        cars.add(car);
        car.setPerson(this);
    }
}