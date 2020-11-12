package com.porlar.techhousestudio.voteapp;

import com.google.firebase.database.FirebaseDatabase;

import me.myatminsoe.mdetect.MDetect;


/**
  Created by USER on 12/17/2018.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MDetect.INSTANCE.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
