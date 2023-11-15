package com.muedsa.hatv.repository

import android.net.Uri
import com.muedsa.hatv.model.ha.PagedVideoInfoModel
import com.muedsa.hatv.model.ha.SearchOptionsModel
import com.muedsa.hatv.model.ha.VideoDetailModel
import com.muedsa.hatv.model.ha.VideosRowModel
import com.muedsa.hatv.util.parseHomePageBody
import com.muedsa.hatv.util.parsePagedVideosFromSearchPage
import com.muedsa.hatv.util.parseSearchOptionsFromSearchPage
import com.muedsa.hatv.util.parseWatchPageBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HARepositoryImpl : IHARepository {
    override fun fetchHomeVideosRows(): List<VideosRowModel> {
        val body = fetchGet(HAUrls.HOME).body()
        return parseHomePageBody(body).filter { row -> row.videos.isNotEmpty() }
    }

    override fun fetchVideoDetail(videoId: String): VideoDetailModel {
        val uriBuilder = Uri.parse(HAUrls.WATCH).buildUpon()
            .clearQuery()
            .appendQueryParameter("v", videoId)
        val body = fetchGet(uriBuilder.build().toString()).body()
        return parseWatchPageBody(body)
    }

    override fun fetchSearchOptions(): SearchOptionsModel {
        val body = fetchGet(HAUrls.SEARCH).body()
        return parseSearchOptionsFromSearchPage(body)
    }

    override fun fetchSearchVideos(
        query: String,
        genre: String,
        tags: Set<String>,
        page: Int
    ): PagedVideoInfoModel {
        val uriBuilder = Uri.parse(HAUrls.SEARCH).buildUpon()
            .clearQuery()
            .appendQueryParameter("query", query)
            .appendQueryParameter("genre", genre)
            .appendQueryParameter("page", page.toString())
        tags.forEach { tag ->
            uriBuilder.appendQueryParameter("tags[]", tag)
        }
        val body = fetchGet(uriBuilder.build().toString()).body()
        return parsePagedVideosFromSearchPage(body)
    }

    private fun fetchGet(url: String): Document {
        return Jsoup.connect(url)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
            )
            .timeout(TIMEOUT_MS)
            .get()
    }

    companion object {
        const val TIMEOUT_MS = 10 * 1000
    }
}