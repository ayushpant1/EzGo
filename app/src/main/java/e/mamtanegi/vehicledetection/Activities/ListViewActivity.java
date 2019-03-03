package e.mamtanegi.vehicledetection.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import e.mamtanegi.vehicledetection.R;

public class ListViewActivity extends AppCompatActivity {
    ListView listView;
    ListviewAdapter listviewAdapter;
    List<JourneyModel> journeyModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView) findViewById(R.id.journey_list);
        journeyModelArrayList = JourneyModel.getjourneyModelList();
        listviewAdapter = new ListviewAdapter(ListViewActivity.this, journeyModelArrayList);
        listView.setAdapter(listviewAdapter);


    }

}
