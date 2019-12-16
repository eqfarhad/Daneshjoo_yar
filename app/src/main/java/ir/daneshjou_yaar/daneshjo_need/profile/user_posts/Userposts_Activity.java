package ir.daneshjou_yaar.daneshjo_need.profile.user_posts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.mainpage.Ad;
import ir.daneshjou_yaar.daneshjo_need.mainpage.Ads_api;
import ir.daneshjou_yaar.daneshjo_need.mainpage.GetDataFromWeb;
import ir.daneshjou_yaar.daneshjo_need.mainpage.PaginationAdapterCallback;
import ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail.Mainpage_Activity_Detail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Userposts_Activity extends AppCompatActivity implements Recycler_Item_Click_Listener.OnRecyclerClickListener , PaginationAdapterCallback ,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private static final String TAG = "Userposts_Activity";

    PaginationAdapter_userposts adapter;
    LinearLayoutManager linearLayoutManager;
    private CoordinatorLayout coordinatorLayout;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 200;
    private int currentPage = PAGE_START;
    private String current_user_id;

    private Ads_Service_Userposts mAdsService;

    //--------no Ad handling ---------------

    LinearLayout noadLayout;
    Button no_ad_return_btn;


    //error handling
    LinearLayout errorLayout;
    Button btnRetry;
    Custom_Text_View txtError;
    ProgressBar mProgressBar_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userposts);

        final Intent a=getIntent();
        current_user_id = a.getStringExtra("id");

        noadLayout = (LinearLayout) findViewById(R.id.userpost_main_no_ad_linearLayout);
        no_ad_return_btn = (Button) findViewById(R.id.userpost_main_no_ad_btn_return);

        errorLayout = (LinearLayout) findViewById(R.id.userpost_main_error_layout);
        btnRetry = (Button) findViewById(R.id.userpost_main_error_btn_retry);
        txtError = (Custom_Text_View) findViewById(R.id.userpost_main_error_txt_cause);

        progressBar = (ProgressBar) findViewById(R.id.userpost_mainpage_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        rv = (RecyclerView) findViewById(R.id.userpost_mainpage_recycleview);
        rv.setVisibility(View.GONE);

        mProgressBar_loading = (ProgressBar) findViewById(R.id.mainpage_recycle_loading_detail);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.userpost_mainpage_coordinator_layout) ;

        //------------------------------item click listener--------
        //test mRecyclerView.addOnItemTouchListener(new Recycler_Item_Click_Listener(getActivity() , mRecyclerView , this));
        rv.addOnItemTouchListener(new Recycler_Item_Click_Listener(this , rv , this));

        //---------------------------------------pagination-----------------------------------------------------------------------
        adapter = new PaginationAdapter_userposts(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);


        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);


        rv.addOnScrollListener(new PaginationScrollListener_userpost(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        mAdsService = Ads_api.getClient().create(Ads_Service_Userposts.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });

        //--------------------------swipe o delete----------------------------

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(rv);

        //-------------return btn---------------
        no_ad_return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof PaginationAdapter_userposts.AdvertiseVH) {
            // get the removed item name to display it in snack bar
            String name = adapter.getItem(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Ad deletedItem = adapter.getItem(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.remove(adapter.getItem(viewHolder.getAdapterPosition()));

            //removing from database
            String method = "removing_ad";
            BackgroundTask_Removing_Ad backgroundTask_removing_ad = new BackgroundTask_Removing_Ad(Userposts_Activity.this);
            backgroundTask_removing_ad.execute(method , deletedItem.getUserId() , deletedItem.getId() );

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " از آگهی های شما حذف شد!", Snackbar.LENGTH_LONG);
            snackbar.setAction("موفق", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                   // adapter.restoreItem(deletedItem, deletedIndex);
                    Toast.makeText(Userposts_Activity.this, "restore", Toast.LENGTH_SHORT).show();
                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == adapter.getItemCount()){
            adapter.showRetry(false, null);
            retryPageLoad();
        }else{
            //  View progressBar_detail = rv.getRootView().findViewById(R.id.mainpage_recycle_loading_detail);
            //   progressBar_detail.setVisibility(View.VISIBLE);
            //  mProgressBar_loading.setVisibility(View.VISIBLE);
            ImageView imageView = view.findViewById(R.id.profile_userpost_recycle_image);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            //Mainpage_Activity_Detail.navigate(this, view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position));
            //Mainpage_Activity_Detail.navigate(this, progressBar_detail , view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position));

            Mainpage_Activity_Detail.navigate(Userposts_Activity.this, byteArray , view.findViewById(R.id.profile_userpost_recycle_image), adapter.getads(position) , Boolean.TRUE);
        }
    }

    @Override
    public void OnItemLongClick(View view, int position) {
        Toast.makeText(this, "longpress", Toast.LENGTH_SHORT).show();
    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        /*List<Category_Ad> movies = Category_Ad.createMovies(adapter.getItemCount());
        progressBar.setVisibility(View.GONE);
        adapter.addAll(movies);*/
/*
        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/
        callGET_ADS_sApi().enqueue(new Callback<GetDataFromWeb>() {
            @Override
            public void onResponse(Call<GetDataFromWeb> call, Response<GetDataFromWeb> response) {
                Log.d(TAG, "onResponse: starts");
                // Got data. Send it to adapter
                hideErrorView();
                List<Ad> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                adapter.addAll(results);
                TOTAL_PAGES = response.body().getTotolPage();

                if (TOTAL_PAGES==0){
                    noadLayout.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }

                if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<GetDataFromWeb> call, Throwable t) {
                Log.d(TAG, "onFailure: starts");
                t.printStackTrace();
                showErrorView(t);
            }
        });


    }

    private List<Ad> fetchResults(Response<GetDataFromWeb> response) {
        GetDataFromWeb complete_ad = response.body();
        return complete_ad.getAds();
    }

    public void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
       /* List<Category_Ad> movies = Category_Ad.createMovies(adapter.getItemCount());

        adapter.removeLoadingFooter();
        isLoading = false;

        adapter.addAll(movies);

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;*/

        callGET_ADS_sApi().enqueue(new Callback<GetDataFromWeb>() {
            @Override
            public void onResponse(Call<GetDataFromWeb> call, Response<GetDataFromWeb> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Ad> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<GetDataFromWeb> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });

    }

    private Call<GetDataFromWeb> callGET_ADS_sApi() {
        return mAdsService.GET_DATA_FROM_WEB_CALL(
                current_user_id,currentPage );
    }


    //error handling parts :


    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {
        Log.d(TAG, "showErrorView: starts");

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: "+resultCode);
        if (resultCode == 2 ){
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
