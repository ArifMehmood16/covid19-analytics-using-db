package application.helper;

import application.model.CountryData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Analytics {

    private static final Analytics instance = new Analytics();
    private List<CountryData> covidFilteredData;
    private Map<String, Integer> casesToday;

    private Analytics() {
    }

    public static Analytics getInstance() {
        return instance;
    }

    public void readCsvFile() {
        List<List<String>> csvData = new ArrayList<List<String>>();

        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withSkipHeaderRecord(true);

        try {

            List covidRecords = new ArrayList();
            fileReader = new FileReader("target/data.csv");
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            List csvRecords = csvFileParser.getRecords();

            for (int i = 1; i < csvRecords.size(); i++) {
                List<String> record = Arrays.asList(csvRecords.get(i).toString().replace("]", "").split(","));
                csvData.add(filterData(record));
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        }
        covidFilteredData = convertToCountryData(csvData);
    }

    private List<CountryData> convertToCountryData(List<List<String>> covidFilteredData) {
        List<CountryData> output = new ArrayList<CountryData>();
        for (List<String> record : covidFilteredData) {
            CountryData newObj = new CountryData();
            newObj.setName(record.get(0).replace(" ", "").toLowerCase());
            newObj.setCases(record.subList(1, record.size()).stream()
                    .map(String::trim)
                    .map(Double::parseDouble)
                    .map(Double::intValue)
                    .collect(Collectors.toList()));
            output.add(newObj);
        }
        return output;
    }

    private List<String> filterData(List<String> input) {
        List<String> filteredData = new ArrayList<String>();
        filteredData.add(input.get(1));
        List<String> sub = input.subList(4, input.size());
        filteredData.addAll(sub);
        return filteredData;
    }

    public void casesToday() {
        Map<String, Integer> hmap = new HashMap<String, Integer>();
        for (CountryData record : covidFilteredData) {
            Integer newCount = record.getCases().get(record.getCases().size() - 1) - record.getCases().get(record.getCases().size() - 2);
            if (hmap.containsKey(record.getName())) {
                newCount = hmap.get(record.getName()) + newCount;
                hmap.remove(record.getName());
            }
            hmap.put(record.getName(), newCount);
        }
        casesToday = hmap;
    }

    public Integer totalNumberOfNewCasesToday() {
        readCsvFile();
        casesToday();
        return casesToday.values().stream().reduce(0, Integer::sum);
    }

    public Map<String, Integer> casesTodayInCountryByName(String countryName) {
        Map<String, Integer> hmap = new HashMap<String, Integer>();
        if (casesToday.containsKey(countryName.toLowerCase())) {
            hmap.put(countryName.toLowerCase(), casesToday.get(countryName.toLowerCase()));
        }
        return hmap;
    }

    public Map<String, Integer> topNCountriesCases(Integer numberOfCountries) {
        return casesToday.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .limit(numberOfCountries)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<String, Integer> casesInCountrySinceDate(String date, String country) {
        int daysToRecord = getDaysTillDate(date);
        Map<String, Integer> hmap = new HashMap<String, Integer>();
        for (CountryData record : covidFilteredData) {
            Integer newCount = record.getCases().get(record.getCases().size() - 1) - record.getCases().get(record.getCases().size() - daysToRecord);
            if (hmap.containsKey(record.getName())) {
                newCount = hmap.get(record.getName()) + newCount;
                hmap.remove(record.getName());
            }
            hmap.put(record.getName(), newCount);
        }
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put(country.toLowerCase(), hmap.get(country.toLowerCase()).intValue());
        return result;
    }

    private int getDaysTillDate(String date) {
        int daysToRecord = 1;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date input = formatter.parse(date);
            Date today = new Date();
            if (today.after(input)) {
                daysToRecord = (int) DAYS.between(input.toInstant(), today.toInstant()) - 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return daysToRecord < 90 ? daysToRecord : 90;
    }

    public Map<String, Integer> sortByNumberOfCases() {
        return casesToday.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public String mapToJson(Map<String, Integer> countryCount) {
        GsonBuilder gsonMapBuilder = new GsonBuilder();
        Gson gsonObject = gsonMapBuilder.create();
        return gsonObject.toJson(countryCount);
    }
}