package com.developer.pritampc.goaltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class InputActivity extends AppCompatActivity {
    EditText sd_in,ed_in,des,title;
    Button sub;
    CheckBox started_c,progress_c,notdone_c,complete_c;
    DatabaseReference databaseReference;
    private String gtitle,start_date,end_date;
    private static final String TAG="dbinput";
    private String status=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        sd_in=findViewById(R.id.sd_in);
        ed_in=findViewById(R.id.ed_in);
        sub=findViewById(R.id.submit_btn);
        title=findViewById(R.id.goal_in);
        des=findViewById(R.id.description);
        started_c=findViewById(R.id.started_cb);
        progress_c=findViewById(R.id.progress_cb);
        notdone_c=findViewById(R.id.notdone_cb);
        complete_c=findViewById(R.id.complete_cb);

        complete_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                started_c.setChecked(false);
                progress_c.setChecked(false);
                notdone_c.setChecked(false);
            }
        });

        started_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                complete_c.setChecked(false);
                progress_c.setChecked(false);
                notdone_c.setChecked(false);
            }
        });

        progress_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                complete_c.setChecked(false);
                started_c.setChecked(false);
                notdone_c.setChecked(false);
            }
        });

        notdone_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                complete_c.setChecked(false);
                progress_c.setChecked(false);
                started_c.setChecked(false);
            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference("goal");
    }


    public void OnSubmit(View v)
    {
            addGoal();
            finish();
    }

    private void addGoal() {

        gtitle=title.getText().toString().trim();
        start_date=sd_in.getText().toString().trim();
        end_date=ed_in.getText().toString().trim();
        String description=des.getText().toString().trim();
        if(started_c.isChecked())
        {
            status="started";
        }

        if(progress_c.isChecked())
        {
            status="in Progress";
        }

        if(complete_c.isChecked())
        {
            status="completed";
        }

        if(notdone_c.isChecked())
        {
            status="not done";
        }

        if(!TextUtils.isEmpty(gtitle))
        {
            String id=databaseReference.push().getKey();
            GoalData gdata=new GoalData(id,gtitle,start_date,end_date,description,status);
            databaseReference.child(id).setValue(gdata);
            Toast.makeText(getApplicationContext(),"Goal Submitted!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Please enter the Your Goal!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void checkDateValidity(String st_date,String end_date)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

       // Date d1= sdf.parse(start_date);
      //  Date d2= sdf.parse(end_date);

    }

}
