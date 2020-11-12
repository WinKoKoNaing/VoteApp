package com.porlar.techhousestudio.voteapp.models;

import java.util.List;
import java.util.Map;

/**
 * Created by Porlar on 12/17/2018.
 */

public class Student {

    //for normal student
    public String fbUri, studentName, rollNumber, sectionName, studentImageUri;
    public String gender;
    public boolean isSelection;

    public int allKingCount;
    public Map<String, Boolean> allKings;
    public int allQueenCount;
    public Map<String, Boolean> allQueens;
    public int jockerCount;
    public Map<String, Boolean> jockers;


    public Student() {
    }

}
