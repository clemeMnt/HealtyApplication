package com.exemple.healtyapplication.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.exemple.healtyapplication.R;


public class SettingFragment extends Fragment {

    private TextView feedBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View root = inflater.inflate(R.layout.fragment_setting, container, false);
       feedBack =  (TextView) root.findViewById(R.id.button_feedBack);



       feedBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendFeedback(getActivity());
           }
       });
       return root;
    }



    public static void sendFeedback(Activity context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n " +
                    "Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"clementmarchais@orange.fr"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback HeatlCare");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

}