package application.controller;

import application.service.CovidAnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/analytics"})
public class CovidAnalyticsController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CovidAnalyticsService covidAnalyticsService;

    @RequestMapping({"/"})
    public String analyticsGo() {
        return "This is analytics";
    }

    @RequestMapping({"/newcases"})
    public String newCasesReportedToday() {
        return covidAnalyticsService.newCasesReportedToday();
    }

    @RequestMapping({"/sortnewcase"})
    public String newCasesReportedTodayCountryWise() {
        return covidAnalyticsService.newCasesReportedTodayCountryWise();
    }

    @RequestMapping({"/incountry"})
    public String newCasesReportedByCountryName(@RequestParam String country) {
        return covidAnalyticsService.newCasesReportedInCountryToday(country);
    }

    @RequestMapping({"/bydateincountry"})
    public String newCasesReportedInCountryByDate(@RequestParam String date, @RequestParam String country) {
        return covidAnalyticsService.newCasesReportedInCountryByDate(date, country);
    }

    @RequestMapping({"/topncountries"})
    public String topNCountriesAndCases(@RequestParam String limit) {
        return covidAnalyticsService.topNCountriesAndCases(limit);
    }

}
