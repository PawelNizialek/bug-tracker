package com.example.ztpai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table
@Entity(name = "Project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false,
            nullable = false
    )
    private Long id;
    @NotBlank(message = "Project name cannot be empty!")
    private String name;
    @NotBlank(message = "Project description cannot be empty!")
    private String description;
//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
