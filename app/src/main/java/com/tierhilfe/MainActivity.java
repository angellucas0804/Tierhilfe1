package com.tierhilfe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.tierhilfe.ui.FragmentInteractionInterface;
import com.tierhilfe.ui.content.FragmentHelpContent;
import com.tierhilfe.ui.list.FragmentHelpList;

public class MainActivity extends AppCompatActivity
        implements FragmentInteractionInterface {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        final FragmentHelpList fragmentList;
        if (savedInstanceState == null) {
            fragmentList = new FragmentHelpList();
            fragmentList.setRetainInstance(true);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragmentList)
                    .commit();
        }
    }

    @Override
    public void showAnimalContent(@NonNull String animalId) {
        final FragmentHelpContent fragmentContent = FragmentHelpContent.newInstance(animalId);
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.fragmentContainer, fragmentContent)
                .commit();
    }

    @Override
    public void setTitle(@NonNull String title) {
        super.setTitle(title);
    }

    @Override
    public void closeFragment() {
        onBackPressed();
    }
}
