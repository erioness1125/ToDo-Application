package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    Button btnEdit;
    EditText etEditItem;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // find Button:btnEdit
        btnEdit = (Button) findViewById(R.id.btnEdit);

        // read the value of "originalItem" passed from parent and set it to EditText:etEditItem
        String originalItem = getIntent().getStringExtra("originalItem");
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(originalItem);

        // get the position
        position = getIntent().getIntExtra("position", -1);
    }

    public void onSubmit(View v) {
        // Prepare data intent
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("editedItem", etEditItem.getText().toString());
        data.putExtra("position", position);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
