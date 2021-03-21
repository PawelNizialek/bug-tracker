package com.example.ztpai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
)
@Entity(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false,
            nullable = false
    )
    private Long id;
    @Column(updatable = false,
            nullable = false
    )
    @NotBlank
    private String firstName;
    @Column(updatable = false,
            nullable = false
    )
    @NotBlank
    private String lastName;
    @Email
    @Column(updatable = false,
            nullable = false
    )
    private String email;
    private Boolean enabled;
    @Column(updatable = false,
            nullable = false
    )
    private LocalDateTime created_at;
    @Column(
            nullable = false
    )
    @NotBlank
    private String password;
    private String role;
    @OneToOne(fetch = FetchType.LAZY)
    private VerificationToken verificationToken;
}
