package org.example.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.enums.Category;
import org.example.enums.GameStatus;



@Data
@AllArgsConstructor
public class Employee {
    @NonNull
    private String fullName;
    @NonNull
    private String AcaedmicDegree;
    @NonNull
    private String researchUnit;

    @Override
    public String toString() {
        return String.format("Employee: %s, %s (%s)", fullName, AcaedmicDegree, researchUnit);
    }
}