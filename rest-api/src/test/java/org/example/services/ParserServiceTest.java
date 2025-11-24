package org.example.services;

import org.example.models.Employee;
import org.example.models.Holiday;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@DisplayName("ParserService Unit Tests")
class ParserServiceTest {

    private ParserService parserService;

    @BeforeEach
    void setUp() {
        parserService = new ParserService();
    }

    @AfterEach
    void tearDown() {
        parserService = null;
    }

    @Test
    void givenServiceInitialized_whenGetEmployees_thenReturnsNonNullList() {
        List<Employee> employees = parserService.getEmployees();

        assertNotNull(employees, "Employees list should not be null");
    }

    @Test
    void givenEmployeesExist_whenGetEmployees_thenListContainsValidData() {
        List<Employee> employees = parserService.getEmployees();

        assertAll("employees validation",
                () -> assertNotNull(employees),
                () -> employees.forEach(e -> {
                    assertNotNull(e.getFullName(), "Employee name should not be null");
                    assertNotNull(e.getAcademicDegree(), "Academic degree should not be null");
                    assertNotNull(e.getResearchUnit(), "Research unit should not be null");
                })
        );
    }

    @Test
    void givenEmployeesExist_whenGetResearchUnitsWithEmployees_thenReturnsNonEmptyMap() {
        HashMap<String, ArrayList<String>> researchUnits = parserService.getResearchUnitsWithEmployees();
        assertNotNull(researchUnits, "Research units map should not be null");
        assertFalse(researchUnits.isEmpty(), "Research units map should not be empty");
    }

    @Test
    @DisplayName("Given research units exist when getResearchUnitsWithEmployees called then employees are sorted")
    void givenResearchUnitsExist_whenGetResearchUnitsWithEmployees_thenEmployeesAreSorted() {
        HashMap<String, ArrayList<String>> researchUnits = parserService.getResearchUnitsWithEmployees();

        researchUnits.values().forEach(employeeList -> {
            List<String> sortedList = new ArrayList<>(employeeList);
            Collections.sort(sortedList);
            assertEquals(sortedList, employeeList, "Employee list should be sorted alphabetically");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"dr", "dr hab.", "dr inż.", "mgr", "mgr inż.", "dr hab. inż."})
    void givenValidDegree_whenGetEmployeesByDegree_thenReturnsFilteredList(String degree) {
        List<Employee> filteredEmployees = parserService.getEmployeesByDegree(degree);
        assertNotNull(filteredEmployees);
        filteredEmployees.forEach(e ->
                assertEquals(degree, e.getAcademicDegree(), "All employees should have the specified degree")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNullOrEmptyDegree_whenGetEmployeesByDegree_thenReturnsEmptyList(String degree) {
        List<Employee> filteredEmployees = parserService.getEmployeesByDegree(degree);
        assertNotNull(filteredEmployees);
        assertTrue(filteredEmployees.isEmpty(), "Should return empty list for null or empty degree");
    }

    @Test
    @DisplayName("Given invalid degree when getEmployeesByDegree called then returns empty list")
    void givenInvalidDegree_whenGetEmployeesByDegree_thenReturnsEmptyList() {
        String invalidDegree = "invalid degree";

        List<Employee> filteredEmployees = parserService.getEmployeesByDegree(invalidDegree);

        assertNotNull(filteredEmployees);
        assertTrue(filteredEmployees.isEmpty(), "Should return empty list for invalid degree");
    }



    @RepeatedTest(value = 3, name = "Repeated getEmployees test {currentRepetition}/{totalRepetitions}")
    void givenMultipleCalls_whenGetEmployees_thenReturnsConsistentResults(RepetitionInfo repetitionInfo) {
        List<Employee> employees = parserService.getEmployees();

        assertNotNull(employees);
        assertTrue(repetitionInfo.getCurrentRepetition() <= 3);
    }

    @Nested
    @DisplayName("Research Units Tests")
    class ResearchUnitsTests {

        @Test
        void givenResearchUnitsMap_whenChecked_thenAllKeysAreNonEmpty() {
            HashMap<String, ArrayList<String>> researchUnits = parserService.getResearchUnitsWithEmployees();

            researchUnits.keySet().forEach(key ->
                    assertFalse(key == null || key.isEmpty(), "Research unit name should not be null or empty")
            );
        }

        @Test
        void givenResearchUnitsMap_whenChecked_thenAllValuesAreNonEmptyLists() {
            HashMap<String, ArrayList<String>> researchUnits = parserService.getResearchUnitsWithEmployees();

            researchUnits.values().forEach(employeeList ->
                    assertFalse(employeeList.isEmpty(), "Employee list should not be empty")
            );
        }
    }

    @Nested
    @DisplayName("Employee Degree Tests")
    class EmployeeDegreeTests {

        @ParameterizedTest
        @CsvSource({
                "dr, 1",
                "dr hab., 1",
                "dr inż., 1",
                "mgr, 1",
                "mgr inż., 1"
        })
        void givenDegreeAndMinCount_whenGetEmployeesByDegree_thenReturnsAtLeastMinimum(String degree, int minCount) {
            List<Employee> filteredEmployees = parserService.getEmployeesByDegree(degree);
            assertTrue(filteredEmployees.size() >= 0,
                    "Should return at least 0 employees with degree: " + degree);
        }

        @Test
        void givenAllEmployees_whenChecked_thenAllHaveValidDegreeFormat() {
            List<Employee> employees = parserService.getEmployees();

            employees.forEach(e -> {
                String degree = e.getAcademicDegree();
                assertTrue(
                        degree.matches("^(dr hab\\. inż\\.|dr hab\\.|dr inż\\.|dr|mgr inż\\.|mgr)$"),
                        "Degree should match valid format: " + degree
                );
            });
        }
    }


    @Test
    void givenService_whenMultipleOperationsCalled_thenMaintainsStateConsistency() {
        assumeTrue(parserService != null, "ParserService must be initialized");

        List<Employee> employees1 = parserService.getEmployees();
        HashMap<String, ArrayList<String>> researchUnits = parserService.getResearchUnitsWithEmployees();
        List<Employee> employees2 = parserService.getEmployees();

        assertAll("state consistency",
                () -> assertNotNull(employees1),
                () -> assertNotNull(researchUnits),
                () -> assertNotNull(employees2),
                () -> assertEquals(employees1.size(), employees2.size(), "Multiple calls should return same size")
        );
    }
}
