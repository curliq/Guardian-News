package com.assessment.androidtest;

import android.app.Application;
import android.content.Context;

import com.assessment.androidtest.articles.model.ArticlesModule;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class HeadlinesApp extends Application {
    private final ArticlesModule articlesModule = new ArticlesModule();

    public static ArticlesModule from(Context applicationContext) {
        return ((HeadlinesApp) applicationContext).articlesModule;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ViewPump.init(
                ViewPump.builder().addInterceptor(
                        new CalligraphyInterceptor(new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/GoogleSans-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build())
                ).build()
        );
    }
}
