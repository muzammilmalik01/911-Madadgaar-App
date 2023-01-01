package com.alpha1.A911madadgaar.admin;

public class report {
    public String getReportno() {
        return reportno;
    }

    public report() {
    }

    public report(String reportno, String incident, String city) {
        this.reportno = reportno;
        this.incident = incident;
        this.city = city;
    }

    public void setReportno(String reportno) {
        this.reportno = reportno;
    }

    public String getIncident() {
        return incident;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String reportno,incident,city;
}
