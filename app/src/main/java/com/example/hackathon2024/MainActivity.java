package com.example.hackathon2024;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE=1232;
    private TextView concluziiTextView;
    private TextView pulsTextView, oxigenTextView, tensiuneTextView;

    // Inițializare variabile pentru valori simulate
    private int puls;
    private int oxigen;
    private int tensiuneSistolic;
    private int tensiuneDiastolic;

    // Simulator de senzori
    private SensorSimulator simulator;
    Button buttonCreatePDF;
    Button buttonIsRunig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
                // Actualizează valorile din interfață
                puls = pulse;
                oxigen = oxygenLevel;
                tensiuneSistolic = systolicPressure;
                tensiuneDiastolic = diastolicPressure;

                pulsTextView.setText(pulse + " BPM");
                oxigenTextView.setText(oxygenLevel + "% SpO2");
                tensiuneTextView.setText(systolicPressure + "/" + diastolicPressure + " mmHg");
            }
        });

        // Pornește simularea la inițializarea aplicației
        simulator.startSimulation();
        askPermissions();

        buttonCreatePDF = findViewById(R.id.buttonPdf);
        buttonCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });

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
    private void askPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    public void createPdf() {

        // Dimensiunile pentru pagina A4 în pixeli (la 72 DPI)
        int pageWidth = 595; // lățimea A4
        int pageHeight = 842; // înălțimea A4

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Setăm dimensiunea textului pentru titlu și dată
        paint.setTextSize(28);
        paint.setFakeBoldText(true);

        // Desenăm titlul "RaportMonitor" în centru sus
        String title = "RaportCareMonitor";
        float titleX = (pageInfo.getPageWidth() / 2) - (paint.measureText(title) / 2); // Centrarea titlului
        canvas.drawText(title, titleX, 60, paint);

        // Formatăm data curentă ca "an.luna.zi"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        // Desenăm data în colțul din dreapta sus
        paint.setTextSize(16); // Setăm o dimensiune mai mică pentru data
        float dateX = pageInfo.getPageWidth() - paint.measureText(currentDate) - 20; // Aliniere dreapta
        canvas.drawText(currentDate, dateX, 30, paint);

        pdfDocument.finishPage(page);

        // Nume fișier cu data curentă
        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdfFileName.format(new Date());

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "hope_" + currentDateAndTime + ".pdf";
        File file = new File(downloadsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();
            Toast.makeText(this, "PDF created successfully: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
