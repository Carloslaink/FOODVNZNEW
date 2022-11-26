package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView userNombre, userEmail, userID;
    private CircleImageView userImg;
    private FirebaseAuth mAuth;
    private Button btnCerrarSesion;
    private Button btnEliminarCuenta;
    private ImageView btnGoMap;
    private Bundle bundle;


    /////////// IMPLEMENTS SQL  ////////////////////7


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userNombre =(TextView) findViewById(R.id.userNombre);
        userEmail =(TextView) findViewById(R.id.userEmail);
        userID =(TextView) findViewById(R.id.userId);
        userImg =(CircleImageView) findViewById(R.id.userImagen);
        mAuth = FirebaseAuth.getInstance();
        btnGoMap =(ImageView) findViewById(R.id.btnmap);


        ////Bundle instance
        try {
            bundle = getIntent().getExtras();
            userNombre.setText(bundle.getString("email"));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Exception", Toast.LENGTH_LONG).show();
        }




        FirebaseUser user = mAuth.getCurrentUser();

        btnCerrarSesion =(Button) findViewById(R.id.btnLogout);
        btnEliminarCuenta =(Button) findViewById(R.id.btnEliminarCta);

        userEmail.setText(user.getEmail());
        userID.setText(user.getUid());


        btnGoMap.setOnClickListener(new android.view.View.OnClickListener()  {
            @Override
            public void onClick(android.view.View view) {
                Intent i = new Intent(getApplicationContext(), MapboxActivity2.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Suerte en tu busqueda.", Toast.LENGTH_LONG).show();
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "¡Sesion Cerrada con Exito!", Toast.LENGTH_LONG).show();
            }
        });

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                securityDelete();
            }
        });

    }
    private void securityDelete(){

        new AlertDialog.Builder(this)

                .setTitle("Confirma por Seguridad")
                .setMessage("¿Seguro Decea Eliminar Su Cuenta?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider.getCredential("user@example.com","password1234");
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(MainActivity.this, loginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    Toast.makeText(getApplicationContext(), "Su Cuenta se Elimino Correctamente", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No se pudo Eliminar Cuenta", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Mensaje","Se Cancelo la Accion");
                    }
                })
                .show();
    }

}