package com.gal.smartcalender;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Handler;
import android.os.Looper;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.ViewHolder> {
    private static ArrayList<Event> _localDataSet;

    private static AppDatabase _appDatabase;

    private static ArraySet<Event> _checked_events = null;

    private static CheckBox _selectAllCheckBox = null;

    private static Toolbar _toolbar = null;


    private static CalenderManager calenderManager = null;

    public static String ZonedDateTimeToHumanReadableStr(ZonedDateTime zonedDateTime) {
        ZonedDateTime systemZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return systemZonedDateTime.format(formatter);
    }

    public ArraySet<Event> get_checked_events() {
        return _checked_events;
    }

    public void clearSelection() {
        if(_checked_events != null){
            _checked_events.clear();
            new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);
        }
    }

    public void selectAll() {
        if(_checked_events != null){
            _checked_events.clear();
            _checked_events.addAll(_localDataSet);
            new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);
        }
    }

    public static boolean isAllSelected() {
        return _checked_events.size() == _localDataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventDescriptionView;
        private final TextView eventStartDate;
        private final TextView eventEndDate;
        private final TextView eventImportance;
        private final TextView eventUrgency;
        private final TextView appName;
        private final CheckBox checkBox;
        private final Button addToCalenderButton;

        public ViewHolder(View view) {
            super(view);
            eventDescriptionView = view.findViewById(R.id.eventDescription);
            eventStartDate = view.findViewById(R.id.startDateTime);
            eventEndDate = view.findViewById(R.id.endDateTime);
            eventImportance = view.findViewById(R.id.importance);
            eventUrgency = view.findViewById(R.id.urgency);
            appName = view.findViewById(R.id.app_name);
            checkBox = view.findViewById(R.id.checkBox);
            addToCalenderButton = view.findViewById(R.id.addToCalendarButton);
            if(calenderManager == null){
                calenderManager = new CalenderManager(view.getContext());
            }
        }

        public void bind(Event event) {
            eventDescriptionView.setText(event.eventInfo);
            eventStartDate.setText(ZonedDateTimeToHumanReadableStr(event.startDate));
            eventEndDate.setText(ZonedDateTimeToHumanReadableStr(event.endDate));
            eventImportance.setText(String.valueOf(event.importance));
            appName.setText(event.dataSource);
            addToCalenderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   calenderManager.addToSelectedCalenders(event);
                    _appDatabase.EventsDao().delete(event);
                }
            });
            eventUrgency.setText(String.valueOf(event.urgency));
            if (_checked_events == null) {
                _checked_events = new ArraySet<Event>();
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()) {
                        _checked_events.add(event);
                    }else{
                        _checked_events.remove(event);
                    }
                    if (isAllSelected()) {
                        _selectAllCheckBox.setChecked(true);
                    } else {
                        _selectAllCheckBox.setChecked(false);
                    }
                    if (_checked_events.isEmpty()){
                        _toolbar.getMenu().clear();
                    } else {
                        if(!_toolbar.getMenu().hasVisibleItems()){
                            _toolbar.inflateMenu(R.menu.batch_event_operation_menu);
                        }
                    }
                }
            });
            if (_checked_events.contains(event)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }

    public RecyclerViewEventsAdapter(AppDatabase database, LifecycleOwner lifecycleOwner, CheckBox selectAllCheckBox, TextView emptyInstructTextView, Toolbar toolbar) {
        _appDatabase = database;
        _selectAllCheckBox = selectAllCheckBox;
        _localDataSet = new ArrayList<Event>();
        _toolbar = toolbar;
        _appDatabase.EventsDao().getAllLive().observe(lifecycleOwner, events -> {
            _localDataSet.clear();
            _localDataSet.addAll(events);
            if(events.isEmpty()) emptyInstructTextView.setVisibility(VISIBLE); else emptyInstructTextView.setVisibility(GONE);
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_calendar_event, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(_localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return _localDataSet.size();
    }
}