package com.assessment.androidtest.articles.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.assessment.androidtest.HeadlinesApp;
import com.assessment.androidtest.R;
import com.assessment.androidtest.articles.model.Article;
import com.assessment.androidtest.common.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class ArticleDetailActivity extends BaseActivity implements ArticleDetailPresenter.View {

    private final static String ARG_ARTICLE_KEY = "article";

    public static Intent createIntent(Context context, Article article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.ARG_ARTICLE_KEY, article);
        return intent;
    }

    @BindView(R.id.activityArticleDetail_headerImage_iv)
    ImageView headerIv;
    @BindView(R.id.activityArticleDetail_backArrow_ib)
    ImageButton backArrowIb;
    @BindView(R.id.activityArticleDetail_share_ib)
    ImageButton shareIb;
    @BindView(R.id.activityArticleDetail_favourite_iv)
    ImageView favouriteIv;
    @BindView(R.id.activityArticleDetail_title_tv)
    TextView titleTv;
    @BindView(R.id.activityArticleDetail_body_tv)
    TextView bodyTv;
    @BindView(R.id.activityArticleDetail_date_tv)
    TextView dateTv;
    @BindView(R.id.activityArticleDetail_loading_pb)
    ProgressBar loadingPb;

    private ArticleDetailPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        presenter = HeadlinesApp.from(getApplicationContext()).inject(
                getIntent().getParcelableExtra(ARG_ARTICLE_KEY),
                this
        );
        presenter.register(this);
    }

    @Override
    protected void onDestroy() {
        presenter.unregister();
        super.onDestroy();
    }

    @Override
    public void showLoading(boolean isLoading) {
        loadingPb.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPublishedDate(String date) {
        dateTv.setText(date);
    }

    @Override
    public void showHeadline(String headline) {
        titleTv.setText(headline);
    }

    @Override
    public void showBody(String body) {
        bodyTv.setText(body);
    }

    @Override
    public void showThumbnail(String url) {
        Glide.with(this).load(url).into(headerIv);
    }

    @Override
    public void markAsFavourite(boolean marked) {
        favouriteIv.setImageDrawable(
                ContextCompat.getDrawable(this, marked ? R.drawable.ic_star_filled : R.drawable.ic_star)
        );
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void openShareIntent(String shareMessage) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(sharingIntent);
    }

    @Override
    public Observable<Object> onArticleMarkedFavourite() {
        return Observable.create(emitter -> favouriteIv.setOnClickListener(v -> emitter.onNext(true)));
    }

    @Override
    public Observable<Object> onArticleShared() {
        return Observable.create(emitter -> shareIb.setOnClickListener(v -> emitter.onNext(true)));
    }

    @Override
    public Observable<Object> onBackArrowPressed() {
        return Observable.create(emitter -> backArrowIb.setOnClickListener(v -> emitter.onNext(true)));
    }

}
