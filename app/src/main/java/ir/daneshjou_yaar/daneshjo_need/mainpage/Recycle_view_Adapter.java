package ir.daneshjou_yaar.daneshjo_need.mainpage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;


/**
 * Created by iqfarhad on 2/7/2018.
 */

public class Recycle_view_Adapter extends RecyclerView.Adapter<Recycle_view_Adapter.Recycle_view_Holder> {

    private static final String TAG = "Recycle_view_Adapter";
    private List<Advertise_Model> mAdvertise_mlist;
    private Context mContext;

    public Recycle_view_Adapter(List<Advertise_Model> advertise_mlist, Context context) {
        mAdvertise_mlist = advertise_mlist;
        mContext = context;
    }

    @Override
    public Recycle_view_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_daneshjo_need_mainpage_item_recycle_view , parent , false);
        return new Recycle_view_Holder(view);
    }

    @Override
    public void onBindViewHolder(Recycle_view_Holder holder, int position) {
        //called by the layput manager when it wants new date in an existing raw

        Advertise_Model advertise_model = mAdvertise_mlist.get(position);
        Log.d(TAG, "onBindViewHolder: "+ advertise_model.getTitle() + "-> "+ position);
        Picasso.with(mContext).load(advertise_model.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        holder.title.setText(advertise_model.getTitle());
        holder.price.setText(advertise_model.getPrice());
        holder.date.setText(advertise_model.getDate());

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mAdvertise_mlist != null) && (mAdvertise_mlist.size() != 0) ? mAdvertise_mlist.size() : 0);
    }

    void LoadnewData(List<Advertise_Model> newAd){
        mAdvertise_mlist = newAd;
        notifyDataSetChanged();
    }

    public Advertise_Model getAdvertise(int position){
        return ((mAdvertise_mlist != null) && (mAdvertise_mlist.size() != 0) ? mAdvertise_mlist.get(position) : null);
    }

    static class  Recycle_view_Holder extends RecyclerView.ViewHolder{
        private static final String TAG = "Recycle_view_Holder";
        ImageView image = null;
        Custom_Text_View title = null;
        Custom_Text_View price = null;
        Custom_Text_View date = null;

        public Recycle_view_Holder(View itemView) {
            super(itemView);
            Log.d(TAG, "Recycle_view_Holder: starts");
            this.image = (ImageView) itemView.findViewById(R.id.mainpage_recycle_image);
            this.title = (Custom_Text_View) itemView.findViewById(R.id.mainpage_recycle_title);
            this.price = (Custom_Text_View) itemView.findViewById(R.id.mainpage_recycle_price);
            this.date = (Custom_Text_View) itemView.findViewById(R.id.mainpage_recycle_date);
        }
    }
    
}
