package com.developer.pritampc.goaltracker;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton addgoal;
    RecyclerView recyclerView;
    MyAdapter adapter;
    RecyclerView.LayoutManager manager;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    ArrayList<GoalData> goaldatalist=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    TextView login_tv;
    EditText input_email,input_password;
    ProgressBar login_progress;
    ConstraintLayout loginlayout;
    private static final String TAG="dbread";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycleview);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference("goal");
        mAuth=FirebaseAuth.getInstance();

        addgoal=findViewById(R.id.floatingActionButton);
        login_tv=findViewById(R.id.login_tv);
        input_email=findViewById(R.id.email_in_log);
        input_password=findViewById(R.id.pass_in_log);
        login_progress=findViewById(R.id.progress_log);
        loginlayout=findViewById(R.id.LoginLay);
        loginlayout.setVisibility(View.VISIBLE);
        login_progress.setVisibility(View.INVISIBLE);
        findViewById(R.id.email_in_log).setOnClickListener(this);
        findViewById(R.id.pass_in_log).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);
        login_tv.setOnClickListener(this);
        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        addgoal.setVisibility(View.INVISIBLE);
        overridePendingTransition(R.anim.slide_up,R.anim.fade_out);

        addgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),InputActivity.class);
                startActivity(i);

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goaldatalist.clear();
                onDataRead();
            }
        });

        adapter=new MyAdapter(goaldatalist, MainActivity.this);
        manager=new LinearLayoutManager(MainActivity.this);

        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();
                if(direction==ItemTouchHelper.LEFT)
                {
                    GoalData del_item_data=goaldatalist.get(position);
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("goal").child(del_item_data.getId());
                    ref.removeValue();
                    goaldatalist.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeRemoved(0,goaldatalist.size());
                    Toast.makeText(getApplicationContext(),"Successfully Deleted!",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());

                }
            }
        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onStart() {
        super.onStart();
        goaldatalist.clear();
        //// reading from Firebase database
        onDataRead();
    }

    public void onDataRead()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot goalsnapshot: dataSnapshot.getChildren())
                {
                    GoalData data=goalsnapshot.getValue(GoalData.class);
                    goaldatalist.add(data);
                }
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.login_tv:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
            case R.id.login_btn:
                loginUser();

                break;
            case R.id.email_in_log:

                break;
            case R.id.pass_in_log:

                break;
        }
    }
    public void loginUser()
    {
        String email=input_email.getText().toString().trim();
        String password=input_password.getText().toString().trim();

        if(email.isEmpty())
        {
            input_email.setError("Please enter a email");
            input_email.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            input_password.setError("Please enter a email");
            input_password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            input_email.setError("Enter a valid email");
            input_email.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            input_password.setError("Minimum password length is 6");
            input_password.requestFocus();
            return;
        }
        login_progress.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            login_progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"SignIn Successful",Toast.LENGTH_SHORT).show();
                            loginlayout.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            addgoal.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }
}
