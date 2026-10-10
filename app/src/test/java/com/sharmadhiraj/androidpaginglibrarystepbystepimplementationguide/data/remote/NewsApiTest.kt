package com.sharmadhiraj.androidpaginglibrarystepbystepimplementationguide.data.remote

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: NewsApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/v2/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @After
    fun tearDown() = server.shutdown()

    @Test
    fun parsesArticlesAndSendsPagingParams() = runTest {
        server.enqueue(
            MockResponse().setBody(
                """{"articles":[
                    {"title":"A","url":"u1","urlToImage":"i1"},
                    {"title":"B","url":"u2","urlToImage":null}]}"""
            )
        )

        val response = api.getNews(page = 2, pageSize = 10)

        assertEquals(listOf("A", "B"), response.articles?.map { it.title })
        assertNull(response.articles?.get(1)?.imageUrl)
        val request = server.takeRequest()
        assertEquals("2", request.requestUrl?.queryParameter("page"))
        assertEquals("10", request.requestUrl?.queryParameter("pageSize"))
    }
}
