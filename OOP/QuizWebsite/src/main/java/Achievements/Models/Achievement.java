package Achievements.Models;

public class Achievement {

    private String name;
    private String requirement;

    public Achievement(String name, String requirement) {
        this.name = name;
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public String getRequirement() {
        return requirement;
    }
}
