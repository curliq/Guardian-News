package com.assessment.androidtest.api;

import com.assessment.androidtest.api.model.ApiArticleListResponse;
import com.assessment.androidtest.api.model.ApiArticleSingleResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GuardianService {
    @GET("search?show-fields=headline,thumbnail&page-size=50")
    Single<ApiArticleListResponse> searchArticles(@Query("q") String searchTerm);

    @GET
    Single<ApiArticleSingleResponse> getArticle(@Url String articleUrl, @Query("show-fields") String fields);
}
