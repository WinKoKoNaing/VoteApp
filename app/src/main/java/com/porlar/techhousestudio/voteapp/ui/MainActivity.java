package com.porlar.techhousestudio.voteapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.porlar.techhousestudio.voteapp.Constants;
import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.helpers.FontHelper;
import com.porlar.techhousestudio.voteapp.helpers.PSHLoading;
import com.porlar.techhousestudio.voteapp.models.CoupleStudent;
import com.porlar.techhousestudio.voteapp.models.SelectionStudent;
import com.porlar.techhousestudio.voteapp.models.Student;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

@SuppressWarnings({"deprecation", "unchecked"})
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Widget
    private LinearLayout layout_all_king, layout_all_queen, layout_king, layout_queen, layout_smart, layout_attraction, layout_glory, layout_innocence, layout_best_couple, layout_jocker, layout_handsome;
    private View fixError;
    private ListView lvSchedule;
    private ImageView ivDev, ivLock;
    private ImageView ivQueen, ivKing;
    private TextView tvCoupleCode;
    private NestedScrollView nestedScrollView;
    private TextView tvAllKingName, tvAllQueenName, tvJockerName, tvKingName, tvQueenName, tvSmartName, tvAttractionName, tvGloryName, tvInnocenceName, tvGirlName,tvBoyName, tvHandsomeName;
    private RelativeLayout layout_schedule, relativeLayout;
    //var
    private BottomSheetBehavior behavior;
    private String androidId;
    private DialogFragment dialogFragment = null, dialogFragment1 = null;
    private RequestOptions requestOptions;
    private List<String> scheduleList = new ArrayList<>();
    //FireBase
    DatabaseReference dbRooRef;

    @SuppressLint({"HardwareIds", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // listView
        lvSchedule = findViewById(R.id.lvSchedule);


        // Selection Image

        ivQueen = findViewById(R.id.ivQueen);
        ivKing = findViewById(R.id.ivKing);
        //findViewById
        fixError = findViewById(R.id.FixError);
        layout_all_king = findViewById(R.id.layout_all_king);
        layout_all_queen = findViewById(R.id.layout_all_queen);
        layout_king = findViewById(R.id.layout_king);
        layout_queen = findViewById(R.id.layout_queen);
        layout_smart = findViewById(R.id.layout_smart);
        layout_attraction = findViewById(R.id.layout_attraction);
        layout_glory = findViewById(R.id.layout_glory);
        layout_innocence = findViewById(R.id.layout_innocence);
        layout_best_couple = findViewById(R.id.layout_best_couple);
        layout_jocker = findViewById(R.id.layout_jocker);
        layout_handsome = findViewById(R.id.layout_handsome);






        //layout schedule
        layout_schedule = findViewById(R.id.layout_schedule);
        relativeLayout = findViewById(R.id.relative_layout);
        ivDev = findViewById(R.id.ivDev);
        ivLock = findViewById(R.id.ivLock);
        tvCoupleCode = findViewById(R.id.tvCoupleCode);
        //category name
        tvAllKingName = findViewById(R.id.tvAllKingName);
        tvAllQueenName = findViewById(R.id.tvAllQueenName);
        tvKingName = findViewById(R.id.tvKingName);
        tvQueenName = findViewById(R.id.tvQueenName);
        tvSmartName = findViewById(R.id.tvSmartName);
        tvAttractionName = findViewById(R.id.tvAttractionName);
        tvGloryName = findViewById(R.id.tvGloryName);
        tvInnocenceName = findViewById(R.id.tvInnocenceName);
        tvGirlName = findViewById(R.id.tvCoupleGirlName);
        tvBoyName = findViewById(R.id.tvCoupleBoyName);
        tvJockerName = findViewById(R.id.tvJockerName);
        tvHandsomeName = findViewById(R.id.tvHandsomeName);


        layout_all_queen.setOnClickListener(this);
        layout_all_king.setOnClickListener(this);
        layout_king.setOnClickListener(this);
        layout_queen.setOnClickListener(this);
        layout_smart.setOnClickListener(this);
        layout_attraction.setOnClickListener(this);
        layout_glory.setOnClickListener(this);
        layout_innocence.setOnClickListener(this);
        layout_best_couple.setOnClickListener(this);
        layout_jocker.setOnClickListener(this);
        layout_handsome.setOnClickListener(this);

        // disable
        relativeLayout.setEnabled(false);


        dbRooRef = FirebaseDatabase.getInstance().getReference();


        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        // fonts
        FontHelper.setCustomTypeface(tvAllKingName, "fonts/pt.ttf");

        View bottomSheet = findViewById(R.id.bottom_sheet);

        behavior = BottomSheetBehavior.from(bottomSheet);
        layout_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        ivDev.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fixError.setVisibility(View.VISIBLE);
                        ivDev.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        fixError.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ResultActivity.class));
            }
        });

    }

    public void onDevClick(View view) {
        DialogFragment dialogFragment = new CustomDeveloperDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "Developer");
    }

    @Override
    public void onBackPressed() {

        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        enableLayout();
        new FetchAsynchTask().execute();
        fetchScheduleData();

    }

    public void checkResultButton() {
        dbRooRef.child("others").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean check = dataSnapshot.child("isShow").getValue(Boolean.class);
                relativeLayout.setEnabled(check);
                if (check) {
                    ivLock.setImageResource(R.drawable.ic_lockopen);
                } else {
                    ivLock.setImageResource(R.drawable.ic_lock);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        ActivityOptionsCompat options = null;
        Pair<View, String> p1;
        Pair<View, String> p2;
        Pair<View, String> p3;
        switch (view.getId()) {
            case R.id.layout_all_king:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseStudentActivity.class);
                intent.putExtra("clickName", "ALLKING");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_all_king.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvAllKing), "title");
                p2 = Pair.create(findViewById(R.id.ivAllKing), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivityForResult(intent, Constants.All_KING_REQUEST_CODE, options.toBundle());
                break;
            case R.id.layout_all_queen:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseStudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_all_queen.setEnabled(false);
                intent.putExtra("clickName", "ALLQUEEN");
                p1 = Pair.create(findViewById(R.id.tvAllQueen), "title");
                p2 = Pair.create(findViewById(R.id.ivAllQueen), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivityForResult(intent, Constants.All_QUEEN_REQUEST_CODE, options.toBundle());
                break;
            case R.id.layout_king:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.putExtra("clickName", "KING");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_king.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvKing), "title");
                p2 = Pair.create(findViewById(R.id.ivKing), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_queen:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_queen.setEnabled(false);
                intent.putExtra("clickName", "QUEEN");
                p1 = Pair.create(findViewById(R.id.tvQueen), "title");
                p2 = Pair.create(findViewById(R.id.ivQueen), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_smart:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_smart.setEnabled(false);
                intent.putExtra("clickName", "SMART");
                p1 = Pair.create(findViewById(R.id.tvSmart), "title");
                p2 = Pair.create(findViewById(R.id.ivSmart), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_attraction:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_attraction.setEnabled(false);
                intent.putExtra("clickName", "ATTRACTION");
                p1 = Pair.create(findViewById(R.id.tvAttraction), "title");
                p2 = Pair.create(findViewById(R.id.ivAttraction), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_glory:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.putExtra("clickName", "GLORY");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_glory.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvGlory), "title");
                p2 = Pair.create(findViewById(R.id.ivGlory), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_innocence:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.putExtra("clickName", "INNOCENCE");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_innocence.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvInnocence), "title");
                p2 = Pair.create(findViewById(R.id.ivInnocence), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_best_couple:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseBestCoupleActivity.class);
                intent.putExtra("clickName", "BEST_COUPLE");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_best_couple.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvBestCouple), "title");
                p2 = Pair.create(findViewById(R.id.ivGirl), "category");
                p3 = Pair.create(findViewById(R.id.ivBoy), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2,p3);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_jocker:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseStudentActivity.class);
                intent.putExtra("clickName", "JOCKER");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_jocker.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvJocker), "title");
                p2 = Pair.create(findViewById(R.id.ivJocker), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;
            case R.id.layout_handsome:
                if (!isOnline()) {
                    Toasty.warning(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(getApplicationContext(), ChooseSelectionActivity.class);
                intent.putExtra("clickName", "HANDSOME");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                layout_handsome.setEnabled(false);
                p1 = Pair.create(findViewById(R.id.tvHandsome), "title");
                p2 = Pair.create(findViewById(R.id.ivHandsome), "category");
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
                startActivity(intent, options.toBundle());
                break;

        }

    }

    private void fetchSelectionStudentData() {
        showLoading();
        dbRooRef.child("selectionStudents").addChildEventListener(new ChildEventListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable final String s) {
                SelectionStudent std = dataSnapshot.getValue(SelectionStudent.class);

                if (std.kings.containsKey(androidId)) {
                    tvKingName.setText(std.studentName);
                    layout_king.setEnabled(false);
                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.king);
                    tvKingName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into(ivKing);
                    tvKingName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                if (std.queens.containsKey(androidId)) {
                    tvQueenName.setText(std.studentName);
                    layout_queen.setEnabled(false);
                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.queen);

                    tvQueenName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into(ivQueen);
                    tvQueenName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                if (std.smarts.containsKey(androidId)) {
                    tvSmartName.setText(std.studentName);
                    layout_smart.setEnabled(false);

                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.smart);
                    tvSmartName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivSmart));
                    tvSmartName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                if (std.attractions.containsKey(androidId)) {
                    tvAttractionName.setText(std.studentName);
                    layout_attraction.setEnabled(false);
                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.attraction);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivAttraction));
                    tvAttractionName.setTextColor(Color.WHITE);
                    tvAttractionName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }

                if (std.handsomes.containsKey(androidId)) {
                    tvHandsomeName.setText(std.studentName);
                    layout_handsome.setEnabled(false);
                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.handsome);
                    tvHandsomeName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivHandsome));
                    tvHandsomeName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                if (std.glorys.containsKey(androidId)) {
                    tvGloryName.setText(std.studentName);
                    layout_glory.setEnabled(false);

                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.glory);

                    tvGloryName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivGlory));
                    tvGloryName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }

                if (std.innocences.containsKey(androidId)) {
                    tvInnocenceName.setText(std.studentName);
                    layout_innocence.setEnabled(false);

                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.innocence);
                    tvInnocenceName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivInnocence));
                    tvInnocenceName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                try {
                    if (dialogFragment != null) {
                        dismissDialog();
                    }
                } catch (Exception e) {

                }


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

    private void fetchStudentData() {


        dialogFragment1 = new PSHLoading();
        dialogFragment1.show(getSupportFragmentManager(), "hello");
        dialogFragment1.setCancelable(false);
        dbRooRef.child("students").addChildEventListener(new ChildEventListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable final String s) {
                Student std = dataSnapshot.getValue(Student.class);
                if (std.allKings.containsKey(androidId)) {
                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.allking);

                    tvAllKingName.setText(std.studentName);
                    layout_all_king.setEnabled(false);
                    tvAllKingName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivAllKing));
                    tvAllKingName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                if (std.allQueens.containsKey(androidId)) {

                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.allqueen);

                    tvAllQueenName.setText(std.studentName);
                    layout_all_queen.setEnabled(false);
                    tvAllQueenName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivAllQueen));
                    tvAllQueenName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }

                if (std.jockers.containsKey(androidId)) {

                    requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.jocker);

                    tvJockerName.setText(std.studentName);
                    layout_jocker.setEnabled(false);
                    tvJockerName.setTextColor(Color.WHITE);
                    Glide.with(MainActivity.this).load(std.studentImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivJocker));
                    tvJockerName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
                try {
                    if (dialogFragment1 != null) {
                        dialogFragment1.dismiss();
                    }
                } catch (Exception e) {

                }


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

    private void showLoading() {
        dialogFragment = new PSHLoading();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        dialogFragment.setCancelable(false);
    }



    private void dismissDialog() {
        dialogFragment.dismiss();
    }

