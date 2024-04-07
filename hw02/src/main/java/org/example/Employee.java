package org.example;

import java.time.LocalDate;

public record Employee(
        String name,
        LocalDate dateOfBirth,
        JobTitle jobTitle
) {
}
