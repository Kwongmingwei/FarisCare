package com.example.fariscare.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fariscare.Member;
import com.example.fariscare.OCall;
import com.example.fariscare.R;

import java.util.ArrayList;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.AllUsersViewHolder> {

    Activity context;
    ArrayList<Member> memberArrayList;

    public AllUsersAdapter(Activity context, ArrayList<Member> memberArrayList){
        this.context=context;
        this.memberArrayList=memberArrayList;
    }

    @NonNull
    @Override
    public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users,parent,false);
        AllUsersViewHolder allUsersAdapter=new AllUsersViewHolder(view);
        return allUsersAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersViewHolder holder, int position) {
        Member member=memberArrayList.get(position);
        holder.textViewName.setText(member.getName());
        Log.v("Adapter",member.getName());



    }

    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }

    public class AllUsersViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        CardView cardView;


        public AllUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=(TextView)itemView.findViewById(R.id.itemName);
            cardView=itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Member member=memberArrayList.get(getAdapterPosition());
                    ((OCall)context).callUser(member);
                }
            });
        }
    }

}
