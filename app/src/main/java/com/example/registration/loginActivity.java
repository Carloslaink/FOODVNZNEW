package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;

public class loginActivity extends AppCompatActivity {
    TextView lblCrearCuenta,lblForgotPassword;
    EditText txtInputEmail, txtInputPassword;
    Button btnLogin,btnFacebook,btnGoogle;
    private ProgressDialog mProgressBar;
    private FirebaseAuth mAuth;
    String FACE ="https://www.facebook.com";
    String GOO ="https://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_login);

        btnFacebook =(Button) findViewById(R.id.btnFacebook);
        btnGoogle =(Button) findViewById(R.id.btnGoogle);
        txtInputEmail  =(EditText)  findViewById(R.id.inputEmail);
        txtInputPassword =(EditText) findViewById(R.id.inputPassword);
        btnLogin =(Button) findViewById(R.id.btnlogin);
        lblCrearCuenta =(TextView) findViewById(R.id.txtNotieneCuenta);
        lblForgotPassword =(TextView) findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(GOO));
                startActivity(intent);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(FACE));
                startActivity(intent);
            }
        });

        lblCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this,RegisterActivity.class));
            }
        });
        lblForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, ResetPassw.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificarCredenciales();
            }
        });

        mProgressBar = new ProgressDialog(loginActivity.this);
    }

    public void verificarCredenciales(){
        String email = txtInputEmail.getText().toString();
        String password = txtInputPassword.getText().toString();
        if(email.isEmpty() || !email.contains("@")){
            showError(txtInputEmail, "Email no valido");
        }else if(password.isEmpty()|| password.length()<7){
            showError(txtInputPassword, "Password invalida");
        }else{
            //Mostrar ProgressBar
            mProgressBar.setTitle("Login");
            mProgressBar.setMessage("Iniciando sesión, espere un momento..");
            mProgressBar.setCanceledOnTouchOutside(false);
            mProgressBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        mProgressBar.dismiss();
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("email", txtInputEmail.getText().toString());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "¡Bienvenido!",Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Correo o contraseña son incorrectos.",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void showError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onStart();
    }
}