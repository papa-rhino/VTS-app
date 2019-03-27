package com.example.dhruv.vts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {


    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;

    Uri uriProfileImage;
    String profileImageUrl;


    //database code
    EditText editText1;
    EditText editText2;
    EditText editText3;
    Button btnsave;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;
    StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth=FirebaseAuth.getInstance();


        //database code
        editText1=(EditText)findViewById(R.id.editTextName);
        editText2=(EditText)findViewById(R.id.editTextMail);
        editText2.setEnabled(false);
        editText3=(EditText)findViewById(R.id.editTextPhoneNumber);
        btnsave=(Button)findViewById(R.id.save);
        mAuthListener=new FirebaseAuth.AuthStateListener() {
          @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User= firebaseAuth.getCurrentUser();
                if(User != null )
                {
                    //write code
                }
            }
        };
        myRef= FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

        mStorageRef=FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.imageView);
        imageView.setDrawingCacheEnabled(true);
        loadUserInfo();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveuserInfo();

                //database code
                saveUserProfile();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,UserLoginAct.class));
            Toast.makeText(this, "Please Login To Continue", Toast.LENGTH_SHORT).show();
        }
    }



    //database code
    private void saveUserProfile(){
        String FullName;
        String UserEmail;
        String PhoneNumber;

        FullName= editText1.getText().toString().trim();
        UserEmail= editText2.getText().toString().trim();
        PhoneNumber= editText3.getText().toString().trim();

        if(!TextUtils.isEmpty(FullName)){

            myRef.child("user_fullname").setValue(FullName);
           myRef.child("user_email").setValue(UserEmail);
           myRef.child("user_phonenumber").setValue(PhoneNumber);




        }
    }



    private void loadUserInfo() {

        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null) {
            if (user.getPhotoUrl() != null) {

                Glide.with(this).load(user.getPhotoUrl().toString()).into(imageView);
            }
            if (user.getDisplayName() != null) {

                editText1.setText(user.getDisplayName());
                editText2.setText(user.getEmail());
                Toast.makeText(this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }
//            if(user.getEmail()!=null){}
//            Toast.makeText(this, user.getEmail().toString()+""+user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        }

    }

    private void saveuserInfo() {

        String Name=editText1.getText().toString();

        String Phone=editText3.getText().toString();

        if(Name.isEmpty()){
            editText1.setError("Name Required");
            editText1.requestFocus();
            return;
        }

        if(Phone.isEmpty()){
            editText3.setError("Phone Required");
            editText3.requestFocus();
            return;
        }

        FirebaseUser user =mAuth.getCurrentUser();
        Toast.makeText(this, mAuth.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
        if(user!=null && profileImageUrl!=null){
            UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder()
                    .setDisplayName(Name)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();


            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EditProfileActivity.this,"PROFILE UPDATED",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null){

            uriProfileImage=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
private void uploadImageToFirebaseStorage(){
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis() +".jpg");

        if(uriProfileImage != null){
            profileImageRef.
                    putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    profileImageUrl=taskSnapshot.getDownloadUrl().toString();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(EditProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
            };
        }

    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT PROFILE IMAGE "),CHOOSE_IMAGE);
    }


}
