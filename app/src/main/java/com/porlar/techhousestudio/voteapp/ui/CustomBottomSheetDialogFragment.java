package com.porlar.techhousestudio.voteapp.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.porlar.techhousestudio.voteapp.R;
import com.porlar.techhousestudio.voteapp.adapters.ImageSlideAdapter;
import com.porlar.techhousestudio.voteapp.utils.SelectionImageUtils;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by USER on 12/25/2018.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private ViewPager pager;
    private CircleIndicator indicator;
    private List<String> imageList;
    private TextView tvName;
    private ImageView ivFb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageList = SelectionImageUtils.checkSelectionRollNumber(getArguments().getString("roll"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_image_slide_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);
        tvName = view.findViewById(R.id.tvName);
        ivFb = view.findViewById(R.id.ivFacebook);


        tvName.setText(getArguments().getString("name"));


        ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fbUri = getArguments().getString("fbUri", "No");
                if (fbUri.equals("No")){
                    Toasty.info(getContext(),"No connected Fb Acc",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = getFacebookIntent(fbUri);
                startActivity(intent);
            }
        });


        ImageSlideAdapter slideAdapter = new ImageSlideAdapter(getContext(), imageList);
        pager.setAdapter(slideAdapter);
        indicator.setViewPager(pager);
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
