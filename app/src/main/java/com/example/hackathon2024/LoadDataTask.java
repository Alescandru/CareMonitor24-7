package com.example.hackathon2024;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.hackathon2024.RaportActivity;
import com.example.hackathon2024.database.HealthRecord;
import com.example.hackathon2024.database.HealthRecordDao;

import java.util.List;

private class LoadDataTask extends AsyncTask<Void, Void, List<HealthRecord>> {
    @Override
    protected List<HealthRecord> doInBackground(Void... voids) {
        HealthRecordDao dao = db.healthRecordDao();
        return dao.getAll(); // Obține toate înregistrările din baza de date
    }

    @Override
    protected void onPostExecute(List<HealthRecord> listDayData) {
        if (listDayData.isEmpty()) {
            Toast.makeText(RaportActivity.this, "Nu sunt date pentru a genera graficul.", Toast.LENGTH_SHORT).show();
            return; // Iese din metoda onPostExecute() dacă nu sunt date
        }
        setupChart(listDayData); // Configurează graficul
    }
}
