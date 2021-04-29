package com.exemple.healtyapplication.ui.detected;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.model.DetectedActivitiesAdapter;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class DetectedFragment extends Fragment   implements SharedPreferences.OnSharedPreferenceChangeListener{
    protected static final String TAG = "MainActivity";

    Context mContent;


    private ActivityRecognitionClient mActivityRecognitionClient;

    private Button mRequestActivityUpdatesButton, mRemoveActivityUpdatesButton;

    private DetectedActivitiesAdapter mAdapter;

    View root;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_detected, container, false);
        mContent = container.getContext();

        mRequestActivityUpdatesButton = (Button) root.findViewById(R.id.request_button);

        mRequestActivityUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestActivityUpdatesButtonHandler(v);
            }
        });

        ListView detectedActivitiesListView = (ListView)  root.findViewById(R.id.detected_activities_listview);


        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(getContext()).getString(
                        Constant.KEY_DETECTED_ACTIVITIES, ""));

        // Bind the adapter to the ListView responsible for display data for detected activities.
        mAdapter = new DetectedActivitiesAdapter(mContent, detectedActivities);
        detectedActivitiesListView.setAdapter(mAdapter);

        mActivityRecognitionClient = new ActivityRecognitionClient(mContent);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(mContent)
                .registerOnSharedPreferenceChangeListener(this);
        updateDetectedActivitiesList();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constant.KEY_DETECTED_ACTIVITIES)) {
            updateDetectedActivitiesList();
        }
    }

    private void setUpdatesRequestedState(boolean requesting) {
        PreferenceManager.getDefaultSharedPreferences(mContent)
                .edit()
                .putBoolean(Constant.KEY_ACTIVITY_UPDATES_REQUESTED, requesting)
                .apply();
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(getActivity() , DetectedActivitiesIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(mContent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private boolean getUpdatesRequestedState() {
        return PreferenceManager.getDefaultSharedPreferences(mContent)
                .getBoolean(Constant.KEY_ACTIVITY_UPDATES_REQUESTED, false);
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                Constant.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent());

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(mContent, "Success", Toast.LENGTH_SHORT).show();
                setUpdatesRequestedState(true);
                updateDetectedActivitiesList();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error to request activities");
                Toast.makeText(mContent, "Error to request activities", Toast.LENGTH_SHORT).show();
                setUpdatesRequestedState(false);
            }
        });
    }


    private void updateDetectedActivitiesList() {
        ArrayList<DetectedActivity> detectedActivities = Utils.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(mContent)
                        .getString(Constant.KEY_DETECTED_ACTIVITIES, ""));

        mAdapter.updateActivities(detectedActivities);
    }
}
