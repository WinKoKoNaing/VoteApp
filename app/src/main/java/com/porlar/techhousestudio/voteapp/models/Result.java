package com.porlar.techhousestudio.voteapp.models;

/**
 * Created by USER on 1/4/2019.
 */

public class Result {
    public String rollNumber,studentName;
    public int voteCount;

    public Result(String rollNumber, String studentName, int voteCount) {
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.voteCount = voteCount;
    }
}
