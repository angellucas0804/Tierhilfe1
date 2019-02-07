package com.tierhilfe.ui;

import android.support.annotation.NonNull;

public interface FragmentInteractionInterface {

    void showAnimalContent(@NonNull final String animalId);

    void setTitle(@NonNull final String title);

    void closeFragment();

}
