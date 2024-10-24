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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class RaportActivity extends AppCompatActivity {

    Button buttonCreatePDF;

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