package com.example.semestralhuella;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

public class Login extends AppCompatActivity {
    EditText password;
    Editable edtPassword;
    Button btnIniciar,btnHuella;

    String contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                contra = preferences.getString("pass", "");

                password = findViewById(R.id.password);
                edtPassword = password.getText();
                String strPassword = edtPassword.toString();
                if (strPassword.equals(contra)){
                    /*Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(getApplicationContext(), "Las contrasenas son iguales", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Las contrasenas NO son iguales", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuella = findViewById(R.id.btnHuella);
        btnHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FuncionHuella();
            }
        });
    }

    public void FuncionHuella(){
        //FUNCION DE HUELLA
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Título de autenticación")
                .setSubtitle("Subtítulo de autenticación")
                .setDescription("Descripción de autenticación")
                .setNegativeButtonText("Cancelar")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        // La autenticación biométrica fue exitosa
                        Toast.makeText(getApplicationContext(), "Hola Derek, tas fuerte!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        // La autenticación biométrica falló
                    }
                });

        biometricPrompt.authenticate(promptInfo);
        //FIN FUNCION HUELLA
    }
}