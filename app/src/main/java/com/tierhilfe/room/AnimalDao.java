package com.tierhilfe.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AnimalDao {

    @Query("SELECT * FROM tierhilfe")
    List<Animal> getAll();

    @Query("SELECT * FROM tierhilfe WHERE id=:id")
    Animal getById(final long id);

    @Delete
    int delete(Animal animal);

    @Update
    int update(Animal animal);

    @Insert
    long insert(Animal animal);

}
