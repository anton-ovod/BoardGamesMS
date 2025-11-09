package org.example.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Holiday{
    String title;
    LocalDate date;

    @Override
    public String toString() {
        return String.format("Holiday: %s (%s)", title, date);
    }
}
