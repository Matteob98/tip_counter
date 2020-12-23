package com.italianswapp.tipcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.italianswapp.tipcounter.MainActivity.MainActivity;
import com.italianswapp.tipcounter.OfflineDatabase.Tip;
import com.italianswapp.tipcounter.OfflineDatabase.TipDatabase;

import java.util.Calendar;

public class AddTipActivity extends AppCompatActivity {

    /**
     * Stringa che rappresenta il testo mostrato nella textView
     */
    private String totalTip;

    /**
     * Stringa che rappresenta il titolo della mancia
     */
    private String title;

    /**
     * Segna se è già stata aggiunta una virgola, ne può essere aggiunta solo una naturalmente
     */
    private boolean commaAdded;

    /**
     * Indica i numero dopo la virgola
     * Si possono aggiungere massimo 2 numeri dopo la virgola
     */
    private int numberAfterComma;

    /*
    Oggetti dell'xml
     */
    private Toolbar mToolbar;
    private TextView mTotalTipTextView;
    private EditText mTitle;
    private ImageButton mAddTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        importLayouts();

        initializeVariables();

        toolbarSettings();

        loadAdBanner();

        /*
        Imposto che quando cambia il testo del titolo aggiorno la variabile titolo
         */
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                title=s.toString();
            }
        });

        /*
        Quando si preme sul pulsante salva viene salvata la mancia
         */
        mAddTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Se la mancia è vuota lancia un messaggio di errore e impedisce di salvare
                 */
                if(totalTip.length()==0 || Double.parseDouble(totalTip) == 0 ) {
                    Snackbar.make(v, getResources().getString(R.string.empty_tip), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                /*
                Creo la mancia
                 */
                Tip tip = new Tip();
                tip.setTitle(title);
                tip.setDate(Calendar.getInstance().getTime());
                tip.setValue(Double.parseDouble(totalTip));

                /*
                Salvo la mancia
                 */
                saveTipToDatabase(tip, getApplicationContext());

                /*
                Torna alla home page
                 */
                goToHome();
            }
        });

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
        getSupportActionBar().setTitle(getResources().getString(R.string.New_Tip));

        /*
        Visualizza il pulsante back per tornare alla main activity
        Il collegamento tra questa activity e al main activity è fatto nel manifest
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Inizializza le variabili dell'activity
     */
    private void initializeVariables() {
        totalTip = "";
        title = "";
        commaAdded=false;
        numberAfterComma = 0;
    }

    /**
     * Importa i layout dall'xml
     */
    private void importLayouts() {
        mTotalTipTextView = findViewById(R.id.totalTipActivityAddTip);
        mTitle = findViewById(R.id.titleActivityAddTip);
        mAddTip = findViewById(R.id.addButtonActivityAddTip);
        mToolbar = findViewById(R.id.toolbarActivityAddTip);
    }

    public static Intent getInstance(Context context) {
        return new Intent(context, AddTipActivity.class);
    }

    /**
     * Posso aggiungere un numero se non ho aggiunto la virgola
     * oppure se dopo la virgola ci sono meno di 2 numeri
     * @return vero se posso aggiungere un numero, falso altrimenti
     */
    private boolean ifCanAddNumber() {
        if(!commaAdded)
            return true;
        if(/*commaAdded && */numberAfterComma<2) {
            numberAfterComma++;
            return true;
        }
        return false;
    }

    public void addTipButtonClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.button0AddTip:
                if(ifCanAddNumber()) {
                    /*
                    Aggiungo lo zero solo se non è il primo carattere
                    Non avrebbe senso aggiungere zero euro di mancia
                    Nel caso di centesimi (es. 0.2), lo 0 viene aggiunto automaticamente quando si preme la virgola
                     */
                    if (totalTip.length() > 0)
                        totalTip += "0";
                }
                break;
            case R.id.button1AddTip:
                if(ifCanAddNumber())
                    totalTip += "1";
                break;
            case R.id.button2AddTip:
                if(ifCanAddNumber())
                    totalTip += "2";
                break;
            case R.id.button3AddTip:
                if(ifCanAddNumber())
                    totalTip += "3";
                break;
            case R.id.button4AddTip:
                if(ifCanAddNumber())
                    totalTip += "4";
                break;
            case R.id.button5AddTip:
                if(ifCanAddNumber())
                    totalTip += "5";
                break;
            case R.id.button6AddTip:
                if(ifCanAddNumber())
                    totalTip += "6";
                break;
            case R.id.button7AddTip:
                if(ifCanAddNumber())
                    totalTip += "7";
                break;
            case R.id.button8AddTip:
                if(ifCanAddNumber())
                    totalTip += "8";
                break;
            case R.id.button9AddTip:
                if(ifCanAddNumber())
                    totalTip += "9";
                break;
            case R.id.buttonCommaAddTip:
                if(!commaAdded) {
                    if (totalTip.length() > 0)
                        totalTip += ".";
                    else
                        totalTip += "0.";
                    commaAdded = true;
                    numberAfterComma=0;
                }
                break;
            case R.id.buttonBackAddTip:
                if(totalTip.length()>0) {
                    /*
                    Se sto scrivendo dopo la virgola e ho già aggiunto numeri diminuisco di 1 il contatore di numeri scritti dopo la virgola
                     */
                    if(commaAdded && numberAfterComma>0)
                        numberAfterComma--;
                    /*
                    Se l'ultimo carattere (quello che sto eliminando) era una virgola metto false su comaAdded perché
                    potrò in futuro aggiungere una nuova (ed unica) virgola
                     */
                    if(commaAdded && totalTip.charAt(totalTip.length()-1) == '.')
                        commaAdded=false;
                    totalTip = totalTip.substring(0, totalTip.length()-1);
                }

                break;
        }

        if(totalTip.length()==0) //Se sto aggiungendo qualcosa di vuoto lo inizializzo a "0"
            totalTip="0";

        mTotalTipTextView.setText(totalTip);
    }

    /**
     * Carica il banner pubblicitario in basso
     */
    private void loadAdBanner() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        /*
         * Banner pubblicitario
         */
        AdView mAdView = findViewById(R.id.bannerAddTipActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        goToHome();
    }

    /**
     * Torna alla home page e chiude questa schermata
     */
    private void goToHome() {
        Intent intent = MainActivity.getInstance(getApplicationContext());
        startActivity(intent);
        finish();
    }

    /**
     * Metodo statico che permette di aggiungere una mancia al database
     * @param tip mancia da aggiungere al database
     */
    public static void saveTipToDatabase(Tip tip, Context context) {
        TipDatabase db = TipDatabase.getDatabase(context.getApplicationContext());
        db.tipDao().insertAll(tip);
    }
}