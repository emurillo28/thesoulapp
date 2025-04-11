package com.thesoulapp.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor // Required by JPA
@EqualsAndHashCode // Value objects should implement equals/hashCode
public class Email implements Serializable {

    @jakarta.validation.constraints.Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "email", nullable = false, unique = true)
    private String value;

    public Email(String value) {
        // Basic validation could be added here, but Bean Validation handles it primarily
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
} 