package ir.daneshjou_yaar.news.university;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import ir.daneshjou_yaar.Custom_Text_View;
import ir.daneshjou_yaar.R;
import ir.daneshjou_yaar.news.PaginationAdapterCallback;

import static android.content.ContentValues.TAG;

/**
 * Created by iqfarhad on 4/13/2018.
 */

public class PaginationAdapter_uninews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HEADER = 2;
    private static final String BASE_URL_IMG = "";

    private List<News_uni> news;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private String errorMsg;

    private PaginationAdapterCallback mCallback;


    public PaginationAdapter_uninews(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        news = new ArrayList<>();
    }

    public News_uni getnews(int position) {
        return news.get(position);
    }

    public void setAdss(List<News_uni> news) {
        this.news = news;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER: {
                viewHolder = getViewHolder_Header(parent , inflater);
                break;
            }
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.news_daneshjo_need_mainpage_item_recycle_view, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder_Header(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.list_item_card_big_news, parent, false);
        viewHolder = new HeaderVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        News_uni news_model = news.get(position);
        Log.d(TAG, "onBindViewHolder: "+ news_model.getTitle() + "-> "+ position);

        switch (getItemViewType(position)) {
            case HEADER:
                final HeaderVH headerVH = (HeaderVH) holder;
                Glide
                        .with(context)
                        .load(news_model.getImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //  adsVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                headerVH.mProgress.setVisibility(View.GONE);
                                headerVH.image.setVisibility(View.VISIBLE);
                                return false;
                            }

                        }).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform().override(512 , 512))
                        .into(headerVH.image);

                headerVH.title.setText(news_model.getTitle());
                headerVH.description.setText(news_model.getDescription());
                headerVH.date.setText(news_model.getDate());
                break;

            case ITEM:
                final MovieVH adsVH = (MovieVH) holder;
                /*Picasso.with(context).load(ad_model.getImage())
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(adsVH.image);*/

                Glide
                    .with(context)
                    .load(BASE_URL_IMG+news_model.getImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                          //  adsVH.mProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            adsVH.mProgress.setVisibility(View.GONE);
                            adsVH.image.setVisibility(View.VISIBLE);
                            return false;
                        }

                    }).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform().override(512 , 512))
                        .into(adsVH.image);

                adsVH.title.setText(news_model.getTitle());
                adsVH.date.setText(news_model.getDate());
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                //Do nothing
                break;
        }

/*
        Movie movie = movies.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                AdvertiseVH movieVH = (AdvertiseVH) holder;

                movieVH.textView.setText(movie.getTitle());
                break;
            case LOADING:
//                Do nothing
                break;
        }*/

    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        return (position == news.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(News_uni mc) {
        news.add(mc);
        notifyItemInserted(news.size() - 1);
    }

    public void addAll(List<News_uni> mcList) {
        for (News_uni mc : mcList) {
            add(mc);
        }
    }

    public void remove(News_uni city) {
        int position = news.indexOf(city);
        if (position > -1) {
            news.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new News_uni());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = news.size() - 1;
        News_uni item = getItem(position);

        if (item != null) {
            news.remove(position);
            notifyItemRemoved(position);
        }
    }

    public News_uni getItem(int position) {
        return news.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(news.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {


        /*private TextView textView;

        public AdvertiseVH(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.item_text);
        }*/

        private static final String TAG = "AdvertiseVH";
        ImageView image = null;
        Custom_Text_View title = null;
        Custom_Text_View price = null;
        Custom_Text_View date = null;
        ProgressBar mProgress = null;
        ProgressBar mProgress_detail = null;

        public MovieVH(View itemView) {
            super(itemView);
            Log.d(TAG, "Recycle_view_Holder: starts");
            this.image = (ImageView) itemView.findViewById(R.id.news_recycle_image);
            this.title = (Custom_Text_View) itemView.findViewById(R.id.news_mainpage_recycle_title);
            this.date = (Custom_Text_View) itemView.findViewById(R.id.news_mainpage_recycle_date);
            this.mProgress = (ProgressBar) itemView.findViewById(R.id.progressBar_mainpage_news);
            this.mProgress_detail = (ProgressBar) itemView.findViewById(R.id.news_mainpage_recycle_loading_detail);
        }
    }

    protected  class HeaderVH extends RecyclerView.ViewHolder{
        private static final String TAG = "HeaderVH";

        ImageView image = null;
        Custom_Text_View title = null;
        Custom_Text_View description = null;
        Custom_Text_View date = null;
        ProgressBar mProgress = null;
        ProgressBar mProgress_detail = null;

        public HeaderVH(View itemView) {
            super(itemView);
            Log.d(TAG, "HeaderVH: starts");
            this.image = (ImageView) itemView.findViewById(R.id.news_recycle_image_header);
            this.title = (Custom_Text_View) itemView.findViewById(R.id.news_mainpage_recycle_title_header);
            this.description = (Custom_Text_View) itemView.findViewById(R.id.news_mainpage_recycle_description_header);
            this.date = (Custom_Text_View) itemView.findViewById(R.id.news_mainpage_recycle_date_header);
            this.mProgress = (ProgressBar) itemView.findViewById(R.id.progressBar_mainpage_news_header);
            this.mProgress_detail = (ProgressBar) itemView.findViewById(R.id.news_mainpage_recycle_loading_detail_header);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder  {

        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);
/*
            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);*/
        }

       /* @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }*/
    }


}