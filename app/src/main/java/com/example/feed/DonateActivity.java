package com.example.feed;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DonateActivity extends AppCompatActivity {


    private EditText uname, fname, quantity, number;
    private DatabaseReference reff;
    private Child member;
    private long maxid = 0;
    private Double lat=0.0,lng=0.0;
//    private boolean isValidPhoneNumber(String number) {
//        // Use regex pattern to validate the phone number
//        String regex = "^\\d{10}$"; // Regex pattern for a 10-digit phone number
//        return number.matches(regex);
//    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        uname = (EditText) findViewById(R.id.etname);
        fname = (EditText) findViewById(R.id.etElename);
        quantity = (EditText) findViewById(R.id.etquantity);
        number = (EditText) findViewById(R.id.etnum);

        Button btn = (Button) findViewById(R.id.btnsubmit);

        member = new Child();

        reff = FirebaseDatabase.getInstance("https://ewasteproj-default-rtdb.firebaseio.com/").getReference().child("Child");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String un, fn, qt, nm;

                un = uname.getText().toString().trim();
                fn = fname.getText().toString().trim();
                qt = quantity.getText().toString().trim();
                nm = number.getText().toString().trim();

                if (TextUtils.isEmpty(un) || TextUtils.isEmpty(fn) || TextUtils.isEmpty(qt) || TextUtils.isEmpty(nm)) {
                    Toast.makeText(DonateActivity.this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
//                } else if (!isValidPhoneNumber(nm)) {
//                    Toast.makeText(DonateActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    member.setUserName(un);
                    member.setEleName(fn);
                    member.setQuantity(qt);
                    member.setNumber(nm);
                    reff.child(String.valueOf(maxid + 1)).setValue(member);
                    Toast.makeText(DonateActivity.this, "Donated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DonateActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
    });
}

}