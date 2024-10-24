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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfilActivity extends AppCompatActivity {

    private TextView textNumePrenume; // Declara variabila pentru TextView
    private static EditText editTextName, editTextPrenume, editTextVarsta, editTextGreutate, editTextInaltime;
    private Spinner spinnerSex;
    private Button buttonSave;

    // Numele fișierului pentru SharedPreferences
    private static final String PREFS_NAME = "UserProfilePrefs";

    public static String getName(){
        return editTextName.getText().toString() + " " + editTextPrenume.getText().toString();
    }

    public static int getVarsta(){
        return Integer.parseInt(editTextVarsta.getText().toString());
    }

    public static double getGreutate(){
        return Double.parseDouble(editTextGreutate.getText().toString());
    }

    public static double getInaltime(){
        return Double.parseDouble(editTextInaltime.getText().toString());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);

        // Configurăm view-urile
        textNumePrenume = findViewById(R.id.textNumePrenume); // Inițializare
        editTextName = findViewById(R.id.editTextName);
        editTextPrenume = findViewById(R.id.editTextPrenume);
        editTextVarsta = findViewById(R.id.editTextVarsta);
        editTextGreutate = findViewById(R.id.editTextGreutate);
        editTextInaltime = findViewById(R.id.editTextInaltime);
        spinnerSex = findViewById(R.id.spinnerSex);
        buttonSave = findViewById(R.id.buttonSave);

        // Configurăm adapterul pentru Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, R.layout.spinner_item);
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

        // Obține valorile din EditText-uri
        String name = editTextName.getText().toString();
        String prenume = editTextPrenume.getText().toString();

        try {
            int varsta = Integer.parseInt(editTextVarsta.getText().toString());
            float greutate = Float.parseFloat(editTextGreutate.getText().toString());
            float inaltime = Float.parseFloat(editTextInaltime.getText().toString());

            // Salvează valorile în SharedPreferences
            editor.putString("name", name);
            editor.putString("prenume", prenume);
            editor.putInt("varsta", varsta);
            editor.putFloat("greutate", greutate);
            editor.putFloat("inaltime", inaltime);
            editor.putString("sex", spinnerSex.getSelectedItem().toString());
            editor.apply(); // Aplică modificările

            // Actualizează TextView-ul cu noul text
            textNumePrenume.setText(name + " " + prenume);

            // Afișează un mesaj de succes
            Toast.makeText(this, "Profil salvat cu succes!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            // Afișează un mesaj de eroare dacă intrările nu sunt valide
            Toast.makeText(this, "Introduceți valori valide pentru vârstă, greutate și înălțime!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Obține valorile de nume și prenume din SharedPreferences
        String name = sharedPreferences.getString("name", "");
        String prenume = sharedPreferences.getString("prenume", "");

        // Setează textul combinat pentru textNumePrenume (TextView)
        textNumePrenume.setText(name + " " + prenume);
        editTextName.setText(name);
        editTextPrenume.setText(prenume);
        editTextVarsta.setText(String.valueOf(sharedPreferences.getInt("varsta", 0)));
        editTextGreutate.setText(String.valueOf(sharedPreferences.getFloat("greutate", 0)));
        editTextInaltime.setText(String.valueOf(sharedPreferences.getFloat("inaltime", 0)));
        spinnerSex.setSelection(((ArrayAdapter) spinnerSex.getAdapter()).getPosition(sharedPreferences.getString("sex", "Masculin")));
    }
}
