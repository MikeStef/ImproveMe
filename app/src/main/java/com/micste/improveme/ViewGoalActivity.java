package com.micste.improveme;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewGoalActivity extends AppCompatActivity {
    private static final String TAG = "ViewGoalActivity";
    private Goal goal;
    private TextView tv_title, tv_description, tv_day, tv_time;
    private SwitchCompat notification_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        Bundle b = getIntent().getExtras();
        goal = b.getParcelable("GOAL");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        setupView();

    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.viewgoal_title);
        tv_title.setText(goal.getTitle());
        tv_description = (TextView) findViewById(R.id.viewgoal_description);
        tv_description.setText(goal.getDescription());
        tv_day = (TextView) findViewById(R.id.viewgoal_day);
        tv_day.setText(goal.getDay());
        tv_time = (TextView) findViewById(R.id.viewgoal_time);
        tv_time.setText(new StringBuilder().append(formatTime(goal.getHour()))
                .append(":").append(formatTime(goal.getMinute())));
        notification_switch = (SwitchCompat) findViewById(R.id.notification_switch);

        if (goal.getIsNotificationEnabled()) {
            notification_switch.setChecked(true);
        } else {
            notification_switch.setChecked(false);
        }
        
        notification_switch.setOnCheckedChangeListener(checkedChangeListener);

    }

    private String formatTime(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            switch (compoundButton.getId()) {
                case R.id.notification_switch:
                    if (!isChecked) {
                        Log.d(TAG, "onCheckedChanged: off");
                        toggleNotification(false);
                    } else {
                        Log.d(TAG, "onCheckedChanged: on");
                        toggleNotification(true);
                    }
            }
        }
    };

    private void toggleNotification(boolean isOn) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        databaseReference.child("users").child(currentUser.getUid()).child("active_goals")
                .child(goal.getGoalId()).child("isNotificationEnabled").setValue(isOn);

        AlarmUtils alarmUtils = new AlarmUtils(ViewGoalActivity.this);
        if (isOn) {
            alarmUtils.scheduleAlarm(goal);
            Log.d(TAG, "toggleNotification: alarm on");
        } else {
            alarmUtils.cancelAlarm(goal);
            Log.d(TAG, "toggleNotification: alarm off");
        }

    }

}
