package com.exemple.healtyapplication.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.exemple.healtyapplication.R;

import java.security.acl.NotOwnerException;
import java.util.ArrayList;

public class NoteAdapter  extends BaseAdapter {
    Activity context;
    ArrayList<Note> notes;
    private static LayoutInflater inflater = null;
    private TextView title, description;

    public NoteAdapter(Activity context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount( ) {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        item = inflater.inflate(R.layout.notes_card, null);
        Note n = notes.get(position);

        title = item.findViewById(R.id.text_notes_title);
        description = item.findViewById(R.id.text_notes_desc);

        title.setText(n.getTitle());
        description.setText(n.getDescription());
        return item;
    }
}
