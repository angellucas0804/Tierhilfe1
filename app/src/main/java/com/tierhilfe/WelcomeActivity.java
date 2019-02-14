package com.tierhilfe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tierhilfe.api.QWService;
import com.tierhilfe.api.RetrofitClient;
import com.tierhilfe.api.model.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.ll_go_help_animal).setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.ll_go_search).setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ImageDogsActivity.class);
            startActivity(intent);
        });


    }
}
