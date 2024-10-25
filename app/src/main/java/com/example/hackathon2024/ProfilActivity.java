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

    private TextView textNumePrenume;
    private EditText editTextName, editTextPrenume, editTextVarsta, editTextGreutate, editTextInaltime;
    private EditText editTextDoctorName, editTextDoctorPhone, editTextDoctorEmail; // Campurile pentru medic
    private Spinner spinnerSex;
    private Button buttonSave, saveDoctorButton; // Butonul pentru salvare profil și medic

    private static final String PREFS_NAME = "UserProfilePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);

        // Inițializare view-uri
        textNumePrenume = findViewById(R.id.textNumePrenume);
        editTextName = findViewById(R.id.editTextName);
        editTextPrenume = findViewById(R.id.editTextPrenume);
        editTextVarsta = findViewById(R.id.editTextVarsta);
        editTextGreutate = findViewById(R.id.editTextGreutate);
        editTextInaltime = findViewById(R.id.editTextInaltime);
        editTextDoctorName = findViewById(R.id.editTextDoctorName); // Inițializare campuri pentru medic
        editTextDoctorPhone = findViewById(R.id.editTextDoctorPhone);
        editTextDoctorEmail = findViewById(R.id.editTextDoctorEmail);
        spinnerSex = findViewById(R.id.spinnerSex);
        buttonSave = findViewById(R.id.buttonSave);
        saveDoctorButton = findViewById(R.id.saveDoctorButton); // Inițializare buton de salvare a datelor medicului

        // Configurăm adapterul pentru Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);

        // Încărcăm datele utilizatorului
        loadUserProfile();

        // Încărcăm datele medicului
        loadDoctorProfile();

        // Salvăm datele utilizatorului la apăsarea butonului
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        // Salvăm datele medicului la apăsarea butonului
        saveDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDoctorProfile();
            }
        });

        // Configurare pentru Edge to Edge
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

    // Funcție pentru salvarea detaliilor medicului
    private void saveDoctorProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Obține valorile din EditText-uri pentru medic
        String doctorName = editTextDoctorName.getText().toString();
        String doctorPhone = editTextDoctorPhone.getText().toString();
        String doctorEmail = editTextDoctorEmail.getText().toString();

        // Salvează datele medicului în SharedPreferences
        editor.putString("doctor_name", doctorName);
        editor.putString("doctor_phone", doctorPhone);
        editor.putString("doctor_email", doctorEmail);

        editor.apply(); // Aplică modificările

        // Afișează un mesaj de succes
        Toast.makeText(this, "Datele medicului au fost salvate cu succes!", Toast.LENGTH_SHORT).show();
    }

    // Funcție pentru încărcarea detaliilor medicului
    private void loadDoctorProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Obține valorile medicului din SharedPreferences
        String doctorName = sharedPreferences.getString("doctor_name", "");
        String doctorPhone = sharedPreferences.getString("doctor_phone", "");
        String doctorEmail = sharedPreferences.getString("doctor_email", "");

        // Setează valorile în EditText-urile corespunzătoare
        editTextDoctorName.setText(doctorName);
        editTextDoctorPhone.setText(doctorPhone);
        editTextDoctorEmail.setText(doctorEmail);
    }

    // Funcție pentru salvarea datelor utilizatorului (nume, prenume, etc.)
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

    // Funcție pentru încărcarea datelor utilizatorului
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
