package com.example.dolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class NewTaskAct extends AppCompatActivity {

    Button btnSaveTask,btnCancel;
    EditText titledoes,descdoes,datedoes;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    DatabaseReference reference;

    Integer doesNum = new Random().nextInt();

    //penyimpanan ke stroge local

    final String USERNAME_KEY = "usernamekey";
    final String username_key = "";
    String  username_key_new = "";


    String keydoes = Integer.toString(doesNum);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        getUsernameLocal();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);

        datedoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(NewTaskAct.this,MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").child(username_key_new).child("Does" + doesNum);
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titledoes.getText().toString().length()==0){
                        titledoes.setError(" Mohon lengkapi title !");
                } if (descdoes.getText().toString().length()==0){
                    descdoes.setError("Isi Deskripsi!");
                }
                else {
//                    insert dat ake databas
//                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE)
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        insert data
                            dataSnapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                            dataSnapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                            dataSnapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                            dataSnapshot.getRef().child("keydoes").setValue(keydoes);

                            Intent a = new Intent(NewTaskAct.this,MainActivity.class);
                            startActivity(a);
                            finish();

                            Toast.makeText(NewTaskAct.this, "Scheduled Has Been Added", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
//
            }
        });

    }

    private void showDateDialog() {

        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                datedoes.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    public  void  getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
