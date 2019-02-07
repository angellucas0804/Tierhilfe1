package com.tierhilfe.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tierhilfe")
public class Animal {

    @PrimaryKey(autoGenerate = true)
    public final long id;
    public final String name;
    public final String description;
    public final String latitud;
    public final String longitud;
    public final String address;
    public final String image;
    public final int phone;
    public final long creationTimestamp;

    public Animal(long id, String name, String description, String latitud, String longitud, String address, String image, int phone, long creationTimestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitud = latitud;
        this.longitud = longitud;
        this.address = address;
        this.image = image;
        this.phone = phone;
        this.creationTimestamp = creationTimestamp;
    }

    @Ignore
    public Animal(String name, String description, String latitud, String longitud, String address, String image, int phone, long creationTimestamp) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.latitud = latitud;
        this.longitud = longitud;
        this.address = address;
        this.image = image;
        this.phone = phone;
        this.creationTimestamp = creationTimestamp;
    }

    @Ignore
    public Animal(long id) {
        this.id = id;
        this.name = "";
        this.description = "";
        this.latitud = "";
        this.longitud = "";
        this.address = "";
        this.image = "";
        this.phone = 0;
        this.creationTimestamp = 0;
    }


}
