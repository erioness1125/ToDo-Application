package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String originalItem = getIntent().getStringExtra("originalItem");
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(originalItem);
    }

    public void onSubmit(View v) {
        this.finish();
    }
}
