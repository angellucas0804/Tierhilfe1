package com.tierhilfe.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tierhilfe.Application;
import com.tierhilfe.ViewModelFactory;

public class FragmentBase extends Fragment {

    protected ViewModelFactory viewModelFactory;
    protected FragmentInteractionInterface fragmentInteractionInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionInterface = (FragmentInteractionInterface) context;
        final Application application =
                (Application) context.getApplicationContext();
        viewModelFactory =
                application.getViewModelFactory();

    }
}
