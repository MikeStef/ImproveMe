package com.micste.improveme;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ActiveGoalsFragment extends Fragment {

    private List<Goal> list;
    private GoalsRecyclerView recyclerView;
    private GoalsRecyclerView.LayoutManager glm, llm;
    private CustomRecyclerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference activeGoalsRef;
    private ValueEventListener getDataListener;
    private MenuItem toggleViewMenuItem;
    private boolean isGrid = false;
    private CoordinatorLayout coordinatorLayout;
    private AlarmUtils alarmUtils;


    public ActiveGoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_active_goals, container, false);

        GoalsFragment parentFragment = ((GoalsFragment)this.getParentFragment());
        coordinatorLayout = parentFragment.coordinatorLayout;

        alarmUtils = new AlarmUtils(getActivity());

        loadFirebaseData();

        recyclerView = (GoalsRecyclerView) inflatedView.findViewById(R.id.recycle_view);
        adapter = new CustomRecyclerAdapter(list, getActivity().getApplicationContext(), 0);
        glm = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        llm = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        View emptyView = inflatedView.findViewById(R.id.active_goals_empty_view);
        recyclerView.setEmptyView(emptyView);

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.d("adapterClick", "onItemClick: " + position);

                        Goal goal = list.get(position);

                        Intent intent = new Intent(getActivity(), ViewGoalActivity.class);
                        intent.putExtra("GOAL", goal);
                        startActivity(intent);
                    }
                });

        setupSwipe();

        return inflatedView;
    }

    private void loadFirebaseData() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            activeGoalsRef = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(currentUser.getUid()).child("active_goals");

            getDataListener = activeGoalsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Goal goal = dataSnapshot1.getValue(Goal.class);
                        list.add(goal);
                    }
                    adapter.update(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("ActiveGoalsFragment", "onCancelled: ", databaseError.toException());
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_config_view, menu);
        toggleViewMenuItem = menu.findItem(R.id.menu_toggle_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_view:
                toggleView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleView() {
        Drawable grid_icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_view_grid, null);
        Drawable linear_icon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_view_linear, null);

        if (!isGrid) {
            toggleViewMenuItem.setIcon(linear_icon);
            recyclerView.setLayoutManager(glm);
            isGrid = true;
        } else {
            toggleViewMenuItem.setIcon(grid_icon);
            recyclerView.setLayoutManager(llm);
            isGrid = false;
        }
    }

    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int pos = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.RIGHT) {
                            moveGoal(list.get(pos));
                        }
                        if (direction == ItemTouchHelper.LEFT) {
                            deleteGoal(list.get(pos));
                        }

                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void moveGoal(final Goal goal) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference fromPath = rootRef.child("users").child(currentUser.getUid())
                .child("active_goals").child(goal.getGoalId());
        final DatabaseReference toPath = rootRef.child("users").child(currentUser.getUid())
                .child("completed_goals").child(goal.getGoalId());

        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w("Firebase", "onComplete failed: ", databaseError.toException() );
                        } else {
                            Log.d("Firebase", "Copied goal successfully.");
                            fromPath.removeValue();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        alarmUtils.cancelAlarm(goal);
        showSnackbar(R.string.info_mark_goal_complete);
    }

    private void deleteGoal(Goal goal) {
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUser.getUid()).child("active_goals").child(goal.getGoalId());
        deleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    showSnackbar(R.string.error_general);
                }
            }
        });

        alarmUtils.cancelAlarm(goal);
        showSnackbar(R.string.info_goal_deleted);
    }

    private void showSnackbar(int message) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getDataListener != null) {
            activeGoalsRef.removeEventListener(getDataListener);
        }
    }
}
