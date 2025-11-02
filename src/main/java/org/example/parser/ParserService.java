package org.example.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserService {

    private ParsedData parseRawData(){
        List<String> researchUnits = new ArrayList<>();
        List<List<String>> employeesData = new ArrayList<>();
        try{
            Document doc = Jsoup.connect("https://cs.pollub.pl/staff/").get();
            Elements researchUnitsElem = doc.selectXpath("//h3[contains(text(), 'Zakład')]");
            researchUnits.addAll(researchUnitsElem.eachText());

            for(int i = 1; i<=researchUnits.size(); i++){
                Elements employeesSections = doc.selectXpath("/html/body/div[1]/div/div/div[1]/section/div/div[1]/div/article/p["+i+"]/a");
                employeesData.add(employeesSections.eachText());
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return new ParsedData(researchUnits, employeesData);
    }


    public List<Employee> getEmployees(){
        ParsedData parsedData = parseRawData();
        List<String> researchUnits = parsedData.getResearchUnits();
        List<List<String>> employeesData = parsedData.getEmployeesData();
        List<Employee> employees = new ArrayList<>();

        for(int i = 0;  i < employeesData.size(); i++)
        {
            for(int j = 0; j < employeesData.get(i).size(); j++)
            {
                Matcher m = DEGREE_PATTERN.matcher(employeesData.get(i).get(j).trim());

                if (!m.find()) {
                    throw new IllegalArgumentException("Cannot parse employee" + employeesData.get(i).get(j));
                }

                String degree = m.group(1).trim();
                String name = m.group(2).trim();

                employees.add(new Employee(name, degree, researchUnits.get(i)));
            }
        }

        return employees;
    }

    private static final Pattern DEGREE_PATTERN = Pattern.compile(
            "^(dr hab\\. inż\\.|dr hab\\.|dr inż\\.|dr|mgr inż\\.|mgr)\\s+(.*)$"
    );

    public HashMap<String, ArrayList<String>> getResearchUnitsWithEmployees(){
        List<Employee> employees = getEmployees();
        HashMap<String, ArrayList<String>> researchUnitsMap = new HashMap<>();
        for(Employee e : employees){
            researchUnitsMap.putIfAbsent(e.getResearchUnit(), new ArrayList<>());
            researchUnitsMap.get(e.getResearchUnit()).add(e.getFullName());
        }
        for(String ru : researchUnitsMap.keySet()){
            researchUnitsMap.get(ru).sort(String::compareTo);
        }
        return researchUnitsMap;
    }

    public List<Employee> getEmployeesByDegree(String degree){
        List<Employee> employees = getEmployees();
        List<Employee> filteredEmployees = new ArrayList<>();
        for(Employee e : employees){
            if(e.getAcaedmicDegree().equals(degree)){
                filteredEmployees.add(e);
            }
        }
        return filteredEmployees;
    }

}
