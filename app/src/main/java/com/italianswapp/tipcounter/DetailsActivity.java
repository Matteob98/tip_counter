package com.italianswapp.tipcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.italianswapp.tipcounter.MainActivity.TipRecyclerViewAdapter;
import com.italianswapp.tipcounter.OfflineDatabase.Tip;
import com.italianswapp.tipcounter.Utilities.App;
import com.italianswapp.tipcounter.Utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    private static final String START_KEY = "start_key", TYPE_KEY ="type_key", END_KEY = "end_key";

    /**
     * Tipo di dato che viene mostrato nell'activity
     */
    public enum Type {
        YESTERDAY,
        WEEK,
        MONTH,
        YEAR;

        @Override
        public String toString() {
            Resources res = App.getContext().getResources();
            switch (this) {
                case YESTERDAY:
                    return res.getString(R.string.Yesterday);
                case WEEK:
                    return res.getString(R.string.Week);
                case MONTH:
                    return res.getString(R.string.Month);
                case YEAR:
                    return res.getString(R.string.Year);
                default:
                    return "errorTypeToString";

            }
        }
    }

    /**
     * Data di inizio e di fine di cui mostrare le mance
     */
    private Date start, end;

    /**
     * Titolo dell'activity, ad esempio:
     *  - Ultima settimana
     *  - Ultimo mese
     *  - Ultimo anno
     */
    private TextView mTitle;

    /**
     * RecyclerView dove vengono mostrati i dettagli
     */
    private RecyclerView mRecyclerView;

    /**
     * Toolbar dell'activity
     */
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        start = (Date) getIntent().getSerializableExtra(START_KEY);
        end = (Date) getIntent().getSerializableExtra(END_KEY);
        /*
         * Tipo di dato che vado a mostrare
         */
        Type type = Type.valueOf(getIntent().getStringExtra(TYPE_KEY));

        importsLayout();
        toolbarSettings();
        loadAdBanner();

        mTitle.setText(getTitleFromType(type));

        setRecyclerView();
    }

    private void setRecyclerView() {

        ArrayList<Tip> tipList = Utilities.getTipsFromDatabase(start, end, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new TipRecyclerViewAdapter(tipList, this));
    }

    /**
     * Ritorna il titolo dell'activity in base al tipo passato
     * @param type tipo dell'activity
     * @return titolo
     */
    private String getTitleFromType(Type type) {
        int res;
        switch (type) {
            case YESTERDAY:
                res = R.string.Yesterday;
                break;
            case WEEK:
                res = R.string.This_week;
                break;
            case MONTH:
                res = R.string.This_month;
                break;
            case YEAR:
                res = R.string.This_year;
                break;
            default:
                return null;
        }
        return getResources().getString(res);
    }

    /**
     * Importa gli oggetti dall'xml
     */
    private void importsLayout() {
        mTitle = findViewById(R.id.titleDetailsActivity);
        mRecyclerView = findViewById(R.id.recyclerViewDetailsActivity);
        mToolbar = findViewById(R.id.toolbarDetailsActivity);
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
        getSupportActionBar().setTitle(getResources().getString(R.string.Details));

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
        AdView mAdView = findViewById(R.id.adViewDetailsActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    /**
     * Richiama l'activity passando la data di inizio e di fine di cui mostrare le mance una volta aperta l'activity.
     * Passa inoltre il titolo dell'activity (che verrà mostrato in alto)
     * @param context contesto
     * @param start inizio
     * @param end fine
     * @return Istanza dei dettagli
     */
    public static Intent getInstance(Context context, Date start, Date end, Type title) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(START_KEY, start);
        intent.putExtra(END_KEY, end);
        intent.putExtra(TYPE_KEY, title.name());
        return intent;
    }
}