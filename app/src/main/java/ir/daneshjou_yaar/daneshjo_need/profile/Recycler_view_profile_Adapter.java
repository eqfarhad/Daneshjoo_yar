package ir.daneshjou_yaar.daneshjo_need.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;

/**
 * Created by iqfarhad on 2/15/2018.
 */

public class Recycler_view_profile_Adapter extends RecyclerView.Adapter<Recycler_view_profile_Adapter.Recycle_view_Holder> {

    private static final String TAG = "ReV_profile_Adapter";
    private Context mContext;
    private ArrayList mArrayList_name;
    private ArrayList mArrayList_image;

    public Recycler_view_profile_Adapter(Context context, ArrayList arrayList_name, ArrayList arrayList_image) {
        mContext = context;
        mArrayList_name = arrayList_name;
        mArrayList_image = arrayList_image;
    }



    @Override
    public Recycler_view_profile_Adapter.Recycle_view_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_daneshjo_need_profile_recycler_item,parent , false);
        return new Recycle_view_Holder(view);
    }

    @Override
    public void onBindViewHolder(Recycler_view_profile_Adapter.Recycle_view_Holder holder, int position) {
        holder.title.setText(mArrayList_name.get(position).toString());
        holder.image.setImageResource((Integer) mArrayList_image.get(position));

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return mArrayList_image.size();
    }

    static class  Recycle_view_Holder extends RecyclerView.ViewHolder{
        private static final String TAG = "Recycle_view_Holder";
        ImageView image = null;
        Custom_Text_View title = null;

        public Recycle_view_Holder(View itemView) {
            super(itemView);
            Log.d(TAG, "Recycle_view_Holder: starts");
            this.image = (ImageView) itemView.findViewById(R.id.fragment_profile_recyclerview_image);
            this.title = (Custom_Text_View) itemView.findViewById(R.id.fragment_profile_recyclerview_title);
        }
    }

}
