package e.mamtanegi.vehicledetection.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import e.mamtanegi.vehicledetection.Fragments.MainFragment;
import e.mamtanegi.vehicledetection.Fragments.SettingsFragment;
import e.mamtanegi.vehicledetection.Fragments.ViewPagerFrgment;
import e.mamtanegi.vehicledetection.R;
import e.mamtanegi.vehicledetection.Upload;
import e.mamtanegi.vehicledetection.Utils;

public class HomeActivity extends NavigationLiveo implements OnItemClickListener {
    HelpLiveo mHelpLiveo;
    Uri resultUri;
    List<Upload> uploads = new ArrayList<>();
    private byte[] photoBytesArray;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    public static Bitmap getBitmapFromUri(Context context, Uri uri)
            throws FileNotFoundException {
        final InputStream imageStream = context.getContentResolver()
                .openInputStream(uri);
        try {
            return BitmapFactory.decodeStream(imageStream);
        } finally {
        }
    }

    @Override
    public void onInt(Bundle savedInstanceState) {
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                if (uploads.size() > 0) {
                    setImageGlide();
                }

            }

            private void setImageGlide() {
                Uri uri = Uri.parse(uploads.get(0).getUrl());
                Picasso.get().load(uploads.get(0).getUrl()).resize(200, 200)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(HomeActivity.this.userPhoto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        this.userName.setText("Rudson Lima");
        this.userEmail.setText("rudsonlive@gmail.com");
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);


        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add("Inbox", R.drawable.ezgologo, 7);
        mHelpLiveo.addSubHeader("Categories"); //Item subHeader
        mHelpLiveo.add("Starred", R.mipmap.ic_launcher);
        mHelpLiveo.add("Sent Mail", R.mipmap.ic_launcher);
        mHelpLiveo.add("drafts", R.mipmap.ic_launcher);
        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add("trash", R.mipmap.ic_launcher);
        mHelpLiveo.add("spam", R.mipmap.ic_launcher, 120);

        View.OnClickListener onClickPhoto = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(HomeActivity.this);
            }
        };
        OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
            @Override
            public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
            }
        };
        View.OnClickListener onClickFooter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        };
        with(this) // default theme is dark
                .startingPosition(1) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem("Settings", R.mipmap.ic_launcher)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position) {
            case 1:
                mFragment = new MainFragment();
                break;
            case 2:
                mFragment = new ViewPagerFrgment();
                break;

            case 3:
                mFragment = new SettingsFragment();
                break;

            default:
                mFragment = MainFragment.newInstance(mHelpLiveo.get(position).getName(), "");
                break;
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    Bitmap photo = getBitmapFromUri(this, resultUri);
                    this.userPhoto.setImageBitmap(photo);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    photoBytesArray = stream.toByteArray();
                    uploadFile();
                } catch (Exception ex) {
                    String x = "";
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void uploadFile() {
        if (resultUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference sRef = storageReference.child("uploads/" + System.currentTimeMillis() + "." + Utils.getFileExtension(resultUri, HomeActivity.this));
            sRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl =
                                    uri.toString();
                            Upload upload = new Upload(downloadUrl);
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //displaying the upload progress
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });

        }

    }


}