//    public void animatedTextView(TextView animatedTextView) {
//        animatedTextView.createStrokeAnimator(0.05f).start();
//        animatedTextView.createItalicAnimator(0.4f)
//                .setDuration(1000)
//                .start();
//    }

    public void enableLayout() {
        layout_all_king.setEnabled(true);
        layout_all_queen.setEnabled(true);
        layout_king.setEnabled(true);
        layout_queen.setEnabled(true);
        layout_smart.setEnabled(true);
        layout_attraction.setEnabled(true);
        layout_handsome.setEnabled(true);
        layout_glory.setEnabled(true);
        layout_jocker.setEnabled(true);
        layout_innocence.setEnabled(true);
        layout_best_couple.setEnabled(true);
    }

    private void fetchStudentCoupleData() {
        dbRooRef.child("coupleStudents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable final String s) {
                CoupleStudent student = dataSnapshot.getValue(CoupleStudent.class);
                if (student.bestCouples.containsKey(androidId)) {
                    layout_best_couple.setEnabled(false);
                    tvBoyName.setText(student.boyName);
                    tvGirlName.setText(student.girlName);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.bestcouple);
                    Glide.with(MainActivity.this).load(student.girlImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivGirl));
                    Glide.with(MainActivity.this).load(student.boyImageUri).apply(requestOptions).into((ImageView) findViewById(R.id.ivBoy));
                    tvBoyName.setTextColor(Color.WHITE);
                    tvCoupleCode.setText(student.coupleCode+"");
                    tvBoyName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                    tvGirlName.setTextColor(Color.WHITE);
                    tvGirlName.setBackground(getResources().getDrawable(R.drawable.voted_success_style));
                }
//
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


    class FetchAsynchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            new LooperThread().start();
            checkResultButton();

            fetchSelectionStudentData();
            fetchStudentData();
            fetchStudentCoupleData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class LooperThread extends Thread {

        int i;

        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();
            final Handler mHandler = new Handler();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        if (dialogFragment.isVisible() || dialogFragment1.isVisible()) {
                            dialogFragment.dismiss();
                            dialogFragment1.dismiss();
                            Dialog alertDialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            View errorView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_error_dialog, null, false);
                            alertDialog.setContentView(errorView);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                            errorView.findViewById(R.id.btnCloseApp).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            });

            Looper.loop();
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void fetchScheduleData(){
        if (!scheduleList.isEmpty()){
            scheduleList.clear();
        }
        dbRooRef.child("others").child("schedule").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String row = dataSnapshot.getValue(String.class);
                scheduleList.add(row);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, scheduleList);

                lvSchedule.setAdapter(arrayAdapter);
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
}
