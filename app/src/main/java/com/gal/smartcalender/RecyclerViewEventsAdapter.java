package com.gal.smartcalender;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.ViewHolder> {
    private final Event[] localDataSet;


    public static String ZonedDateTimeToHumanReadableStr(ZonedDateTime zonedDateTime) {
        ZonedDateTime systemZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return systemZonedDateTime.format(formatter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventDescriptionView;
        private final TextView eventStartDate;
        private final TextView eventEndDate;
        private final TextView eventImportance;
        private final TextView eventUrgency;

        public ViewHolder(View view) {
            super(view);
            eventDescriptionView = view.findViewById(R.id.eventDescription);
            eventStartDate = view.findViewById(R.id.startDateTime);
            eventEndDate = view.findViewById(R.id.endDateTime);
            eventImportance = view.findViewById(R.id.importance);
            eventUrgency = view.findViewById(R.id.urgency);
        }

        public void bind(Event event) {
            eventDescriptionView.setText(event.eventInfo);
            eventStartDate.setText(ZonedDateTimeToHumanReadableStr(event.startDate));
            eventEndDate.setText(ZonedDateTimeToHumanReadableStr(event.endDate));
            eventImportance.setText(String.valueOf(event.importance));
            eventUrgency.setText(String.valueOf(event.urgency));
        }
    }

    public RecyclerViewEventsAdapter(Event[] dataSet) {
        localDataSet = dataSet;
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
        viewHolder.bind(localDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
