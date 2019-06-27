package com.assessment.androidtest.articles.model;


import android.content.SharedPreferences;
import android.content.res.Resources;

import com.assessment.androidtest.R;
import com.assessment.androidtest.api.GuardianService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;


public class ArticlesRepository {

    private final String arraySeperator = " & ";
    private final GuardianService guardianService;
    private final ArticleMapper articleMapper;
    private SharedPreferences prefs;
    private Resources resources;

    public ArticlesRepository(GuardianService service, ArticleMapper mapper, SharedPreferences prefs, Resources res) {
        this.guardianService = service;
        this.articleMapper = mapper;
        this.prefs = prefs;
        this.resources = res;
    }

    public Single<List<Article>> latestFintechArticles() {
        return guardianService.searchArticles("fintech,brexit").map(articleMapper::mapList);
    }

    public Single<Article> getArticle(String articleUrl) {
        return guardianService.getArticle(articleUrl, "main,body,headline,thumbnail").map(articleMapper::mapSingle);
    }

    public Single<ArrayList<String>> getFavouriteArticles() {
        return Single.create(emitter -> emitter.onSuccess(getFavouriteArticlesMapped()));
    }

    public Single<Boolean> toggleArticleAsFavourites(Article article, boolean add) {
        return Single.create(emitter -> {
            ArrayList<String> favouriteArticles = getFavouriteArticlesMapped();
            if (add) {
                if (!favouriteArticles.contains(article.getId()))
                    favouriteArticles.add(article.getId());
            } else {
                favouriteArticles.remove(article.getId());
            }

            String storable = "";
            for (String id : favouriteArticles) {
                storable += id + arraySeperator;
            }
            storable = storable.substring(0, storable.length() - arraySeperator.length());
            emitter.onSuccess(storeString(resources.getString(R.string.st_favourites), storable));
        });
    }

    private ArrayList<String> getFavouriteArticlesMapped() {
        String ids = getStoredString(resources.getString(R.string.st_favourites), "");
        return ids.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(ids.split(arraySeperator)));
    }

    private boolean storeString(String key, String value) {
        return prefs.edit().putString(key, value).commit();
    }

    private String getStoredString(String key, String def) {
        return prefs.getString(key, def);
    }
}
