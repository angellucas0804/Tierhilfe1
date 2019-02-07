package com.tierhilfe.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tierhilfe.R;
import com.tierhilfe.domain.Animal;
import com.tierhilfe.ui.FragmentBase;

import java.util.List;


public class FragmentHelpList extends FragmentBase {

    public static final String TAG = "ListFragment";

    private ArrayAdapter<Animal> animalArrayAdapter;
    private ViewModelHelpList viewModelList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModelList = ViewModelProviders
                .of(this, viewModelFactory)
                .get(ViewModelHelpList.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_help_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCreateHelp:
                createAnimal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_help_list, container, false);
        final ListView listViewElements = view.findViewById(R.id.listView);
        animalArrayAdapter = new AnimalAdapter(getActivity());
        listViewElements.setAdapter(animalArrayAdapter);
        listViewElements.setOnItemClickListener((adapterView, view1, i, l) -> {
            final Animal animal = animalArrayAdapter.getItem(i);
            fragmentInteractionInterface.showAnimalContent(animal.getId());
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelList.getFetchAllAnimalsResponse()
                .observe(this, this::onAnimalChanged);
        viewModelList.getCreateAnimalResponse()
                .observe(this, animal -> {
                    if (animal == null) {
                        Log.d("FragmentList", "note == null");
                        return;
                    }
                    Log.d("FragmentList", "note != null");
                    onAnimalCreated(animal);
                    viewModelList
                            .getCreateAnimalResponse()
                            .setValue(null);
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModelList.fetchAllAnimals();
        fragmentInteractionInterface.setTitle(getString(R.string.app_name));
    }

    private void onAnimalChanged(final List<Animal> animals) {
        animalArrayAdapter.clear();
        animalArrayAdapter.addAll(animals);
    }

    private void onAnimalCreated(final Animal animal) {
        fragmentInteractionInterface.showAnimalContent(animal.getId());
    }

    private void createAnimal() {
        final long creationTimestamp = System.currentTimeMillis();
        viewModelList.createAnimal("", "", "", "", "", "", 0, creationTimestamp);
    }

    private static class AnimalAdapter extends ArrayAdapter<Animal> {

        private final LayoutInflater layoutInflater;

        public AnimalAdapter(final Context context) {
            super(context, 0);
            layoutInflater = LayoutInflater.from(getContext());
        }

        private static class ViewHolder {
            TextView tv_element_name;
            ImageView iv_element_animal;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Animal animal = getItem(position);
            final View view;
            final ViewHolder viewHolder;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.element_help, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_element_name = view.findViewById(R.id.tv_element_name);
                viewHolder.iv_element_animal = view.findViewById(R.id.iv_element_animal);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String animalTitle = animal.getName();
            if (animalTitle.trim().length() == 0) {
                animalTitle = getContext().getString(R.string.no_hay);
            }
            viewHolder.tv_element_name.setText(animalTitle);

            Bitmap bitmap = StringToBitMap(animal.getImage());
            if (bitmap != null){
                bitmap = scaleDown(bitmap,1024,true);
            }
            viewHolder.iv_element_animal.setImageBitmap(bitmap);

            return view;
        }

        Bitmap StringToBitMap(String encodedString) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }

        static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
            float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
            int width = Math.round(ratio * realImage.getWidth());
            int height = Math.round(ratio * realImage.getHeight());

            return Bitmap.createScaledBitmap(realImage, width, height, filter);
        }
    }


}
