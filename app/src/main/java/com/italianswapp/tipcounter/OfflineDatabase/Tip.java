package com.italianswapp.tipcounter.OfflineDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.italianswapp.tipcounter.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tip {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * Titolo che viene dato alla mancia
     */
    @ColumnInfo
    private String title;

    /**
     * Data in cui la mancia è stata ricevuta
     */
    @ColumnInfo
    private Date date;

    /**
     * Valore della mancia
     */
     @ColumnInfo
    private double value;

     public Tip() {
         title="";
         date= Calendar.getInstance().getTime();
         value=0;
     }

     @Ignore
     public Tip(int id, String title, Date date, double value) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return Utilities.roundDouble(value, 2);
    }

    public void setValue(double value) {

        this.value = Utilities.roundDouble(value, 2);
    }

    /**
     * Ritorna la somma delle mance ricevute in input arrotondata a 2 numeri dopo la virgola
     * @param tipList ArrayList contenente le mance da sommare
     * @return double contenente il valore totale della somma
     */
    public static double sumTip(ArrayList<Tip> tipList) {
         double sum=0.0;

         for (Tip t: tipList)
             sum+=t.getValue();

         return Utilities.roundDouble(sum, 2);
    }

    /**
     * Ritorna la media delle mance ricevute in input e arrotonda a 2 numeri dopo la virgola
     * @param tipList ArrayList contenente le mance di cui si cerca la media
     * @return double contenente la media
     */
    public static double avgTip(ArrayList<Tip> tipList) {
        double avg = sumTip(tipList) / tipList.size();
        return Utilities.roundDouble(avg, 2);
    }

    /**
     * Ritorno il numero di giorni diversi in cui ho ricevuto mance
     * @param tipList Lista di mance
     * @return Numero di giorni diversi in cui ho lavoratp
     */
    public static int differentDaysCount(ArrayList<Tip> tipList) {
        /*
        Inizializzo un insieme di date
        In questo modo se voglio aggiungere più volte la stessa data automaticamente non lo farà
         */
        Set<Date> differentDays = new HashSet<>();

        Calendar calendar = Calendar.getInstance();
        /*
        Per ogni mancia passata in input
         */
        for( Tip t: tipList) {
            /*
            Prendo la data della mancia
             */
            calendar.setTime(t.getDate());
            /*
            Imposto ora, minuti, secondi e millisecondi di quella data a 0
            In questo modo lavorerò solo su giorno/mese/anno della data
             */
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            /*
            Aggiungo la data all'insieme
            Se non è presente viene aggiunta
            Se è già presente non verrà aggiunta
             */
            differentDays.add(calendar.getTime());
        }

        return differentDays.size();
    }
}
