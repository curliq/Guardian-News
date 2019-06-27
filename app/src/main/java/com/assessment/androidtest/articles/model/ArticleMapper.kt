package com.assessment.androidtest.articles.model

import com.assessment.androidtest.api.model.ApiArticle
import com.assessment.androidtest.api.model.ApiArticleListResponse
import com.assessment.androidtest.api.model.ApiArticleSingleResponse

class ArticleMapper {

    fun mapList(apiArticleListResponse: ApiArticleListResponse): List<Article> {
        return apiArticleListResponse.response.results.map { mapArticle(it) }
    }

    fun mapSingle(apiArticleResponse: ApiArticleSingleResponse): Article {
        return mapArticle(apiArticleResponse.response.content)
    }

    private fun mapArticle(apiArticleResponse: ApiArticle): Article {
        return Article(
                apiArticleResponse.id,
                apiArticleResponse.fields?.thumbnail ?: "",
                apiArticleResponse.sectionId,
                apiArticleResponse.sectionName,
                apiArticleResponse.webPublicationDate,
                apiArticleResponse.fields?.headline ?: "",
                apiArticleResponse.fields?.body ?: "",
                apiArticleResponse.apiUrl,
                false,
                ""
        )
    }
}
