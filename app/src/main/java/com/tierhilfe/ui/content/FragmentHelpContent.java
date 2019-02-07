package com.tierhilfe.ui.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.tierhilfe.R;
import com.tierhilfe.domain.Animal;
import com.tierhilfe.ui.FragmentBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FragmentHelpContent extends FragmentBase {

    private static final String KEY_ARG_ANIMAL_ID = "animalId";

    private Uri pictureContentUri;
    public static final int REQUEST_TAKE_PHOTO = 1;
    private String bitmapString = "";
    private String latitud = "";
    private String longitud = "";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

    private EditText et_animal_name;
    private EditText et_owner_phone;
    private TextView tv_animal_address;
    private ImageView iv_animal;
    private EditText et_animal_description;

    private String animalId;
    private boolean isDeletingAnimal;

    private ViewModelHelpContent viewModelContent;

    public static FragmentHelpContent newInstance(final String animalId) {
        final FragmentHelpContent contentFragment = new FragmentHelpContent();
        final Bundle arguments = new Bundle();
        arguments.putString(KEY_ARG_ANIMAL_ID, animalId);
        contentFragment.setArguments(arguments);
        return contentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModelContent = ViewModelProviders.of(
                this,
                viewModelFactory
        ).get(ViewModelHelpContent.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        checkArguments(arguments);
        animalId = arguments.getString(KEY_ARG_ANIMAL_ID);
        viewModelContent.fetchAnimalById(animalId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isDeletingAnimal) {
            return;
        }
        updateAnimal();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_help_content, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDeleteAnimal:
                deleteAnimal();
                break;
            case R.id.actionPhotoAnimal:
                takePhoto();
                break;
            case R.id.actionShareAnimal:
                shareInformation();
                break;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_help_content, container, false);
        et_animal_name = view.findViewById(R.id.et_animal_name);
        et_owner_phone = view.findViewById(R.id.et_owner_phone);
        tv_animal_address = view.findViewById(R.id.tv_animal_address);
        iv_animal = view.findViewById(R.id.iv_animal);
        et_animal_description = view.findViewById(R.id.et_animal_description);

        view.findViewById(R.id.btn_go_rout).setOnClickListener(v -> popUpRoute());
        view.findViewById(R.id.ll_ubication).setOnClickListener(v -> showDialogGooglePlaces());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelContent.getFetchAnimalByIdResponse().observe(
                this,
                this::showAnimal
        );
    }

    private static void checkArguments(final Bundle arguments) {
        if (arguments != null &&
                arguments.containsKey(KEY_ARG_ANIMAL_ID)) {
            return;
        }
        throw new RuntimeException("Fragment doesn't have needed arguments. Call newInstance() static creation method.");
    }

    private void updateAnimal() {
        final Animal animal = new Animal(
                animalId,
                et_animal_name.getText().toString(),
                et_animal_description.getText().toString(),
                latitud,
                longitud,
                tv_animal_address.getText().toString(),
                bitmapString,
                Integer.parseInt(et_owner_phone.getText().toString()),
                System.currentTimeMillis()
        );
        viewModelContent.updateAnimal(animal);
    }

    private void showAnimal(final Animal animal) {
        if (animal == null) {
            return;
        }
        final Date animalDate = new Date(animal.getCreationTimestamp());
        final DateFormat dateFormat = DateFormat.getInstance();
        fragmentInteractionInterface.setTitle(dateFormat.format(animalDate));
        et_animal_name.setText(animal.getName());
        et_animal_description.setText(animal.getDescription());
        et_owner_phone.setText(String.valueOf(animal.getPhone()));

        bitmapString = animal.getImage();
        iv_animal.setImageBitmap(StringToBitMap(animal.getImage()));

        latitud = String.valueOf(animal.getLatitud());
        longitud = String.valueOf(animal.getLongitud());
        tv_animal_address.setText(animal.getAddress());


    }

    private void deleteAnimal() {
        viewModelContent.deleteAnimalById(animalId);
        isDeletingAnimal = true;
        fragmentInteractionInterface.closeFragment();
    }

    //TOMAR FOTO
    private void takePhoto() {
        dispatchTakePhotoIntent();
    }

    private void dispatchTakePhotoIntent() {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isActivityAvailable(Objects.requireNonNull(getContext()), takePictureIntent)) {
            try {
                pictureContentUri = createTempPictureContentUri();

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureContentUri);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", pictureContentUri));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            MessageDialogFragment.newInstance(
                    "error",
                    "no camera app"
            ).show(Objects.requireNonNull(getFragmentManager()), "alert_dialog");
        }
    }

    private static boolean isActivityAvailable(final Context context, final Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    public static class MessageDialogFragment extends DialogFragment {

        private static final String ARG_TITLE = "title";
        private static final String ARG_MESSAGE = "message";

        public static MessageDialogFragment newInstance(final String title, final String message) {
            final MessageDialogFragment fragment = new MessageDialogFragment();
            final Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            args.putString(ARG_MESSAGE, message);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String title = Objects.requireNonNull(getArguments()).getString(ARG_TITLE, "");
            final String message = getArguments().getString(ARG_MESSAGE, "");
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
    }

    private Uri createTempPictureContentUri() throws IOException {
        final String fileName = "picture_" + System.currentTimeMillis();
        final File storageDir = new File(Objects.requireNonNull(getContext()).getCacheDir(), "taked_photos");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        final File pictureFile = File.createTempFile(fileName, ".jpg", storageDir);
        return FileProvider.getUriForFile(getContext(), "com.tierhilfe.FileProvider", pictureFile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    try {
                        setPicture();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    Place place = PlaceAutocomplete.getPlace(Objects.requireNonNull(getContext()), data);
                    savePlace(place);
                    break;

            }
        }
    }

    private void setPicture() throws FileNotFoundException {
        final int imageViewWidth = iv_animal.getWidth();
        final int imageViewHeight = iv_animal.getHeight();

        final BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(
                Objects.requireNonNull(getContext()).getContentResolver().openInputStream(pictureContentUri),
                null,
                bitmapFactoryOptions
        );

        final int bitmapWidth = bitmapFactoryOptions.outWidth;
        final int bitmapHeight = bitmapFactoryOptions.outHeight;

        final int scaleFactor = Math.min(bitmapWidth / imageViewWidth, bitmapHeight / imageViewHeight);

        bitmapFactoryOptions.inJustDecodeBounds = false;
        bitmapFactoryOptions.inSampleSize = scaleFactor;
        bitmapFactoryOptions.inPurgeable = true;

        final Bitmap decodedBitmap = BitmapFactory.decodeStream(
                getContext().getContentResolver().openInputStream(pictureContentUri),
                null,
                bitmapFactoryOptions
        );

        assert decodedBitmap != null;
        Bitmap compressBitmap = scaleDown(decodedBitmap, 1024, true);

        bitmapString = bitMapToString(compressBitmap);
        iv_animal.setImageBitmap(compressBitmap);
    }

    public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    //ADDRESS
    private void showDialogGooglePlaces() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    //.setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .setCountry("PE")
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter).build(Objects.requireNonNull(getActivity()));
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void savePlace(Place place) {
        tv_animal_address.setText(place.getAddress());
        latitud = String.valueOf(place.getLatLng().latitude);
        longitud = String.valueOf(place.getLatLng().longitude);
    }

    //COMPARTIR

    private void shareInformation() {
        Uri imageUri = getLocalBitmapUri(StringToBitMap(bitmapString));

        String shareMessage = "Hola, mi nombre es "
                + et_animal_name.getText().toString()
                + " :\n" + et_animal_description.getText()
                + "\n" + "Aqui te dejo mis datos: \n" + "Teléfono: "
                + et_owner_phone.getText().toString() + "\n"
                + "Dirección: " + tv_animal_address.getText().toString();


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, "Sharing Using"));
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    //MOREEEE
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    //ROUTE
    private void popUpRoute() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_route, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(view);
        final ImageView iv_waze = view.findViewById(R.id.iv_waze);
        final ImageView iv_gmaps = view.findViewById(R.id.iv_gmaps);
        dialog.create();
        final AlertDialog ad = dialog.show();
        iv_waze.setOnClickListener(view12 -> {
            ad.dismiss();
            startWaze();
        });
        iv_gmaps.setOnClickListener(view1 -> {
            ad.dismiss();
            startGmaps();
        });
    }

    private void startGmaps() {
        try {
            Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + latitud + "," + longitud + "&navigate=yes");
            Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
            startActivity(intent);
        }
    }

    private void startWaze() {
        try {
            String uri = "waze://?ll=" + latitud + "," + longitud + "&navigate=yes";
            startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

}
