package com.tyit.climatetracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangeLocation extends AppCompatActivity {
    static String cityName = "";

    TextView cityInput;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        cityInput = findViewById(R.id.cityName);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityName = cityInput.getText().toString();

                finish();
            }
        });
    }
}
