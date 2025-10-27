package org.example.parser;

import java.util.ArrayList;
import java.util.List;

public class ParsedData {
    private final List<String> researchUnits;
    private final List<List<String>> employeesData;

    public ParsedData(List<String> researchUnits, List<List<String>> employeesData) {
        this.researchUnits = researchUnits;
        this.employeesData = employeesData;
    }

    public List<String> getResearchUnits() {
        return researchUnits;
    }

    public List<List<String>> getEmployeesData() {
        return employeesData;
    }
}

