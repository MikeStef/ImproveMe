package com.micste.improveme;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddGoalFragment extends Fragment implements DayDialogFragment.DayDialogListener {

    private TextView et_title, tv_day, et_description, tv_time;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int hour, minute;
    private Goal goal;

    public AddGoalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_add_goal, container, false);

        et_title = (EditText) inflatedView.findViewById(R.id.et_goal_title);
        et_description = (EditText) inflatedView.findViewById(R.id.et_goal_description);
        tv_day = (TextView) inflatedView.findViewById(R.id.tv_scheduled_day);
        tv_time = (TextView) inflatedView.findViewById(R.id.tv_scheduled_time);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button btn_schedule = (Button) inflatedView.findViewById(R.id.btn_schedule);
        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDayPicker();
            }
        });

        return inflatedView;
    }

    public void showDayPicker() {
        FragmentManager fm = getFragmentManager();
        DayDialogFragment dialog = new DayDialogFragment();
        dialog.setTargetFragment(AddGoalFragment.this, 300);
        dialog.show(fm, "fragment_dialog");
    }

    @Override
    public void onFinishDayDialog(String day) {
        if (!TextUtils.isEmpty(day)) {
            tv_day.setVisibility(View.VISIBLE);
            tv_day.setText(day);
            showTimePicker();

        }
    }

    public void showTimePicker() {
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                timeSetListener, hour, minute, false);
        timePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    Log.d("Timepicker", String.valueOf(hour) + " " + String.valueOf(minute));

                    tv_time.setVisibility(View.VISIBLE);
                    tv_time.setText(new StringBuilder().append(formatTime(hour))
                    .append(":").append(formatTime(minute)));
                }
            };

    private static String formatTime(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_goal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveGoal(et_title.getText().toString(), et_description.getText().toString(),
                        tv_day.getText().toString(), hour, minute);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean formValidate() {
        boolean valid = true;

        String title = et_title.getText().toString();
        if (TextUtils.isEmpty(title)) {
            et_title.setError("Required");
            valid = false;
        } else {
            et_title.setError(null);
        }

        String description = et_description.getText().toString();
        if (TextUtils.isEmpty(description)) {
            et_description.setError("Required");
            valid = false;
        } else {
            et_description.setError(null);
        }

        String day = tv_day.getText().toString();
        if (TextUtils.isEmpty(day)) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "You need to schedule a day for this weekly goal.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;

    }

    private void saveGoal(String title, String description, String day, int hour, int minute) {
        if (!formValidate()) {
            return;
        }

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String goalId = databaseReference.push().getKey();
        int notificationId = (int) System.currentTimeMillis();

        goal = new Goal(title, description, day, goalId, hour, minute, notificationId, true);

        databaseReference.child("users").child(currentUser.getUid()).child("active_goals").child(goalId).setValue(goal,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w("Firebase", "Error: " + databaseError.getDetails());
                            displayToast(R.string.error_general);
                        }
                    }
                });

        AlarmUtils alarmUtils = new AlarmUtils(getActivity());
        alarmUtils.scheduleAlarm(goal);

        Fragment fragment = new GoalsFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void displayToast(int msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
