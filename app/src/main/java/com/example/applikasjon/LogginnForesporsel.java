package com.example.applikasjon;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class LogginnForesporsel extends StringRequest {

    private static final String LOGGINNURL = "https://folk.ntnu.no/sturlaba/sfa/";
    private Map<String, String> parametere;   //Brukes av Volley for å sende data til siden


    public LogginnForesporsel(String brukernavn, String passord, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGGINNURL, listener, null);

        KeyStore keyStore = null;
        PublicKey offentligNokkel = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            offentligNokkel =
                    keyStore.getCertificate(FingerprintActivity.KEYNAME).getPublicKey();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Log.d("NØKKELERROR", "Error ved henting av offentlig nøkkel ");
        }
        parametere = new HashMap<>();
        parametere.put("epost", brukernavn);
        parametere.put("passord", passord);
        parametere.put("offentlig_nokkel",offentligNokkel.toString());
        parametere.put("forstegangsautentisering", "true");
    }

    @Override
    public Map<String, String> getParams(){
        return parametere;
    }

}
