package com.tierhilfe.api;


import com.tierhilfe.api.model.Pet;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QWService {

    @GET("breed/{breed}/images/random")
    Call<Pet> getImageByBreed(@Path("breed") String breedName);


}
