package e.mamtanegi.vehicledetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ownerlogin extends AppCompatActivity {
    TextView createone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createone = findViewById(R.id.createone);
        setContentView(R.layout.activity_ownerlogin);
createone=findViewById(R.id.createone);
createone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(ownerlogin.this,ownersignup.class);
        startActivity(intent);
    }
});
    }
}
