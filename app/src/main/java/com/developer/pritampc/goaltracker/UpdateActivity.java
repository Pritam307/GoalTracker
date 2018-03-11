package com.developer.pritampc.goaltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {
    EditText goal_input,date_st,date_ed,des;
    CheckBox up_start,up_progress,up_complete,up_notdone;
    private String current_id;
    private String curr_stdate,curr_eddate,curr_des,curr_goal;
    private static final String TAG="dbupdate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        goal_input=findViewById(R.id.up_goal_in);
        date_ed=findViewById(R.id.up_ed_in);
        date_st=findViewById(R.id.up_sd_in);
        des=findViewById(R.id.up_description);

        up_start=findViewById(R.id.up_started_cb);
        up_progress=findViewById(R.id.up_progress_cb);
        up_complete=findViewById(R.id.up_complete_cb);
        up_notdone=findViewById(R.id.up_notdone_cb);

        current_id=getIntent().getStringExtra("current_id");
        curr_stdate=getIntent().getStringExtra("curr_st_date");
        curr_eddate=getIntent().getStringExtra("curr_ed_date");
        curr_des=getIntent().getStringExtra("curr_des");
        curr_goal=getIntent().getStringExtra("curr_goal");
        Log.d(TAG, "onCreate: name"+curr_goal);
        Log.d(TAG, "onCreate: des"+curr_des);
        Log.d(TAG, "onCreate: end date"+curr_eddate);
        Log.d(TAG, "onCreate: start date"+curr_stdate);
        //Toast.makeText(this, , Toast.LENGTH_SHORT).show();
        des.setText(curr_des);
    }


    public void OnUpdate(View v)
    {
        String new_goal,new_srtdate,new_eddate,new_des;
        String status=null;
        if(up_start.isChecked())
        {
            status="started";
        }

        if(up_progress.isChecked())
        {
            status="in progress";
        }

        if(up_complete.isChecked())
        {
            status="completed";
        }

        if(up_notdone.isChecked())
        {
            status="not done";
        }
        ////////////////////////////////
        if(!TextUtils.isEmpty(goal_input.getText().toString().trim()))
        {
            new_goal=goal_input.getText().toString().trim();
        }else
        {
            new_goal=curr_goal;
        }

        if(!TextUtils.isEmpty(date_st.getText().toString().trim()))
        {
            new_srtdate=date_st.getText().toString().trim();
        }else
        {
            new_srtdate=curr_stdate;
        }

        if(!TextUtils.isEmpty(date_ed.getText().toString().trim()))
        {
            new_eddate=date_ed.getText().toString().trim();
        }else
        {
            new_eddate=curr_eddate;
        }

        if(!TextUtils.isEmpty(des.getText().toString().trim()))
        {
            new_des=des.getText().toString().trim();
        }else
        {
            new_des=curr_des;
        }

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("goal").child(current_id);
        GoalData new_Data=new GoalData(current_id,new_goal,new_srtdate,new_eddate,new_des,status);
        ref.setValue(new_Data);
        Toast.makeText(this, "Values Updated Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
