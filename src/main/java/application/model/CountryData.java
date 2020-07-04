package application.model;

import java.util.List;

public class CountryData {
    private String name;
    private List<Integer> cases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getCases() {
        return cases;
    }

    public void setCases(List<Integer> cases) {
        this.cases = cases;
    }
}
