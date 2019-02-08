package com.tierhilfe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tierhilfe.api.QWService;
import com.tierhilfe.api.RetrofitClient;
import com.tierhilfe.api.model.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    private QWService mQWService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mQWService = RetrofitClient.getRetrofit().create(QWService.class);

        findViewById(R.id.btn_go_animal).setOnClickListener(v -> {
            /*Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);*/
            Call<Pet> getImageUrl = mQWService.getImageByBreed("bullterrier");
            getImageUrl.enqueue(new Callback<Pet>() {
                @Override
                public void onResponse(Call<Pet> call, Response<Pet> response) {
                    Log.d("ahp", "0");
                    if (response.isSuccessful()) {
                        Log.d("ahp", "1");
                        Log.d("ahp,body", response.body()+"");
                    }
                    if (response.body() == null) {
                        Log.d("ahp", "2");
                    }
                    if (response.errorBody() != null) {
                        Log.d("ahp", "3");
                    }
                }

                @Override
                public void onFailure(Call<Pet> call, Throwable t) {
                    Log.d("ahp,failure", t.getMessage());
                }
            });

        });


    }
}
