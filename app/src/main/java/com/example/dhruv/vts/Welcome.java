package com.example.dhruv.vts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class Welcome extends AppCompatActivity {

    LinearLayout l1,l2;
    Button btnusr,btnadm,btndrv;
    Animation uptodown,downtoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        btndrv=(Button)findViewById(R.id.DriverB);
        btnusr = (Button)findViewById(R.id.UserB);
        btnadm = (Button)findViewById(R.id.AdminB);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);

    }

    public void Admin(View v){
        Intent in=new Intent(this,AdminAct.class);
        startActivity(in);
    }
    public void User(View v)
    {
        Intent in=new Intent(this,UserLoginAct.class);
        startActivity(in);
    }

    public void Driver(View v)
    {
        Intent in=new Intent(this,Signin_driver.class);
        startActivity(in);
    }
    }