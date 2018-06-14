package com.example.pietruszka.myapplicationmapactivi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Pietruszka on 12.04.2018.
 */

public class logowanie extends AppCompatActivity {

    View rootView;
    //logowanie
    private CheckBox mCbShowPwd;
    private Context mycontext;
    private EditText loginTV;
    private EditText hasloTV;
    private Button ZalogujButton;
    private String login;
    private String haslo;
    //wybor trybu
    private String trybWybor="";
    private Button wlasnyButton;
    private Button veturiloButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logowanie);
        mycontext=this;

        //logowanie
        final String teoretycznyLogin="user1";//test
        final String teoretyczneHaslo="admin";//test

        ZalogujButton = (Button)findViewById(R.id.button3);
        mCbShowPwd = findViewById(R.id.cbShowPwd);
        loginTV = (EditText) findViewById(R.id.editTextLogin);
        hasloTV = (EditText) findViewById(R.id.editTextHaslo);
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    hasloTV.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    hasloTV.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        ZalogujButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login=loginTV.getText().toString();
                haslo=hasloTV.getText().toString();
                Log.d("gf",login+" " +haslo);
                if (!teoretyczneHaslo.equals(haslo) || !teoretycznyLogin.equals(login))
                {
                    Log.d("fd","fdsha");
                    final AlertDialog alertDialog = new AlertDialog.Builder(mycontext).create();
                    alertDialog.setMessage("Niepoprawny login lub hasło");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            alertDialog.hide();
                        }
                    });
                    alertDialog.show();
                }
                if (trybWybor=="")
                {
                    final AlertDialog alertDialog1 = new AlertDialog.Builder(mycontext).create();
                    alertDialog1.setMessage("Wybierz tryb podróży: własny rower czy Veturilo?");
                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            alertDialog1.hide();
                        }
                    });
                    alertDialog1.show();
                }
                if(trybWybor!="" && teoretyczneHaslo.equals(haslo) && teoretycznyLogin.equals(login))
                {
                    Intent myIntent = new Intent(mycontext, MapsActivity.class);
                    myIntent.putExtra("userName", login);
                    myIntent.putExtra("trybWybor", trybWybor);
                    startActivity(myIntent);
                }
            }
        });

        //wybor trybu
        wlasnyButton = (Button)findViewById(R.id.button4);
        veturiloButton = (Button)findViewById(R.id.button5);
        wlasnyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wlasnyButton.getBackground().setAlpha(255);
                veturiloButton.getBackground().setAlpha(70);
                trybWybor="wlasny";
            }
        });
       veturiloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veturiloButton.getBackground().setAlpha(255);
                wlasnyButton.getBackground().setAlpha(70);
                trybWybor="veturilo";
            }
        });

       //przekazanie login i trybWybor do nastepnego activity
    }
}
