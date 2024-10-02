package com.niquewrld.buildco.Main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niquewrld.buildco.R;
import com.niquewrld.buildco.User.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mAuth = FirebaseAuth.getInstance();

    }

    public void signUp(View v){

        EditText etFirstName = (EditText) findViewById(R.id.etName);
        String firstName = etFirstName.getText().toString();

        EditText etLastName = (EditText) findViewById(R.id.etSurname);
        String lastName = etLastName.getText().toString();

        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        String email = etEmail.getText().toString();

        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        String password = etPassword.getText().toString();

        EditText etTelNumber = (EditText) findViewById(R.id.etTelNumber);
        String telNumber = etTelNumber.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            String userId = currentUser.getUid();

                            // Create a Map to hold the additional user data
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("firstname", firstName);
                            userData.put("lastname", lastName);
                            userData.put("telephone", telNumber);

                            // Get a reference to the Realtime Database
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                            // Save the data under the user's UID
                            database.child("Users").child(userId)
                                    .setValue(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("RealtimeDB", "User data updated.");
                                            } else {
                                                Log.e("RealtimeDB", "Error updating user data", task.getException());
                                            }
                                        }
                                    });

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                            finish();//to finish the current activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void startSignInActivity(View v){
        Intent i = new Intent(this , SignInActivity.class);
        startActivity(i);
    }

}