package e.mamtanegi.vehicledetection.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import e.mamtanegi.vehicledetection.R;

/**
 * Created by abhishek.srivastava on 12/13/2018.
 */

public class ListviewAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context mcontext;
    List<JourneyModel> journeyModelList;

    public ListviewAdapter(Context context, List<JourneyModel> journeyModels) {
        this.mcontext = context;
        journeyModelList = journeyModels;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return journeyModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        ViewHolder viewHolder;
        if (view == null) {
            rowView = inflater.inflate(R.layout.row, null);
            viewHolder = new ViewHolder();
            viewHolder.tvdate = (TextView) rowView.findViewById(R.id.date);
            viewHolder.tvTotalkm = (TextView) rowView.findViewById(R.id.total_km);
            viewHolder.tvTime = (TextView) rowView.findViewById(R.id.time);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.tvdate.setText(journeyModelList.get(i).getDate());
        viewHolder.tvTotalkm.setText(journeyModelList.get(i).getTotalKm());
        viewHolder.tvTime.setText(journeyModelList.get(i).getTime());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, DetailActivity.class);
                mcontext.startActivity(intent);

            }
        });
        return rowView;
    }

    public static class ViewHolder {
        TextView tvdate, tvTotalkm, tvTime;

    }
}
