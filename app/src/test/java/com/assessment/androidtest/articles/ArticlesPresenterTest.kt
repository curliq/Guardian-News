package com.assessment.androidtest.articles


import com.assessment.androidtest.articles.model.Article
import com.assessment.androidtest.articles.model.ArticlesRepository
import com.assessment.androidtest.articles.ui.list.ArticlesPresenter
import com.assessment.androidtest.common.Event
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations.initMocks
import java.time.Instant
import java.util.*


class ArticlesPresenterTest {

    @Mock
    private lateinit var articlesRepository: ArticlesRepository
    @Mock
    private lateinit var view: ArticlesPresenter.View

    private lateinit var presenter: ArticlesPresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        initMocks(this)

        presenter = ArticlesPresenter(Schedulers.trampoline(), Schedulers.trampoline(), articlesRepository)

        // Stub behaviours
        Mockito.`when`(articlesRepository.latestFintechArticles()).thenReturn(
                Single.create { emitter ->
                    emitter.onSuccess(listOf(Article(
                            "someId",
                            "",
                            "someId",
                            "Tech",
                            Date.from(Instant.now()),
                            "title",
                            "body",
                            "url",
                            false,
                            ""
                    )))
                }
        )
    }

    @Test
    @Throws(Exception::class)
    fun register() {
        presenter.register(view)

        // This isn't passing. Need to stub a few of the methods on the View
        Mockito.verify(view, times(1)).showArticles(ArgumentMatchers.anyList())
    }
}