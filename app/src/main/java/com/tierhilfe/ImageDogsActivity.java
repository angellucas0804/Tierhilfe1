package com.tierhilfe;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tierhilfe.api.QWService;
import com.tierhilfe.api.RetrofitClient;
import com.tierhilfe.api.model.Pet;
import com.tierhilfe.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDogsActivity extends AppCompatActivity {

    private QWService mQWService;
    ImageView iv_dog;
    Spinner sp_dogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dogs);
        mQWService = RetrofitClient.getRetrofit().create(QWService.class);
        iv_dog = findViewById(R.id.iv_dogs);
        sp_dogs = findViewById(R.id.sp_dogs);
        loadSpinner();
        sp_dogs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!sp_dogs.getSelectedItem().toString().equals("SELECCIONE")) {
                    getPhotoDog(sp_dogs.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("ahp,spinner", "nada");
            }
        });
    }

    private void loadSpinner() {
        List<String> listTurno = new ArrayList<>();
        listTurno.add("SELECCIONE");
        listTurno.add("labrador");
        listTurno.add("germanshepherd");
        listTurno.add("beagle");
        listTurno.add("terrier");
        listTurno.add("boxer");
        listTurno.add("affenpinscher");
        listTurno.add("akita");
        listTurno.add("bulldog");
        listTurno.add("bullterrier");
        listTurno.add("chihuahua");
        listTurno.add("dane");
        listTurno.add("husky");
        listTurno.add("malamute");
        listTurno.add("maltese");
        listTurno.add("mexicanhairless");
        listTurno.add("pekinese");
        listTurno.add("pinscher");
        listTurno.add("poodle");
        listTurno.add("puggle");
        listTurno.add("retriever");
        listTurno.add("rottweiler");
        listTurno.add("schnauzer");
        listTurno.add("sheepdog");
        listTurno.add("spaniel");
        listTurno.add("wolfhound");
        ArrayAdapter<String> turnoArrayAdater = new ArrayAdapter<>(ImageDogsActivity.this, android.R.layout.simple_spinner_item, listTurno);
        turnoArrayAdater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_dogs.setAdapter(turnoArrayAdater);
    }

    public void getPhotoDog(String s) {
        Util.showLoading(this);
        Call<Pet> getImageUrl = mQWService.getImageByBreed(s);
        getImageUrl.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(@NonNull Call<Pet> call, @NonNull Response<Pet> response) {
                if (response.isSuccessful()) {
                    Pet asd = response.body();
                    assert asd != null;
                    Log.d("ahp,body", asd.message);
                    String photoUrl = asd.message;
                    Glide.with(ImageDogsActivity.this).load(photoUrl).into(iv_dog);
                    Util.hideLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Pet> call, @NonNull Throwable t) {
                Util.hideLoading();
                Toast.makeText(ImageDogsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
