package ir.daneshjou_yaar.daneshjo_need.mainpage;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.daneshjo_need.DownloadStatus;
import ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail.Mainpage_Activity_Detail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iqfarhad on 1/28/2018.
 */

public class MainpageFragment extends Fragment implements  Mainpage_Get_JSON_Data.onDataAvailable , Recycler_Item_Click_Listener.OnRecyclerClickListener ,PaginationAdapterCallback{
    public static final String TAG = "MainpageFragment";
    private Recycle_view_Adapter mRecycle_view_adapter;
    private ProgressBar mProgressBar;
    public static int page = 0;

    //---------------------pagination------------

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 200;
    private int currentPage = PAGE_START;

    private Ads_Service mAdsService;

    //error handling
    LinearLayout errorLayout;
    Button btnRetry;
    Custom_Text_View txtError;

    ProgressBar mProgressBar_loading;

//------------------------------------

    public static MainpageFragment newInstance() {
        MainpageFragment fm = new MainpageFragment();
        return fm;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        final View v = inflater.inflate(R.layout.fragment_daneshjo_need_mainpage, container, false);

       //test mProgressBar = (ProgressBar) v.findViewById(R.id.fragment_mainpage_progressbar);
       //test mProgressBar.setVisibility(View.VISIBLE);

        errorLayout = (LinearLayout) v.findViewById(R.id.fragment_main_error_layout);
        btnRetry = (Button) v.findViewById(R.id.fragment_main_error_btn_retry);
        txtError = (Custom_Text_View) v.findViewById(R.id.fragment_main_error_txt_cause);

        progressBar = (ProgressBar) v.findViewById(R.id.fragment_mainpage_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        rv = (RecyclerView) v.findViewById(R.id.fragment_mainpage_recycleview);
        rv.setVisibility(View.GONE);
     //test   RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_mainpage_recycleview);
      //test  mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgressBar_loading = (ProgressBar) v.findViewById(R.id.mainpage_recycle_loading_detail);

     //test   mRecycle_view_adapter = new Recycle_view_Adapter( new ArrayList<Advertise_Model>() , getActivity());
     //test   mRecyclerView.setAdapter(mRecycle_view_adapter);
//------------------------------item click listener--------
       //test mRecyclerView.addOnItemTouchListener(new Recycler_Item_Click_Listener(getActivity() , mRecyclerView , this));
        rv.addOnItemTouchListener(new Recycler_Item_Click_Listener(getActivity() , rv , this));

//------------------------------------# lazy laod #---------------------------------------------------

       /* mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: starts");

                //lazy load MONDE !!!!!!!!!!!!!!!!!!!!
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });*/

       //---------------------------------------pagination-----------------------------------------------------------------------
        adapter = new PaginationAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);


        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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


       mAdsService = Ads_api.getClient().create(Ads_Service.class);

       loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        currentPage = PAGE_START;
        isLoading = false;
        isLastPage = false;
        Log.d(TAG, "onResume: starts");
        super.onResume();

/*test
            Mainpage_Get_JSON_Data categoriesGet_json_data = new Mainpage_Get_JSON_Data(this,"http://eqtech.ir/get_data.php",page , true);
            // categoriesGet_json_data.executeOnSameThread(null);
            categoriesGet_json_data.execute("");*/
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(List<Advertise_Model> data, DownloadStatus status) {
      /*test  Log.d(TAG, "onDataAvailable : starts");

        if (status == DownloadStatus.OK){
            mRecycle_view_adapter.LoadnewData(data);
            mProgressBar.setVisibility(View.GONE);
        }else{
            //download or processing failed
            Log.e(TAG, "onDataAvailavle: failed with status" + status );
        }

        Log.d(TAG, "onDataAvailable: ends");*/
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
       // Toast.makeText(getActivity(), "item Clicked :" + position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: "+ position);
        Log.d(TAG, "onItemClick: "+adapter.getItemCount());
        Log.d(TAG, "onItemClick: "+view.getId());


        if (position+1 == adapter.getItemCount()){
                adapter.showRetry(false, null);
                retryPageLoad();
        }else{

            //view.findViewById(R.id.mainpage_recycle_loading_detail).setVisibility(View.VISIBLE);
           // View progressBar_detail = rv.getRootView().findViewById(R.id.mainpage_recycle_loading_detail);
           // progressBar_detail.setVisibility(View.VISIBLE);
//            mProgressBar_loading.setVisibility(View.VISIBLE);

            ImageView imageView = view.findViewById(R.id.mainpage_recycle_image);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();



            if (imageView.getVisibility() == View.VISIBLE){
                Mainpage_Activity_Detail.navigate(getActivity(), byteArray , view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position) , Boolean.FALSE);
            }else{

               // Toast.makeText(getActivity(), "در حال بارگزاری ...", Toast.LENGTH_SHORT).show();
                new StyleableToast.Builder(getActivity()).text("در حال بارگزاری...").textColor(Color.WHITE).backgroundColor(getActivity().getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();

            }
            //TODO: 5/9/2018  progressbar ro check konim kar mikone


        }

    }

    @Override
    public void OnItemLongClick(View view, int position) {
        Log.d(TAG, "OnItemLongClick: starts");
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
                currentPage );
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
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }
}
