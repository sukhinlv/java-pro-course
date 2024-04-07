package org.example;

public enum JobTitle {
    TURNER( "Токарь"),
    LOCKSMITH( "Слесарь"),
    ENGINEER( "Инженер"),
    WORKER( "Разнорабочий");

    private final String jobTitleName;

    public String getJobTitleName() {
        return jobTitleName;
    }

    JobTitle(String jobTitleName) {
        this.jobTitleName = jobTitleName;
    }
}
