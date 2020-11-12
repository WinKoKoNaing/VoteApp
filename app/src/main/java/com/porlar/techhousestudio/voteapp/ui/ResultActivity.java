package com.porlar.techhousestudio.voteapp.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.stetho.inspector.protocol.module.Network;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.adapters.ResultAdapter;
import com.porlar.techhousestudio.voteapp.adapters.StudentAdapter;
import com.porlar.techhousestudio.voteapp.models.CoupleStudent;
import com.porlar.techhousestudio.voteapp.models.Result;
import com.porlar.techhousestudio.voteapp.models.SelectionStudent;
import com.porlar.techhousestudio.voteapp.models.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ResultActivity extends AppCompatActivity {
    //widget
    private RecyclerView rvAllKing, rvAllQueen, rvKing, rvQueen, rvSmart, rvAttraction, rvHandsome, rvGlory, rvJocker, rvInnocence, rvBestCouple;
    //FireBase
    DatabaseReference rootRef, selectionRef, studentRef, coupleRef;
    //var


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //findViewById
        rvAllKing = findViewById(R.id.rvAllKing);
        rvAllQueen = findViewById(R.id.rvAllQueen);
        rvKing = findViewById(R.id.rvKing);
        rvQueen = findViewById(R.id.rvQueen);
        rvSmart = findViewById(R.id.rvSmart);
        rvAttraction = findViewById(R.id.rvAttraction);
        rvHandsome = findViewById(R.id.rvHandsome);
        rvGlory = findViewById(R.id.rvGlory);
        rvJocker = findViewById(R.id.rvJocker);
        rvInnocence = findViewById(R.id.rvInnocence);
        rvBestCouple = findViewById(R.id.rvBestCouple);

        rootRef = FirebaseDatabase.getInstance().getReference();
        selectionRef = FirebaseDatabase.getInstance().getReference().child("selectionStudents");
        studentRef = FirebaseDatabase.getInstance().getReference().child("students");
        coupleRef = FirebaseDatabase.getInstance().getReference().child("coupleStudents");


    }

    @Override
    protected void onResume() {
        super.onResume();

//        checkResultButton();

        fetchTopAllKing();
        fetchTopAllQueen();
        fetchTopJocker();
        //selection
        fetchTopKing();
        fetchTopSmart();
        fetchTopQueen();
        fetchTopAttraction();
        fetchTopHandsome();
        fetchTopGlory();
        fetchTopInnocence();
        fetchTopBestCouple();
    }

    private void fetchTopAllKing() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvAllKing.setLayoutManager(linearLayoutManager);


        final List<Result> studentList;
        studentList = new ArrayList<>();

        studentRef.orderByChild("allKingCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student.gender.equals("Male")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.allKingCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvAllKing.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchTopAllQueen() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvAllQueen.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        studentRef.orderByChild("allQueenCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student.gender.equals("Female")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.allQueenCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvAllQueen.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopJocker() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvJocker.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        studentRef.orderByChild("jockerCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student.gender.equals("Male")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.jockerCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvJocker.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopKing() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvKing.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        selectionRef.orderByChild("kingCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Male")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.kingCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvKing.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopQueen() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvQueen.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        selectionRef.orderByChild("queenCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Female")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.queenCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvQueen.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopSmart() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvSmart.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        selectionRef.orderByChild("smartCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Male")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.smartCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvSmart.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopAttraction() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvAttraction.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        selectionRef.orderByChild("attractionCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Female")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.attractionCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvAttraction.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void fetchTopHandsome() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvHandsome.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        selectionRef.orderByChild("handsomeCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Male")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.handsomeCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvHandsome.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopGlory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvGlory.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        selectionRef.orderByChild("gloryCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Female")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.gloryCount);
                    studentList.add(result);
                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvGlory.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopInnocence() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvInnocence.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();
        selectionRef.orderByChild("innocenceCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                if (student.gender.equals("Female")) {
                    Result result = new Result(student.rollNumber, student.studentName, student.innocenceCount);
                    studentList.add(result);

                }

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvInnocence.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchTopBestCouple() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvBestCouple.setLayoutManager(linearLayoutManager);

        final List<Result> studentList;
        studentList = new ArrayList<>();

        coupleRef.orderByChild("bestCoupleCount").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CoupleStudent student = dataSnapshot.getValue(CoupleStudent.class);
                Result result = new Result("Code " + student.coupleCode + "", student.boyName + "\n + \n" + student.girlName, student.bestCoupleCount);
                studentList.add(result);

                ResultAdapter studentAdapter = new ResultAdapter(ResultActivity.this, studentList);
                rvBestCouple.setAdapter(studentAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void checkResultButton() {
        rootRef.child("others").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean check = dataSnapshot.child("isShow").getValue(Boolean.class);
                if (check) {
                    Toasty.success(ResultActivity.this, "Result Show is opened Now", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(ResultActivity.this, "Result Show is Closed Now", Toast.LENGTH_SHORT).show();
                    finishAndRemoveTask();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
