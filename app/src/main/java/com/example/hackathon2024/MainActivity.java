package com.example.hackathon2024;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.room.Room;

import com.example.hackathon2024.database.AppDatabase;
import com.example.hackathon2024.database.DailyReport;
import com.example.hackathon2024.database.DailyReportDao;
import com.example.hackathon2024.database.HealthRecord;
import com.example.hackathon2024.database.HealthRecordDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import android.content.pm.PackageManager;
import android.widget.LinearLayout;
import android.content.Intent;



public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE=1232;
    private TextView concluziiTextView;
    private TextView pulsTextView, oxigenTextView, tensiuneTextView;

    // Inițializare variabile pentru valori simulate
    private List<Integer> puls;
    private List<Integer> oxigen;
    private List<Integer> tensiuneSistolic;
    private List<Integer> tensiuneDiastolic;

    private AppDatabase db;

    // Simulator de senzori
    private SensorSimulator simulator;
    Button buttonIsRunig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fieldInit();

        db = AppDatabase.getInstance(getApplicationContext());

        // Ajustează padding-ul pentru layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inițializează elementele UI
        Button buttonAnalizare = findViewById(R.id.buttonAnalizare);
        concluziiTextView = findViewById(R.id.concluziiTextView);
        pulsTextView = findViewById(R.id.pulsVal);       // TextView pentru puls
        oxigenTextView = findViewById(R.id.oxigenVal);   // TextView pentru oxigen
        tensiuneTextView = findViewById(R.id.tensiuneVal); // TextView pentru tensiune

        // Creează simulatorul de senzori și pornește simularea
        simulator = new SensorSimulator(new SensorSimulator.SensorDataListener() {
            @Override
            public void onSensorDataChanged(int pulse, int oxygenLevel, int systolicPressure, int diastolicPressure) {

                puls.add(pulse);
                oxigen.add(oxygenLevel);
                tensiuneSistolic.add(systolicPressure);
                tensiuneDiastolic.add(diastolicPressure);

                if (puls.size() > 5) {
                    collectData();
                    backupData();
                }

                // Actualizează valorile din interfață
                pulsTextView.setText(pulse + " BPM");
                oxigenTextView.setText(oxygenLevel + "% SpO2");
                tensiuneTextView.setText(systolicPressure + "/" + diastolicPressure + " mmHg");
            }
        }, AppDatabase.getInstance(this.getApplicationContext()));

        // Pornește simularea la inițializarea aplicației
        simulator.startSimulation();

        // Adaugă listener pentru profil sa schimba pagina
        LinearLayout profilLayout = findViewById(R.id.menuLayout).findViewById(R.id.profilLayout);
        profilLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea de profil
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });
        // Adaugă listener pentru rapoarte sa schimba pagina
        LinearLayout raportLayout = findViewById(R.id.menuLayout).findViewById(R.id.raportLayout);
        raportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea de raport
                Intent intent = new Intent(MainActivity.this, RaportActivity.class);
                startActivity(intent);
            }
        });
        // Setează listener-ul pentru butonul de analizare
        buttonAnalizare.setOnClickListener(view -> analizaDateMedicale());

        buttonIsRunig = findViewById(R.id.isRunningB);
        buttonIsRunig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the isRunning state
                boolean newState = !simulator.isRunning(); // Use the getter method
                simulator.setRunning(newState); // Update the simulator's running state

                // Optionally, update the button text based on the new state
                buttonIsRunig.setText(newState ? "Running" : "notRunning");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Oprește simularea la distrugerea activității
        simulator.stopSimulation();
    }

    /**
     * Initialize class fields
     */
    private void fieldInit() {
        puls = new ArrayList<>();
        oxigen = new ArrayList<>();
        tensiuneDiastolic = new ArrayList<>();
        tensiuneSistolic = new ArrayList<>();
    }

    // Funcția care analizează datele medicale
    private void analizaDateMedicale() {
        StringBuilder concluzii = new StringBuilder();

        // Check if simulation is running and set thresholds accordingly
        boolean running = simulator.isRunning(); // Get the current running state

        // Analiza pulsului

        if (running) {
            // Higher thresholds for running state
            if (puls < 100) {
                concluzii.append("Bradicardie: Pulsul este prea mic.\n");
            } else if (puls > 150) {
                concluzii.append("Tahicardie: Pulsul este prea mare.\n");
            } else {
                concluzii.append("Pulsul este în limite normale.\n");
            }
        } else {
            // Normal thresholds when not running
            if (puls < 60) {
                concluzii.append("Bradicardie: Pulsul este prea mic.\n");
            } else if (puls > 100) {
                concluzii.append("Tahicardie: Pulsul este prea mare.\n");
            } else {
                concluzii.append("Pulsul este în limite normale.\n");
            }
        }

        // Analiza nivelului de oxigen din sânge

        if (running) {
            // Adjusted threshold for oxygen level when running
            if (oxigen < 92) {
                concluzii.append("Nivel scăzut de oxigen: Hipoxemie.\n");
            } else {
                concluzii.append("Nivelul de oxigen este în limite normale.\n");
            }
        } else {
            // Normal threshold for oxygen level
            if (oxigen < 90) {
                concluzii.append("Nivel scăzut de oxigen: Hipoxemie.\n");
            } else {
                concluzii.append("Nivelul de oxigen este în limite normale.\n");
            }
        }

        // Analiza tensiunii arteriale
        if (running) {
            // Higher thresholds for blood pressure when running
            if (tensiuneSistolic > 200 || tensiuneDiastolic > 110) {
                concluzii.append("Tensiune arterială ridicată: Hipertensiune.\n");
            } else if (tensiuneSistolic < 160 || tensiuneDiastolic < 80) {
                concluzii.append("Tensiune arterială scăzută: Hipotensiune.\n");
            } else {
                concluzii.append("Tensiunea arterială este în limite normale.\n");
            }
        } else {
            // Normal thresholds for blood pressure when not running
            if (tensiuneSistolic > 140 || tensiuneDiastolic > 90) {
                concluzii.append("Tensiune arterială ridicată: Hipertensiune.\n");
            } else if (tensiuneSistolic < 90 || tensiuneDiastolic < 60) {
                concluzii.append("Tensiune arterială scăzută: Hipotensiune.\n");
            } else {
                concluzii.append("Tensiunea arterială este în limite normale.\n");
            }
        }

        // Afișează concluziile în TextView
        concluziiTextView.setText(concluzii.toString());
    }

    public static <T> T lastElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null; // Better be safe
        }
        return list.get(list.size() - 1);
    }

    /**
     * Insert vitals lists (puls, oxigen, tensiuneSistolic, tensiuneDiastolic) into db and clears them
     */
    public void collectData() {
        HealthRecordDao dao = this.db.healthRecordDao();

        // Create copies of the lists to ensure thread safety
        List<Integer> pulsCopy;
        List<Integer> oxigenCopy;
        List<Integer> tensiuneSistolicCopy;
        List<Integer> tensiuneDiastolicCopy;

        synchronized (this) {
            // Create copies of the lists
            pulsCopy = new ArrayList<>(puls);
            oxigenCopy = new ArrayList<>(oxigen);
            tensiuneSistolicCopy = new ArrayList<>(tensiuneSistolic);
            tensiuneDiastolicCopy = new ArrayList<>(tensiuneDiastolic);

            // Clear the original lists
            puls.clear();
            oxigen.clear();
            tensiuneSistolic.clear();
            tensiuneDiastolic.clear();
        }

        // Pass the copies to the thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(HealthRecord.create(pulsCopy, oxigenCopy, tensiuneSistolicCopy, tensiuneDiastolicCopy));
            }
        }).start();
    }

    public void backupData() {
        DailyReportDao dailyReportDao = this.db.dailyReportDao();
        HealthRecordDao healthRecordDao = this.db.healthRecordDao();


        CompletableFuture.supplyAsync(() -> {
            // Get data from the first DAO
            List<HealthRecord> reports = healthRecordDao.getAll();
            return reports;
        }).thenAcceptAsync(reports -> {
            healthRecordDao.clear();
            dailyReportDao.insert(DailyReport.create(reports));

        });
    }

}
