package com.porlar.techhousestudio.voteapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.adapters.BestCoupleAdapter;
import com.porlar.techhousestudio.voteapp.adapters.StudentAdapter;
import com.porlar.techhousestudio.voteapp.helpers.PSHLoading;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnBestCoupleImageListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnBestCoupleListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnImageClickListener;
import com.porlar.techhousestudio.voteapp.interfaces.SetOnStudentClickListener;
import com.porlar.techhousestudio.voteapp.models.CoupleStudent;
import com.porlar.techhousestudio.voteapp.models.SelectionStudent;
import com.porlar.techhousestudio.voteapp.models.Student;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

@SuppressWarnings({"deprecation", "unchecked"})
public class ChooseBestCoupleActivity extends AppCompatActivity {
    //widget
    private RecyclerView rvStudentList;
    private TextView tvTitle;
    //FireBase
    DatabaseReference dbStudentRef, dbRootRef;

    //var
    DialogFragment dialogFragment;
    private String androidId, clickName;
    private BestCoupleAdapter studentAdapter;
    private List<CoupleStudent> coupleStudentList = new ArrayList<>();
    private CoupleStudent studentData;
    private SharedPreferences preference;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_best_couple);
        preference = getSharedPreferences("password", MODE_PRIVATE);
        //findViewById
        tvTitle = findViewById(R.id.tvTitle);

        dbRootRef = FirebaseDatabase.getInstance().getReference();
        dbStudentRef = dbRootRef.child("coupleStudents");

        clickName = getIntent().getExtras().getString("clickName");
        tvTitle.setText(clickName);
        //android Id
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        rvStudentList = findViewById(R.id.rvStudentList);
        rvStudentList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchStudentData();
    }

    private void fetchStudentData() {
        if (!coupleStudentList.isEmpty()) {
            coupleStudentList.clear();
        }
        dbStudentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable final String s) {
                CoupleStudent student = dataSnapshot.getValue(CoupleStudent.class);

                try {
                    coupleStudentList.add(student);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                studentAdapter = new BestCoupleAdapter(ChooseBestCoupleActivity.this, coupleStudentList);
                studentAdapter.setSetOnBestCoupleListener(new SetOnBestCoupleListener() {
                    @Override
                    public void bestCoupleOnClick(CoupleStudent student) {
                        studentData = student;
                        tvTitle.setText(student.coupleCode + "");
                    }
                });
                studentAdapter.setSetOnBestCoupleImageListener(new SetOnBestCoupleImageListener() {
                    @Override
                    public void onBestCoupleImageClick(CoupleStudent student, boolean isBoy) {
                        BottomSheetDialogFragment dialogFragment = new CustomBottomSheetDialogFragment();
                        Bundle bundle = new Bundle();
                        if (isBoy) {
                            bundle.putString("roll", student.boyRollNumber);
                            bundle.putString("name", student.boyName);
                            bundle.putString("fbUri", student.boyFbUri);
                        } else {
                            bundle.putString("roll", student.girlRollNumber);
                            bundle.putString("fbUri", student.girlFbUri);
                            bundle.putString("name", student.girlName);
                        }


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
    }

    public void voteOnClick(View view) {
        String pass = preference.getString("pass", "No");
        if (tvTitle.getText().toString().equals("BEST_COUPLE")) {
            Toasty.warning(ChooseBestCoupleActivity.this, "Select a student", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isOnline()) {
            Toasty.warning(ChooseBestCoupleActivity.this, "no internet connection", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass.equals("No")) {
            Snackbar.make(findViewById(R.id.layout_choose_couple), "no found secret code", Snackbar.LENGTH_LONG).setAction("Insert Pin Code", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChooseBestCoupleActivity.this, LockActivity.class));
                    finish();
                }
            }).show();
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseBestCoupleActivity.this, android.R.style.Theme_Material_Dialog_Alert);
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
                    onInnocenceVoteClick(dbStudentRef.child(studentData.coupleCode + ""));
                }
            });

            builder.show();

        }

    }

    private void onInnocenceVoteClick(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                CoupleStudent p = mutableData.getValue(CoupleStudent.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                if (!p.bestCouples.containsKey(androidId)) {
                    // Unstar the post and remove self from stars
                    // Star the post and add self to stars
                    p.bestCoupleCount += 1;
                    p.bestCouples.put(androidId, true);
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
