package application.service;

import application.helper.Analytics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CovidAnalyticsServiceTest {

    Fixture fixture;

    @Before
    public void initialize() {
        fixture = new Fixture();
        fixture.init();
    }

    @Test
    public void test_getNewCasesReportedToday() {
        fixture.given_CSVFileIsReadAndProcessed();
        fixture.given_NewCasesAreReportedTodayInWorld();
        fixture.when_NewCasesAreCheckedTodayInWorld();
        fixture.then_VerifyCasesReportedTodayInWorld();
    }

    @Test
    public void test_getNewCasesReportedByCountry() {
        String country = "xyz";
        fixture.given_CSVFileIsReadAndProcessed();
        fixture.given_NewCasesAreReportedByCountry(country);
        fixture.when_NewCasesAreCheckedByCountry(country);
        fixture.then_VerifyJsonResponse();
    }

    @Test
    public void test_getNewCasesReportedSorted() {
        fixture.given_CSVFileIsReadAndProcessed();
        fixture.given_NewCasesAreSorted();
        fixture.when_NewCasesAreCheckedSortedByCountry();
        fixture.then_VerifyJsonResponse();
    }

    @Test
    public void test_getNewCasesByDateCountry() {
        String country = "xyz";
        String date = "20202020";
        fixture.given_CSVFileIsReadAndProcessed();
        fixture.given_NewCasesAreByDateCountry(date, country);
        fixture.when_NewCasesAreCheckedCountryByDate(date, country);
        fixture.then_VerifyJsonResponse();
    }

    @Test
    public void test_CountriesWithTopCases() {
        Integer limit = 2;
        fixture.given_CSVFileIsReadAndProcessed();
        fixture.given_NHighestCasesCountires(limit);
        fixture.when_NewCasesAreCheckedForTopNCountires(limit);
        fixture.then_VerifyJsonResponse();
    }

    private class Fixture {
        private final Integer NEW_CASES_NUMBER = 123;
        private final String JSON_RESPONSE_RETURN = "json_response";
        @Mock
        private Analytics analytics;
        @InjectMocks
        private CovidAnalyticsService covidAnalyticsService;
        private String results;

        public void init() {
            MockitoAnnotations.initMocks(this);
        }

        public void given_CSVFileIsReadAndProcessed() {
            doNothing().when(analytics).readCsvFile();
            doNothing().when(analytics).casesToday();
        }

        public void given_NewCasesAreReportedTodayInWorld() {
            when(analytics.totalNumberOfNewCasesToday()).thenReturn(NEW_CASES_NUMBER);
        }

        public void given_NewCasesAreReportedByCountry(String country) {
            Map<String, Integer> casesInCoutry = new HashMap<String, Integer>();
            casesInCoutry.put(country, NEW_CASES_NUMBER);
            when(analytics.casesTodayInCountryByName(country)).thenReturn(casesInCoutry);
            when(analytics.mapToJson(casesInCoutry)).thenReturn(JSON_RESPONSE_RETURN);
        }

        public void given_NewCasesAreSorted() {
            Map<String, Integer> casesInCoutry = new HashMap<String, Integer>();
            casesInCoutry.put("Country", NEW_CASES_NUMBER);
            when(analytics.sortByNumberOfCases()).thenReturn(casesInCoutry);
            when(analytics.mapToJson(casesInCoutry)).thenReturn(JSON_RESPONSE_RETURN);
        }

        public void given_NewCasesAreByDateCountry(String date, String country) {
            Map<String, Integer> casesInCoutry = new HashMap<String, Integer>();
            casesInCoutry.put(country, NEW_CASES_NUMBER);
            when(analytics.casesInCountrySinceDate(date, country)).thenReturn(casesInCoutry);
            when(analytics.mapToJson(casesInCoutry)).thenReturn(JSON_RESPONSE_RETURN);
        }

        public void given_NHighestCasesCountires(Integer limit) {
            Map<String, Integer> casesInCoutry = new HashMap<String, Integer>();
            casesInCoutry.put("country", NEW_CASES_NUMBER);
            when(analytics.topNCountriesCases(limit)).thenReturn(casesInCoutry);
            when(analytics.mapToJson(casesInCoutry)).thenReturn(JSON_RESPONSE_RETURN);
        }

        public void when_NewCasesAreCheckedTodayInWorld() {
            results = covidAnalyticsService.newCasesReportedToday();
        }

        public void when_NewCasesAreCheckedByCountry(String country) {
            results = covidAnalyticsService.newCasesReportedInCountryToday(country);
        }

        public void when_NewCasesAreCheckedSortedByCountry() {
            results = covidAnalyticsService.newCasesReportedTodayCountryWise();
        }

        public void when_NewCasesAreCheckedCountryByDate(String date, String country) {
            results = covidAnalyticsService.newCasesReportedInCountryByDate(date, country);
        }

        public void when_NewCasesAreCheckedForTopNCountires(Integer limit) {
            results = covidAnalyticsService.topNCountriesAndCases(limit.toString());
        }

        public void then_VerifyCasesReportedTodayInWorld() {
            assertEquals(NEW_CASES_NUMBER.toString(), results);
        }

        public void then_VerifyJsonResponse() {
            assertEquals(JSON_RESPONSE_RETURN, results);
        }
    }
}
