package com.italianswapp.tipcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.italianswapp.tipcounter.OfflineDatabase.Tip;
import com.italianswapp.tipcounter.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity {

    /**
     * Data di oggi
     */
    private Date today, yesterday, two_days_ago, lastWeek, lastMonth, lastYear;

    /*
    TextView che contengono tutti i dati da mostrare nell'activity
     */
    private TextView mYesterdayEarnTV, mYesterdayTotalTipTV,
            mWeekEarnTV, mWeekTotalTipTV, mWeekDaysWorkedTV,
            mMonthEarnTV, mMonthTotalTipTV, mMonthDaysWorkedTV,
            mYearEarnTV, mYearTotalTipTV, mYearDaysWorkedTV;

    /**
     * Toolbar dell'activity
     */
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        importLayouts();

        toolbarSettings();

        loadAdBanner();

        /*
        Carico la data di oggi
         */
        Calendar calendar = Calendar.getInstance();
        today= calendar.getTime();

        loadYesterdayData();
        loadLastWeekData();
        loadLastMonthData();
        loadLastYearData();

    }

    /**
     * Carica i dati dell'ultimo anno
     */
    @SuppressLint("SetTextI18n")
    private void loadLastYearData() {
        /*
        Prendo la data dell'ultimo anno
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        lastYear = calendar.getTime();

        /*
        Carico le mance dell'ultimo anno dal database
         */
        ArrayList<Tip> lastYearTips = Utilities.getTipsFromDatabase(lastYear, today, getApplicationContext());

        if(lastYearTips.size()>0) {
            mYearEarnTV.setText(Tip.sumTip(lastYearTips) + " " + Utilities.VALUTA);
            mYearTotalTipTV.setText(Integer.toString(lastYearTips.size()));
            mYearDaysWorkedTV.setText(Integer.toString(Tip.differentDaysCount(lastYearTips)));
        }
        else {
            mYearEarnTV.setText("00,00" + " " + Utilities.VALUTA);
            mYearTotalTipTV.setText("0");
            mYearDaysWorkedTV.setText("0");
        }
    }

    /**
     * Carica i dati dell'ultimo mese
     */
    @SuppressLint("SetTextI18n")
    private void loadLastMonthData() {
        /*
        Prendo la data dell'ultimo mese
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        lastMonth = calendar.getTime();

        /*
        Carico le mance dell'ultimo mese dal database
         */
        ArrayList<Tip> lastMonthTips = Utilities.getTipsFromDatabase(lastMonth, today, getApplicationContext());

        if(lastMonthTips.size()>0) {
            mMonthEarnTV.setText(Tip.sumTip(lastMonthTips) + " " + Utilities.VALUTA);
            mMonthTotalTipTV.setText(Integer.toString(lastMonthTips.size()));
            mMonthDaysWorkedTV.setText(Integer.toString(Tip.differentDaysCount(lastMonthTips)));
        }
        else {
            mMonthEarnTV.setText("00,00" + " " + Utilities.VALUTA);
            mMonthTotalTipTV.setText("0");
            mMonthDaysWorkedTV.setText("0");
        }
    }

    /**
     * Carica i dati dell'ultima settimana
     */
    @SuppressLint("SetTextI18n")
    private void loadLastWeekData() {
        /*
        Prendo la data dell'ultima settimana
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        lastWeek = calendar.getTime();

        /*
        Carico le mance dell'ultima settimana dal database
         */
        ArrayList<Tip> lastWeekTips = Utilities.getTipsFromDatabase(lastWeek, today, getApplicationContext());

        if(lastWeekTips.size()>0) {
            mWeekEarnTV.setText(Tip.sumTip(lastWeekTips) + " " + Utilities.VALUTA);
            mWeekTotalTipTV.setText(Integer.toString(lastWeekTips.size()));
            mWeekDaysWorkedTV.setText(Integer.toString(Tip.differentDaysCount(lastWeekTips)));
        }
        else {
            mWeekEarnTV.setText("00,00" + " " + Utilities.VALUTA);
            mWeekTotalTipTV.setText("0");
            mWeekDaysWorkedTV.setText("0");
        }

    }

    /**
     * Carico di dati di ieri
     */
    @SuppressLint("SetTextI18n")
    private void loadYesterdayData() {
        /*
        Prendo la data di ieri
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = calendar.getTime();

        /*
        Prendo la data dell'altroieri
         */
        calendar.add(Calendar.DAY_OF_YEAR, -1); //secondo -1 -> cioè -2
        two_days_ago = calendar.getTime();

        /*
        Carico le mance di ieri dal database
         */
        /*
    Liste che conengono rispettivamente le mance dell'ultima settimana,
     ultimo mese e ultimo anno
     */
        ArrayList<Tip> yesterdayTips = Utilities.getTipsFromDatabase(two_days_ago, yesterday, getApplicationContext());

        if(yesterdayTips.size()>0) {
            mYesterdayEarnTV.setText(Tip.sumTip(yesterdayTips) + " " + Utilities.VALUTA);
            mYesterdayTotalTipTV.setText(Integer.toString(yesterdayTips.size()));
        }
        else {
            mYesterdayEarnTV.setText("00,00" + " " + Utilities.VALUTA);
            mYesterdayTotalTipTV.setText("0");
        }

    }

    /**
     * Importa i layout dall'xml
     */
    private void importLayouts() {

        /*
        Ieri
         */
        mYesterdayEarnTV = findViewById(R.id.yesterdayEarnTextViewStatisticsActivity);
        mYesterdayTotalTipTV = findViewById(R.id.totalTipYesterdayTextViewStatisticsActivity);

        /*
        Ultima settimana
         */
        mWeekEarnTV = findViewById(R.id.lastWeekEarnTextViewStatisticsActivity);
        mWeekTotalTipTV = findViewById(R.id.totalTipLastWeekTextViewStatisticsActivity);
        mWeekDaysWorkedTV = findViewById(R.id.daysWorkedLastWeekTextViewStatisticsActivity);

        /*
        Ultimo mese
         */
        mMonthEarnTV = findViewById(R.id.lastMonthEarnTextViewStatisticsActivity);
        mMonthTotalTipTV = findViewById(R.id.totalTipLastMonthTextViewStatisticsActivity);
        mMonthDaysWorkedTV = findViewById(R.id.daysWorkedLastMonthTextViewStatisticsActivity);

        /*
        Ultimo anno
         */
        mYearEarnTV = findViewById(R.id.lastYearEarnTextViewStatisticsActivity);
        mYearTotalTipTV = findViewById(R.id.totalTipLastYearTextViewStatisticsActivity);
        mYearDaysWorkedTV = findViewById(R.id.daysWorkedLastYearTextViewStatisticsActivity);

        /*
        Toolbar
         */
        mToolbar = findViewById(R.id.toolbarStatisticsActivity);
    }

    public void detailsButtonClick(View view) {
        /*
        Passa alla schermata la lista dei dati corrispondenti al button premuto
         */
        int id = view.getId();
        DetailsActivity.Type type ;
        Date startDate;
        Date endDate = today;
        switch (id) {
            case R.id.yesterdayDetailsButtonStatisticsActivity:
                type = DetailsActivity.Type.YESTERDAY;
                startDate = two_days_ago;
                endDate = yesterday;
                break;
            case R.id.lastWeekDetailsButtonStatisticsActivity:
                type = DetailsActivity.Type.WEEK;
                startDate = lastWeek;
                break;
            case R.id.lastMonthDetailsButtonStatisticsActivity:
                type = DetailsActivity.Type.MONTH;
                startDate = lastMonth;
                break;
            case R.id.lastYearDetailsButtonStatisticsActivity:
                type = DetailsActivity.Type.YEAR;
                startDate = lastYear;
                break;
            default:
                throw new IllegalArgumentException("Id non valido in detailsButtonClick()");
        }

        Intent intent = DetailsActivity.getInstance(getApplicationContext(), startDate, endDate, type);
        startActivity(intent);
    }

    /**
     * Imposta la toolbar
     */
    private void toolbarSettings() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark)); //Colora barra notifiche
        setSupportActionBar(mToolbar);
        /*
        Imposta il titolo
         */
        getSupportActionBar().setTitle(getResources().getString(R.string.Statistics));


        /*
        Visualizza il pulsante back per tornare alla main activity
        Il collegamento tra questa activity e al main activity è fatto nel manifest
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
        AdView mAdView = findViewById(R.id.adViewStatisticsActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public static Intent getInstance(Context context) {
        return new Intent(context, StatisticsActivity.class);
    }


}