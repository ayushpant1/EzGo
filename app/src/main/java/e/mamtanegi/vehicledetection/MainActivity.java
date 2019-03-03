package e.mamtanegi.vehicledetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button bookyourride,owner,yourlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookyourride=findViewById(R.id.bookyourride);
        owner=findViewById(R.id.owner);
        yourlocation=findViewById(R.id.yourlocation);
             bookyourride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,usersignin.class);
                startActivity(intent);
            }
        });
        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ownerlogin.class);
                startActivity(intent);
            }
        });
        yourlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Googlemapactivity.class);
                startActivity(intent);
            }
        });

        }

    }

