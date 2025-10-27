package org.example.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserService {

    private ParsedData parseRawData(){
        ArrayList<String> researchUnits = new ArrayList<>();
        ArrayList<String> employeesData = new ArrayList<>();
        try{
            Document doc = Jsoup.connect("https://cs.pollub.pl/staff/").get();
            Elements researchUnitsElem = doc.selectXpath("//h3[contains(text(), 'Zakład')]");
            researchUnits.addAll(researchUnitsElem.eachText());
            researchUnits.forEach(System.out::println);

            for(int i = 1; i<=researchUnits.size(); i++){
                Elements employeesSections = doc.selectXpath("/html/body/div[1]/div/div/div[1]/section/div/div[1]/div/article/p["+i+"]/a");
                employeesData.addAll(employeesSections.eachText());
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return new ParsedData(researchUnits, employeesData);
    }


    public ArrayList<Employee> getEmployees(){
        ParsedData parsedData = parseRawData();
        ArrayList<String> researchUnits = new ArrayList<>(parsedData.getResearchUnits());
        ArrayList<String> employeesData = new ArrayList<>(parsedData.getEmployeesData());
        ArrayList<Employee> employees = new ArrayList<>();

        for(int i = 1; i<=researchUnits.size(); i++){
            for(String ed : employeesData){
                Matcher m = DEGREE_PATTERN.matcher(ed.trim());

                if (!m.find()) {
                    throw new IllegalArgumentException("Cannot parse employee: " + ed);
                }

                String degree = m.group(1).trim();
                String name = m.group(2).trim();

                employees.add(new Employee(name, degree, researchUnits.get(i-1)));
            }
        }

        return employees;
    }

    private static final Pattern DEGREE_PATTERN = Pattern.compile(
            "^(dr hab\\. inż\\.|dr hab\\.|dr inż\\.|dr|mgr inż\\.|mgr)\\s+(.*)$"
    );

}
