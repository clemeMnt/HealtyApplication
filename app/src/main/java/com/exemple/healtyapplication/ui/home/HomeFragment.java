package com.exemple.healtyapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.exemple.healtyapplication.MainActivity;
import com.exemple.healtyapplication.R;
import com.exemple.healtyapplication.ui.detected.DetectedFragment;
import com.exemple.healtyapplication.ui.food.FoodFragment;
import com.exemple.healtyapplication.ui.notes.NotesFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;


public class HomeFragment extends Fragment {

    private CarouselView carouselView;
    private CalendarView calendar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        calendar = (CalendarView) root.findViewById(R.id.calendarView);


        return root;
    }



}