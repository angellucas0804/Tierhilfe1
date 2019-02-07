package com.tierhilfe.room;

import android.support.annotation.NonNull;

import com.tierhilfe.domain.Animal;
import com.tierhilfe.domain.AnimalRepository;

import java.util.ArrayList;
import java.util.List;

public class RoomAnimalsRepository implements AnimalRepository {

    private final RoomDatabase roomDatabase;

    public RoomAnimalsRepository(RoomDatabase roomDatabase) {
        this.roomDatabase = roomDatabase;
    }

    @NonNull
    @Override
    public List<Animal> getAllAnimals() {
        final List<com.tierhilfe.room.Animal> roomAnimals
                = roomDatabase.getAnimalDao().getAll();
        final List<Animal> domainAnimals = new ArrayList<>();
        for (final com.tierhilfe.room.Animal roomAnimal : roomAnimals) {
            domainAnimals.add(createDomainAnimal(roomAnimal));
        }
        return domainAnimals;
    }

    @Override
    public boolean updateAnimal(@NonNull Animal animal) {
        return roomDatabase.getAnimalDao().update(createRoomAnimal(animal))
                != 0;
    }

    @Override
    public boolean deleteAnimal(@NonNull String animalId) {
        return roomDatabase.getAnimalDao().delete(new com.tierhilfe.room.Animal(
                Long.valueOf(animalId)
        )) != 0;
    }

    @NonNull
    @Override
    public Animal createAnimal(@NonNull Animal animal) {
        final long createdAnimalId = roomDatabase.getAnimalDao().insert(createRoomAnimal(animal));
        return new Animal(
                String.valueOf(createdAnimalId),
                animal.getName(),
                animal.getDescription(),
                animal.getLatitud(),
                animal.getLongitud(),
                animal.getAddress(),
                animal.getImage(),
                animal.getPhone(),
                animal.getCreationTimestamp()
        );
    }

    @NonNull
    @Override
    public Animal getAnimalById(@NonNull String animalId) {
        return createDomainAnimal(roomDatabase.getAnimalDao().getById(Long.valueOf(animalId)));
    }

    private Animal createDomainAnimal(final com.tierhilfe.room.Animal roomAnimal) {
        return new Animal(
                String.valueOf(roomAnimal.id),
                roomAnimal.name,
                roomAnimal.description,
                roomAnimal.latitud,
                roomAnimal.longitud,
                roomAnimal.address,
                roomAnimal.image,
                roomAnimal.phone,
                roomAnimal.creationTimestamp
        );
    }

    private com.tierhilfe.room.Animal createRoomAnimal(final Animal domainAnimal) {
        long id;
        try {
            id = Long.valueOf(domainAnimal.getId());
        } catch (NumberFormatException e) {
            id = 0;
        }
        return new com.tierhilfe.room.Animal(
                id,
                domainAnimal.getName(),
                domainAnimal.getDescription(),
                domainAnimal.getLatitud(),
                domainAnimal.getLongitud(),
                domainAnimal.getAddress(),
                domainAnimal.getImage(),
                domainAnimal.getPhone(),
                domainAnimal.getCreationTimestamp()
        );
    }
}
