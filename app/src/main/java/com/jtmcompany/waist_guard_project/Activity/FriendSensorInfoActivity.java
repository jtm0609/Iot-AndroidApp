package com.jtmcompany.waist_guard_project.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jtmcompany.waist_guard_project.R;

//보호자기능: 친구(사용자)의 센서정보 모니터링 엑티비티
public class FriendSensorInfoActivity extends AppCompatActivity {
    TextView name_text,heart_text,temp_text,vibrate_text;
    ProgressBar heart_progress,temp_progress;
    DatabaseReference mDatabase;
    ValueEventListener valueEventListener;
    String name,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_sensor_info);
        init();
        callbackInit();
        initSet();
    }

    public void init(){
        name_text=findViewById(R.id.name_textvw);
        heart_text=findViewById(R.id.heart_sub_text);
        temp_text=findViewById(R.id.temp_sub_text);
        vibrate_text=findViewById(R.id.vibrate_text);
        heart_progress=findViewById(R.id.heart_progress2);
        temp_progress=findViewById(R.id.temp_progress2);
        mDatabase= FirebaseDatabase.getInstance().getReference();

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        uid=intent.getStringExtra("uid");
    }

    public void callbackInit(){
        valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mvibration = (String) dataSnapshot.child("vibration").getValue();
                String mtemp = (String) dataSnapshot.child("temp").getValue();
                String mheart = (String) dataSnapshot.child("pulse").getValue();
                heart_progress.setProgress(Integer.parseInt(mheart));
                heart_text.setText(mheart);
                temp_progress.setProgress(Integer.parseInt(mtemp));
                temp_text.setText(mtemp);
                vibrate_text.setText(mvibration);
                Log.d("tak333", "test입니다" + mheart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
    }

    public void initSet(){
        name_text.setText(name);
        mDatabase.child("유저").child("사용자").child(uid).child("sensorInfo").addValueEventListener(valueEventListener);
    }

    //액티비티가 종료될대도 비동기작업이 되므로 종료시 비동기작업을 종료시켜여야함( 하지만 onPause에서 하는게 더낫다고함)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tak333","onDestroy");
        mDatabase.child("유저").child("사용자").child(uid).child("sensorInfo").removeEventListener(valueEventListener);

    }

}
