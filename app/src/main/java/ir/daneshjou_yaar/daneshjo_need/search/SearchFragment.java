package ir.daneshjou_yaar.daneshjo_need.search;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ir.daneshjou_yaar.daneshjo_need.mainpage.PaginationAdapter;
import ir.daneshjou_yaar.daneshjo_need.mainpage.PaginationAdapterCallback;
import ir.daneshjou_yaar.daneshjo_need.mainpage.PaginationScrollListener;
import ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail.Mainpage_Activity_Detail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iqfarhad on 1/28/2018.
 */

public class SearchFragment extends Fragment implements  Recycler_Item_Click_Listener.OnRecyclerClickListener ,PaginationAdapterCallback {
    private static final String TAG = "SearchFragment";

    private SearchView mSearchView;
    private String searched_word;

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

    //-------test progress detail loading
        ProgressBar mProgressBar_loading;


//------------------------------------

    public static SearchFragment newInstance() {
        SearchFragment fm = new SearchFragment();
        return fm;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        currentPage = PAGE_START;
        isLoading = false;
        isLastPage = false;
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        View v = inflater.inflate(R.layout.fragment_daneshjo_need_search, container, false);

        mSearchView = (SearchView) v.findViewById(R.id.fragment_search_search);

        errorLayout = (LinearLayout) v.findViewById(R.id.fragment_search_error_layout);
        btnRetry = (Button) v.findViewById(R.id.fragment_search_error_btn_retry);
        txtError = (Custom_Text_View) v.findViewById(R.id.fragment_search_error_txt_cause);

        progressBar = (ProgressBar) v.findViewById(R.id.fragment_search_progressbar);
        progressBar.setVisibility(View.GONE);

        rv = (RecyclerView) v.findViewById(R.id.fragment_search_recycleview);
        rv.setVisibility(View.GONE);

        mProgressBar_loading = (ProgressBar) v.findViewById(R.id.mainpage_recycle_loading_detail);

        //---------------------------dorost kardan font o hint searchview---------------------------
        final Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(),"IRKamran.ttf");
        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.icons));
        searchAutoComplete.setTypeface(myCustomFont);
        searchAutoComplete.setTextSize(22);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.icons));

        mSearchView.setQueryHint("متن مورد نیاز خود را بنویسید");
        mSearchView.clearFocus();

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchView.clearFocus();
            }
        }, 30);*/

    /*    final TextView searchText = (TextView)
                mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchText.setTypeface(myCustomFont);
        searchText.setTextSize(22);
        searchText.setTextColor(getResources().getColor(R.color.icons));
        searchText.setHintTextColor(getResources().getColor(R.color.icons));
*/

        /*searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    searchText.setHint("");
                else
                    mSearchView.setQueryHint("متن مورد نیاز خود را بنویسید");
            }
        });*/

        //------------------------------item click listener--------
        rv.addOnItemTouchListener(new Recycler_Item_Click_Listener(getActivity() , rv , this));
       /* Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
            searched_word = query;
        }*/

       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               searched_word = mSearchView.getQuery().toString();
              // Toast.makeText(getActivity(), searched_word, Toast.LENGTH_SHORT).show();
               adapter.clear();
               progressBar.setVisibility(View.VISIBLE);
               loadFirstPage();
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });

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

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });




        return v;
    }



    @Override
    public void onItemClick(View view, int position) {

        Log.d(TAG, "onItemClick: starts");
       // Toast.makeText(getActivity(), "item Clicked :" + position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: "+ position);
        Log.d(TAG, "onItemClick: "+adapter.getItemCount());
        Log.d(TAG, "onItemClick: "+view.getId());


        if (position == adapter.getItemCount()){
            adapter.showRetry(false, null);
            retryPageLoad();
        }else{

            //view.findViewById(R.id.mainpage_recycle_loading_detail).setVisibility(View.VISIBLE);
           // View progressBar_detail = rv.getRootView().findViewById(R.id.mainpage_recycle_loading_detail);
           // progressBar_detail.setVisibility(View.VISIBLE);
       //     mProgressBar_loading.setVisibility(View.VISIBLE);
            ImageView imageView = view.findViewById(R.id.mainpage_recycle_image);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            //TODO: 5/9/2018  progressbar ro check konim kar mikone
            Mainpage_Activity_Detail.navigate(getActivity(),byteArray , view.findViewById(R.id.mainpage_recycle_image), adapter.getads(position),Boolean.FALSE);


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
                searched_word,
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



