package ir.daneshjou_yaar.daneshjo_need.categories.Category_Choosed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ir.daneshjou_yaar.daneshjo_need.mainpage.GetDataFromWeb;
import ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail.Mainpage_Activity_Detail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Category_Choosed_Activity extends AppCompatActivity implements Recycler_Item_Click_Listener.OnRecyclerClickListener , Category_PaginationAdapterCallback {
    private static final String TAG = "Category_Choosed_Activi";

    //---------------------pagination------------

    Category_PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 200;
    private int currentPage = PAGE_START;
    private int currentCat = 1;

    private Category_Ads_Service mAdsService;

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
        setContentView(R.layout.activity_category_choosed);

        Intent a=getIntent();
        //currentPage = Integer.parseInt(a.getStringExtra("cat"));
        currentCat = Integer.parseInt(a.getStringExtra("cat"));


        errorLayout = (LinearLayout) findViewById(R.id.category_main_error_layout);
        btnRetry = (Button) findViewById(R.id.category_main_error_btn_retry);
        txtError = (Custom_Text_View) findViewById(R.id.category_main_error_txt_cause);

        progressBar = (ProgressBar) findViewById(R.id.category_mainpage_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        rv = (RecyclerView) findViewById(R.id.category_mainpage_recycleview);
        rv.setVisibility(View.GONE);

        mProgressBar_loading = (ProgressBar) findViewById(R.id.mainpage_recycle_loading_detail);



        //------------------------------item click listener--------
        //test mRecyclerView.addOnItemTouchListener(new Recycler_Item_Click_Listener(getActivity() , mRecyclerView , this));
        rv.addOnItemTouchListener(new Recycler_Item_Click_Listener(this , rv , this));

        //---------------------------------------pagination-----------------------------------------------------------------------
        adapter = new Category_PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);


        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);


        rv.addOnScrollListener(new Category_PaginationScrollListener(linearLayoutManager) {
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


        mAdsService = Category_Ads_api.getClient().create(Category_Ads_Service.class);

        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });


    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    @Override
    public void onItemClick(View view, int position) {

        Log.d(TAG, "onItemClick: starts");
       // Toast.makeText(this, "item Clicked :" + position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: "+ position);
        Log.d(TAG, "onItemClick: "+adapter.getItemCount());
        Log.d(TAG, "onItemClick: "+view.getId());
        Log.d(TAG, "onItemClick: +"+R.id.loadmore_errorlayout);

        if (position+1 == adapter.getItemCount()){
            adapter.showRetry(false, null);
            retryPageLoad();
        }else{
          //  View progressBar_detail = rv.getRootView().findViewById(R.id.mainpage_recycle_loading_detail);
         //   progressBar_detail.setVisibility(View.VISIBLE);
          //  mProgressBar_loading.setVisibility(View.VISIBLE);
            ImageView imageView = view.findViewById(R.id.mainpage_recycle_image);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            //Mainpage_Activity_Detail.navigate(this, view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position));
            //Mainpage_Activity_Detail.navigate(this, progressBar_detail , view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position));
            Mainpage_Activity_Detail.navigate(this, byteArray , view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position) ,Boolean.FALSE);

        }

    }

    @Override
    public void OnItemLongClick(View view, int position) {

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

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
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
                currentCat,currentPage );
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


}
