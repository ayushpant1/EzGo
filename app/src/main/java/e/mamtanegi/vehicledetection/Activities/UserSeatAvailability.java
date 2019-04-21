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
import e.mamtanegi.vehicledetection.SharedPrefUtils;
import e.mamtanegi.vehicledetection.Utils;

public class UserSeatAvailability extends AppCompatActivity implements View.OnClickListener {
    ImageView[] imageViews = new ImageView[10];
    private boolean[] checkedSeats = new boolean[10];
    private boolean[] usercheckedSeats = new boolean[10];
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_seat_availability);
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
        Utils.showProgressDialog(UserSeatAvailability.this, true);
        callFirebase();

        for (int i = 0; i < 10; i++) {
            imageViews[i].setOnClickListener(this);


        }
        btnSubmit.setOnClickListener(this);


    }

    private void callFirebase() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriverAvailable").child("AvailableSeats").child(SharedPrefUtils.getStringPreference(UserSeatAvailability.this, "DriverFoundId"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utils.dismissProgressDialog();
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();
                    checkedSeats[0] = (boolean) map.get("seat1");
                    checkedSeats[1] = (boolean) map.get("seat2");
                    checkedSeats[2] = (boolean) map.get("seat3");
                    checkedSeats[3] = (boolean) map.get("seat4");
                    checkedSeats[4] = (boolean) map.get("seat5");
                    checkedSeats[5] = (boolean) map.get("seat6");
                    checkedSeats[6] = (boolean) map.get("seat7");
                    checkedSeats[7] = (boolean) map.get("seat8");
                    checkedSeats[8] = (boolean) map.get("seat9");
                    checkedSeats[9] = (boolean) map.get("seat10");
                    setCheckedSeats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setCheckedSeats() {
        if (checkedSeats[0]) {
            imageViews[0].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[0] = false;
        }

        if (checkedSeats[1]) {
            imageViews[1].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[1] = false;
        }
        if (checkedSeats[2]) {
            imageViews[2].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[2] = false;
        }
        if (checkedSeats[3]) {
            imageViews[3].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[3] = false;
        }
        if (checkedSeats[4]) {
            imageViews[4].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[4] = false;
        }
        if (checkedSeats[5]) {
            imageViews[5].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[5] = false;
        }
        if (checkedSeats[6]) {
            imageViews[6].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[6] = false;
        }
        if (checkedSeats[7]) {
            imageViews[7].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[7] = false;
        }
        if (checkedSeats[8]) {
            imageViews[8].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[8] = false;
        }
        if (checkedSeats[9]) {
            imageViews[9].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
        } else {
            usercheckedSeats[9] = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_seat1:
                if (!checkedSeats[0]) {
                    if (usercheckedSeats[0]) {
                        imageViews[0].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[0] = false;

                    } else {
                        imageViews[0].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[0] = true;
                    }
                }

                break;
            case R.id.img_seat2:
                if (!checkedSeats[1]) {
                    if (usercheckedSeats[1]) {
                        imageViews[1].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[1] = false;
                    } else {
                        imageViews[1].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[1] = true;
                    }
                }
                break;
            case R.id.img_seat3:
                if (!checkedSeats[2]) {
                    if (usercheckedSeats[2]) {
                        imageViews[2].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[2] = false;
                    } else {
                        imageViews[2].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[2] = true;
                    }
                }
                break;
            case R.id.img_seat4:
                if (!checkedSeats[3]) {
                    if (usercheckedSeats[3]) {
                        imageViews[3].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[3] = false;
                    } else {
                        imageViews[3].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[3] = true;
                    }
                }
                break;
            case R.id.img_seat5:
                if (!checkedSeats[4]) {
                    if (usercheckedSeats[4]) {
                        imageViews[4].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[4] = false;
                    } else {
                        imageViews[4].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[4] = true;
                    }
                }
                break;
            case R.id.img_seat6:
                if (!checkedSeats[5]) {
                    if (usercheckedSeats[5]) {
                        imageViews[5].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[5] = false;
                    } else {
                        imageViews[5].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[5] = true;
                    }
                }
                break;
            case R.id.img_seat7:
                if (!checkedSeats[6]) {
                    if (usercheckedSeats[6]) {
                        imageViews[6].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[6] = false;
                    } else {
                        imageViews[6].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[6] = true;
                    }
                }
                break;
            case R.id.img_seat8:
                if (!checkedSeats[7]) {
                    if (usercheckedSeats[7]) {
                        imageViews[7].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[7] = false;
                    } else {
                        imageViews[7].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[7] = true;
                    }
                }
                break;
            case R.id.img_seat9:
                if (!checkedSeats[8]) {
                    if (usercheckedSeats[8]) {
                        imageViews[8].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[8] = false;
                    } else {
                        imageViews[8].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[8] = true;
                    }
                }
                break;
            case R.id.img_seat10:
                if (!checkedSeats[9]) {
                    if (usercheckedSeats[9]) {
                        imageViews[9].setImageDrawable(getResources().getDrawable(R.drawable.car_seat));
                        usercheckedSeats[9] = false;
                    } else {
                        imageViews[9].setImageDrawable(getResources().getDrawable(R.drawable.car_seat_green));
                        usercheckedSeats[9] = true;
                    }
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
        for (int i = 0; i < 10; i++) {
            if (usercheckedSeats[i]) {
                checkedSeats[i] = usercheckedSeats[i];
            }
        }
        DatabaseReference availableSeatsReference = FirebaseDatabase.getInstance().getReference("DriverAvailable").child("AvailableSeats").child(SharedPrefUtils.getStringPreference(UserSeatAvailability.this, "DriverFoundId"));
        availableSeatsReference.child("seat1").setValue(checkedSeats[0]);
        availableSeatsReference.child("seat2").setValue(checkedSeats[1]);
        availableSeatsReference.child("seat3").setValue(checkedSeats[2]);
        availableSeatsReference.child("seat4").setValue(checkedSeats[3]);
        availableSeatsReference.child("seat5").setValue(checkedSeats[4]);
        availableSeatsReference.child("seat6").setValue(checkedSeats[5]);
        availableSeatsReference.child("seat7").setValue(checkedSeats[6]);
        availableSeatsReference.child("seat8").setValue(checkedSeats[7]);
        availableSeatsReference.child("seat9").setValue(checkedSeats[8]);
        availableSeatsReference.child("seat10").setValue(checkedSeats[9]);


    }
}
