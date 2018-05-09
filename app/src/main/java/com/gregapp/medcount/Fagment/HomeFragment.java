package com.gregapp.medcount.Fagment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gregapp.medcount.DB.DBHelper;
import com.gregapp.medcount.Model.BarcodeItem;
import com.gregapp.medcount.Model.PhotoItem;
import com.gregapp.medcount.MyPreference;
import com.gregapp.medcount.R;
import com.gregapp.medcount.ScannerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private static final int SCAN_CODE = 0;
    private static final int TAKE_PHOTO = 1;

    private int mCurrentOption;

    private String mCurrentPhotoPath;
    private DBHelper dbHelper;
    private MyPreference myPreference;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        myPreference = new MyPreference(getActivity());

        Map<String, ?> allPrefs = myPreference.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        Button scanBarCodeBtn = (Button) fragmentView.findViewById(R.id.scanBarcodeBtn);
        Button takePhotoBtn = (Button) fragmentView.findViewById(R.id.takePhotoBtn);
        Button sendReportBtn = (Button) fragmentView.findViewById(R.id.sendReportBtn);

        scanBarCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(SCAN_CODE);
            }
        });

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(TAKE_PHOTO);
            }
        });

        sendReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        return fragmentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void launchActivity(int currentOption) {
        this.mCurrentOption = currentOption;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            if(mCurrentOption == SCAN_CODE){
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(intent);
            }else{
                dispatchTakePictureIntent();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchActivity(this.mCurrentOption);
                } else {
                    Toast.makeText(getActivity(), "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.gregapp.medcount.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:

                if(resultCode == Activity.RESULT_OK){
                    PhotoItem photoItem = new PhotoItem(System.currentTimeMillis(), mCurrentPhotoPath, myPreference.getLastRemindTime());
                    long id = dbHelper.savePhotoItem(photoItem);
                    Toast.makeText(getActivity(), "photo insert id = " + id, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private Uri makeBarcodeReport(){
        StringBuilder barcodeStrBuilder = new StringBuilder();
        List<BarcodeItem> barcodeItems = dbHelper.getAllBarcodeItems();
        for (BarcodeItem item: barcodeItems) {
            barcodeStrBuilder.append(item.timeStr)
                            .append("\t=\"")
                            .append(item.barcodeContent)
                            .append("\"\n");
        }

        String barcodefilename = "barcode.xls";
        File barcodeReportFile = new File(getActivity().getExternalFilesDir("Reports"), barcodefilename);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(barcodeReportFile);
            outputStream.write(barcodeStrBuilder.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FileProvider.getUriForFile(getActivity(), "com.gregapp.medcount.fileprovider", barcodeReportFile);

    }

    private Uri makePhotoReport(){

        StringBuilder photoStrBuilder = new StringBuilder();
        List<PhotoItem> photoItems = dbHelper.getAllPhotoItems();
        for (PhotoItem item: photoItems) {
            String[] splitUrl = item.photoUrl.split("/");
            String photoName = splitUrl[splitUrl.length-1];
            photoStrBuilder.append(item.lastRemindTimeStr)
                    .append("\t")
                    .append(item.timeStr)
                    .append("\t=\"")
                    .append(photoName)
                    .append("\"\n");
        }

        String photofilename = "photo.xls";
        File photoReportFile = new File(getActivity().getExternalFilesDir("Reports"), photofilename);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(photoReportFile);
            outputStream.write(photoStrBuilder.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(getActivity(), "com.gregapp.medcount.fileprovider", photoReportFile);
    }


    private void sendEmail(){
        Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{myPreference.getDoctorEmail()});
        email.putExtra(Intent.EXTRA_SUBJECT, "MedCount Report");

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Age:\t").append(myPreference.getAge()).append("\n")
                    .append("Gender:\t").append(myPreference.getGender()).append("\n")
                    .append("Medication Name:\t").append(myPreference.getMedicName());

        email.putExtra(Intent.EXTRA_TEXT, bodyBuilder.toString());

        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(makeBarcodeReport());
        uris.add(makePhotoReport());
        List<PhotoItem> photoItems = dbHelper.getAllPhotoItems();
        for (PhotoItem item: photoItems) {
            File file = new File(item.photoUrl);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.gregapp.medcount.fileprovider", file);
            uris.add(contentUri);
        }

        email.putExtra(Intent.EXTRA_STREAM, uris);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
