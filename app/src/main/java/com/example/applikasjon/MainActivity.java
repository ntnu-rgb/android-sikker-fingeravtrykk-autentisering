package com.example.applikasjon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


   public static SharedPreferences pref = null;
   public static String uuid = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        uuid = pref.getString(getString(R.string.lagret_uuid), null);  //Hvis lagret_uuid ikke finnes, lagre null som default verdi
        this.startActivity(new Intent(this, FingerprintActivity.class));


    }

}
