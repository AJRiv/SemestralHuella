package com.example.semestralhuella;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Notas extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_NOTE = 1;
    private static final String SHARED_PREFS_KEY = "notes_shared_prefs";
    private static final String NOTES_SET_KEY = "notes_set";

    private LinearLayout layoutNotesContainer;
    private List<String> notesList = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        layoutNotesContainer = findViewById(R.id.layoutNotesContainer);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);

        Button buttonAddNote = findViewById(R.id.buttonAddNote);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la actividad de edición de notas al presionar "Agregar Nota"
                Intent intent = new Intent(Notas.this, EditarNotas.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
            }
        });

        // Cargar las notas almacenadas en SharedPreferences
        loadNotes();

        updateNotesLayout();
    }

    // Método para refrescar la lista de notas en el LinearLayout
    private void updateNotesLayout() {
        layoutNotesContainer.removeAllViews();
        for (int i = 0; i < notesList.size(); i++) {
            addNoteToLayout(i, notesList.get(i));
        }
    }

    // Método para agregar una nueva nota al LinearLayout como un nuevo cuadro visual
    private void addNoteToLayout(int position, String noteText) {
        TextView textViewNote = new TextView(this);
        textViewNote.setText(noteText);
        textViewNote.setTextColor(getResources().getColor(android.R.color.white)); // Color del texto
        textViewNote.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Color del fondo
        textViewNote.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 8, 0, 0); // Márgenes para espaciado entre cuadros
        textViewNote.setLayoutParams(layoutParams);

        // Agregar el OnLongClickListener para mostrar el cuadro de diálogo de eliminación
        textViewNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteConfirmationDialog(position);
                return true; // Indica que el evento ha sido procesado
            }
        });

        layoutNotesContainer.addView(textViewNote);
    }

    // Método para mostrar el cuadro de diálogo de confirmación de eliminación
    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Nota")
                .setMessage("¿Estás seguro de que deseas eliminar esta nota?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Eliminar la nota si el usuario confirma
                        deleteNoteAtPosition(position);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Método para eliminar una nota basada en su posición en la lista
    private void deleteNoteAtPosition(int position) {
        if (position >= 0 && position < notesList.size()) {
            notesList.remove(position);
            updateNotesLayout();
            saveNotes();
        }
    }

    // Método para guardar las notas en SharedPreferences
    private void saveNotes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> notesSet = new HashSet<>(notesList);
        editor.putStringSet(NOTES_SET_KEY, notesSet);
        editor.apply();
    }

    // Método para cargar las notas desde SharedPreferences
    private void loadNotes() {
        Set<String> notesSet = sharedPreferences.getStringSet(NOTES_SET_KEY, new HashSet<>());
        notesList.clear();
        notesList.addAll(notesSet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            // Obtener la nota editada de la actividad de edición
            String editedNote = data.getStringExtra("edited_note");
            // Verificar si se ha creado una nueva nota
            boolean isNewNote = data.getBooleanExtra("is_new_note", false);
            if (isNewNote) {
                // Agregar la nueva nota a la lista y refrescar la lista de notas
                notesList.add(editedNote);
            } else {
                // Obtener la posición de la nota editada
                int editedNotePosition = data.getIntExtra("note_position", -1);
                // Verificar que se haya enviado una posición válida
                if (editedNotePosition >= 0 && editedNotePosition < notesList.size()) {
                    // Actualizar la nota en la lista y reflejar los cambios en la lista de notas
                    notesList.set(editedNotePosition, editedNote);
                }
            }
            updateNotesLayout();
            // Guardar las notas actualizadas en SharedPreferences
            saveNotes();
        }
    }
}
