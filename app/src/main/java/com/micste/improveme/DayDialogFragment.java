package com.micste.improveme;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by micste on 2017-08-29.
 */

public class DayDialogFragment extends android.support.v4.app.DialogFragment {
    private RadioButton rb_monday, rb_tuesday, rb_wednesday, rb_thursday, rb_friday;
    private RadioButton rb_saturday, rb_sunday;
    private String day;

    public interface DayDialogListener {
        void onFinishDayDialog(String day);
    }

    public void sendBackResult() {
        DayDialogListener listener = (DayDialogListener) getTargetFragment();
        listener.onFinishDayDialog(day);
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_daypicker, null);
        builder.setView(view);

        builder.setTitle(R.string.day_dialog_title)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendBackResult();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        rb_monday = (RadioButton) view.findViewById(R.id.rb_monday);
        rb_monday.setOnClickListener(onClickListener);
        rb_tuesday = (RadioButton) view.findViewById(R.id.rb_tuesday);
        rb_tuesday.setOnClickListener(onClickListener);
        rb_wednesday = (RadioButton) view.findViewById(R.id.rb_wednesday);
        rb_wednesday.setOnClickListener(onClickListener);
        rb_thursday = (RadioButton) view.findViewById(R.id.rb_thursday);
        rb_thursday.setOnClickListener(onClickListener);
        rb_friday = (RadioButton) view.findViewById(R.id.rb_friday);
        rb_friday.setOnClickListener(onClickListener);
        rb_saturday = (RadioButton) view.findViewById(R.id.rb_saturday);
        rb_saturday.setOnClickListener(onClickListener);
        rb_sunday = (RadioButton) view.findViewById(R.id.rb_sunday);
        rb_sunday.setOnClickListener(onClickListener);

        return builder.create();
    }

    private RadioButton.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((RadioButton) view).isChecked();

            switch (view.getId()) {
                case R.id.rb_monday:
                    if (checked) {
                        rb_tuesday.setChecked(false);
                        rb_wednesday.setChecked(false);
                        rb_thursday.setChecked(false);
                        rb_friday.setChecked(false);
                        rb_saturday.setChecked(false);
                        rb_sunday.setChecked(false);
                        day = rb_monday.getTag().toString();
                    }
                    break;
                case R.id.rb_tuesday:
                    if (checked) {
                        rb_monday.setChecked(false);
                        rb_wednesday.setChecked(false);
                        rb_thursday.setChecked(false);
                        rb_friday.setChecked(false);
                        rb_saturday.setChecked(false);
                        rb_sunday.setChecked(false);
                        day = rb_tuesday.getTag().toString();
                    }
                    break;
                case R.id.rb_wednesday:
                    if (checked) {
                        rb_tuesday.setChecked(false);
                        rb_monday.setChecked(false);
                        rb_thursday.setChecked(false);
                        rb_friday.setChecked(false);
                        rb_saturday.setChecked(false);
                        rb_sunday.setChecked(false);
                        day = rb_wednesday.getTag().toString();
                    }
                    break;
                case R.id.rb_thursday:
                    if (checked) {
                        rb_tuesday.setChecked(false);
                        rb_wednesday.setChecked(false);
                        rb_monday.setChecked(false);
                        rb_friday.setChecked(false);
                        rb_saturday.setChecked(false);
                        rb_sunday.setChecked(false);
                        day = rb_thursday.getTag().toString();
                    }
                    break;
                case R.id.rb_friday:
                    if (checked) {
                        rb_tuesday.setChecked(false);
                        rb_wednesday.setChecked(false);
                        rb_thursday.setChecked(false);
                        rb_monday.setChecked(false);
                        rb_saturday.setChecked(false);
                        rb_sunday.setChecked(false);
                        day = rb_friday.getTag().toString();
                    }
                    break;
                case R.id.rb_saturday:
                    if (checked) {
                        rb_tuesday.setChecked(false);
                        rb_wednesday.setChecked(false);
                        rb_thursday.setChecked(false);
                        rb_friday.setChecked(false);
                        rb_monday.setChecked(false);
                        rb_sunday.setChecked(false);
                        day = rb_saturday.getTag().toString();
                    }
                    break;
                case R.id.rb_sunday:
                    if (checked) {
                        rb_tuesday.setChecked(false);
                        rb_wednesday.setChecked(false);
                        rb_thursday.setChecked(false);
                        rb_friday.setChecked(false);
                        rb_saturday.setChecked(false);
                        rb_monday.setChecked(false);
                        day = rb_sunday.getTag().toString();
                    }
            }

        }
    };
}
