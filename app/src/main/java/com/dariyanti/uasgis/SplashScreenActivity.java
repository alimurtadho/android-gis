package com.dariyanti.uasgis;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SplashScreenActivity extends AppCompatActivity {

    private Button btn_mulai;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btn_mulai=findViewById(R.id.btn_mulai);
        btn_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextScreen();
            }
        });

    }

    private void goToNextScreen() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
