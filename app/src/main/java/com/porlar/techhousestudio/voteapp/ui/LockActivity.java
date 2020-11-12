package com.porlar.techhousestudio.voteapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.goodiebag.pinview.Pinview;
import com.google.zxing.Result;
import com.porlar.techhousestudio.voteapp.R;

import es.dmoral.toasty.Toasty;

import static com.porlar.techhousestudio.voteapp.Constants.MY_PERMISSIONS_REQUEST_CAMER;
import static com.porlar.techhousestudio.voteapp.Constants.SECRET_CODE;


public class LockActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private SharedPreferences preferences;
    private CodeScannerView scannerView;
    private Pinview pinview;
    private boolean isShown = false;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        preferences = getSharedPreferences("password", MODE_PRIVATE);


        pinview = findViewById(R.id.pinview);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (result.getText().equals(SECRET_CODE)) {
                            savePass(result.getText().trim());
                            Toasty.success(LockActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LockActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toasty.info(LockActivity.this, "Not Equal Scanner Code", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });


        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCodeScanner.startPreview();
            }
        });

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                Log.d("WKKN", pinview.getValue());
                if (pinview.getValue().equals(SECRET_CODE)) {
                    savePass(pinview.getValue());
                    Intent intent = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(LockActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LockActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMER);

        } else {
            // Permission has already been granted

            if (isPass()) {
                Intent intent = new Intent(LockActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            mCodeScanner.startPreview();
        }


    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void savePass(String pass) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pass", pass);
        editor.apply();
    }

    private boolean isPass() {
        String pass = preferences.getString("pass", "No");
        if (pass.equals("No")) {
            return false;
        } else if (pass.equals(SECRET_CODE)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMER: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCodeScanner.startPreview();
                } else {
                    Toasty.error(LockActivity.this, "Permission Fail", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void onTogglePassInput(View view) {
        if (!isOnline()) {
            Toasty.info(LockActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isShown) {
            findViewById(R.id.layout_textLock).setVisibility(View.GONE);
            scannerView.setVisibility(View.VISIBLE);
            isShown = false;
        } else {
            scannerView.setVisibility(View.GONE);
            findViewById(R.id.layout_textLock).setVisibility(View.VISIBLE);
            isShown = true;
        }


    }

    public void onSkipClick(View view) {
        if (!isOnline()) {
            Toasty.info(LockActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(LockActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
