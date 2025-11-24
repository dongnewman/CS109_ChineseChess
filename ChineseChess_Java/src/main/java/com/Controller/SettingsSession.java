package com.Controller;

public class SettingsSession {
    private boolean startTimer;
    private boolean disableSurrender;
    private boolean disableMusic;

    public boolean settingsInit() {
        try {
            java.io.File file = new java.io.File("settings/settings.json");
            if (!file.exists()) return false;
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            String json = sb.toString();
            // 使用正则解析布尔值
            this.startTimer = json.matches("(?s).*\\\"startTimer\\\"\\s*:\\s*true.*");
            this.disableSurrender = json.matches("(?s).*\\\"disableSurrender\\\"\\s*:\\s*true.*");
            this.disableMusic = json.matches("(?s).*\\\"disableMusic\\\"\\s*:\\s*true.*");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStartTimer() {
        return startTimer;
    }

    public void setStartTimer(boolean startTimer) {
        this.startTimer = startTimer;
    }

    public boolean isDisableSurrender() {
        return disableSurrender;
    }

    public void setDisableSurrender(boolean disableSurrender) {
        this.disableSurrender = disableSurrender;
    }

    public boolean isDisableMusic() {
        return disableMusic;
    }

    public void setDisableMusic(boolean disableMusic) {
        this.disableMusic = disableMusic;
    }
}
