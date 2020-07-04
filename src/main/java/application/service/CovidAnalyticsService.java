package application.service;

import application.helper.Analytics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CovidAnalyticsService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Analytics analytics = Analytics.getInstance();

    public Analytics getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Analytics analytics) {
        this.analytics = analytics;
    }

    public String newCasesReportedToday() {
        analytics.readCsvFile();
        analytics.casesToday();
        return analytics.totalNumberOfNewCasesToday().toString();
    }

    public String newCasesReportedTodayCountryWise() {
        analytics.readCsvFile();
        analytics.casesToday();
        return analytics.mapToJson(analytics.sortByNumberOfCases());
    }

    public String newCasesReportedInCountryToday(String country) {
        analytics.readCsvFile();
        analytics.casesToday();
        return analytics.mapToJson(analytics.casesTodayInCountryByName(country));
    }

    public String newCasesReportedInCountryByDate(String date, String country) {
        analytics.readCsvFile();
        analytics.casesToday();
        return analytics.mapToJson(analytics.casesInCountrySinceDate(date, country));
    }

    public String topNCountriesAndCases(String number) {
        analytics.readCsvFile();
        analytics.casesToday();
        return analytics.mapToJson(analytics.topNCountriesCases(Integer.parseInt(number)));
    }
}
