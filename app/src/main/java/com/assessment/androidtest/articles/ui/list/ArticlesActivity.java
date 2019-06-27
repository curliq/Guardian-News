package com.assessment.androidtest.articles.ui.list;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.assessment.androidtest.HeadlinesApp;
import com.assessment.androidtest.R;
import com.assessment.androidtest.articles.ui.detail.ArticleDetailActivity;
import com.assessment.androidtest.articles.model.Article;
import com.assessment.androidtest.common.BaseActivity;
import com.assessment.androidtest.common.Event;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;


public class ArticlesActivity extends BaseActivity implements ArticlesPresenter.View {

    @BindView(R.id.activityArticlesList_root_srf)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.activityArticlesList_list_rv)
    RecyclerView recyclerView;
    @BindView(R.id.activityArticlesList_errorMessage_tv)
    TextView errorMessageTv;

    private ArticlesPresenter presenter;
    private ArticleAdapter adapter;
    private EventsListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);

        adapter = new ArticleAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        recyclerView.addItemDecoration(dividerDecorator);
        presenter = HeadlinesApp.from(getApplicationContext()).inject(this);
        presenter.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (eventListener != null)
            eventListener.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.unregister();
        super.onDestroy();
    }

    @Override
    public void showArticles(List<Article> articles) {
        adapter.showArticles(articles);
    }

    @Override
    public Observable<Article> onArticleClicked() {
        return Observable.create(emitter -> adapter.setEventListener(emitter::onNext));
    }

    @Override
    public Observable<Object> onRefreshAction() {
        return Observable.create(emitter -> {
            swipeRefreshLayout.setOnRefreshListener(() -> emitter.onNext(true));
            emitter.setCancellable(() -> swipeRefreshLayout.setOnRefreshListener(null));
        }).startWith(Event.IGNORE);
    }

    @Override
    public Observable<Object> onRefreshView() {
        return Observable.create(emitter -> eventListener = () -> emitter.onNext(true));
    }

    @Override
    public void openArticleDetail(Article article) {
        startActivity(ArticleDetailActivity.createIntent(this, article));
    }

    @Override
    public void showRefreshing(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void showErrorMessage(String message, boolean asToast) {
        if (asToast) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            errorMessageTv.setVisibility(View.VISIBLE);
            errorMessageTv.setText(message);
        }
    }

    @Override
    public void hideErrorMessage() {
        errorMessageTv.setVisibility(View.GONE);
    }

    interface EventsListener {
        void onResume();
    }

}
