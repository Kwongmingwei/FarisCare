package com.example.fariscare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fariscare.Adapters.EventItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EventListener;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {//ADAPTER 1/4
    private ArrayList<EventItem> mEventList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder { //VIEWHOLDER
        //public ImageView mImageView;
        public TextView mEventName;
        public TextView mEventDate;
        public TextView mEventType;
        public TextView mEventDesc;

        public EventViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mEventName = itemView.findViewById(R.id.eventName);
            mEventDate = itemView.findViewById(R.id.eventDate);
            mEventType = itemView.findViewById(R.id.eventType);
            mEventDesc = itemView.findViewById(R.id.eventDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public EventAdapter(ArrayList<EventItem> eventList) {
        mEventList = eventList;
    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        EventViewHolder evh = new EventViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventAdapter.EventViewHolder holder, int position) {
        EventItem currentItem = mEventList.get(position);
        
        holder.mEventName.setText(currentItem.getEventName());
        holder.mEventDate.setText(currentItem.getEventDate());
        holder.mEventType.setText(currentItem.getEventType());
        holder.mEventDesc.setText(currentItem.getEventDesc());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
