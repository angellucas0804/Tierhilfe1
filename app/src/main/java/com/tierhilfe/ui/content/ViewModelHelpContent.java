package com.tierhilfe.ui.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.tierhilfe.domain.Animal;
import com.tierhilfe.domain.AnimalRepository;

public class ViewModelHelpContent extends ViewModel {

    private final AnimalRepository animalRepository;
    private final MutableLiveData<Animal> fetchAnimalByIdResponse;

    public ViewModelHelpContent(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
        fetchAnimalByIdResponse = new MutableLiveData<>();
    }

    public void fetchAnimalById(@NonNull final String animalId) {
        AsyncTask.execute(() -> fetchAnimalByIdResponse.postValue(
                animalRepository.getAnimalById(animalId)
        ));
    }

    public void deleteAnimalById(@NonNull final String animalId) {
        AsyncTask.execute(() -> animalRepository.deleteAnimal(animalId));
    }

    public void updateAnimal(final Animal animal) {
        AsyncTask.execute(() -> animalRepository.updateAnimal(animal));
    }

    public LiveData<Animal> getFetchAnimalByIdResponse() {
        return fetchAnimalByIdResponse;
    }
}
