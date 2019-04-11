package e.mamtanegi.vehicledetection.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import e.mamtanegi.vehicledetection.Contants.Constants;
import e.mamtanegi.vehicledetection.R;
import e.mamtanegi.vehicledetection.SharedPrefUtils;
import e.mamtanegi.vehicledetection.Utils;

public class DriverSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilEmail;
    private TextInputLayout tilPhoneNo;
    private TextInputLayout tilVehicleType;

    private EditText etEmail;
    private EditText etPhoneNo;
    private EditText etVehicleType;

    private Button btnConfirm;
    private Button btnCancel;

    private DatabaseReference driverDatabase;

    private String userId;

    private String driverEmail;
    private String phoneNo;
    private String driverVehicleType;

    private ImageView imgProfile;

    private Uri resultUri;

    private String profileUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);
        init();
        showUI();
        setClickListener();
    }

    private void init() {
        tilEmail = (TextInputLayout) findViewById(R.id.til_name);
        tilPhoneNo = (TextInputLayout) findViewById(R.id.til_phone_no);
        tilVehicleType = (TextInputLayout) findViewById(R.id.til_car_type);

        etEmail = (EditText) findViewById(R.id.et_name);
        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);
        etVehicleType = (EditText) findViewById(R.id.et_car_type);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        imgProfile = (ImageView) findViewById(R.id.img_profile);

        userId = SharedPrefUtils.getStringPreference(this, Constants.USER_ID);
        driverDatabase = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(userId);
    }


    private void showUI() {
        getDriverInfo();

    }

    private void getDriverInfo() {
        Utils.showProgressDialog(this, true);
        driverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Utils.dismissProgressDialog();
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("email") != null) {
                        driverEmail = map.get("email").toString();
                        etEmail.setText(driverEmail);
                    }
                    if (map.get("phoneno") != null) {
                        phoneNo = map.get("phoneno").toString();
                        etPhoneNo.setText(phoneNo);

                    }
                    if (map.get("profileImageUri") != null) {
                        profileUrl = map.get("profileImageUri").toString();
                        Glide.with(getApplication()).load(profileUrl).into(imgProfile);
                    }
                    if (map.get("vehicleType") != null) {
                        driverVehicleType = map.get("vehicleType").toString();
                        etVehicleType.setText(driverVehicleType);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setClickListener() {
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            imgProfile.setImageURI(resultUri);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                saveUserInformation();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.img_profile:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
        }

    }

    private void saveUserInformation() {
        Utils.showProgressDialog(this, true);
        driverEmail = etEmail.getText().toString();
        phoneNo = etPhoneNo.getText().toString();
        driverVehicleType = etVehicleType.getText().toString();

        if (resultUri != null) {
            uploadImageToDatabase();
        } else {
            Utils.dismissProgressDialog();
            finish();
        }
    }

    private void uploadImageToDatabase() {
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userId);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = filePath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                Utils.dismissProgressDialog();
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUri = task.getResult().toString();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("email", driverEmail);
                    userInfo.put("phoneno", phoneNo);
                    userInfo.put("profileImageUri", downloadUri);
                    userInfo.put("vehicleType", driverVehicleType);
                    driverDatabase.updateChildren(userInfo);
                    Utils.dismissProgressDialog();
                    finish();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}

