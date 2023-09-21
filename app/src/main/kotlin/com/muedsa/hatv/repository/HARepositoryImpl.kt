package com.muedsa.hatv.repository

import android.net.Uri
import com.muedsa.hatv.model.TagsRowModel
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.model.VideosRowModel
import com.muedsa.hatv.util.parseHomePageBody
import com.muedsa.hatv.util.parseSearchPageForTags
import com.muedsa.hatv.util.parseSearchPageForVideos
import com.muedsa.hatv.util.parseWatchPageBody
import io.reactivex.rxjava3.core.Single
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HARepositoryImpl : IHARepository {
    override fun fetchHomeVideosRows(): Single<List<VideosRowModel>> {
        return Single.create {
            val body = fetchGet(HAUrls.HOME).body()
            it.onSuccess(parseHomePageBody(body).filter { row -> row.videos.isNotEmpty() })
        }
    }

    override fun fetchVideoDetail(videoId: String): Single<VideoDetailModel> {
        return Single.create {
            val uriBuilder = Uri.parse(HAUrls.WATCH).buildUpon()
                .clearQuery()
                .appendQueryParameter("v", videoId)
            val body = fetchGet(uriBuilder.build().toString()).body()
            it.onSuccess(parseWatchPageBody(body))
        }
    }

    override fun fetchSearchTags(): Single<List<TagsRowModel>> {
        return Single.create {
            val body = fetchGet(HAUrls.SEARCH).body()
            it.onSuccess(parseSearchPageForTags(body))
        }
    }

    override fun fetchSearchVideos(
        query: String,
        type: String,
        tags: List<String>
    ): Single<List<VideoInfoModel>> {
        return Single.create {
            val uriBuilder = Uri.parse(HAUrls.SEARCH).buildUpon()
                .clearQuery()
                .appendQueryParameter("query", query)
                .appendQueryParameter("type", type)
            tags.forEach { tag ->
                uriBuilder.appendQueryParameter("tags[]", tag)
            }
            val body = fetchGet(uriBuilder.build().toString()).body()
            it.onSuccess(parseSearchPageForVideos(body))
        }
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