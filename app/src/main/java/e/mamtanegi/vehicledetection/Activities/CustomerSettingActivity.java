package e.mamtanegi.vehicledetection.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        }

    }

    private void saveUserInformation() {
        customerEmail = etEmail.getText().toString();
        phoneNo = etPhoneNo.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("email", customerEmail);
        userInfo.put("phoneno", phoneNo);
        customerDatabase.updateChildren(userInfo);

        finish();

    }
}
