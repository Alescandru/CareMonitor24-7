package com.example.hackathon2024;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.example.hackathon2024.database.AppDatabase;
import com.example.hackathon2024.database.HealthRecord;
import com.example.hackathon2024.database.HealthRecordDao;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import java.util.List;


public class RaportActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    Button buttonCreatePDF;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_raport);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(getApplicationContext());
        executorService.execute(() -> {
            HealthRecordDao dao = db.healthRecordDao();
            List<HealthRecord> listDayData = dao.getAll();

            runOnUiThread(() -> {
                if (listDayData.isEmpty()) {
                    Toast.makeText(RaportActivity.this, "Nu sunt date pentru a genera graficul.", Toast.LENGTH_SHORT).show();
                } else {
                    setupChart(listDayData); // Configurează graficul
                }
            });
        });

        // Adaugă listener pentru a merge la ProfilActivity
        LinearLayout profilLayout = findViewById(R.id.menuLayout).findViewById(R.id.profilLayout);
        profilLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea de profil
                Intent intent = new Intent(RaportActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        // Adaugă listener pentru a merge la MainActivity
        LinearLayout mainLayout = findViewById(R.id.menuLayout).findViewById(R.id.mainLayout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea principală (MainActivity)
                Intent intent = new Intent(RaportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonCreatePDF = findViewById(R.id.buttonPdf);
        buttonCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });
    }

    private void setupChart(List<HealthRecord> list) {
        // Obține referința la graficul de tip bară din layout
        BarChart barChart = findViewById(R.id.pulsLunaChart);

        // Creează o listă de Entry-uri pentru grafic pe baza valorilor medii ale pulsului din lista HealthRecord
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i + 1, (float) list.get(i).hearBeatAvg)); // i + 1 pentru a începe de la 1
        }

        // Creează un set de date pentru grafic folosind lista primită ca parametru
        BarDataSet dataSet = new BarDataSet(entries, "Puls mediu"); // Poți schimba titlul aici
        dataSet.setColor(getResources().getColor(R.color.red));
        dataSet.setValueTextColor(getResources().getColor(R.color.blue));

        // Configurează datele graficului
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Configurează axa X pentru a afișa orele (1-24)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // Pasul să fie de o oră
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Legenda jos
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"1", "2", "3", "4", "5", "6"})); // Exemplu pentru ore

        // Configurează axa Y pentru a afișa valori ale pulsului
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Minim 0 puls
        leftAxis.setAxisMaximum(400f); // Maxim 400 puls (ajustează în funcție de valori)

        barChart.getAxisRight().setEnabled(false); // Dezactivează axa Y din dreapta

        // Opțional: configurare descriere grafic
        Description description = new Description();
        description.setText("Grafic de puls zilnic"); // Poți modifica descrierea aici
        barChart.setDescription(description);

        // Reîmprospătează graficul
        barChart.invalidate();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}