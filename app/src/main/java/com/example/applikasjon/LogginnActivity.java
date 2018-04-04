package com.example.applikasjon;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Klasse som håndterer innlogging av bruker
 */
public class LogginnActivity extends AppCompatActivity {

    // UI referanser
    private AutoCompleteTextView ePost;
    private EditText passord;
    private StartOkt okt = null;

    /**
     * Konstruktor som setter opp variabler og håndterer serverrespons med responslisteners
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logginn);
        ePost = (AutoCompleteTextView) findViewById(R.id.epost);
        passord = (EditText) findViewById(R.id.passord);
        final Button loggInnKnapp = (Button) findViewById(R.id.loggInnKnapp);

        loggInnKnapp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                loggInnKnapp.setEnabled(false);  //Skrur av knappen mens forespørselen prosesserer
                final String brukernavn = ePost.getText().toString();
                final String pass = passord.getText().toString();

                Response.Listener<String> respons = new Response.Listener<String>() {
                    /**
                     * Håndterer logginnresponsen fra serveren.
                     * Hvis suksess settes uuid og en økt vil starte opp
                     * Hvis ikke vises en feilmelding som til slutt vil sende brukeren tilbake til start
                     * @param response String Responsen fra serveren i JSON format
                     */
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRespons = new JSONObject(response);
                            boolean suksess = jsonRespons.getBoolean("suksess");  //Henter ut verdien som sier om forespørselen var vellykket eller ikke
                            if (suksess) {
                                String uuid = jsonRespons.getString("uuid");
                                //Lagrer uuid
                                MainActivity.setUuid(uuid);
                                startOkt(LogginnActivity.this);
                            } else {
                                MainActivity.visFeilMelding("Kunne ikke oppdatere data", LogginnActivity.this);
                            }
                        } catch (JSONException e) {
                            MainActivity.visFeilMelding("En feil har oppstått", LogginnActivity.this);
                        }
                    }
                };
                //Setter opp en forespørsel som skal sendes til serveren via Volley
                LogginnForesporsel foresporsel = new LogginnForesporsel(brukernavn, pass, respons, LogginnActivity.this);
                RequestQueue queue = Volley.newRequestQueue(LogginnActivity.this);
                queue.add(foresporsel);
            }
        });

        //Knapp som sender deg til registreringssiden ved klikk
        Button regKnapp = (Button) findViewById(R.id.registrerKnapp);
        regKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                Intent regIntent = new Intent(LogginnActivity.this, RegistrerActivity.class);
                LogginnActivity.this.startActivity(regIntent);
            }
        });
    }

    /**
     * Hindrer brukeren fra å gå bakover i autentiseringen
     */
    @Override
    public void onBackPressed() {
        //Ikke gjør noe
    }

    /**
     * Setter opp nødvendige parametere og starter en økt
     * @param con Context Konteksten dette skal gjøres i
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void startOkt(Context con) {
        Signature signatur = FingerprintHjelper.kryptOb.getSignature();
        try {
            byte[] forSigning = FingerprintActivity.pemOktKey.getBytes();
            signatur.update(forSigning);
            byte[] signert = signatur.sign();
            FingerprintHjelper.pemSign = Base64.encodeToString(signert, Base64.DEFAULT);
        } catch (SignatureException e) {
            MainActivity.visFeilMelding("En feil har oppstått", con);
        }

        Response.Listener<String> responsOkt = new Response.Listener<String>() {
            /**
             * Håndterer respons fra serveren.
             * Hvis suksess så lagres øktnummeret unna og brukeren videreføres til UtforHandlingActivity
             * Hvis ikke suksess så sendes brukeren tilbake til fingerprintactivity for å prøve å opprette økten på nytt
             * @param res String Responsen fra serveren, sendt i JSON format.
             */
            @Override
            public void onResponse(String res) {
                JSONObject jsonRes = null;
                try {
                    jsonRes = new JSONObject(res);
                    boolean suksess = jsonRes.getBoolean("suksess");
                    if (suksess) {
                        MainActivity.OktNr = jsonRes.getString("oktNr");
                        Intent regIntent = new Intent(con, UtforHandlingActivity.class);
                        con.startActivity(regIntent);
                    }
                    else {
                        MainActivity.OktNr = null;
                        MainActivity.setUuid(null);
                        MainActivity.visFeilMelding("En feil har oppstått, vennligst prøv igjen", con);
                    }
                } catch (JSONException e) {
                    MainActivity.visFeilMelding("En feil har oppstått", con);
                }
            }
        };
        //Sender økten til serveren
        StartOkt okt = new StartOkt(FingerprintHjelper.pemSign, responsOkt, con);
        RequestQueue q = Volley.newRequestQueue(con);
        q.add(okt);
    }
}

