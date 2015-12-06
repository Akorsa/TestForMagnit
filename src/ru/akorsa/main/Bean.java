package ru.akorsa.main;

public class Bean{
    private int count;
    private String dbURL;
    private String dbUser;
    private String dpPassword;

    // Конструктор по умолчанию (без аргументов).
    public Bean() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDpPassword() {
        return dpPassword;
    }

    public void setDpPassword(String dpPassword) {
        this.dpPassword = dpPassword;
    }
}

