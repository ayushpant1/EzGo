package e.mamtanegi.vehicledetection.Activities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek.srivastava on 12/13/2018.
 */

public class JourneyModel {

    public String Date;
    public String TotalKm;
    public String Time;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTotalKm() {
        return TotalKm;
    }

    public void setTotalKm(String totalKm) {
        TotalKm = totalKm;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public static List<JourneyModel> getjourneyModelList() {
        List<JourneyModel> journeyModelList = new ArrayList<>();
        JourneyModel journeyModel = new JourneyModel();
        journeyModel.setDate("4/12/18");
        journeyModel.setTotalKm("3");
        journeyModel.setTime("2");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("2/12/18");
        journeyModel.setTotalKm("2");
        journeyModel.setTime("3");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("4/12/18");
        journeyModel.setTotalKm("3");
        journeyModel.setTime("2");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("2/12/18");
        journeyModel.setTotalKm("2");
        journeyModel.setTime("3");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("4/12/18");
        journeyModel.setTotalKm("3");
        journeyModel.setTime("2");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("2/12/18");
        journeyModel.setTotalKm("2");
        journeyModel.setTime("3");
        journeyModelList.add(journeyModel);

        journeyModel = new JourneyModel();
        journeyModel.setDate("4/12/18");
        journeyModel.setTotalKm("3");
        journeyModel.setTime("2");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("2/12/18");
        journeyModel.setTotalKm("2");
        journeyModel.setTime("3");
        journeyModelList.add(journeyModel);

        journeyModel = new JourneyModel();
        journeyModel.setDate("4/12/18");
        journeyModel.setTotalKm("3");
        journeyModel.setTime("2");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("2/12/18");
        journeyModel.setTotalKm("2");
        journeyModel.setTime("3");
        journeyModelList.add(journeyModel);

        journeyModel = new JourneyModel();
        journeyModel.setDate("4/12/18");
        journeyModel.setTotalKm("3");
        journeyModel.setTime("2");
        journeyModelList.add(journeyModel);
        journeyModel = new JourneyModel();
        journeyModel.setDate("2/12/18");
        journeyModel.setTotalKm("2");
        journeyModel.setTime("3");
        journeyModelList.add(journeyModel);


        return journeyModelList;
    }
}
