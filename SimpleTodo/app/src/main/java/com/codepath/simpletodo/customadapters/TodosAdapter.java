package com.codepath.simpletodo.customadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.sqlite.models.Todos;

import java.util.ArrayList;

public class TodosAdapter extends ArrayAdapter<Todos> {

    public TodosAdapter(Context context, ArrayList<Todos> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todos t = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        // Lookup view for data population
        TextView tvTodoId = (TextView) convertView.findViewById(R.id.tvTodoId);
        TextView tvTodoValue = (TextView) convertView.findViewById(R.id.tvTodoValue);

        // Populate the data into the template view using the data object
        tvTodoId.setText(String.valueOf(t.id));
        tvTodoValue.setText(t.value);

        // Return the completed view to render on screen
        return convertView;
    }
}
