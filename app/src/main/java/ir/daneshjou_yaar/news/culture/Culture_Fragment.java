package ir.daneshjou_yaar.news.culture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.news.GetDataFromWeb;
import ir.daneshjou_yaar.news.News;
import ir.daneshjou_yaar.news.News_api;
import ir.daneshjou_yaar.news.PaginationAdapter;
import ir.daneshjou_yaar.news.PaginationAdapterCallback;
import ir.daneshjou_yaar.news.PaginationScrollListener;
import ir.daneshjou_yaar.news.news_detail.News_Detail_Activity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Culture_Fragment extends Fragment implements Recycler_Item_Click_Listener.OnRecyclerClickListener ,PaginationAdapterCallback {
    private static final String TAG = "Culture_Fragment";


    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 200;
    private int currentPage = PAGE_START;

    private News_Culture_Service mNews_culture_service;

    //error handling
    LinearLayout errorLayout;
    Button btnRetry;
    Custom_Text_View txtError;

    ProgressBar mProgressBar_loading;


    public static Culture_Fragment newInstance() {
        Culture_Fragment fm = new Culture_Fragment();
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        final View v = inflater.inflate(R.layout.fragment_generalnews, container, false);

        errorLayout = (LinearLayout) v.findViewById(R.id.fragment_news_general_error_layout);
        btnRetry = (Button) v.findViewById(R.id.fragment_news_general_error_btn_retry);
        txtError = (Custom_Text_View) v.findViewById(R.id.fragment_news_general_error_txt_cause);

        progressBar = (ProgressBar) v.findViewById(R.id.fragment_news_general_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        rv = (RecyclerView) v.findViewById(R.id.fragment_news_general_recycleview);
        rv.setVisibility(View.GONE);

        //------------------------------item click listener--------
        rv.addOnItemTouchListener(new Recycler_Item_Click_Listener( getActivity() , rv , this));

        //---------------------------------------pagination-----------------------------------------------------------------------
        adapter = new PaginationAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new MaterialViewPagerHeaderDecorator());

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


        mNews_culture_service = News_api.getClient().create(News_Culture_Service.class);

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
    }



    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        //Toast.makeText(getActivity(), "item Clicked :" + position, Toast.LENGTH_SHORT).show();
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
            ImageView imageView;

            /*if (position == 0){// bekhater inke item headeremoon ba baghie fargh dare
                imageView = view.findViewById(R.id.news_recycle_image_header);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                if (imageView.getVisibility() == View.VISIBLE){
                    News_Detail_Activity.navigate(getActivity(), bitmap , view.findViewById(R.id.news_recycle_image_header), adapter.getnews(position));
                }else{

                    Toast.makeText(getActivity(), "در حال بارگزاری ...", Toast.LENGTH_SHORT).show();
                }

            }else {*/
            imageView = view.findViewById(R.id.news_recycle_image);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            if (imageView.getVisibility() == View.VISIBLE){
                News_Detail_Activity.navigate(getActivity(), byteArray , view.findViewById(R.id.news_recycle_image), adapter.getnews(position));
            }else{

                //Toast.makeText(getActivity(), "در حال بارگزاری ...", Toast.LENGTH_SHORT).show();
                new StyleableToast.Builder(getActivity()).text("در حال بارگزاری ...").textColor(Color.WHITE).backgroundColor(getActivity().getResources().getColor(R.color.colorPrimary)).cornerRadius(5).show();

            }
            //}

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
                List<News> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                adapter.addAll(results);
                TOTAL_PAGES = response.body().getTotolPage();

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

                List<News> results = fetchResults(response);
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

    private List<News> fetchResults(Response<GetDataFromWeb> response) {
        GetDataFromWeb complete_news = response.body();
        return complete_news.getNews();
    }

    private Call<GetDataFromWeb> callGET_ADS_sApi() {
        return mNews_culture_service.GET_DATA_FROM_WEB_CALL(currentPage);
    }

   /* private Call<GetDataFromWeb> callGET_NEWS_slider_sApi() {
        return mNews_slider_service.GET_DATA_FROM_WEB_CALL(slidernum);
    }*/

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
