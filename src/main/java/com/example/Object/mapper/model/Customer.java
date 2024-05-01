package com.example.Object.mapper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(name = "first_name")
    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Email(message = "Некорректное значение email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
    @Column(name = "contact_number")
    private String contactNumber;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;
}
