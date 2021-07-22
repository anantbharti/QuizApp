package com.example.bookworm.model;

public class Question {
    String Que;
    String A,B,C,D;
    String Ans,UserAns;
    int QueNo;
    int Score;
    public Question() {
    }

    public Question(String que, String a, String b, String c, String d, String ans, String userAns, int queNo, int score) {
        Que = que;
        A = a;
        B = b;
        C = c;
        D = d;
        Ans = ans;
        Score = score;
        UserAns = userAns;
        QueNo = queNo;
    }

    public String getQue() {
        return Que;
    }

    public void setQue(String que) {
        Que = que;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }

    public int getQueNo() {
        return QueNo;
    }

    public void setQueNo(int queNo) {
        QueNo = queNo;
    }

    public String getUserAns() {
        return UserAns;
    }

    public void setUserAns(String userAns) {
        UserAns = userAns;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
