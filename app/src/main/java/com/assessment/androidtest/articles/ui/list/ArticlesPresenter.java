package com.assessment.androidtest.articles.ui.list;

import com.assessment.androidtest.articles.model.ArticlesRepository;
import com.assessment.androidtest.articles.model.Article;
import com.assessment.androidtest.common.BasePresenter;
import com.assessment.androidtest.common.BasePresenterView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


public class ArticlesPresenter extends BasePresenter<ArticlesPresenter.View> {

    private final Scheduler uiScheduler;
    private final Scheduler ioScheduler;
    private final ArticlesRepository articlesRepository;

    public ArticlesPresenter(Scheduler uiScheduler, Scheduler ioScheduler, ArticlesRepository articlesRepository) {
        this.uiScheduler = uiScheduler;
        this.ioScheduler = ioScheduler;
        this.articlesRepository = articlesRepository;
    }

    private List<Article> articles = null;

    @Override
    public void register(View view) {
        super.register(view);

        addToUnsubscribe(view.onRefreshAction()
                .doOnNext(ignored -> {
                    view.showRefreshing(true);
                    view.hideErrorMessage();
                })
                .switchMapSingle(ignored -> articlesRepository.latestFintechArticles().subscribeOn(ioScheduler))
                .observeOn(uiScheduler)
                .map(articles -> {
                    this.articles = articles;
                    return adjustArticles(articles);
                })
                .subscribe(resp -> {
                    view.showArticles(articles);
                    view.showRefreshing(false);
                }, error -> {
                    view.showRefreshing(false);
                    view.showErrorMessage("Something went wrong, please try again in a bit!", articles != null);
                }));

        addToUnsubscribe(view.onArticleClicked().subscribe(view::openArticleDetail));
        addToUnsubscribe(view.onRefreshView().subscribeOn(ioScheduler).observeOn(uiScheduler).subscribe(it -> {
            if (articles != null)
                view.showArticles(adjustArticles(articles));
        }));
    }

    private List<Article> adjustArticles(List<Article> articles) {
        return markArticlesAsDividers(sortArticles(markArticleAsFavourites(articles)));
    }

    private List<Article> markArticleAsFavourites(List<Article> articles) {
        AtomicReference<List<Article>> mapped = new AtomicReference<>();
        articlesRepository.getFavouriteArticles().subscribe(favourites -> {
            for (Article article : articles) {
                article.setFavourite(false);
                for (String favouriteId : favourites)
                    if (article.getId().equals(favouriteId))
                        article.setFavourite(true);
            }
            mapped.set(articles);
        });
        return mapped.get();
    }

    private List<Article> sortArticles(List<Article> articles) {
        Collections.sort(articles, (o1, o2) -> {
            int i = Boolean.compare(o2.isFavourite(), o1.isFavourite());
            if (i == 0)
                i = o2.getPublished().compareTo(o1.getPublished());
            return i;
        });
        return articles;
    }

    private List<Article> markArticlesAsDividers(List<Article> articles) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            article.setDividerText("");
            if (article.isFavourite() && i == 0)
                article.setDividerText("Favourites");
            else if (i == 0)
                article.setDividerText(sdf.format(article.getPublished()));
            else if (!article.isFavourite()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(article.getPublished());
                Article prev = articles.get(i - 1);
                Calendar prevCal = Calendar.getInstance();
                prevCal.setTime(prev.getPublished());
                if (prev.isFavourite() ||
                        (cal.get(Calendar.DAY_OF_YEAR) < prevCal.get(Calendar.DAY_OF_YEAR) ||
                                cal.get(Calendar.YEAR) < prevCal.get(Calendar.YEAR))) {
                    article.setDividerText(sdf.format(article.getPublished()));
                }
            }
        }
        return articles;
    }

    public interface View extends BasePresenterView {
        void showRefreshing(boolean isRefreshing);

        void showArticles(List<Article> articles);

        void showErrorMessage(String message, boolean asToast);

        void hideErrorMessage();

        void openArticleDetail(Article article);

        Observable<Article> onArticleClicked();

        Observable<Object> onRefreshAction();

        Observable<Object> onRefreshView();
    }
}