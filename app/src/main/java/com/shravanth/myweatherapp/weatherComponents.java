package com.shravanth.myweatherapp;

public class weatherComponents {

    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;
    private String dayOrNight;
    private String humidity;
    private String pressure;
    private String dewPoint;
    private Boolean expanded;


    public weatherComponents(String time, String temperature, String icon, String windSpeed, String dayOrNight, String humidity, String pressure, String dewPoint, boolean expanded) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.dayOrNight = dayOrNight;
        this.humidity = humidity;
        this.pressure = pressure;
        this.dewPoint = dewPoint;
        this.expanded = false;
    }

    public Boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(String dewPoint) {
        this.dewPoint = dewPoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }
}
