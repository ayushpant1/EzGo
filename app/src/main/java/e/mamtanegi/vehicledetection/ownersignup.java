package e.mamtanegi.vehicledetection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ownersignup extends AppCompatActivity {
    EditText usernameo,passwordo,ownerphoneno;
    Button add;
    DatabaseReference databasemyRef;
    ArrayList<myRef> myRefArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databasemyRef = FirebaseDatabase.getInstance().getReference("myRef");
        setContentView(R.layout.activity_ownersignup);
        usernameo = (EditText) findViewById(R.id.username);
        ownerphoneno = (EditText) findViewById(R.id.phoneno);
        passwordo = (EditText) findViewById(R.id.password);
        add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addmyRef();

            }
        });
    }
    private void addmyRef(){
        String username = usernameo.getText().toString().trim();
        int phoneno = Integer.parseInt(ownerphoneno.getText().toString());
        String password=passwordo.getText().toString();
        if(!TextUtils.isEmpty(username)){
            String username1 = databasemyRef.push().getKey();
            myRef myRef1=new myRef(username,phoneno,password);

            databasemyRef.child(username != null ? username1 : null).setValue(myRef1);
            Toast.makeText(this,"owner added",Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "you should enter your name", Toast.LENGTH_LONG).show();

}
}
