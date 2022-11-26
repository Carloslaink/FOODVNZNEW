package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassw extends AppCompatActivity {

    private TextView btnyatienecuenta;
    private Button btnRestablecer;
    private EditText Email;
    private String emailEs ="";
    private FirebaseAuth  mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passw);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        Email =(EditText) findViewById(R.id.inputEmail);
        btnRestablecer =(Button) findViewById(R.id.btnRestablecer);
        btnyatienecuenta=(TextView) findViewById(R.id.btnyatienecuenta);

        btnyatienecuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Ya tienes una cuenta.", Toast.LENGTH_LONG).show();
            }
        });


        btnRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailEs = Email.getText().toString();
                if(!emailEs.isEmpty()){
                    mDialog.setMessage("Espera un momento..");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetpassword();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar Su correo",Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();


            }
        });

    }

    private void resetpassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(emailEs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "¡Se ha enviado un correo para restablecer tu contraseña!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ResetPassw.this, loginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo enviar el correo de restablecimiento, intente nuevamente.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}