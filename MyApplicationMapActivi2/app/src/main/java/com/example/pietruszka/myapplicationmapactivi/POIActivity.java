package com.example.pietruszka.myapplicationmapactivi;

/**
 * Created by Pietruszka on 12.03.2018.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class POIActivity extends AppCompatActivity {

    private Button confirm;
    private Switch veturiloSwitch;
    private Switch naprawaSwitch;
    private Character firstArg;
    private Character secArg;
    private String num2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);


        veturiloSwitch = (Switch) findViewById(R.id.switch1);
        naprawaSwitch = (Switch) findViewById(R.id.switch2);
        firstArg= getIntent().getStringExtra("state").charAt(0);
        secArg= getIntent().getStringExtra("state").charAt(1);

        if (firstArg == '1') {
            veturiloSwitch.setChecked(true);
        }
        if (secArg == '1') {
            naprawaSwitch.setChecked(true);
        }

        veturiloSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    firstArg = '1';
                } else if (!isChecked) {
                    firstArg = '0';
                }

            }
        });

        naprawaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    secArg = '1';
                } else if (!isChecked) {
                    secArg = '0';
                }

            }
        });


        confirm = findViewById(R.id.button6);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num2 = Character.toString(firstArg) +  Character.toString(secArg);
                Intent output = new Intent();
                output.putExtra("mail", num2);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}
