package com.italianswapp.tipcounter.MainActivity;

class TipCard {

    private String title, total, date, time;

    public TipCard(String title, String total, String date, String time) {
        this.title = title;
        this.total = total;
        this.date = date;
        this.time = time;
    }

    public TipCard() {
        title="";
        total="";
        date="";
        time="";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
