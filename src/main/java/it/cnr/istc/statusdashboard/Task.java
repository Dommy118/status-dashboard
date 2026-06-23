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
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;

    // Many tasks can belong to one person
    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore // Prevents infinite JSON loop in browser
    private Person person;

    public Task(String title, String description, String status, Person person) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.person = person;
    }
}