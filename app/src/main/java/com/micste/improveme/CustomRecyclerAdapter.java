package com.micste.improveme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by micste on 2017-09-01.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.MyHolder> {

    List<Goal> mList;
    Context context;
    int layout_type;

    public CustomRecyclerAdapter(List<Goal> list, Context context, int layout_type) {
        this.mList = list;
        this.context = context;
        this.layout_type = layout_type;
    }

    @Override
    public CustomRecyclerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int card;

        if (layout_type == 0) {
            card = R.layout.goal_card;
        } else {
            card = R.layout.goal_card_complete;
        }

        View view = LayoutInflater.from(context).inflate(card, parent, false);
        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Goal goal = mList.get(position);
        holder.title.setText(goal.getTitle());
        holder.day.setText(goal.getDay());
        holder.time.setText(new StringBuilder().append(formatTime(goal.getHour()))
                .append(":").append(formatTime(goal.getMinute())));
        holder.description.setText(goal.getDescription());
    }

    @Override
    public int getItemCount() {
        int i = 0;

        try {
            if (mList == null) {
                i = 0;
            } else {
                i = mList.size();
            }
        } catch (Exception e) {
            Log.w("Adapter", "getItemCount: ", e );
        }

        return i;
    }

    private String formatTime(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void update(List<Goal> list) {
        if (mList != null) {
            mList.clear();
            mList.addAll(list);
        } else {
            mList = list;
        }

        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public String getId(int position) {

        return mList.get(position).getGoalId();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView title, day, time, description;

        public MyHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.card_title);
            day = (TextView) itemView.findViewById(R.id.card_day);
            description = (TextView) itemView.findViewById(R.id.card_description);
            time = (TextView) itemView.findViewById(R.id.card_time);
        }

    }

}
