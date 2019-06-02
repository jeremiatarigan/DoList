package com.example.dolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    final String USERNAME_KEY = "usernamekey";
    final String username_key = "";
    String  username_key_new = "";

DatabaseReference reference;
RecyclerView ourdoes;
ArrayList<MyDoes> list;
DoesAdapter doesAdapter;

Button btnAddNew,btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUsernameLocal();

        btnAddNew = findViewById(R.id.btnAddNew);
        btnSignOut = findViewById(R.id.btnSignOut);

//        menyiapkan tempat untuk data
            ourdoes = findViewById(R.id.ourdoes);
            ourdoes.setLayoutManager(new LinearLayoutManager(this));
            list = new ArrayList<MyDoes>();

            btnAddNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(MainActivity.this,NewTaskAct.class);
                    startActivity(a);
                    finish();
                }
            });

            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//menghapus isi atau nilai dari username local
                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(username_key,null);
                    editor.apply();

                    //berpindah acctivity
                    Intent signout = new Intent(MainActivity.this,SignIn.class);
                    startActivity(signout);
                    finish();
                }
            });

//           ambil data dari firebase
            reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(username_key_new);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    retrieve data
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        MyDoes p =dataSnapshot1.getValue(MyDoes.class);
                        list.add(p);
                    }
                    doesAdapter = new DoesAdapter(MainActivity.this,list);
                    ourdoes.setAdapter(doesAdapter);
                    doesAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
//jika terjadi error
                    Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
                }
            });


    }

    public  void  getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }

}
