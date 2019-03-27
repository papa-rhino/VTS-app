package com.example.dhruv.vts;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class AdminAct extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText emailid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


         database = FirebaseDatabase.getInstance();
        myRef= database.getReference("Admin");
        emailid=findViewById(R.id.editextEmail);



    }

   public void LoginAdmin(View v){



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String email=emailid.getText().toString().trim();
                String value[]=new String[(int) dataSnapshot.getChildrenCount()];
                Log.e("children count",dataSnapshot.getChildrenCount()+"");
                int i=0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


            value[i]= postSnapshot.getValue().toString();

                    ++i;
                }
                Log.e("Value",value[0]+"  "+value[1]+"  "+email);
                for (int j=0;j<value.length;j++) {
                    if (email.equalsIgnoreCase(value[j])) {

                        Intent in = new Intent(AdminAct.this, AdminControl.class);
                        startActivity(in);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("databaseError", "Failed to read value.", error.toException());
            }
        });

    }


}
