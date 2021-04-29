package com.exemple.healtyapplication.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.ui.detected.Constant;
import com.exemple.healtyapplication.ui.detected.Utils;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DetectedActivitiesAdapter  extends ArrayAdapter<DetectedActivity> {

    public DetectedActivitiesAdapter(Context context, ArrayList<DetectedActivity> detectedActivities) {
        super(context, 0, detectedActivities);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        DetectedActivity detectedActivity = getItem(position);
        if (view == null) { view = LayoutInflater.from(getContext()).inflate(
                    R.layout.detected_items, parent, false);
        }

        // Find the UI widgets.
        TextView activityName = (TextView) view.findViewById(R.id.detected_activity_name);
        TextView activityConfidenceLevel = (TextView) view.findViewById(R.id.detected_activity_confidence_level);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.detected_activity_progress_bar);

        // Populate widgets with values.
        if (detectedActivity != null) {
            activityName.setText(Utils.getActivityString(getContext(),
                    detectedActivity.getType()));

            activityConfidenceLevel.setText(getContext().getString(R.string.percent, detectedActivity.getConfidence()));
            progressBar.setProgress(detectedActivity.getConfidence());
        }
        return view;
    }


    public void updateActivities(ArrayList<DetectedActivity> detectedActivities) {
        HashMap<Integer, Integer> detectedActivitiesMap = new HashMap<>();
        for (DetectedActivity activity : detectedActivities) {
            detectedActivitiesMap.put(activity.getType(), activity.getConfidence());
        }
        // Every time we detect new activities, we want to reset the confidence level of ALL
        // activities that we monitor. Since we cannot directly change the confidence
        // of a DetectedActivity, we use a temporary list of DetectedActivity objects. If an
        // activity was freshly detected, we use its confidence level. Otherwise, we set the
        // confidence level to zero.
        ArrayList<DetectedActivity> tempList = new ArrayList<>();
        for (int i = 0; i < Constant.MONITORED_ACTIVITIES.length; i++) {
            int confidence = detectedActivitiesMap.containsKey(Constant.MONITORED_ACTIVITIES[i]) ?
                    detectedActivitiesMap.get(Constant.MONITORED_ACTIVITIES[i]) : 0;

            tempList.add(new DetectedActivity(Constant.MONITORED_ACTIVITIES[i],
                    confidence));
        }

        // Remove all items.
        this.clear();

        // Adding the new list items notifies attached observers that the underlying data has
        // changed and views reflecting the data should refresh.
        for (DetectedActivity detectedActivity: tempList) {
            this.add(detectedActivity);
        }
    }
}
