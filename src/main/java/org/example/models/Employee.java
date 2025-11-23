package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


@Data
@AllArgsConstructor
public class Employee {
    @NonNull
    private String fullName;
    @NonNull
    private String AcademicDegree;
    @NonNull
    private String researchUnit;

    @Override
    public String toString() {
        return String.format("Employee: %s, %s (%s)", fullName, AcademicDegree, researchUnit);
    }
}