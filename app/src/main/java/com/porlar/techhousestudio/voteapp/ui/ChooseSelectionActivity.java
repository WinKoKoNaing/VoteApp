package com.porlar.techhousestudio.voteapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.adapters.ImageSlideAdapter;
import com.porlar.techhousestudio.voteapp.adapters.SelectionstudentAdapter;
import com.porlar.techhousestudio.voteapp.adapters.StudentAdapter;
import com.porlar.techhousestudio.voteapp.helpers.PSHLoading;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnImageClickListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnSelectionImageClickListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnStudentClickListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetSelectionStudentClickListener;
import com.porlar.techhousestudio.voteapp.models.SelectionStudent;
import com.porlar.techhousestudio.voteapp.models.Student;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

@SuppressWarnings({"deprecation", "unchecked"})
public class ChooseSelectionActivity extends AppCompatActivity {
    //widget
    private TextView tvTitle;
    private RecyclerView rvStudentList;
    private ImageView ivCategory;
    private ProgressBar progressBar;
    //FireBase
    DatabaseReference dbStudentRef, dbRootRef;
    private Query query;
    //var
    private List<SelectionStudent> studentList = new ArrayList<>();
    private SelectionstudentAdapter studentAdapter;
    private String androidId;
    private SelectionStudent studentData;
    private String clickName;
    private DialogFragment dialogFragment;
    private SharedPreferences preference;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_selection);

        preference = getSharedPreferences("password", MODE_PRIVATE);

        //android Id
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //findViewById
        tvTitle = findViewById(R.id.tvTitle);
        rvStudentList = findViewById(R.id.rvStudentList);
        ivCategory = findViewById(R.id.ivCategory);
        progressBar = findViewById(R.id.progressBar);
        clickName = getIntent().getExtras().getString("clickName");
        tvTitle.setText(clickName);
        setCategoryImage(clickName);

        dbRootRef = FirebaseDatabase.getInstance().getReference();
        dbStudentRef = dbRootRef.child("selectionStudents");
        rvStudentList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clickName.equals("KING")
                || clickName.equals("SMART")
                || clickName.equals("HANDSOME")) {
            query = dbStudentRef.orderByChild("gender").equalTo("Male");

        } else {
            query = dbStudentRef.orderByChild("gender").equalTo("Female");
        }
        fetchStudentData();
    }

    public void voteOnClick(View view) {
        String pass = preference.getString("pass", "No");
        if (tvTitle.getText().toString().equals("KING")
                || tvTitle.getText().toString().equals("SMART")
                || tvTitle.getText().toString().equals("HANDSOME")
                || tvTitle.getText().toString().equals("QUEEN")
                || tvTitle.getText().toString().equals("ATTRACTION")
                || tvTitle.getText().toString().equals("GLORY")
                || tvTitle.getText().toString().equals("INNOCENCE")
                ) {
            Snackbar.make(findViewById(R.id.layout_selection), "Select a student bro", Snackbar.LENGTH_LONG).show();

            return;
        } else if (!isOnline()) {
            Toasty.warning(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else if (pass.equals("No")) {
            Snackbar.make(findViewById(R.id.layout_selection), "no found secret code", Snackbar.LENGTH_LONG).setAction("Insert Pin Code", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChooseSelectionActivity.this, LockActivity.class));
                    finish();
                }
            }).show();
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseSelectionActivity.this, android.R.style.Theme_Material_Dialog_Alert);
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
                    switch (clickName) {
                        case "KING":
                            onKingVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "QUEEN":
                            onQueenVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "SMART":
                            onSmartVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "ATTRACTION":
                            onAttractionVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "HANDSOME":
                            onHandsomeVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "GLORY":
                            onGloryVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                        case "INNOCENCE":
                            onInnocenceVoteClick(dbStudentRef.child(studentData.rollNumber));
                            break;
                    }
                }
            });

            builder.show();
        }


    }


    private void fetchStudentData() {
        if (!studentList.isEmpty()) {
            studentList.clear();
        }
        progressBar.setVisibility(View.VISIBLE);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable final String s) {
                SelectionStudent student = dataSnapshot.getValue(SelectionStudent.class);
                try {
                    studentList.add(student);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                studentAdapter = new SelectionstudentAdapter(ChooseSelectionActivity.this, studentList);
                studentAdapter.setDefault_photo(setCategoryImage(clickName));

//                studentAdapter.setOnStudentClickListener(new SetOnStudentClickListener() {
//                    @Override
//                    public void studentOnClick(Student student) {
//
//                    }
//                });
                studentAdapter.setOnStudentClickListener(new SetSelectionStudentClickListener() {
                    @Override
                    public void selectionStudentOnClick(SelectionStudent student) {
                        studentData = student;
                        tvTitle.setText(student.studentName);
                        Glide.with(ChooseSelectionActivity.this).load(student.studentImageUri).into(ivCategory);
                    }
                });

                studentAdapter.setOnImageClickListener(new SetOnSelectionImageClickListener() {
                    @Override
                    public void onStudentOnClick(SelectionStudent student) {
                        BottomSheetDialogFragment dialogFragment = new CustomBottomSheetDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("roll", student.rollNumber);
                        bundle.putString("name", student.studentName);
                        bundle.putString("fbUri", student.fbUri);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getSupportFragmentManager(), "dialog");
                    }
                });
                rvStudentList.setAdapter(studentAdapter);

//                rvStudentList.setItemAnimator(new FiltersListItemAnimator());
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

    private int setCategoryImage(String clickName) {
        int default_photo = -1;
        switch (clickName) {
            case "KING":
                ivCategory.setImageResource(R.drawable.king);
                default_photo = R.drawable.king;
                break;
            case "QUEEN":
                ivCategory.setImageResource(R.drawable.queen);
                default_photo = R.drawable.queen;
                break;
            case "SMART":
                ivCategory.setImageResource(R.drawable.smart);
                default_photo = R.drawable.smart;
                break;
            case "ATTRACTION":
                ivCategory.setImageResource(R.drawable.attraction);
                default_photo = R.drawable.attraction;
                break;
            case "HANDSOME":
                ivCategory.setImageResource(R.drawable.handsome);
                default_photo = R.drawable.handsome;
                break;
            case "GLORY":
                ivCategory.setImageResource(R.drawable.glory);
                default_photo = R.drawable.glory;
                break;
            case "JOCKER":
                ivCategory.setImageResource(R.drawable.jocker);
                default_photo = R.drawable.jocker;
                break;
            case "INNOCENCE":
                ivCategory.setImageResource(R.drawable.innocence);
                default_photo = R.drawable.innocence;
                break;

        }
        return default_photo;
    }

    private void onKingVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.kings.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.kingCount += 1;
                    p.kings.put(androidId, true);
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
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.queens.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.queenCount += 1;
                    p.queens.put(androidId, true);
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

    private void onSmartVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.smarts.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.smartCount += 1;
                    p.smarts.put(androidId, true);
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

    private void onAttractionVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.attractions.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.attractionCount += 1;
                    p.attractions.put(androidId, true);
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


    private void onHandsomeVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.handsomes.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.handsomeCount += 1;
                    p.handsomes.put(androidId, true);
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

    private void onGloryVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.glorys.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.gloryCount += 1;
                    p.glorys.put(androidId, true);
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

    private void onInnocenceVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                SelectionStudent p = mutableData.getValue(SelectionStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.innocences.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.innocenceCount += 1;
                    p.innocences.put(androidId, true);
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

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
