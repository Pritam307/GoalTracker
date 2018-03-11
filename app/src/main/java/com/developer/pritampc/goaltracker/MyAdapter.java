package com.developer.pritampc.goaltracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by pritamPC on 3/4/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<GoalData> data;
    private static final String TAG="dbread";
    public  Context context;
    private ItemLongClickListener longClickListener;
    public MyAdapter(ArrayList<GoalData> data,Context context) {
        this.data = data;
        this.context=context;
       // this.longClickListener=longClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder view=new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false));
       /* View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        final MyViewHolder myViewHolder=new MyViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClickListener.onLongClickListener(myViewHolder.getAdapterPosition(),data);
                return true;
            }
        }); */
        return view;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GoalData curr_data=data.get(position);
        if(!TextUtils.isEmpty(curr_data.getGoal_name()))
        {
            holder.goal_name.setText(curr_data.getGoal_name());
        }
        if(!TextUtils.isEmpty(curr_data.getStartDate()))
        {
            holder.sd.setText(curr_data.getStartDate());
        }
        if(!TextUtils.isEmpty(curr_data.getEndDate()))
        {
            holder.ed.setText(curr_data.getEndDate());
        }
        if(!TextUtils.isEmpty(curr_data.getStatus()))
        {
            holder.st.setText(curr_data.getStatus());
        }

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public int getPosition()
    {
            return getPosition();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView goal,goal_name,sd,ed,st;
        public CardView item_holder;
        public MyViewHolder(View itemView) {
            super(itemView);
            goal=itemView.findViewById(R.id.goal);
            goal_name=itemView.findViewById(R.id.name);
            sd=itemView.findViewById(R.id.sd_tv);
            ed=itemView.findViewById(R.id.ed_tv);
            st=itemView.findViewById(R.id.status_tv);
            item_holder=itemView.findViewById(R.id.item_card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    GoalData current_data =data.get(pos);
                    Intent i=new Intent(context,UpdateActivity.class);

                    i.putExtra("current_id", current_data.getId());
                    i.putExtra("curr_goal", current_data.getGoal_name());
                    i.putExtra("curr_st_date", current_data.getStartDate());
                    i.putExtra("curr_ed_date", current_data.getEndDate());
                    i.putExtra("curr_des", current_data.getDescription());
                    context.startActivity(i);
                }
            });


           /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos=getAdapterPosition();
                    GoalData del_item_data=data.get(pos);
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("goal").child(del_item_data.getId());
                    ref.removeValue();
                    removeItem(pos);
                    //activity=new MainActivity();

                    Toast.makeText(context,"date_removed successfully!",Toast.LENGTH_SHORT).show();

                    return true;
                }
            });*/
        }
    }


}
