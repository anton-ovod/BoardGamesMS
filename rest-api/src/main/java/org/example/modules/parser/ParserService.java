package org.example.modules.parser;

import org.example.models.Employee;
import org.example.models.Holiday;
import org.example.dtos.ParsedData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class ParserService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


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
        List<String> researchUnits = parsedData.researchUnits();
        List<List<String>> employeesData = parsedData.employeesData();
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
            if(e.getAcademicDegree().equals(degree)){
                filteredEmployees.add(e);
            }
        }
        return filteredEmployees;
    }


    public ArrayList<Holiday> getHolidays() {
        ArrayList<Holiday> holidays = new ArrayList<>();
        try {
            disableSSLCertCheck();

            int totalPages = fetchTotalPages();
            Elements allPosts = fetchAllPosts(totalPages);
            Elements holidayPosts = filterHolidayPosts(allPosts);
            holidays = new ArrayList<>(parseHolidayPosts(holidayPosts));

            holidays.sort(Comparator.comparing(Holiday::getDate));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return holidays;
    }


    private int fetchTotalPages() throws IOException {
        Document doc = Jsoup.connect("https://pollub.pl/rekrutacja/aktualnosci").get();
        Elements lastPage = doc.selectXpath("/html/body/section[3]/div/div/div/div/div[1]/ul/li[5]/a");
        return Integer.parseInt(lastPage.get(0).text());
    }

    private Elements fetchAllPosts(int totalPages) throws IOException {
        Elements allPosts = new Elements();
        for (int i = 1; i <= totalPages; i++) {
            Document pageDoc = Jsoup.connect("https://pollub.pl/rekrutacja/aktualnosci/page" + i + ".html").get();
            Elements posts = pageDoc.selectXpath("//div[@class='news-content px-3']");
            allPosts.addAll(posts);
        }
        return allPosts;
    }

    private Elements filterHolidayPosts(Elements allPosts) {
        Elements holidayPosts = new Elements();
        for (Element e : allPosts) {
            String title = e.selectFirst("a").text().toLowerCase();
            if (title.contains("dzień rektorski") ||
                    title.contains("dni rektorskie") ||
                    title.contains("godziny rektorskie") ||
                    title.contains("informacja o godzinach rektorskich")) {
                holidayPosts.add(e);
            }
        }
        return holidayPosts;
    }

    private List<Holiday> parseHolidayPosts(Elements holidayPosts) {
        List<Holiday> holidays = new ArrayList<>();
        for (Element e : holidayPosts) {
            String title = e.selectFirst("a.title").text();
            String dateText = e.selectFirst("div.text-primary").text();
            holidays.add(new Holiday(title, LocalDate.parse(dateText, formatter)));
        }
        return holidays;
    }

    private static void disableSSLCertCheck() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
