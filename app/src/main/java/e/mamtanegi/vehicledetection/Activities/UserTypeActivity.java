package e.mamtanegi.vehicledetection.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import e.mamtanegi.vehicledetection.R;

public class UserTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rbOwner;
    private RadioButton rbUser;
    private Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        init();
        showUI();
        setOnClickListener();
    }

    private void setOnClickListener() {
        btnNext.setOnClickListener(this);

    }

    private void showUI() {

    }

    private void init() {
        rbOwner = (RadioButton) findViewById(R.id.rb_owner);
        rbUser = (RadioButton) findViewById(R.id.rb_user);
        btnNext = (Button) findViewById(R.id.btn_next);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (rbOwner.isChecked()) {
                    Intent intent = new Intent(UserTypeActivity.this, SignupActivity.class);
                    intent.putExtra("id", 2);
                    startActivity(intent);

                } else if (rbUser.isChecked()) {
                    Intent intent = new Intent(UserTypeActivity.this, SignupActivity.class);
                    intent.putExtra("id", 1);
                    startActivity(intent);

                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(UserTypeActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
