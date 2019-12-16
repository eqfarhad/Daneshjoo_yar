package ir.daneshjou_yaar.map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.daneshjou_yaar.R;

/**
 * Created by iqfarhad on 12/29/2017.
 */

public class grid_adapter_map extends BaseAdapter {

    private String names[];
    private int icons[];
    private LinearLayout mLinearLayout;
    private Context mContext;

    public grid_adapter_map(String[] names, int[] icons, Context context) {
        this.names = names;
        this.icons = icons;
        mContext = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView==null){
            rowView = LayoutInflater.from(mContext).inflate(R.layout.location_grid_item, parent, false);
        }else {
            rowView =  convertView ;
        }

        LinearLayout linearLayout = ( LinearLayout) rowView.findViewById(R.id.location_grid_item_linear);
        TextView name = (TextView) rowView.findViewById(R.id.text_grid);
        ImageView image = (ImageView) rowView.findViewById(R.id.image_grid);

        if (position == 0){

        }else {
            linearLayout.setBackgroundColor(Color.GRAY);

        }
        name.setText(names[position]);
        image.setImageResource(icons[position]);

        return rowView;
    }
}
