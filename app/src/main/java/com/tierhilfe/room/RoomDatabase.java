package com.tierhilfe.room;

import android.arch.persistence.room.Database;

@Database(entities = {Animal.class}, version = 1)
public abstract class RoomDatabase extends android.arch.persistence.room.RoomDatabase {

    public abstract AnimalDao getAnimalDao();
}
