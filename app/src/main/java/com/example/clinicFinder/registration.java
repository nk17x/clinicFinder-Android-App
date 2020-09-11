package com.example.clinicFinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {
    Button button,login;
    TextView textView2;
    TextInputEditText textphoneno,textfullname,textemail,textpassword;
    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    boolean registerstatus=true;
    String email,password,phone,fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth=FirebaseAuth.getInstance();
        initilaize();

        rootNode=FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(registration.this,login.class);
                startActivity(i);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone=textphoneno.getText().toString();
                fullname =textfullname.getText().toString();
                email=textemail.getText().toString();
                password =textpassword.getText().toString();
                String str = email;
                String username="";
                String [] twoStringArray= str.split("@",2);
                username= twoStringArray[0];
                databaseReference=rootNode.getReference("users");
                UserHelperClass helperClass= new UserHelperClass(fullname,phone,email,password,username);
                databaseReference.child(username).setValue(helperClass);
                    if(TextUtils.isEmpty(email)){
                        textemail.setError("enter email");
                        return;
                    }
                   if(TextUtils.isEmpty(password)){
                       textpassword.setError("enter password");
                   return;}
                if(TextUtils.isEmpty(phone)){
                    textphoneno.setError("enter phone no");
                    return;
                }
                if(TextUtils.isEmpty(fullname)){
                    textfullname.setError("enter fullname");
                    return;}

                   if(registerstatus){
                       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   Log.i("login","register is going");
                                   registerstatus=false;

                                   Intent i =new Intent(registration.this,login.class);
                                   startActivity(i);
                                   Toast.makeText(registration.this, "registeration succesfull", Toast.LENGTH_SHORT).show();
                                   mAuth.signOut();
                                    finish();
                               }
                               else{
                                   Toast.makeText(registration.this, "not succesfull", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }
                   else{
                       mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   Log.i("login","login is going");
                                   Intent i =new Intent(registration.this,MainActivity.class);
                                   startActivity(i);
                                   finish();
                               }

                           }
                       }); }
                   }



            });
        }

    public void initilaize(){
        button=findViewById(R.id.button);
        login=findViewById(R.id.login);
        textphoneno=findViewById(R.id.phoneno);
        textemail=findViewById(R.id.email);
        textpassword=findViewById(R.id.password);
        textfullname=findViewById(R.id.fullname);

    }
}