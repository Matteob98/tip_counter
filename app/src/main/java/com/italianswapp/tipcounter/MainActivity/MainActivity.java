package com.italianswapp.tipcounter.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.italianswapp.tipcounter.AddTipActivity;
import com.italianswapp.tipcounter.OfflineDatabase.Tip;
import com.italianswapp.tipcounter.R;
import com.italianswapp.tipcounter.StatisticsActivity;
import com.italianswapp.tipcounter.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //todo fai logo

    /**
     * Lista che contiene tutte le mance del giorno corrente
     */
    private ArrayList<Tip> dailyTipList;

    /**
     * Mancia eliminata dalla lista
     * Viene salvata nel caso in cui l'utente annullasse la cancellazione
     */
    private Tip erasedTip;

    /**
     * Bottone che alla pressione porta all'activity epr inserire una nuova mancia
     */
    private ImageButton mNewTip;
    /**
     * RecyclerView dove vengono visualizzate le mance inserite
     */
    private RecyclerView mTipRecyclerView;

    /**
     * Immagine che viene mostrata quando la lista della recyclerView è vuota
     */
    private ImageView mEmptyListImage;

    /**
     * TextView che mostra il guadagno totale derivato dalle mance di oggi
     */
    private TextView mDailyEarn;

    /**
     * TextView che mostra il numero totale di mance ricevute oggi
     */
    private TextView mDailyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark)); //Colora barra notifiche

        importLayouts();
        
        loadAdBanner();

        recyclerViewSettings();

        updateDailyTipSummary();

        mNewTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddTipActivity.getInstance(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    /**
     * Aggiorna sia il totale (in soldi) delle mance ricevute oggi sia il numero totale di esse
     * Aggiornando le relative textView
     * Prendendo i dati dalla lista dailyTipList
     */
    @SuppressLint("SetTextI18n")
    private void updateDailyTipSummary() {

        if(dailyTipList.size()>0) {
            mDailyEarn.setText(Tip.sumTip(dailyTipList) + " " + Utilities.VALUTA);
            mDailyCount.setText(Integer.toString(dailyTipList.size()));
        }
        else {
            mDailyEarn.setText("00,00" + " " + Utilities.VALUTA);
            mDailyCount.setText("0");
        }
    }

    /**
     * Carica il banner pubblicitario in basso
     */
    private void loadAdBanner() {

        /*
        Inizializzazione del banner in basso
         */

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        /*
         * Banner pubblicitario
         */
        AdView mAdView = findViewById(R.id.adViewActivityMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    /**
     * Carica tutte le mance salvate nel database
     */
    private void loadTipList() {
        Date today = Calendar.getInstance().getTime();
        dailyTipList = Utilities.getTipsFromDatabase(today, getApplicationContext());
    }



    /**
     * Importa i layout dall'xml
     */
    private void importLayouts() {
        mNewTip = findViewById(R.id.floatingActionButtonActivityMain);
        mTipRecyclerView = findViewById(R.id.tipRecyclerViewActivityMain);
        mDailyEarn = findViewById(R.id.dailyEarnTextViewActivityMain);
        mDailyCount = findViewById(R.id.dailyCountTextViewActivityMain);
        mEmptyListImage = findViewById(R.id.emptyListImageActivityMain);
    }

    /**
     * Imposta il layout della recyclerView
     */
    private void recyclerViewSettings() {
        dailyTipList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTipRecyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewSwipeSettings();
        updateRecyclerView();
    }

    /**
     * Imposta che allo swipe su una card la relativa mancia viene eliminata
     */
    private void recyclerViewSwipeSettings() {
         /*
        Cancella card con swipe
         */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                erasedTip = dailyTipList.get(position);

                deleteErasedTip();
                updateRecyclerView();
                updateDailyTipSummary();

            }
        });
        helper.attachToRecyclerView(mTipRecyclerView);
    }

    /**
     * Metodo che quando chiamato elimina la mancia salvata in erasedTip dal database
     */
    private void deleteErasedTip() {
        /*
        Controllo per prima cosa se la mancia da eliminare non è nulla
         */
        if (erasedTip==null)
            return;

        Utilities.removeTipFromDatabase(this, erasedTip);
        erasedTip = null; // Resetto l'ultima tip eliminata
    }


    /**
     * Aggiorna la recyclerView caricando i dati dal database
     */
    private void updateRecyclerView() {
        loadTipList();
        if(dailyTipList.size()>0) {
            mTipRecyclerView.setAdapter(new TipRecyclerViewAdapter(dailyTipList, this));
            mTipRecyclerView.setVisibility(View.VISIBLE);
            mEmptyListImage.setVisibility(View.INVISIBLE);
        }
        else {
            mTipRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyListImage.setVisibility(View.VISIBLE);
        }

    }

    public static Intent getInstance(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /**
     * Metodo che gestisce l'aggiunta rapida di manxce
     * @param view view
     */
    public void quickTipButtonClick(View view) {

        int id = view.getId();
        double value = 0.0;
        switch (id) {
            case R.id.firstQuickTip:
                value = 0.5;
                break;
            case R.id.secondQuickTip:
                value = 1.0;
                break;
           case R.id.thirdQuickTip:
               value = 1.5;
               break;
          case R.id.fourthQuickTip:
              value = 2.0;
              break;
        }

        /*
        Creo la nuova mancia e ne imposto il valore
         */
        Tip tip = new Tip();
        tip.setValue(value);

        /*
        Aggiungo la mancia al database
         */
        AddTipActivity.saveTipToDatabase(tip, getApplicationContext());

        /*
        Aggiorno recyclerView e resoconto giornaliero
         */
        updateRecyclerView();
        updateDailyTipSummary();

    }

    /**
     * Pulsante che gestisce il tocco sul pulsante statistiche
     * Apre l'activity statistiche
     * @param view la vista
     */
    public void statCardClick(View view) {
        Intent intent = StatisticsActivity.getInstance(getApplicationContext());
        startActivity(intent);
    }
}