package com.moutamid.rumtastingadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.rumtastingadmin.databinding.ActivityAddNewBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddNewActivity extends AppCompatActivity {
    ActivityAddNewBinding binding;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Add New");
        binding.toolbar.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.image.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        binding.upload.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                uploadImage();
            }
        });

    }

    private void uploadImage() {
        Constants.storageReference("Images").child(new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date()))
                .putFile(imageUri)
                .addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> {
                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                      uploadData(uri.toString());
                   });
                });
    }

    private void uploadData(String uri) {
        RumModel model = new RumModel(
                UUID.randomUUID().toString(),
                binding.name.getEditText().getText().toString(),
                binding.description.getEditText().getText().toString(),
                uri
        );
        Constants.databaseReference().child(Constants.RUMS).child(model.id)
                .setValue(model)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Data uploaded", Toast.LENGTH_SHORT).show();
                    getOnBackPressedDispatcher().onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private boolean valid() {
        if (binding.name.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.description.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null){
            Toast.makeText(this, "Upload a image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(binding.imageView);
        }
    }
}