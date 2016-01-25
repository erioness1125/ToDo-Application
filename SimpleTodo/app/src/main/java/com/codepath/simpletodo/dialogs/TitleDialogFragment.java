package com.codepath.simpletodo.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TitleDialogFragment extends DialogFragment {

    private EditText input;
    private String originalItem;

    public interface TitleDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public TitleDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static TitleDialogFragment newInstance(String title) {
        TitleDialogFragment frag = new TitleDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setOriginalItem(String originalItem) {
        this.originalItem = originalItem;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        Activity activity = getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);

        // create an editable textfield
        input = new EditText(activity);
        input.setText(originalItem);
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setSelection(input.getText().length());
        LinearLayout layout = new LinearLayout(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 0, 25, 0);
        layout.addView(input, params);
        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                // Return input text to activity
                TitleDialogListener listener = (TitleDialogListener) getActivity();
                listener.onFinishEditDialog(input.getText().toString());
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}