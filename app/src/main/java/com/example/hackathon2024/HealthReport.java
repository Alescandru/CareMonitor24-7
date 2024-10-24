package com.example.hackathon2024;

public class HealthReport {
    public static String analyzeHealth(
            String name, int age, double height, double weight,
            int heartRateMin, int heartRateAvg, int heartRateMax,
            int oxygenMin, int oxygenAvg, int oxygenMax,
            int systolicMin, int systolicAvg, int systolicMax,
            int diastolicMin, int diastolicAvg, int diastolicMax) {

        // Calculate BMI
        double bmi = weight / (height * height);
        StringBuilder healthStatus = new StringBuilder("Sănătos\n");  // Default value

        // Flag to check if any issue was found
        boolean hasDiagnostic = false;

        // Step 1: Categorize based on BMI and Age
        if (bmi < 18.5) {
            if (age < 40) {
                // BMI < 18.5 și sub 40 de ani
                if (heartRateMin < 60) {
                    healthStatus.append("Risc de bradicardie: Puls prea scăzut.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Posibilă anemie: Nivel de oxigen scăzut.\n");
                    hasDiagnostic = true;
                }
                if (systolicMin < 90 || diastolicMin < 60) {
                    healthStatus.append("Risc de hipotensiune: Tensiune arterială scăzută.\n");
                    hasDiagnostic = true;
                }
            } else {
                // BMI < 18.5 și peste 40 de ani
                if (heartRateMin < 60) {
                    healthStatus.append("Risc de bradicardie: Puls prea scăzut.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Posibilă anemie: Nivel de oxigen scăzut.\n");
                    hasDiagnostic = true;
                }
                if (systolicMin < 90 || diastolicMin < 60) {
                    healthStatus.append("Risc de hipotensiune: Tensiune arterială scăzută.\n");
                    hasDiagnostic = true;
                }
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
            }
        } else if (bmi >= 18.5 && bmi < 25) {
            if (age < 40) {
                // 18.5 <= BMI < 25 și sub 40 de ani
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Nivel scăzut de oxigen: Verificați cauzele.\n");
                    hasDiagnostic = true;
                }
                if (systolicMax > 140 || diastolicMax > 90) {
                    healthStatus.append("Risc de hipertensiune: Tensiune arterială ridicată.\n");
                    hasDiagnostic = true;
                }
            } else {
                // 18.5 <= BMI < 25 și peste 40 de ani
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Risc de probleme respiratorii: Nivel scăzut de oxigen.\n");
                    hasDiagnostic = true;
                }
                if (systolicMax > 140 || diastolicMax > 90) {
                    healthStatus.append("Risc de hipertensiune: Tensiune arterială ridicată.\n");
                    hasDiagnostic = true;
                }
                if (heartRateMin < 60) {
                    healthStatus.append("Risc de bradicardie: Puls prea scăzut.\n");
                    hasDiagnostic = true;
                }
            }
        } else if (bmi >= 25 && bmi < 30) {
            if (age < 40) {
                // 25 <= BMI < 30 și sub 40 de ani
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Risc de probleme respiratorii legate de supraponderalitate: Nivel scăzut de oxigen.\n");
                    hasDiagnostic = true;
                }
                if (systolicMax > 140 || diastolicMax > 90) {
                    healthStatus.append("Risc de hipertensiune: Tensiune arterială ridicată.\n");
                    hasDiagnostic = true;
                }
            } else {
                // 25 <= BMI < 30 și peste 40 de ani
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Risc de probleme respiratorii: Nivel scăzut de oxigen.\n");
                    hasDiagnostic = true;
                }
                if (systolicMax > 140 || diastolicMax > 90) {
                    healthStatus.append("Risc de hipertensiune: Tensiune arterială ridicată.\n");
                    hasDiagnostic = true;
                }
                if (heartRateMin < 60) {
                    healthStatus.append("Risc de bradicardie: Puls prea scăzut.\n");
                    hasDiagnostic = true;
                }
            }
        } else if (bmi >= 30) {
            if (age < 40) {
                // BMI > 30 și sub 40 de ani
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Risc de apnee de somn sau probleme respiratorii: Nivel scăzut de oxigen.\n");
                    hasDiagnostic = true;
                }
                if (systolicMax > 140 || diastolicMax > 90) {
                    healthStatus.append("Risc de hipertensiune: Tensiune arterială ridicată.\n");
                    hasDiagnostic = true;
                }
                if (systolicMin < 90 || diastolicMin < 60) {
                    healthStatus.append("Risc de hipotensiune: Tensiune arterială scăzută din cauza problemelor circulatorii.\n");
                    hasDiagnostic = true;
                }
            } else {
                // BMI > 30 și peste 40 de ani
                if (heartRateMax > 100) {
                    healthStatus.append("Risc de tahicardie: Puls prea mare.\n");
                    hasDiagnostic = true;
                }
                if (oxygenMin < 95) {
                    healthStatus.append("Risc crescut de apnee de somn: Nivel scăzut de oxigen.\n");
                    hasDiagnostic = true;
                }
                if (systolicMax > 140 || diastolicMax > 90) {
                    healthStatus.append("Risc de hipertensiune: Tensiune arterială ridicată.\n");
                    hasDiagnostic = true;
                }
                if (systolicMin < 90 || diastolicMin < 60) {
                    healthStatus.append("Risc de hipotensiune: Tensiune arterială scăzută.\n");
                    hasDiagnostic = true;
                }
            }
        }

        // If there is any diagnostic, remove "Sănătos"
        if (hasDiagnostic) {
            int healthyIndex = healthStatus.indexOf("Sănătos");
            if (healthyIndex != -1) {
                healthStatus.delete(healthyIndex, healthyIndex + "Sănătos\n".length());
            }
        }
        return healthStatus.toString();
    }
}
