package com.example.bookworm.model;

import java.util.HashMap;

public class Test {
    int TestCode;
    int FullMarks;
    int MarksObtained;
    HashMap<String,Integer> UserScore= new HashMap<String, Integer>();
    HashMap<String,String> UserAnswer= new HashMap<String, String>();

    public Test() {
    }

    public Test(int testCode, int fullMarks, int marksObtained, HashMap<String, Integer> userScore, HashMap<String, String> userAnswer) {
        TestCode = testCode;
        FullMarks = fullMarks;
        MarksObtained = marksObtained;
        UserScore = userScore;
        UserAnswer = userAnswer;
    }

    public int getTestCode() {
        return TestCode;
    }

    public void setTestCode(int testCode) {
        TestCode = testCode;
    }

    public int getFullMarks() {
        return FullMarks;
    }

    public void setFullMarks(int fullMarks) {
        FullMarks = fullMarks;
    }

    public int getMarksObtained() {
        return MarksObtained;
    }

    public void setMarksObtained(int marksObtained) {
        MarksObtained = marksObtained;
    }

    public HashMap<String, Integer> getUserScore() {
        return UserScore;
    }

    public void setUserScore(HashMap<String, Integer> userScore) {
        UserScore = userScore;
    }

    public HashMap<String, String> getUserAnswer() {
        return UserAnswer;
    }

    public void setUserAnswer(HashMap<String, String> userAnswer) {
        UserAnswer = userAnswer;
    }
}
