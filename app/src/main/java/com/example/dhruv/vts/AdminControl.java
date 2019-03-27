package com.example.dhruv.vts;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;


public class AdminControl extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);


    }

    public void checkStatus(View v){
        Intent in=new Intent(this,Driver_View_Status.class);
        startActivity(in);

    }
    public void LockDriver(View v)
    {
        Intent in=new Intent(this,LockDriversActivity.class);
        startActivity(in);
    }
}
