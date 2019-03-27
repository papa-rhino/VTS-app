package com.example.dhruv.vts;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LockDriversActivity extends AppCompatActivity {
    EditText driverName,busRoute;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String driversName,route;
    ScrollView scr;
    LinearLayout lr;
    String key[];
    String value[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_drivers);
       database = FirebaseDatabase.getInstance();
         myRef = database.getReference("Driver");
       driverName = findViewById(R.id.driverName);
       busRoute = findViewById(R.id.busRoute);
        scr=findViewById(R.id.scrollView);
      lr=  findViewById(R.id.scrHost);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                key=new String[(int) dataSnapshot.getChildrenCount()];
                value=new String[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                    key[i]= postSnapshot.getKey();
                    value[i]=postSnapshot.getValue().toString();
                    Log.e("KEYYYYY",key[i]+"   "+value[i]);
                    ++i;

                }
                lr.removeAllViewsInLayout();

                for (int j = 0; j < key.length; j++) {
                    TextView tv = new TextView(LockDriversActivity.this);


                    tv.setGravity(Gravity.FILL_VERTICAL);
                    tv.setText(key[j] + "               " + value[j]);

                        lr.addView(tv,j);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LockDriversActivity.this,databaseError.toString(), Toast.LENGTH_SHORT).show();
            }

        });




    }

   public void saveAdmin(View view){
       driversName=driverName.getText().toString();
       route=busRoute.getText().toString();

       myRef.child(driversName).setValue(route).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(LockDriversActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
           }
       });



       Log.e("rrrrrrr",route+driversName);

    }


}
