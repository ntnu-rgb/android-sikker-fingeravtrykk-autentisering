package com.example.applikasjon;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasse for å gjøre klar en registreringsforespørsel før den sendes til server
 */
public class RegistrerForesporsel extends StringRequest {

    private Map<String, String> parametere;   //Brukes av Volley for å sende data til siden

    /**
     * Tom constructor
     */
    public RegistrerForesporsel() {
        super(Request.Method.POST, null, null, null);
        parametere = null;
    }

    /**
     * Constructor som legger riktige verdier til arrayen som skal sendes til server
     * @param brukernavn String Brukernavnet til brukeren som skal registrere seg
     * @param passord String Passordet til brukeren som skal registrere seg
     * @param listener Response.Listener<String> Lytter til responsen
     */
    public RegistrerForesporsel(String brukernavn, String passord, Response.Listener<String> listener) {
        super(Method.POST, MainActivity.HandlingsURL, listener, null);
        parametere = new HashMap<>();
        parametere.put("epost", brukernavn);
        parametere.put("passord", passord);
        parametere.put("registrer", "true");
    }

    /**
     * Henter ut innholdet til parameterarrayen(Map)
     * @return Map parameterene til forespørselen
     */
    @Override
    public Map<String, String> getParams(){
        return parametere;
    }
}
