package it.cnr.istc.statusdashboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;

    // Many Cars can belong to One Person
    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore // CRITICAL: This stops an infinite loop when loading the JSON!
    private Person person;

    public Car(String brand, String model, Person person) {
        this.brand = brand;
        this.model = model;
        this.person = person;
    }
}