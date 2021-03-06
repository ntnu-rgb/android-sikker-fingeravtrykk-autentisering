package com.example.applikasjon;

import android.os.Build;
import android.support.annotation.RequiresApi;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *  Klasse for å gjøre klar en handlingsforespørsel før den sendes til server
 */
public class HandlingsForesporsel extends StringRequest {
    private Map<String, String> parametere;   //Brukes av Volley for å sende data til siden


    /**
     * Tom constructor
     */
    public HandlingsForesporsel() {
        super(Request.Method.POST, null, null, null);
        parametere = null;
    }

    /**
     * Constructor som legger riktige verdier til arrayen som skal sendes til server
     * @param listener Response.Listener<String> Lytter til responsen fra server
     * @param transak String Transaksjonen som skal utføres
     * @param sign String Signaturen som skal sendes til server
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public HandlingsForesporsel(Response.Listener<String> listener, String transak, String sign) {
        super(Request.Method.POST, MainActivity.HandlingsURL, listener, null);
        //Setter opp verdiene som skal sendes til server
        parametere = new HashMap<>();
        parametere.put("uuid", MainActivity.uuid);
        parametere.put("oktNr", MainActivity.OktNr);
        parametere.put("transaksjon", transak);
        parametere.put("signatur", sign);
    }
    /**
     * Henter ut data fra parameter arrayen (map)
     * @return Map Parameterene som sendes med til server
     */
    @Override
    public Map<String, String> getParams(){
        return parametere;
    }
}
