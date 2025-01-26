package com.example.demo.entity;
import jakarta.persistence.*;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false )
    @Lob
    private String description;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
