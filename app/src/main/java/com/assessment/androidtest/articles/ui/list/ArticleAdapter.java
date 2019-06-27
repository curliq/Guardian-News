package com.assessment.androidtest.articles.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.assessment.androidtest.R;
import com.assessment.androidtest.articles.model.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface EventListener {
        void onArticleSelected(Article article);
    }

    private EventListener eventListener;
    private final List<Article> articles = new ArrayList<>();
    //  private static Context context;
    //  ;(

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        articleViewHolder.bind(articles.get(position), eventListener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    void showArticles(List<Article> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }

    void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.listItemArticle_root_fl)
        FrameLayout rootFl;
        @BindView(R.id.listItemArticle_divider_tv)
        TextView dividerTv;
        @BindView(R.id.listItemArticle_thumbnail_iv)
        ImageView thumbnailIv;
        @BindView(R.id.listItemArticle_title_tv)
        TextView headlineTv;
        @BindView(R.id.listItemArticle_date_tv)
        TextView dateTv;

        ArticleViewHolder(View view) {
            super(view);
        }

        void bind(Article article, EventListener eventListener) {
            ButterKnife.bind(this, itemView);
            headlineTv.setText(article.getTitle());
            dividerTv.setText(article.getDividerText());
            dividerTv.setVisibility(article.getDividerText().isEmpty() ? View.GONE : View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
            dateTv.setText(sdf.format(article.getPublished()));
            Glide.with(itemView.getContext()).load(article.getThumbnail()).into(thumbnailIv);
            if (eventListener != null)
                rootFl.setOnClickListener((view) -> eventListener.onArticleSelected(article));
        }

    }
}
