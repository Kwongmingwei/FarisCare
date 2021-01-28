package com.example.fariscare.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fariscare.PublicEventSearch;
import com.example.fariscare.R;
import com.example.fariscare.SearchResult;

import java.util.ArrayList;

public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerViewAdapterSearch.ViewHolder> {

    private static final String Tag="RecyclerViewAdapterSearch";
    private Context mcontext;
    private ArrayList<PublicEventSearch> search;

    public RecyclerViewAdapterSearch(ArrayList<PublicEventSearch> search) {
        this.search = search;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterSearch.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_cardview,parent,false);
        ViewHolder View=new ViewHolder(view);
        return View;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PublicEventSearch pt= search.get(position);
        holder.ET.setText(pt.geteventType());
        holder.EN.setText(pt.geteventName());
        holder.desc.setText(pt.getEventDesc());
        holder.DT.setText(pt.getEventDate());
        holder.Time.setText(pt.getTime());
    }

    @Override
    public int getItemCount() {
        return search.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ET, EN, desc, DT,Time;
        public ImageView mImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageView);
            ET = itemView.findViewById(R.id.EventType);
            EN = itemView.findViewById(R.id.EventName);
            desc = itemView.findViewById(R.id.DESC);
            DT = itemView.findViewById(R.id.DateTime);
            Time=itemView.findViewById(R.id.Time);
        }
    }
}