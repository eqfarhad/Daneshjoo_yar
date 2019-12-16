package ir.daneshjou_yaar.daneshjo_need.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;

/**
 * Created by iqfarhad on 12/29/2017.
 */

public class grid_adapter extends BaseAdapter {
    private static final String TAG = "grid_adapter";
    private List<Category_Model> mCategory_List;
    private Context mContext;

    public grid_adapter(List<Category_Model> category_List, Context context) {
        mCategory_List = category_List;
        mContext = context;
    }


    void loadNewData(List<Category_Model> newModel){
        mCategory_List = newModel;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ((mCategory_List != null) && (mCategory_List.size() !=0) ? mCategory_List.size() : 0);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_grid_item, parent, false);
        Category_Model category_model = mCategory_List.get(position);

        ImageView thumnail = (ImageView) view.findViewById(R.id.image_grid);
        Custom_Text_View title = (Custom_Text_View) view.findViewById(R.id.text_grid);
        Custom_Text_View amount = (Custom_Text_View) view.findViewById(R.id.amount_grid);


        Picasso.with(mContext).load(category_model.getImage())
                .error(R.drawable.ic_image_white_48dp)
                .placeholder(R.drawable.ic_image_white_48dp)
                .into(thumnail);

        title.setText(category_model.getName());
        amount.setVisibility(View.VISIBLE);
        amount.setText(category_model.getAmount());

        return view;
    }


    public Category_Model getCategory(int position){
        return ((mCategory_List != null) && (mCategory_List.size() != 0) ? mCategory_List.get(position) : null);
    }
}
