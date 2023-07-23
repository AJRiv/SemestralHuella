package com.example.semestralhuella;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Onboarding extends AppCompatActivity {
    EditText userName, password;
    Button btnGuardar;
    String name = "";
    String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_onboarding);

        SharedPreferences sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        boolean isOnboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false);

        if (isOnboardingCompleted) {
            // El Onboarding ya ha sido completado, pasa directamente a la interfaz principal de la aplicación.
            goToMainActivity();
            finish(); // Finaliza la actividad de bienvenida para que el usuario no pueda volver atrás.
        } else {
            // Mostrar la pantalla de bienvenida normalmente
            setContentView(R.layout.activity_onboarding);

            userName = findViewById(R.id.userName);
            password = findViewById(R.id.password);

            btnGuardar = findViewById(R.id.btnGuardar);
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Registro();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("onboarding_completed", true);
                    editor.apply();

                    // Ir a la actividad principal
                    goToMainActivity();
                    finish();
                }
            });
        }
    }
    public void Registro(){
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        name = userName.getText().toString();
        pass = password.getText().toString();

        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("name", name);
        editor.putString("pass", pass);
        editor.commit();
        Toast.makeText(getApplicationContext(), "Guardado Correctamente", Toast.LENGTH_SHORT).show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
