package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.simpletodo.dialogs.DateDialogFragment;
import com.codepath.simpletodo.dialogs.TitleDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity
        implements TitleDialogFragment.TitleDialogListener, DatePickerDialog.OnDateSetListener {

    Button btnEdit;
    TextView tvOriginalItem;
    TextView tvOriginalDate;
    int position;
    long dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // find Button:btnEdit
        btnEdit = (Button) findViewById(R.id.btnEdit);

        // read the value of "originalItem" passed from parent and set it to TextView:tvOriginalItem
        String originalItem = getIntent().getStringExtra("originalItem");
        dueDate = getIntent().getLongExtra("originalDueDate", System.currentTimeMillis());

        tvOriginalItem = (TextView) findViewById(R.id.tvOriginalItem);
        tvOriginalDate = (TextView) findViewById(R.id.tvOriginalDate);
        tvOriginalItem.setText(originalItem);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(dueDate);
        tvOriginalDate.setText(date);

        // get the position
        position = getIntent().getIntExtra("position", -1);
    }

    public void onSubmit(View v) {
        // Prepare data intent
        Intent data = new Intent();

        String editItem = tvOriginalItem.getText().toString().trim();
        // alert if the user does not enter anything
        if (editItem == null || editItem.trim().isEmpty()) {
            Toast.makeText(this, "Title " + getResources().getString(R.string.err_should_not_be_empty), Toast.LENGTH_SHORT).show();
        }
        else {
            // Pass relevant data back as a result
            data.putExtra("editedItem", editItem);
            data.putExtra("editedDate", dueDate);
            data.putExtra("position", position);

            // Activity finished ok, return the data
            setResult(RESULT_OK, data); // set result code and bundle data for response
            finish(); // closes the activity, pass data to parent
        }
    }

    public void onCancel(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void showTitleDialog(View v) {
        FragmentManager fm = getFragmentManager();
        TitleDialogFragment titleDialog = TitleDialogFragment.newInstance("Edit Todo Title");
        titleDialog.setOriginalItem(tvOriginalItem.getText().toString());
        titleDialog.show(fm, "fragment_title_dialog");
    }

    public void showDateDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DateDialogFragment dateDialog = DateDialogFragment.newInstance("Edit Due Date");
        dateDialog.show(fm, "fragment_date_dialog");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        tvOriginalItem.setText(inputText);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        TextView tvOriginalDate = (TextView) findViewById(R.id.tvOriginalDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.YEAR, year);
        final Date newDueDate = calendar.getTime();
        // update the class variable:dueDate
        dueDate = newDueDate.getTime();
        // update tvOriginalDate
        String date = sdf.format(newDueDate);
        tvOriginalDate.setText(date);
    }
}
