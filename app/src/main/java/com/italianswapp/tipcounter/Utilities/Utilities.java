package com.italianswapp.tipcounter.Utilities;

import android.content.Context;
import com.italianswapp.tipcounter.OfflineDatabase.Tip;
import com.italianswapp.tipcounter.OfflineDatabase.TipDatabase;
import com.italianswapp.tipcounter.OfflineDatabase.TipDatabaseConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utilities {
    /**
     * Valuta corrente
     */
    public static final String VALUTA = "€";

    /**
     * Rimuove dal database tutte le mance passate in input
     * @param context Il contesto chiamante
     * @param tips Le mance (anche solo 1) da eliminare
     */
    public static void removeTipFromDatabase(Context context, Tip... tips) {
        TipDatabase db = TipDatabase.getDatabase(context);

        for (Tip t: tips)
            db.tipDao().delete(t);

    }

    /**
     * Ritorna una lista di mance presa dal database con data compresa tra i due valori passati
     * come parametri
     * @param startDate data prima della quale non accetto mance
     * @param endDate data dopo della quale non accetto mance
     * @return Una lista di mance
     */
    public static ArrayList<Tip> getTipsFromDatabase(Date startDate, Date endDate, Context context) {
        ArrayList<Tip> tipList;
         /*
        Apro una connessione con il database e carico la lista delle mance di oggi
         */
        TipDatabase db = TipDatabase.getDatabase(context);
        tipList = (ArrayList<Tip>) db.tipDao()
                .getBetweenDate(
                        TipDatabaseConverter.dateToTimestamp(startDate),
                        TipDatabaseConverter.dateToTimestamp(endDate));

        return tipList;
    }

    /**
     * Ritorna una lista di mance presa dal database con data compresa tra l'inizio e la fine del giorno passato
     * @param date Data di cui si cercano le mance
     * @param context Contesto
     * @return Una lista di mance
     */
    public static ArrayList<Tip> getTipsFromDatabase(Date date, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        /*
        00:00:00
         */
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();

        /*
        23:59:59:59
         */
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);

        Date end = calendar.getTime();

        return getTipsFromDatabase(start, end, context);
    }

    /**
     * Arrotonda il double passato in input a tanti numeri dopo la virgola quanto è places
     * @param value Il valore da arrotondare
     * @param places I numeri dopo la virgola
     * @return Il numero arrotondato
     */
    public static double roundDouble(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
