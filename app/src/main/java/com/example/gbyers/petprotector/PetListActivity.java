package com.example.gbyers.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class PetListActivity extends AppCompatActivity {
    //store memebr variable to whatevr has been selected default will be (R.drawable.none)
    private Uri imageUri;
    private ImageView selectImage;
    private static final int CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        selectImage = (ImageView) findViewById(R.id.petImageView);
        //constructs Uri to any resource
        imageUri = getUriToResource(this, R.drawable.none);

        //set image uri to the view
       // selectImage = setImageUri(imageUri);
    }
    public void selectPetImage(View v)
    {
        ArrayList<String> permList = new ArrayList<>();

        //start by seeing if we have permisson to camera
        int camPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(camPerm != PackageManager.PERMISSION_GRANTED)
        {
            permList.add(Manifest.permission.CAMERA);
        }
        int readPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readPerm != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writePerm != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permList.size() > 0)
        {
            //method accepts only String
            //convert arrayLList to array of String
            String[] perms = new String[permList.size()];

            ActivityCompat.requestPermissions(this, permList.toArray(perms),100);

        }
        //if we have all thre permissions. Open Image gallery
        if(camPerm == PackageManager.PERMISSION_GRANTED&& writePerm == PackageManager.PERMISSION_GRANTED && readPerm == PackageManager.PERMISSION_GRANTED)
        {
            //use an intent to launch gallery and take pics
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CONSTANT);
        }else
            Toast.makeText(this, "set Permisions",Toast.LENGTH_LONG);

    }
    //code to handle when user closes image Gallery
    //by slecting an image or pressing the back button
    //intent data is Uri
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data != null && requestCode == CONSTANT && resultCode == RESULT_OK){
           //set imageUri to what was clicked
            imageUri = data.getData();
            selectImage.setImageURI(imageUri);
        }

    }
    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId)throws Resources.NotFoundException{
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                "://" + res.getResourcePackageName(resId)+
                '/'+res.getResourceTypeName(resId)+
                '/'+res.getResourceEntryName(resId));
    }
}
