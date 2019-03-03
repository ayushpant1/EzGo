package e.mamtanegi.vehicledetection.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import e.mamtanegi.vehicledetection.Models.DriverModel;
import e.mamtanegi.vehicledetection.R;

public class SignUpDriver extends AppCompatActivity implements View.OnClickListener {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TextInputLayout tilEmail;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPhoneno;
    private TextInputLayout tilPassword;
    private TextInputLayout tilIdProof;
    private TextInputLayout tilVehicleRegistrationNo;
    private TextInputLayout tilVehicleType;
    private TextInputLayout tilLicense;
    private TextInputLayout tilConfirmPassword;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPhoneno;
    private EditText etPassword;
    private EditText etIdProof;
    private EditText etVehicleRegistrationNo;
    private EditText etVehicleType;
    private EditText etLicense;


    private EditText etConfirmPassword;
    private Button btnSignup;
    private TextView tvSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_driver);
        init();
        showUI();
        setOnClickListener();
    }

    private void setOnClickListener() {
        btnSignup.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
        etUsername.addTextChangedListener(new GenericTextWatcher(tilUsername));
        etEmail.addTextChangedListener(new GenericTextWatcher(tilEmail));
        etPhoneno.addTextChangedListener(new GenericTextWatcher(tilPhoneno));
        etPassword.addTextChangedListener(new GenericTextWatcher(tilPassword));
        etConfirmPassword.addTextChangedListener(new GenericTextWatcher(tilConfirmPassword));
        etIdProof.addTextChangedListener(new GenericTextWatcher(tilIdProof));
        etVehicleRegistrationNo.addTextChangedListener(new GenericTextWatcher(tilVehicleRegistrationNo));
        etVehicleType.addTextChangedListener(new GenericTextWatcher(tilVehicleType));
        etLicense.addTextChangedListener(new GenericTextWatcher(tilLicense));

    }

    private void showUI() {

    }

    private void init() {

        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        tilPhoneno = (TextInputLayout) findViewById(R.id.til_phoneno);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.til_confirmpassword);
        tilIdProof = (TextInputLayout) findViewById(R.id.til_id_proof);
        tilVehicleRegistrationNo = (TextInputLayout) findViewById(R.id.til_vehicle_registration_no);
        tilVehicleType = (TextInputLayout) findViewById(R.id.til_vehicle_type);
        tilLicense = (TextInputLayout) findViewById(R.id.til_license);

        etEmail = (EditText) findViewById(R.id.et_email);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPhoneno = (EditText) findViewById(R.id.et_phoneno);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirmpassword);
        etIdProof = (EditText) findViewById(R.id.et_id_proof);
        etVehicleRegistrationNo = (EditText) findViewById(R.id.et_vehicle_registration_no);
        etVehicleType = (EditText) findViewById(R.id.et_vehicle_vehicle_type);
        etLicense = (EditText) findViewById(R.id.et_license);

        btnSignup = (Button) findViewById(R.id.btn_signup);

        tvSignin = (TextView) findViewById(R.id.tv_signin);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if (checkMandatoryInputFields()) {
                    String email = etEmail.getText().toString();
                    String username = etUsername.getText().toString();
                    String phonenumber = etPhoneno.getText().toString();
                    String password = etPassword.getText().toString();
                    String idProof = etIdProof.getText().toString();
                    String vehicleRegistrationNo = etVehicleRegistrationNo.getText().toString();
                    String vehicleType = etVehicleType.getText().toString();
                    String license = etLicense.getText().toString();
                    DriverModel.writeDriverDetails(email, username, phonenumber, password, idProof, vehicleRegistrationNo, vehicleType, license);
                    Toast.makeText(SignUpDriver.this, "Success", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_signin:
                Intent intent = new Intent(SignUpDriver.this, SignupActivity.class);
                startActivity(intent);
                break;


        }
    }

    private boolean checkMandatoryInputFields() {
        if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            tilEmail.setError("Enter Email");
            tilEmail.requestFocus();
            return false;
        } else if (!etEmail.getText().toString().trim().matches(emailPattern)) {
            tilEmail.setError("Enter Valid Email");
            tilEmail.requestFocus();
            return false;
        } else if (etUsername.getText().toString().trim().equalsIgnoreCase("")) {
            tilUsername.setError("Enter Username");
            tilUsername.requestFocus();
            return false;
        } else if (etPhoneno.getText().toString().trim().equalsIgnoreCase("")) {
            tilPhoneno.setError("Enter Phoneno");
            tilPhoneno.requestFocus();
            return false;
        } else if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            tilPassword.setError("Enter Password");
            tilPassword.requestFocus();
            return false;
        } else if (etConfirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
            tilConfirmPassword.setError("Enter ConfirmPassword");
            tilConfirmPassword.requestFocus();
            return false;
        } else if (!Objects.equals(etPassword.getText().toString(), etConfirmPassword.getText().toString())) {
            tilConfirmPassword.setError("Password do not match");
            tilConfirmPassword.requestFocus();
            return false;
        } else if (etIdProof.getText().toString().trim().equalsIgnoreCase("")) {
            tilIdProof.setError("Enter Id Proof");
            tilIdProof.requestFocus();
            return false;
        } else if (etVehicleRegistrationNo.getText().toString().trim().equalsIgnoreCase("")) {
            tilVehicleRegistrationNo.setError("Enter Vehicle Registration No");
            tilVehicleRegistrationNo.requestFocus();
            return false;
        } else if (etVehicleType.getText().toString().trim().equalsIgnoreCase("")) {
            tilVehicleType.setError("Enter Vehicle Type");
            tilVehicleType.requestFocus();
            return false;
        } else if (etLicense.getText().toString().trim().equalsIgnoreCase("")) {
            tilLicense.setError("Enter License No");
            tilLicense.requestFocus();
            return false;
        }

        return true;

    }

    private class GenericTextWatcher implements TextWatcher {

        private TextInputLayout til;

        public GenericTextWatcher(TextInputLayout til) {
            this.til = til;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (til.getId()) {
                case R.id.til_username:
                    if (!etUsername.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_email:
                    if (!etEmail.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_phoneno:
                    if (!etPhoneno.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_password:
                    if (!etPassword.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_confirmpassword:
                    if (!etConfirmPassword.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_id_proof:
                    if (!etIdProof.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_vehicle_registration_no:
                    if (!etVehicleRegistrationNo.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_vehicle_type:
                    if (!etVehicleType.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;
                case R.id.til_license:
                    if (!etLicense.getText().toString().trim().equalsIgnoreCase(""))
                        til.setErrorEnabled(false);
                    break;

            }

        }
    }
}

