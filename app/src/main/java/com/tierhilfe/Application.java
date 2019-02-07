package com.tierhilfe;

import android.arch.persistence.room.Room;

import com.tierhilfe.domain.AnimalRepository;
import com.tierhilfe.room.RoomAnimalsRepository;
import com.tierhilfe.room.RoomDatabase;

import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory;

public class Application extends android.app.Application {

    private AnimalRepository animalRepository;
    private ViewModelFactory viewModelFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        final RoomDatabase roomDatabase = Room.databaseBuilder(
                this,
                RoomDatabase.class,
                "tierhilfe"
        ).openHelperFactory(new RequerySQLiteOpenHelperFactory())
                .build();
        animalRepository = new RoomAnimalsRepository(roomDatabase);

        viewModelFactory = new ViewModelFactory(animalRepository);
    }

    public AnimalRepository getAnimalsRepository() {
        return animalRepository;
    }

    public ViewModelFactory getViewModelFactory() {
        return viewModelFactory;
    }
}
