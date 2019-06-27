package com.assessment.androidtest.articles.ui.detail;

import android.os.Build;
import android.text.Html;

import com.assessment.androidtest.articles.model.ArticlesRepository;
import com.assessment.androidtest.articles.model.Article;
import com.assessment.androidtest.common.BasePresenter;
import com.assessment.androidtest.common.BasePresenterView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


public class ArticleDetailPresenter extends BasePresenter<ArticleDetailPresenter.View> {

    private Article article;
    private final Scheduler uiScheduler;
    private final Scheduler ioScheduler;
    private final ArticlesRepository articlesRepository;

    public ArticleDetailPresenter(Article article,
                                  Scheduler uiScheduler,
                                  Scheduler ioScheduler,
                                  ArticlesRepository articlesRepository) {
        this.article = article;
        this.uiScheduler = uiScheduler;
        this.ioScheduler = ioScheduler;
        this.articlesRepository = articlesRepository;
    }

    @Override
    public void register(View view) {
        super.register(view);

        view.showThumbnail(article.getThumbnail());
        view.showHeadline(article.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy 'at' hh:mm'h'", Locale.UK);
        view.showPublishedDate("Published on " + sdf.format(article.getPublished()));
        view.markAsFavourite(article.isFavourite());

        if (!article.getBody().isEmpty()) {
            view.showBody(article.getBody());
        } else {
            view.showLoading(true);
            addToUnsubscribe(articlesRepository.getArticle(article.getUrl())
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe(response -> {
                        view.showLoading(false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            view.showBody(Html.fromHtml(response.getBody(), Html.FROM_HTML_MODE_COMPACT).toString());
                        else
                            view.showBody(Html.fromHtml(response.getBody()).toString());
                    }, error -> {
                        error.printStackTrace();
                        view.showLoading(false);
                        view.showBody("An error happened loading this article, please try again later!");
                    }));
        }
        addToUnsubscribe(view.onArticleMarkedFavourite().subscribe(val -> {
            article.setFavourite(!article.isFavourite());
            view.markAsFavourite(article.isFavourite());
            addToUnsubscribe(articlesRepository.toggleArticleAsFavourites(article, article.isFavourite()).subscribe());
        }));
        addToUnsubscribe(view.onArticleShared().subscribe(val -> {
            view.openShareIntent("Hot take: " + article.getUrl());
        }));
        addToUnsubscribe(view.onBackArrowPressed().subscribe(val -> view.goBack()));
    }

    interface View extends BasePresenterView {

        void showLoading(boolean isLoading);

        void showPublishedDate(String date);

        void showHeadline(String headline);

        void showBody(String body);

        void showThumbnail(String url);

        void markAsFavourite(boolean marked);

        void goBack();

        void openShareIntent(String shareMessage);

        Observable<Object> onArticleMarkedFavourite();

        Observable<Object> onArticleShared();

        Observable<Object> onBackArrowPressed();
    }
}