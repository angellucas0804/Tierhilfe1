package com.tierhilfe;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.tierhilfe.domain.AnimalRepository;
import com.tierhilfe.ui.content.ViewModelHelpContent;
import com.tierhilfe.ui.list.ViewModelHelpList;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AnimalRepository animalRepository;

    public ViewModelFactory(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelHelpList.class)) {
            return (T) new ViewModelHelpList(animalRepository);
        }
        if (modelClass.isAssignableFrom(ViewModelHelpContent.class)) {
            return (T) new ViewModelHelpContent(animalRepository);
        }
        throw new IllegalArgumentException("Unknown model class!");
    }
}
