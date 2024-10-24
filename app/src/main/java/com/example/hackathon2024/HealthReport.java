package com.example.hackathon2024;

import java.util.ArrayList;
import java.util.List;

public class HealthReport {
    public static List<String> analyzeHealth(
            String name, int age, double height, double weight,
            int heartRateMin, int heartRateAvg, int heartRateMax,
            int oxygenMin, int oxygenAvg, int oxygenMax,
            int systolicMin, int systolicAvg, int systolicMax,
            int diastolicMin, int diastolicAvg, int diastolicMax) {

        // Calculate BMI
        double bmi = weight / (height * height);
        List<String> healthDiagnostics = new ArrayList<>();

        // Step 1: Categorize based on BMI and Age
        if (bmi < 18.5) {
            if (age < 40) {
                if (heartRateMin < 60) healthDiagnostics.add("Risc de bradicardie: Puls prea scăzut.");
                if (oxygenMin < 95) healthDiagnostics.add("Posibilă anemie: Nivel de oxigen scăzut.");
                if (systolicMin < 90 || diastolicMin < 60) healthDiagnostics.add("Risc de hipotensiune: Tensiune arterială scăzută.");
            } else {
                if (heartRateMin < 60) healthDiagnostics.add("Risc de bradicardie: Puls prea scăzut.");
                if (oxygenMin < 95) healthDiagnostics.add("Posibilă anemie: Nivel de oxigen scăzut.");
                if (systolicMin < 90 || diastolicMin < 60) healthDiagnostics.add("Risc de hipotensiune: Tensiune arterială scăzută.");
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
            }
        } else if (bmi >= 18.5 && bmi < 25) {
            if (age < 40) {
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
                if (oxygenMin < 95) healthDiagnostics.add("Nivel scăzut de oxigen: Verificați cauzele.");
                if (systolicMax > 140 || diastolicMax > 90) healthDiagnostics.add("Risc de hipertensiune: Tensiune arterială ridicată.");
            } else {
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
                if (oxygenMin < 95) healthDiagnostics.add("Risc de probleme respiratorii: Nivel scăzut de oxigen.");
                if (systolicMax > 140 || diastolicMax > 90) healthDiagnostics.add("Risc de hipertensiune: Tensiune arterială ridicată.");
                if (heartRateMin < 60) healthDiagnostics.add("Risc de bradicardie: Puls prea scăzut.");
            }
        } else if (bmi >= 25 && bmi < 30) {
            if (age < 40) {
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
                if (oxygenMin < 95) healthDiagnostics.add("Risc de probleme respiratorii legate de supraponderalitate: Nivel scăzut de oxigen.");
                if (systolicMax > 140 || diastolicMax > 90) healthDiagnostics.add("Risc de hipertensiune: Tensiune arterială ridicată.");
            } else {
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
                if (oxygenMin < 95) healthDiagnostics.add("Risc de probleme respiratorii: Nivel scăzut de oxigen.");
                if (systolicMax > 140 || diastolicMax > 90) healthDiagnostics.add("Risc de hipertensiune: Tensiune arterială ridicată.");
                if (heartRateMin < 60) healthDiagnostics.add("Risc de bradicardie: Puls prea scăzut.");
            }
        } else if (bmi >= 30) {
            if (age < 40) {
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
                if (oxygenMin < 95) healthDiagnostics.add("Risc de apnee de somn sau probleme respiratorii: Nivel scăzut de oxigen.");
                if (systolicMax > 140 || diastolicMax > 90) healthDiagnostics.add("Risc de hipertensiune: Tensiune arterială ridicată.");
                if (systolicMin < 90 || diastolicMin < 60) healthDiagnostics.add("Risc de hipotensiune: Tensiune arterială scăzută din cauza problemelor circulatorii.");
            } else {
                if (heartRateMax > 100) healthDiagnostics.add("Risc de tahicardie: Puls prea mare.");
                if (oxygenMin < 95) healthDiagnostics.add("Risc crescut de apnee de somn: Nivel scăzut de oxigen.");
                if (systolicMax > 140 || diastolicMax > 90) healthDiagnostics.add("Risc de hipertensiune: Tensiune arterială ridicată.");
                if (systolicMin < 90 || diastolicMin < 60) healthDiagnostics.add("Risc de hipotensiune: Tensiune arterială scăzută.");
            }
        }

        // If no diagnostic, add "Sănătos"
        if (healthDiagnostics.isEmpty()) {
            healthDiagnostics.add("Sănătos");
        }

        return healthDiagnostics;
    }
}
