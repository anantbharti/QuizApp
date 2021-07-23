package com.example.bookworm.model;

public class Result {
    String testName;
    int ResultCode;
    int MarksObtained;
    int FullMarks;
    int TimeTakenInMin;
    String date;

    public Result() {
    }

    public Result(String testName, int resultCode, int marksObtained, int fullMarks, int timeTakenInMin, String date) {
        this.testName = testName;
        ResultCode = resultCode;
        MarksObtained = marksObtained;
        FullMarks = fullMarks;
        TimeTakenInMin = timeTakenInMin;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getResultCode() {
        return ResultCode;
    }

    public void setResultCode(int resultCode) {
        ResultCode = resultCode;
    }

    public int getMarksObtained() {
        return MarksObtained;
    }

    public void setMarksObtained(int marksObtained) {
        MarksObtained = marksObtained;
    }

    public int getFullMarks() {
        return FullMarks;
    }

    public void setFullMarks(int fullMarks) {
        FullMarks = fullMarks;
    }

    public int getTimeTakenInMin() {
        return TimeTakenInMin;
    }

    public void setTimeTakenInMin(int timeTakenInMin) {
        TimeTakenInMin = timeTakenInMin;
    }
}
