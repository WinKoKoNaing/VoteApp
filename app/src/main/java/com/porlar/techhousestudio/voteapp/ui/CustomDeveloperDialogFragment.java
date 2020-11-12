package com.porlar.techhousestudio.voteapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.porlar.techhousestudio.voteapp.R;

/**
 * Created by USER on 12/31/2018.
 */

public class CustomDeveloperDialogFragment extends DialogFragment implements View.OnClickListener {
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.developer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvwkkn = view.findViewById(R.id.tvWkkn);
        TextView tvkmmh = view.findViewById(R.id.tvkmmt);
        TextView tvndmt = view.findViewById(R.id.tvndmt);
        Button btnReport = view.findViewById(R.id.btnReport);


        tvkmmh.setOnClickListener(this);
        tvwkkn.setOnClickListener(this);
        tvndmt.setOnClickListener(this);
        btnReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.tvWkkn:
                intent = getFacebookIntent("https://www.facebook.com/winkokonaing2122016");
                break;
            case R.id.tvkmmt:
                intent = getFacebookIntent("https://www.facebook.com/kaung.kmmh");
                break;
            case R.id.tvndmt:
                intent = getFacebookIntent("https://www.facebook.com/nanda.thu.31");
                break;
            case R.id.btnReport:
                intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", "09976316188", null));

                break;
        }
        startActivity(intent);
    }


    public Intent getFacebookIntent(String url) {

        PackageManager pm = getActivity().getPackageManager();
        Uri uri = Uri.parse(url);

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return new Intent(Intent.ACTION_VIEW, uri);
    }

}
