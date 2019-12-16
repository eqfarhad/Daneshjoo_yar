package ir.daneshjou_yaar.daneshjo_need.mainpage;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by iqfarhad on 3/7/2018.
 */

class Recycler_Item_Click_Listener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecycleItemClickListene";

    interface OnRecyclerClickListener {
        void onItemClick(View view , int position);
        void OnItemLongClick(View view , int position);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    public Recycler_Item_Click_Listener(Context context , final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetectorCompat(context , new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childview = recyclerView.findChildViewUnder(e.getX() , e.getY());
                if (childview != null && mListener != null){
                    Log.d(TAG, "onSingleTapUp: calling listener.onItemClick");
                    mListener.onItemClick(childview , recyclerView.getChildAdapterPosition(childview));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View childview = recyclerView.findChildViewUnder(e.getX() , e.getY());
                if (childview != null && mListener != null){
                    Log.d(TAG, "onLongPress: calling listener.OnItemLongClick");
                    mListener.OnItemLongClick(childview , recyclerView.getChildAdapterPosition(childview));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if (mGestureDetector != null){
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned" +  result);
            return result;
        }else {
            Log.d(TAG, "onInterceptTouchEvent: returned false");
            return false;
        }
    }
}
