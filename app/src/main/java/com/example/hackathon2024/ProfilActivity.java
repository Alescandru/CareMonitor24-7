package com.example.hackathon2024;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfilActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrenume, editTextVarsta, editTextGreutate, editTextInaltime;
    private Spinner spinnerSex;
    private Button buttonSave;

    // Numele fișierului pentru SharedPreferences
    private static final String PREFS_NAME = "UserProfilePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);

        // Configurăm view-urile
        editTextName = findViewById(R.id.editTextName);
        editTextPrenume = findViewById(R.id.editTextPrenume);
        editTextVarsta = findViewById(R.id.editTextVarsta);
        editTextGreutate = findViewById(R.id.editTextGreutate);
        editTextInaltime = findViewById(R.id.editTextInaltime);
        spinnerSex = findViewById(R.id.spinnerSex);
        buttonSave = findViewById(R.id.buttonSave);

        // Configurăm Spinner pentru sex
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);

        // Încărcăm datele utilizatorului
        loadUserProfile();

        // Salvăm datele la apăsarea butonului
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        // Setează padding pentru aspectul Edge to Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout mainLayout = findViewById(R.id.menuLayout).findViewById(R.id.mainLayout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea Main
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Adaugă listener pentru a merge la RaportActivity
        LinearLayout raportLayout = findViewById(R.id.menuLayout).findViewById(R.id.raportLayout);
        raportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea de rapoarte
                Intent intent = new Intent(ProfilActivity.this, RaportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", editTextName.getText().toString());
        editor.putString("prenume", editTextPrenume.getText().toString());
        editor.putInt("varsta", Integer.parseInt(editTextVarsta.getText().toString()));
        editor.putFloat("greutate", Float.parseFloat(editTextGreutate.getText().toString()));
        editor.putFloat("inaltime", Float.parseFloat(editTextInaltime.getText().toString()));
        editor.putString("sex", spinnerSex.getSelectedItem().toString());

        editor.apply();
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        editTextName.setText(sharedPreferences.getString("name", ""));
        editTextPrenume.setText(sharedPreferences.getString("prenume", ""));
        editTextVarsta.setText(String.valueOf(sharedPreferences.getInt("varsta", 0)));
        editTextGreutate.setText(String.valueOf(sharedPreferences.getFloat("greutate", 0)));
        editTextInaltime.setText(String.valueOf(sharedPreferences.getFloat("inaltime", 0)));
        spinnerSex.setSelection(((ArrayAdapter) spinnerSex.getAdapter()).getPosition(sharedPreferences.getString("sex", "Masculin")));
    }
}
