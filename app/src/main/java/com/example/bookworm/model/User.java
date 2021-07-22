package com.example.bookworm.model;

public class User {
    private String Name;
    private String Email;
    private String Password;
    private String Report;
    private int Cs;
    private int Cqn;
    private int Ctc;
    private String Designation;

    public User() {
    }

    public User(String name, String email, String password, String report, int cs, String designation, int ctc, int cqn) {
        Name = name;
        Email = email;
        Password = password;
        Report = report;
        Cs = cs;
        Cqn = cqn;
        Ctc = ctc;
        Designation= designation;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public int getCs() {
        return Cs;
    }

    public void setCs(int cs) {
        Cs = cs;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public int getCqn() {
        return Cqn;
    }

    public void setCqn(int cqn) {
        Cqn = cqn;
    }

    public int getCtc() {
        return Ctc;
    }

    public void setCtc(int ctc) {
        Ctc = ctc;
    }
}
