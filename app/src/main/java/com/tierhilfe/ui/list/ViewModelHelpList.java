package com.tierhilfe.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.tierhilfe.domain.Animal;
import com.tierhilfe.domain.AnimalRepository;

import java.util.List;

public class ViewModelHelpList extends ViewModel {

    private final AnimalRepository animalRepository;
    private final MutableLiveData<List<Animal>> fetchAllAnimalsResponse;
    private final MutableLiveData<Animal> createAnimalResponse;

    public ViewModelHelpList(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
        fetchAllAnimalsResponse = new MutableLiveData<>();
        createAnimalResponse = new MutableLiveData<>();
    }

    public void fetchAllAnimals() {
        AsyncTask.execute(() -> {
            final List<Animal> result = animalRepository.getAllAnimals();
            //Debemos llamar a "postValue" si no estamos en el MainThread.
            //Este observador se ejecuta en el MainThread,
            //por lo que podemos utilizar "setValue".
            fetchAllAnimalsResponse.postValue(result);
        });
    }

    public void createAnimal(final String name,
                             final String description,
                             final String latitud,
                             final String lontigutd,
                             final String address,
                             final String image,
                             final int phone,
                             final long creationTimestamp) {
        final Animal animal = new Animal(
                name,
                description,
                latitud,
                lontigutd,
                address,
                image,
                phone,
                creationTimestamp
        );
        AsyncTask.execute(() -> {
            final Animal createdAnimal = animalRepository.createAnimal(animal);
            createAnimalResponse.postValue(createdAnimal);
        });
    }

    public LiveData<List<Animal>> getFetchAllAnimalsResponse() {
        return fetchAllAnimalsResponse;
    }

    public MutableLiveData<Animal> getCreateAnimalResponse() {
        return createAnimalResponse;
    }
}
