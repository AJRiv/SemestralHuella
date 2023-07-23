package com.example.semestralhuella;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditarNotas extends AppCompatActivity {

    private EditText editTextNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_notas);

        editTextNote = findViewById(R.id.editTextNote);
        Button buttonSaveNote = findViewById(R.id.buttonSaveNote);

        String originalNote = getIntent().getStringExtra("note_text");
        editTextNote.setText(originalNote);

        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar la nota y enviarla de regreso a MainActivity
                String editedNote = editTextNote.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("edited_note", editedNote);
                resultIntent.putExtra("is_new_note", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
