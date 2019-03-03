package e.mamtanegi.vehicledetection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class usersignup extends AppCompatActivity {
    TextView createone;
    EditText username, userphoneno;
    EditText EditTextcode;
    Button signin, verify;
    FirebaseAuth mAuth;

    String codesend;
    DatabaseReference userlogindata;
    ArrayList<userlogindata> myuserlogindataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usersignup);
        userlogindata = FirebaseDatabase.getInstance().getReference("userlogindata");
        signin = findViewById(R.id.add);
        username = (EditText) findViewById(R.id.Name);
        mAuth = FirebaseAuth.getInstance();
        createone = findViewById(R.id.createone);
        EditTextcode = findViewById(R.id.code);
        userphoneno = findViewById(R.id.phoneno);
        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();

            }
        });
        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifySigninCode();
                adduserlogindata();


            }
        });
    }

    private void adduserlogindata() {

    }
        private void verifySigninCode () {
            String code = EditTextcode.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesend, code);
            signInWithPhoneAuthCredential(credential);
        }
        private void signInWithPhoneAuthCredential (PhoneAuthCredential credential){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                // ...
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(getApplicationContext(), "incorrect Verification", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
        private void sendVerificationCode () {
            String phone = userphoneno.getText().toString();
            if (phone.isEmpty()) {
                userphoneno.setError("phone no is required");
                userphoneno.requestFocus();
                return;
            }
            if (phone.length() < 10) {
                userphoneno.setError("please enter a valid phoneno");
                userphoneno.requestFocus();
                return;

            }
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,        // Phone number to verify,
                    100,// Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks


        }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codesend = s;
            }

        };

    }

