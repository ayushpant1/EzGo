package e.mamtanegi.vehicledetection.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import e.mamtanegi.vehicledetection.R;
import e.mamtanegi.vehicledetection.Utils;

public class SeatAvailability extends AppCompatActivity implements View.OnClickListener {
    ImageView[] imageViews = new ImageView[10];
    private boolean checkedSeat1;
    private boolean checkedSeat2;
    private boolean checkedSeat3;
    private boolean checkedSeat4;
    private boolean checkedSeat5;
    private boolean checkedSeat6;
    private boolean checkedSeat7;
    private boolean checkedSeat8;
    private boolean checkedSeat9;
    private boolean checkedSeat10;

    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        imageViews[0] = (ImageView) findViewById(R.id.img_seat1);
        imageViews[1] = (ImageView) findViewById(R.id.img_seat2);
        imageViews[2] = (ImageView) findViewById(R.id.img_seat3);
        imageViews[3] = (ImageView) findViewById(R.id.img_seat4);
        imageViews[4] = (ImageView) findViewById(R.id.img_seat5);
        imageViews[5] = (ImageView) findViewById(R.id.img_seat6);
        imageViews[6] = (ImageView) findViewById(R.id.img_seat7);
        imageViews[7] = (ImageView) findViewById(R.id.img_seat8);
        imageViews[8] = (ImageView) findViewById(R.id.img_seat9);
        imageViews[9] = (ImageView) findViewById(R.id.img_seat10);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        Utils.showProgressDialog(SeatAvailability.this, true);
        callFirebase();

        for (int i = 0; i < 10; i++) {
            imageViews[i].setOnClickListener(this);
        }
        btnSubmit.setOnClickListener(this);


    }

    private void setCheckedSeats() {
        if (checkedSeat1) {
            imageViews[0].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat2) {
            imageViews[1].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat3) {
            imageViews[2].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat4) {
            imageViews[3].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat5) {
            imageViews[4].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat6) {
            imageViews[5].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat7) {
            imageViews[6].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat8) {
            imageViews[7].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat9) {
            imageViews[8].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
        if (checkedSeat10) {
            imageViews[9].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        }
    }

    private void callFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriverAvailable").child("AvailableSeats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utils.dismissProgressDialog();
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();
                    checkedSeat1 = (boolean) map.get("seat1");
                    checkedSeat2 = (boolean) map.get("seat2");
                    checkedSeat3 = (boolean) map.get("seat3");
                    checkedSeat4 = (boolean) map.get("seat4");
                    checkedSeat5 = (boolean) map.get("seat5");
                    checkedSeat6 = (boolean) map.get("seat6");
                    checkedSeat7 = (boolean) map.get("seat7");
                    checkedSeat8 = (boolean) map.get("seat8");
                    checkedSeat9 = (boolean) map.get("seat9");
                    checkedSeat10 = (boolean) map.get("seat10");
                    setCheckedSeats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_seat1:
                if (checkedSeat1) {
                    imageViews[0].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat1 = false;

                } else {
                    imageViews[0].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat1 = true;
                }

                break;
            case R.id.img_seat2:
                if (checkedSeat2) {
                    imageViews[1].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat2 = false;
                } else {
                    imageViews[1].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat2 = true;
                }
                break;
            case R.id.img_seat3:
                if (checkedSeat3) {
                    imageViews[2].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat3 = false;
                } else {
                    imageViews[2].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat3 = true;
                }
                break;
            case R.id.img_seat4:
                if (checkedSeat4) {
                    imageViews[3].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat4 = false;
                } else {
                    imageViews[3].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat4 = true;
                }
                break;
            case R.id.img_seat5:
                if (checkedSeat5) {
                    imageViews[4].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat5 = false;
                } else {
                    imageViews[4].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat5 = true;
                }
                break;
            case R.id.img_seat6:
                if (checkedSeat6) {
                    imageViews[5].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat6 = false;
                } else {
                    imageViews[5].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat6 = true;
                }
                break;
            case R.id.img_seat7:
                if (checkedSeat7) {
                    imageViews[6].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat7 = false;
                } else {
                    imageViews[6].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat7 = true;
                }
                break;
            case R.id.img_seat8:
                if (checkedSeat8) {
                    imageViews[7].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat8 = false;
                } else {
                    imageViews[7].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat8 = true;
                }
                break;
            case R.id.img_seat9:
                if (checkedSeat9) {
                    imageViews[8].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat9 = false;
                } else {
                    imageViews[8].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat9 = true;
                }
                break;
            case R.id.img_seat10:
                if (checkedSeat10) {
                    imageViews[9].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                    checkedSeat10 = false;
                } else {
                    imageViews[9].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                    checkedSeat10 = true;
                }
                break;
            case R.id.btn_submit:
                showPopUp(this, "Are you Sure ?");
                break;
        }

    }

    public void showPopUp(final Activity activityWrapper, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityWrapper);
        builder.setMessage(message).setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        callSubmitFirebase();

                    }
                }).setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void callSubmitFirebase() {
        DatabaseReference availableSeatsReference = FirebaseDatabase.getInstance().getReference("DriverAvailable").child("AvailableSeats");
        availableSeatsReference.child("seat1").setValue(checkedSeat1);
        availableSeatsReference.child("seat2").setValue(checkedSeat2);
        availableSeatsReference.child("seat3").setValue(checkedSeat3);
        availableSeatsReference.child("seat4").setValue(checkedSeat4);
        availableSeatsReference.child("seat5").setValue(checkedSeat5);
        availableSeatsReference.child("seat6").setValue(checkedSeat6);
        availableSeatsReference.child("seat7").setValue(checkedSeat7);
        availableSeatsReference.child("seat8").setValue(checkedSeat8);
        availableSeatsReference.child("seat9").setValue(checkedSeat9);
        availableSeatsReference.child("seat10").setValue(checkedSeat10);
        recreate();

    }
}
