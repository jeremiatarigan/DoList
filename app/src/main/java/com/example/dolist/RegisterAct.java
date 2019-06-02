package com.example.dolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterAct extends AppCompatActivity {


    Button btn_continue;
    EditText username,password,email_address;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //penyimpanan ke stroge local
        final String USERNAME_KEY = "usernamekey";
        final String username_key = "";


        btn_continue = findViewById(R.id.btn_continue);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ketika telah diklik ubah state tombol jadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Please Wait");

                //mendapat key untuk penyimpanan data data kepada handphone
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key,username.getText().toString());
                editor.apply();

                //simpan ke database
                reference = FirebaseDatabase.getInstance().getReference().child("User").child(username.getText().toString());

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                        dataSnapshot.getRef().child("email").setValue(email_address.getText().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //berpindah activity
                Intent gotonextregister = new Intent(RegisterAct.this , MainActivity.class);
                startActivity(gotonextregister);
                finish();
            }
        });
    }
}
