package com.example.bookworm.model;

public class TeachersTest {
    String testName;
    int testCode;
    String date;
    int NoOfQues;

    public TeachersTest() {
    }

    public TeachersTest(String testName, int testCode, String date, int noOfQues) {
        this.testName = testName;
        this.testCode = testCode;
        this.date = date;
        NoOfQues = noOfQues;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getTestCode() {
        return testCode;
    }

    public void setTestCode(int testCode) {
        this.testCode = testCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNoOfQues() {
        return NoOfQues;
    }

    public void setNoOfQues(int noOfQues) {
        NoOfQues = noOfQues;
    }
}
