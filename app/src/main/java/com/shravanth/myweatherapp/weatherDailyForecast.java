package com.shravanth.myweatherapp;

public class weatherDailyForecast {

    private String date;
    private String condition;
    private String maxTemp;
    private String minTemp;
    private String conditionIcon;
    private String humidity;
    private String windSpeed;
    private String sunRise;
    private String sunSet;
    private Boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getConditionIcon() {
        return conditionIcon;
    }

    public void setConditionIcon(String conditionIcon) {
        this.conditionIcon = conditionIcon;
    }

    public weatherDailyForecast(String date, String condition, String maxTemp, String minTemp, String conditionIcon, String humidity, String windSpeed, String sunRise, String sunSet, boolean expanded) {
        this.date = date;
        this.condition = condition;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.conditionIcon = conditionIcon;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.expanded = false;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }
}
