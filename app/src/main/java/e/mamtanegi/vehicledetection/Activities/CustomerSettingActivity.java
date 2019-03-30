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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class CustomerSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilEmail;
    private TextInputLayout tilPhoneNo;

    private EditText etEmail;
    private EditText etPhoneNo;

    private Button btnConfirm;
    private Button btnCancel;

    private DatabaseReference customerDatabase;

    private String userId;

    private String customerEmail;
    private String phoneNo;

    private ImageView imgProfile;

    private Uri resultUri;

    private String profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_setting);
        init();
        showUI();
        setClickListener();
    }

    private void init() {
        tilEmail = (TextInputLayout) findViewById(R.id.til_name);
        tilPhoneNo = (TextInputLayout) findViewById(R.id.til_phone_no);

        etEmail = (EditText) findViewById(R.id.et_name);
        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        imgProfile = (ImageView) findViewById(R.id.img_profile);

        userId = SharedPrefUtils.getStringPreference(this, Constants.USER_ID);
        customerDatabase = FirebaseDatabase.getInstance().getReference().child("SignupUsers").child(userId);
    }


    private void showUI() {
        getUserInfo();

    }

    private void getUserInfo() {
        Utils.showProgressDialog(this, true);
        customerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Utils.dismissProgressDialog();
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("email") != null) {
                        customerEmail = map.get("email").toString();
                        etEmail.setText(customerEmail);
                    }
                    if (map.get("phoneno") != null) {
                        phoneNo = map.get("phoneno").toString();
                        etPhoneNo.setText(phoneNo);

                    }
                    if (map.get("profileImageUri") != null) {
                        profileUrl = map.get("profileImageUri").toString();
                        Glide.with(getApplication()).load(profileUrl).into(imgProfile);
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
        customerEmail = etEmail.getText().toString();
        phoneNo = etPhoneNo.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("email", customerEmail);
        userInfo.put("phoneno", phoneNo);
        customerDatabase.updateChildren(userInfo);
        if (resultUri != null) {
            uploadImageToDatabase();
        } else {
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
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUri = uri;
                        Map newImage = new HashMap();
                        newImage.put("profileImageUri", downloadUri);
                        customerDatabase.updateChildren(newImage).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                String v = "";
                            }
                        });

                        finish();
                    }

                });
            }
        });
    }
}
