package com.tierhilfe.domain;

import android.support.annotation.NonNull;

public class Animal {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final String description;

    @NonNull
    private final String latitud;

    @NonNull
    private final String longitud;

    @NonNull
    private final String address;

    @NonNull
    private final String image;

    private final int phone;

    private final long creationTimestamp;

    public Animal(@NonNull String id, @NonNull String name, @NonNull String description, @NonNull String latitud, @NonNull String longitud, @NonNull String address, @NonNull String image, @NonNull int phone, long creationTimestamp) {
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

    public Animal(@NonNull String name, @NonNull String description, @NonNull String latitud, @NonNull String longitud, @NonNull String address, @NonNull String image, @NonNull int phone, long creationTimestamp) {
        this.id = "";
        this.name = name;
        this.description = description;
        this.latitud = latitud;
        this.longitud = longitud;
        this.address = address;
        this.image = image;
        this.phone = phone;
        this.creationTimestamp = creationTimestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getLatitud() {
        return latitud;
    }

    @NonNull
    public String getLongitud() {
        return longitud;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getImage() {
        return image;
    }

    public int getPhone() {
        return phone;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }
}
