package com.porlar.techhousestudio.voteapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.adapters.StudentAdapter;
import com.porlar.techhousestudio.voteapp.helpers.PSHLoading;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnStudentClickListener;
import com.porlar.techhousestudio.voteapp.models.Student;
import com.porlar.techhousestudio.voteapp.models.Tag;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

@SuppressWarnings({"deprecation", "unchecked"})
public class ChooseStudentActivity extends AppCompatActivity implements FilterListener<Tag> {
    //widgets
    private Filter<Tag> mFilter;
    private RecyclerView rvStudentList;
    private TextView tvTitle;
    private ProgressBar progressBar;
    //FireBase
    DatabaseReference dbStudentRef, dbRootRef;
    Query studentQuery;
    //var
    private SharedPreferences preference;
    private int[] mColors;
    private String[] mTitles;
    private List<Student> studentList = new ArrayList<>();
    private StudentAdapter studentAdapter;
    private String androidId;
    private Student studentData;
    private String clickName;
    private DialogFragment dialogFragment;
    private ImageView ivCategory;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_student);

        preference = getSharedPreferences("password", MODE_PRIVATE);

        MDetect.INSTANCE.init(this);
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.years_title);


        mFilter = findViewById(R.id.filter);
        rvStudentList = findViewById(R.id.rvStudentList);
        tvTitle = findViewById(R.id.tvTitle);
        ivCategory = findViewById(R.id.ivCategory);
        progressBar = findViewById(R.id.progressBar);


        clickName = getIntent().getExtras().getString("clickName");
        setCategoryImage(clickName);


        tvTitle.setText(clickName);
        dbRootRef = FirebaseDatabase.getInstance().getReference();
        dbStudentRef = dbRootRef.child("students");


        rvStudentList.setLayoutManager(new LinearLayoutManager(this));
        mFilter.setAdapter(new com.porlar.techhousestudio.voteapp.adapters.Adapter(this, getTags()));
        mFilter.setListener(this);
        mFilter.setNoSelectedItemText(getString(R.string.str_all_selected));
        mFilter.build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clickName.equals("ALLKING") || clickName.equals("JOCKER")) {
            studentQuery = dbStudentRef.orderByChild("gender").equalTo("Male");
        } else if (clickName.equals("ALLQUEEN")) {
            studentQuery = dbStudentRef.orderByChild("gender").equalTo("Female");
        }
        fetchStudentData();
    }

    private void fetchStudentData() {
        if (!studentList.isEmpty()) {
            studentList.clear();
        }
        progressBar.setVisibility(View.VISIBLE);
        studentQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable final String s) {
                Student student = dataSnapshot.getValue(Student.class);
                studentList.add(student);
                studentAdapter = new StudentAdapter(ChooseStudentActivity.this, studentList);
                studentAdapter.setDefault_photo(setCategoryImage(clickName));
                studentAdapter.setOnStudentClickListener(new SetOnStudentClickListener() {
                    @Override
                    public void studentOnClick(Student student) {
                        studentData = student;
                        tvTitle.setText(student.studentName);
                    }
                });
                rvStudentList.setAdapter(studentAdapter);


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
        progressBar.setVisibility(View.GONE);
    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Tag(mTitles[i], mColors[i]));
        }

        return tags;
    }

    public void voteOnClick(View view) {
        String pass = preference.getString("pass", "No");
        if (tvTitle.getText().toString().equals("ALLKING")
                || tvTitle.getText().toString().equals("ALLQUEEN")
                || tvTitle.getText().toString().equals("JOCKER")) {
            Snackbar.make(findViewById(R.id.layout_choose_student), "Select a student bro", Snackbar.LENGTH_LONG).show();
            return;
        } else if (!isOnline()) {
            Snackbar.make(findViewById(R.id.layout_choose_student), "No Network Connection", Snackbar.LENGTH_LONG).show();
            return;
        } else if (pass.equals("No")) {
            Snackbar.make(findViewById(R.id.layout_choose_student), "no found secret code", Snackbar.LENGTH_LONG).setAction("Insert Pin Code", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChooseStudentActivity.this, LockActivity.class));
                    finish();
                }
            }).show();
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseStudentActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("Be Careful ....");
            builder.setIcon(R.drawable.ic_warning_white_48dp);
            builder.setMessage("If you have clicked vote button, you cannot try again");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showLoading();
                    switch (clickName)
                    {
                        case "ALLKING":
                            onKingVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "ALLQUEEN":
                            onQueenVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "JOCKER":
                            onJockerVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                    }
                }
            });

            builder.show();
        }


    }


    private List<Student> findByTags(List<Tag> tags) {
        List<Student> studentsLists = new ArrayList<>();
        if (!studentsLists.isEmpty()) {
            studentsLists.clear();
        }
        ;
        for (Student question : studentList) {
            for (Tag tag : tags) {
                if (question.rollNumber.startsWith(tag.getText()) && !studentsLists.contains(question)) {
                    studentsLists.add(question);

                }
            }
        }

        return studentsLists;
    }


    //Filter Listener
    @Override
    public void onFiltersSelected(ArrayList<Tag> filters) {
        List<Student> newStudents = findByTags(filters);
        List<Student> oldStudent = studentAdapter.getStudentList();
        studentAdapter.setStudentList(newStudents);
        calculateDiff(oldStudent, newStudents);
    }

    @Override
    public void onNothingSelected() {
        if (rvStudentList != null && studentAdapter != null) {
            studentAdapter.setStudentList(studentList);
            studentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFilterSelected(Tag item) {
        if (item.getText().equals(mTitles[0])) {
            mFilter.deselectAll();
            mFilter.collapse();
        }
    }

    @Override
    public void onFilterDeselected(Tag tag) {

    }


    private void calculateDiff(final List<Student> oldList, final List<Student> newList) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        }).dispatchUpdatesTo(studentAdapter);
    }

    private void onKingVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Student p = mutableData.getValue(Student.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.allKings.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.allKingCount += 1;
                    p.allKings.put(androidId, true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                dismissDialog();
                finish();
            }
        });
    }

    private void onQueenVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Student p = mutableData.getValue(Student.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.allQueens.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.allQueenCount += 1;
                    p.allQueens.put(androidId, true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                dismissDialog();
                finish();
            }
        });
    }

    private void onJockerVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Student p = mutableData.getValue(Student.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.jockers.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.jockerCount += 1;
                    p.jockers.put(androidId, true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                dismissDialog();
                finish();
            }
        });
    }


    private void showLoading() {
        dialogFragment = new PSHLoading();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        dialogFragment.setCancelable(false);
    }

    private void dismissDialog() {
        dialogFragment.dismiss();
    }


    private int setCategoryImage(String clickName) {
        int default_photo = -1;
        switch (clickName) {
            case "ALLKING":
                ivCategory.setImageResource(R.drawable.allking);
                default_photo = R.drawable.allking;
                break;
            case "ALLQUEEN":
                ivCategory.setImageResource(R.drawable.allqueen);
                default_photo = R.drawable.allqueen;
                break;
            case "JOCKER":
                ivCategory.setImageResource(R.drawable.jocker);
                default_photo = R.drawable.jocker;
                break;
        }
        return default_photo;
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
