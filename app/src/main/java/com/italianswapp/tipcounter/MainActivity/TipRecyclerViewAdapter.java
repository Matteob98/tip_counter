package com.italianswapp.tipcounter.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.italianswapp.tipcounter.OfflineDatabase.Tip;
import com.italianswapp.tipcounter.R;
import com.italianswapp.tipcounter.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class TipRecyclerViewAdapter extends RecyclerView.Adapter<TipRecyclerViewAdapter.ViewHolder> {

    /**
     * Categoria al quale appartiene una mancia rispetto alla media delle altre mance
     */
    private enum TIP_CATEGORY {
        LOW,
        HALF,
        FULL
    }
    /**
     * Lista da mostrare nella recyclerView
     */
    private ArrayList<Tip> tipList;

    /**
     * Contesto in cui mostrare la recyclerView
     */
    private Context context;

    /**
     * Media dei valori delle mance che fanno parte della lista mostrata dalla recyclerView
     */
    private double tipAvg;

    public TipRecyclerViewAdapter(ArrayList<Tip> tipList, Context context) {
        this.tipList = tipList;
        this.context = context;
        this.tipAvg = Tip.avgTip(tipList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext())
                .inflate(R.layout.tip_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*
        Prendo la mancia nella posizione passata
         */
        Tip tip = tipList.get(position);

        /*
        Creo un'istanza di calendario con la data della mancia
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tip.getDate());

        /*
        Creo la stringa per la data
         */
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        String currentDate = new StringBuilder()
                .append(day >= 10 ?
                        day :
                        "0"+day)
                .append("/")
                .append(month >= 10 ?
                        month :
                        "0"+month)
                .append("/")
                .append(calendar.get(Calendar.YEAR))
                .toString();

        /*
        Creo la stringa dell'ora
        Se è PM aggiungi 12
         */
        int hour = calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == Calendar.PM)
            hour+=12;
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        String currentTime = new StringBuilder()
                .append( hour >= 10 ?
                        hour:
                        "0"+hour)
                .append(":")
                .append( minute >= 10 ?
                        minute :
                        "0" + minute)
                .append(":")
                .append( seconds >= 10 ?
                        seconds :
                        "0" + seconds)
                .toString();

        /*
        Imposto i contenuti delle textView
         */
        holder.mDate.setText(currentDate);
        holder.mTime.setText(currentTime);
        holder.mTitle.setText(tip.getTitle());
        holder.mTotal.setText(new StringBuilder()
                .append(tip.getValue())
                .append(" ")
                .append(Utilities.VALUTA)
                .toString());

        /*
        Imposto l'immagine che rappresenta il range a cui appartiene la mancia
         */
        holder.mImage.setBackground(getTipImage(tip));

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getTipImage(Tip tip) {
        switch (getTipCategory(tip, tipAvg)) {
            case LOW:
                return context.getResources().getDrawable(R.drawable.coins_low);
            case HALF:
                return context.getResources().getDrawable(R.drawable.coins_half);
            case FULL:
                return context.getResources().getDrawable(R.drawable.coins_full);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mTitle, mTotal, mDate, mTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.titleTipCard);
            mTotal = itemView.findViewById(R.id.totalTipCard);
            mDate = itemView.findViewById(R.id.dateTipCard);
            mTime = itemView.findViewById(R.id.timeTipCard);
            mImage = itemView.findViewById(R.id.tipCardImage);
        }
    }

    /**
     * Ritorna il range a cui appartiene la mancia passata in input in base alla media delle altre mance
     * La media è a metà del valore (da 1 a 10 è 5)
     * Se  il valore è da 0 a 3 ritorna LOW
     * Se il valore è da 3 a 6 ritorna HALF
     * Se il valore è da 7 a 10 ritorna FULL
     * @param tip Mancia da confrontare
     * @param tipAvg Media delle mance
     * @return Categoria a cui appartiene la mancia
     */
    private TIP_CATEGORY getTipCategory(Tip tip, double tipAvg) {
        if (tipAvg==0)
            return TIP_CATEGORY.LOW;

        double value = tipAvg/3;

        /*
        da 0 a 1/3
         */
        if(tip.getValue()<=tipAvg-value)
            return TIP_CATEGORY.LOW;
        /*
        da 1/3 a 2/3
         */
        if(tip.getValue()<=tipAvg+value)
            return TIP_CATEGORY.HALF;
        /*
        sopra i 2/3
         */
        return TIP_CATEGORY.FULL;
    }
}
