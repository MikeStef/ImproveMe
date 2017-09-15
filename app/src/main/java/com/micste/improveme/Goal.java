package com.micste.improveme;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

/**
 * Created by micste on 2017-08-30.
 */

@IgnoreExtraProperties
public class Goal implements Parcelable {

    public String title;
    public String goalId;
    public String description;
    public String day;
    public int hour, minute;
    public int notificationId;
    public boolean isNotificationEnabled;

    public Goal() {

    }

    public Goal(String title, String description, String day, String goalId, int hour, int minute,
                int notificationId, boolean isNotificationEnabled) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.goalId = goalId;
        this.hour = hour;
        this.minute = minute;
        this.notificationId = notificationId;
        this.isNotificationEnabled = isNotificationEnabled;
    }

    public Goal(Parcel in) {
        readFromParcel(in);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getCalendarDay() {
        int day = 0;
        switch (this.day) {
            case "Monday":
                day = Calendar.MONDAY;
                break;
            case "Tuesday":
                day = Calendar.TUESDAY;
                break;
            case "Wednesday":
                day = Calendar.WEDNESDAY;
                break;
            case "Thursday":
                day = Calendar.THURSDAY;
                break;
            case "Friday":
                day = Calendar.FRIDAY;
                break;
            case "Saturday":
                day = Calendar.SATURDAY;
                break;
            case "Sunday":
                day = Calendar.SUNDAY;
                break;
        }
        return day;
    }

    public boolean getIsNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setIsNotificationEnabled(boolean isNotificationEnabled) {
        this.isNotificationEnabled = isNotificationEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(goalId);
        dest.writeString(description);
        dest.writeString(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(notificationId);
        dest.writeByte((byte) (isNotificationEnabled ? 1 : 0));
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        goalId = in.readString();
        description = in.readString();
        day = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        notificationId = in.readInt();
        isNotificationEnabled = in.readByte() != 0;
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };
}
