package e.mamtanegi.vehicledetection.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.mamtanegi.vehicledetection.Contants.Constants;
import e.mamtanegi.vehicledetection.CustomerMapActivity;
import e.mamtanegi.vehicledetection.DriverMapActivity;
import e.mamtanegi.vehicledetection.Models.CustomerModel;
import e.mamtanegi.vehicledetection.Models.DriverModel;
import e.mamtanegi.vehicledetection.R;
import e.mamtanegi.vehicledetection.SharedPrefUtils;
import e.mamtanegi.vehicledetection.Utils;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    String password;
    CustomerModel customerModel;
    List<CustomerModel> customerModelList;
    DriverModel driverModel;
    List<DriverModel> driverModelList;
    String username;
    private EditText etUsername;
    private EditText etPassword;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private int usertype;
    private Button btnSignup;
    private Button btnSignin;
    private TextView tvCreateOne;
    private boolean IsUserExist;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        id = getIntent().getIntExtra("id", 0);

        init();
        showUI();
        setOnClickListener();
    }


    private void setOnClickListener() {
        btnSignup.setOnClickListener(this);
        tvCreateOne.setOnClickListener(this);

    }

    private void showUI() {

    }

    private void init() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        tvCreateOne = (TextView) findViewById(R.id.tv_create_one);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_signup:
                Utils.showProgressDialog(this, true);
                CallingFirebase();

                break;
            case R.id.tv_create_one:
                if (id == 1) {
                    intent = new Intent(SignupActivity.this, SignUpCustomer.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(SignupActivity.this, SignUpDriver.class);
                    startActivity(intent);
                }
                break;

        }

    }

    private void CallingFirebase() {
        if (id == 1) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SignupUsers");

            mDatabase.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Utils.dismissProgressDialog();
                    customerModelList = new ArrayList<>();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        customerModel = noteDataSnapshot.getValue(CustomerModel.class);
                        customerModelList.add(customerModel);
                    }

                    if (UserExist()) {
                        if (id == 1) {
                            Intent intent = new Intent(SignupActivity.this, CustomerMapActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent1 = new Intent(SignupActivity.this, DriverMapActivity.class);
                            startActivity(intent1);
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SignupOwners");
            mDatabase.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    driverModelList = new ArrayList<>();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        driverModel = noteDataSnapshot.getValue(DriverModel.class);
                        driverModelList.add(driverModel);
                    }
                    if (UserExist()) {
                        if (id == 1) {
                            Intent intent = new Intent(SignupActivity.this, CustomerMapActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent1 = new Intent(SignupActivity.this, DriverMapActivity.class);
                            startActivity(intent1);
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean UserExist() {
        IsUserExist = false;
        if (id == 1) {
            for (int i = 0; i < customerModelList.size(); i++) {
                customerModel = customerModelList.get(i);
                if (etUsername.getText().toString().equalsIgnoreCase(customerModel.username) && etPassword.getText().toString().equalsIgnoreCase(customerModel.password)) {
                    IsUserExist = true;
                    SharedPrefUtils.setStringPreference(this, Constants.USER_ID, customerModel.userId);
                    break;
                } else {
                    IsUserExist = false;
                }
            }
        } else {
            for (int i = 0; i < driverModelList.size(); i++) {
                driverModel = driverModelList.get(i);
                if (etUsername.getText().toString().equalsIgnoreCase(driverModel.username) && etPassword.getText().toString().equalsIgnoreCase(driverModel.password)) {
                    IsUserExist = true;
                    SharedPrefUtils.setStringPreference(this, Constants.USER_ID, driverModel.userId);
                    break;
                } else {
                    IsUserExist = false;
                }
            }
        }

        return IsUserExist;
    }


}



