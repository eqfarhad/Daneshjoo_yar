package ir.daneshjou_yaar.location_address.Showing_Detail_Category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.location_address.Object_model;

/**
 * Created by iqfarhad on 1/1/2018.
 */

public class Recycle_view_Showing_model_adapter extends RecyclerView.Adapter<Recycle_view_Showing_model_adapter.MyViewHolder>  {


    private List<Object_model> model_list;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name , info , address ;
        public ImageView image ,btn_google;

        public MyViewHolder(View view) {
            super(view);
            info = (TextView) view.findViewById(R.id.info);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }


    public Recycle_view_Showing_model_adapter(List<Object_model> modellist , Context mmContext) {
        this.model_list = modellist;
        this.mContext = mmContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_model_detail_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Object_model model = model_list.get(position);
        holder.name.setText(model.getName());
        holder.info.setText(model.getInfo());
        holder.address.setText(model.getAddress());
        Picasso.with(mContext).load(model.getImg())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }


}
