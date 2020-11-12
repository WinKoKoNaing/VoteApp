package com.porlar.techhousestudio.voteapp.models;

import java.util.Map;

/**
 * Created by USER on 12/26/2018.
 */

public class SelectionStudent {
    public String fbUri, studentName, rollNumber, sectionName, studentImageUri;
    public String gender;
    public boolean isSelection;

    public SelectionStudent() {
    }

    public int kingCount;
    public Map<String, Boolean> kings;
    public int queenCount;
    public Map<String, Boolean> queens;
    public int smartCount;
    public Map<String, Boolean> smarts;
    public int attractionCount;
    public Map<String, Boolean> attractions;
    public int mrpopularCount;
    public Map<String, Boolean> mrpopulars;
    public int mrspopularCount;
    public Map<String, Boolean> mrspopulars;
    public int gloryCount;
    public Map<String, Boolean> glorys;
    public int innocenceCount;
    public Map<String, Boolean> innocences;


    public int handsomeCount;
    public Map<String, Boolean> handsomes;

}
