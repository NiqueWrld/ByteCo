package com.niquewrld.buildco.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.niquewrld.buildco.Admin.AdminActivity;
import com.niquewrld.buildco.Main.SignInActivity;
import com.niquewrld.buildco.Main.SignUpActivity;
import com.niquewrld.buildco.R;
import com.niquewrld.buildco.User.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String userUID = currentUser.getUid();
            if(userUID.equals("v528izqengMhEDor0biV9cCkQkD2")){
                Intent intent=new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth=FirebaseAuth.getInstance();

    }

    public void startSignIn(View v){
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
    }

    public void startSignUp(View v){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

}