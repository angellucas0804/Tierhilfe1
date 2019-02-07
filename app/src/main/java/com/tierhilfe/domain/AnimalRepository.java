package com.tierhilfe.domain;

import android.support.annotation.NonNull;

import java.util.List;

public interface AnimalRepository {

    @NonNull
    List<Animal> getAllAnimals();

    boolean updateAnimal(@NonNull final Animal animal);

    boolean deleteAnimal(@NonNull final String animalId);

    @NonNull
    Animal createAnimal(@NonNull final Animal animal);

    @NonNull
    Animal getAnimalById(@NonNull final String animalId);
}
