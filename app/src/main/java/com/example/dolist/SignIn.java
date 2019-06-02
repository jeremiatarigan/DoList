package com.example.dolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    TextView btn_new_account;
    Button btn_signin;
    EditText xusername,xpassword;

    DatabaseReference reference;

    //penyimpanan ke stroge local
    final String USERNAME_KEY = "usernamekey";
    final String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_new_account = findViewById(R.id.btn_new_account);
        btn_signin = findViewById(R.id.btn_signin);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(SignIn.this , RegisterAct.class);
                startActivity(gotoregisterone);
                finish();
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ketika telah diklik ubah state tombol jadi loading
                btn_signin.setEnabled(false);
                btn_signin.setText("Please Wait");

                String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext()," Username Kosong!",Toast.LENGTH_SHORT).show();
                    //ketika telah diklik ubah state tombol jadi loading
                    btn_signin.setEnabled(true);
                    btn_signin.setText("SIGN IN");
                }
                else {
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Password Kosong!",Toast.LENGTH_SHORT).show();
                        //ketika telah diklik ubah state tombol jadi loading
                        btn_signin.setEnabled(true);
                        btn_signin.setText("SIGN IN");
                    }
                    else {
                        reference = FirebaseDatabase.getInstance().getReference().child("User").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    //ambil data password dari firebase
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    //validaasi password
                                    if (password.equals(passwordFromFirebase)){


//                                        //simpan username key pada local
//                                        //mendapat key untuk penyimpanan data data kepada handphone
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key,xusername.getText().toString());
                                        editor.apply();

                                        //berpindah activity
                                        Intent gotohome = new Intent(SignIn.this , MainActivity.class);
                                        startActivity(gotohome);

                                    }else {
                                        Toast.makeText(getApplicationContext(),"Password Salah!",Toast.LENGTH_SHORT).show();
                                        //ketika telah diklik ubah state tombol jadi loading
                                        btn_signin.setEnabled(true);
                                        btn_signin.setText("SIGN IN");
                                    }
                                }

                                else {
                                    Toast.makeText(getApplicationContext(),"Username Tidak Ada!",Toast.LENGTH_SHORT).show();
                                    //ketika telah diklik ubah state tombol jadi loading
                                    btn_signin.setEnabled(true);
                                    btn_signin.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Database Error",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

            }
        });
    }
}
