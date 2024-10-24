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
import java.util.Date;

import com.example.hackathon2024.database.AppDatabase;
import com.example.hackathon2024.database.DailyReport;
import com.example.hackathon2024.database.DailyReportDao;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
        setupChart();
    }
    private void setupChart() {
        // Obține referința la graficul din layout
        LineChart lineChart = findViewById(R.id.lineChart);

        // Crează o listă de puncte pentru grafic
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 100f));
        entries.add(new Entry(2f, 200f));
        entries.add(new Entry(3f, 50f));
        entries.add(new Entry(4f, 300f));

        // Creează un set de date pentru grafic
        LineDataSet dataSet = new LineDataSet(entries, "Exemplu de date");
        dataSet.setColor(getResources().getColor(R.color.red));
        dataSet.setValueTextColor(getResources().getColor(R.color.blue));

        // Configurează datele graficului
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Opțional: configurare grafic (titlu, descriere, etc.)
        Description description = new Description();
        description.setText("Grafic de test");
        lineChart.setDescription(description);

        // Reîmprospătează graficul
        lineChart.invalidate();
    }

    public void createPdf() {
        executorService.execute(() -> {
            DailyReportDao dao = db.dailyReportDao();
            List<DailyReport> reports = dao.getAll(); // Get reports on a background thread

            // Check if reports are empty
            if (reports.isEmpty()) {
                runOnUiThread(() -> {
                    Toast.makeText(RaportActivity.this, "Nu sunt date pentru a genera PDF-ul.", Toast.LENGTH_SHORT).show();
                });
                return; // Exit if no data
            }

            // Proceed with PDF creation if reports are available
            runOnUiThread(() -> {
                // Existing PDF creation code using reports
                createPdfFromReports(reports);
            });
        });
    }

    int heartRateMinAvg,
            heartRateAvgAvg,
            heartRateMaxAvg,
            oxygenMinAvg,
            oxygenAvgAvg,
            oxygenMaxAvg,
            systolicMinAvg,
            systolicAvgAvg,
            systolicMaxAvg,
            diastolicMinAvg,
            diastolicAvgAvg,
            diastolicMaxAvg;

    private void getAverage(List<DailyReport> reports){
        for (int i = 0; i < reports.size(); i++){
            heartRateMinAvg += reports.get(i).heartBeatMin;
            heartRateMaxAvg += reports.get(i).hearBeatMax;
            heartRateAvgAvg += reports.get(i).hearBeatAvg;
            oxygenMinAvg += reports.get(i).oxygenLevelMin;
            oxygenMaxAvg += reports.get(i).oxygenLevelMax;
            oxygenAvgAvg += reports.get(i).oxygenLevelAvg;
            systolicMinAvg += reports.get(i).systolicPressureMin;
            systolicMaxAvg += reports.get(i).systolicPressureMax;
            systolicAvgAvg += reports.get(i).systolicPressureAvg;
            diastolicMinAvg += reports.get(i).diastolicPressureMin;
            diastolicMaxAvg += reports.get(i).diastolicPressureMax;
            diastolicAvgAvg += reports.get(i).diastolicPressureAvg;
        }
        heartRateMinAvg = heartRateMinAvg/reports.size();
        heartRateMaxAvg = heartRateMaxAvg/reports.size();
        heartRateAvgAvg = heartRateAvgAvg/reports.size();
        oxygenMinAvg = oxygenMinAvg/reports.size();
        oxygenMaxAvg = oxygenMaxAvg/reports.size();
        oxygenAvgAvg = oxygenAvgAvg/reports.size();
        systolicMinAvg = systolicMinAvg/reports.size();
        systolicMaxAvg = systolicMaxAvg/reports.size();
        systolicAvgAvg = systolicAvgAvg/reports.size();
        diastolicMinAvg = diastolicMinAvg/reports.size();
        diastolicMaxAvg = diastolicMaxAvg/reports.size();
        diastolicAvgAvg = diastolicAvgAvg/reports.size();
    }
    private void createPdfFromReports(List<DailyReport> reports) {
        // Dimensiunile pentru pagina A4 în pixeli (la 72 DPI)
        int pageWidth = 595; // lățimea A4
        int pageHeight = 842; // înălțimea A4

        PdfDocument pdfDocument = new PdfDocument();
        getAverage(reports);

        List<String> healthReportResult = HealthReport.analyzeHealth(
                ProfilActivity.getName(), ProfilActivity.getVarsta(), ProfilActivity.getInaltime(), ProfilActivity.getGreutate(),
                heartRateMinAvg, heartRateAvgAvg, heartRateMaxAvg, oxygenMinAvg, oxygenAvgAvg, oxygenMaxAvg, systolicMinAvg, systolicAvgAvg,
                systolicMaxAvg, diastolicMinAvg, diastolicAvgAvg, diastolicMaxAvg
        );

        int cellHeight = 20;
        int cellWidth = 130;
        int bloodPressureSpacingAdjustment = 65;

        Paint paint = new Paint();
        paint.setTextSize(28);
        paint.setFakeBoldText(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = sdf.format(new Date());

        // Prima pagină cu healthReportResult și informații de bază
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page firstPage = pdfDocument.startPage(pageInfo);
        Canvas canvas = firstPage.getCanvas();

        // Detalii utilizator în colțul din stânga sus
        paint.setTextSize(16);
        float startLeft = 20;
        float startTop = 40;
        canvas.drawText("Nume: " + ProfilActivity.getName(), startLeft, startTop, paint);
        canvas.drawText("Vârsta: " + ProfilActivity.getVarsta(), startLeft, startTop + 20, paint);
        canvas.drawText("Înălțime: " + ProfilActivity.getInaltime(), startLeft, startTop + 40, paint);
        canvas.drawText("Greutate: " + ProfilActivity.getGreutate(), startLeft, startTop + 60, paint);

        // Titlul "RaportCareMonitor" centrat în partea de sus
        String title = "RaportCareMonitor";
        paint.setTextSize(28);
        float titleX = (pageInfo.getPageWidth() / 2) - (paint.measureText(title) / 2);
        canvas.drawText(title, titleX, 85, paint);

        // Data în colțul din dreapta sus
        paint.setTextSize(16);
        float dateX = pageInfo.getPageWidth() - paint.measureText(currentDate) - 20;
        canvas.drawText(currentDate, dateX, startTop, paint);

        // Adăugare diagnostice din healthReportResult sub titlu, doar pe prima pagină
        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        float diagnosticStartY = 130;  // Începerea diagnosticelor sub titlu
        for (int i = 0; i < healthReportResult.size(); i++) {
            canvas.drawText(healthReportResult.get(i), startLeft, diagnosticStartY, paint);
            diagnosticStartY += 20; // Ajustare spațiere verticală pentru fiecare linie
        }

        pdfDocument.finishPage(firstPage);  // Terminarea primei pagini

        // Continuare cu generarea tabelelor pe paginile următoare
        for (int tableIndex = 0; tableIndex < 3; tableIndex++) {
            PdfDocument.PageInfo tablePageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, tableIndex + 2).create();
            PdfDocument.Page page = pdfDocument.startPage(tablePageInfo);
            canvas = page.getCanvas();

            // Afișarea tabelelor ca în codul original
            paint.setTextSize(14);
            int startX = 50;
            int startY = 120;

            switch (tableIndex) {
                case 0:
                    canvas.drawText("Heart Rate (BPM)", startX, startY, paint);
                    startY += cellHeight * 1.5; // Spațiu suplimentar după titlu
                    canvas.drawText("Min", startX + 10, startY - 10, paint);
                    canvas.drawText("Max", startX + cellWidth + 10, startY - 10, paint);
                    canvas.drawText("Avg", startX + 2 * cellWidth + 10, startY - 10, paint);

                    for (int i = 0; i < reports.size(); i++) {
                        startY += cellHeight;
                        canvas.drawText(String.valueOf(reports.get(i).heartBeatMin), startX + 10, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).hearBeatMax), startX + cellWidth + 10, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).hearBeatAvg), startX + 2 * cellWidth + 10, startY - 10, paint);
                    }
                    break;

                case 1:
                    canvas.drawText("Oxygen Level (%)", startX, startY, paint);
                    startY += cellHeight * 1.5; // Spațiu suplimentar după titlu
                    canvas.drawText("Min", startX + 10, startY - 10, paint);
                    canvas.drawText("Max", startX + cellWidth + 10, startY - 10, paint);
                    canvas.drawText("Avg", startX + 2 * cellWidth + 10, startY - 10, paint);

                    for (int i = 0; i < reports.size(); i++) {
                        startY += cellHeight;
                        canvas.drawText(String.valueOf(reports.get(i).oxygenLevelMin), startX + 10, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).oxygenLevelMax), startX + cellWidth + 10, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).oxygenLevelAvg), startX + 2 * cellWidth + 10, startY - 10, paint);
                    }
                    break;

                case 2:
                    canvas.drawText("Blood Pressure (mmHg)", startX, startY, paint);
                    startY += cellHeight * 1.5; // Spațiu suplimentar după titlu
                    canvas.drawText("Sys Min", startX + 10, startY - 10, paint);
                    canvas.drawText("Sys Max", startX + bloodPressureSpacingAdjustment, startY - 10, paint);
                    canvas.drawText("Sys Avg", startX + 2 * bloodPressureSpacingAdjustment, startY - 10, paint);
                    canvas.drawText("Dia Min", startX + 3 * bloodPressureSpacingAdjustment, startY - 10, paint);
                    canvas.drawText("Dia Max", startX + 4 * bloodPressureSpacingAdjustment, startY - 10, paint);
                    canvas.drawText("Dia Avg", startX + 5 * bloodPressureSpacingAdjustment, startY - 10, paint);

                    for (int i = 0; i < reports.size(); i++) {
                        startY += cellHeight;
                        canvas.drawText(String.valueOf(reports.get(i).systolicPressureMin), startX + 10, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).systolicPressureMax), startX + bloodPressureSpacingAdjustment, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).systolicPressureAvg), startX + 2 * bloodPressureSpacingAdjustment, startY - 10, paint);

                        canvas.drawText(String.valueOf(reports.get(i).diastolicPressureMin), startX + 3 * bloodPressureSpacingAdjustment, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).diastolicPressureMax), startX + 4 * bloodPressureSpacingAdjustment, startY - 10, paint);
                        canvas.drawText(String.valueOf(reports.get(i).diastolicPressureAvg), startX + 5 * bloodPressureSpacingAdjustment, startY - 10, paint);
                    }
                    break;
            }

            pdfDocument.finishPage(page);
        }

        // Salvare PDF în Download
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
            Toast.makeText(this, "PDF salvat în Download", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Eroare la salvarea PDF-ului", Toast.LENGTH_SHORT).show();
        }
    }


}