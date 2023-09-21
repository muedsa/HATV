package com.muedsa.hatv.test

import android.net.Uri
import com.muedsa.hatv.model.VideosRowModel
import com.muedsa.hatv.repository.HAUrls
import com.muedsa.hatv.util.parseHomePageBody
import com.muedsa.hatv.util.parseSearchPageForTags
import com.muedsa.hatv.util.parseSearchPageForVideos
import com.muedsa.hatv.util.parseWatchPageBody
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class HAJsoupUtilTest {

    private val homePageVideosRows: MutableList<VideosRowModel> = mutableListOf()

    @Test
    fun parseHomePageBodyTest() {
        val doc = Jsoup.connect(HAUrls.HOME)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
            )
            .timeout(3 * 1000)
            .proxy("127.0.0.1", 23334)
            .get()
        val body = doc.body()
        val rows = parseHomePageBody(body)
        rows.forEach {
            println("row title:${it.title} horizontal:${it.horizontalVideoImage}")
            assert(it.title.isNotEmpty())
            val keySet = mutableSetOf<String>()
            it.videos.forEach { video ->
                println(video)
                assert(video.id.isNotEmpty())
                assert(video.image.isNotEmpty())
                assert(video.title.isNotEmpty())
                assert(!keySet.contains(video.key))
                keySet.add(video.key)
            }
            println("-------")
        }
        homePageVideosRows.clear()
        homePageVideosRows.addAll(rows)
    }

    @Test
    fun parseWatchPageBodyTest() {
        if (homePageVideosRows.isEmpty()) {
            parseHomePageBodyTest()
        }
        for (row in homePageVideosRows) {
            if (row.videos.isNotEmpty()) {
                Thread.sleep(1000)
                val uriBuilder = Uri.parse(HAUrls.WATCH).buildUpon()
                    .appendQueryParameter("v", row.videos[0].id)
                val doc = Jsoup.connect(uriBuilder.build().toString())
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
                    )
                    .timeout(3 * 1000)
                    .proxy("127.0.0.1", 23334)
                    .get()
                val body = doc.body()
                val detail = parseWatchPageBody(body)
                println(detail)
                assert(detail.id.isNotEmpty())
                assert(detail.image.isNotEmpty())
                assert(detail.title.isNotEmpty())
                assert(detail.playUrl.isNotEmpty())
                val keySet = mutableSetOf<String>()
                detail.videoList.forEach {
                    println(it)
                    assert(it.id.isNotEmpty())
                    assert(it.image.isNotEmpty())
                    assert(it.title.isNotEmpty())
                    assert(!keySet.contains(it.key))
                    keySet.add(it.key)
                }
            }
        }
    }

    @Test
    fun parseSearchPageForVideosTest() {
        val doc = Jsoup.connect(HAUrls.SEARCH)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
            )
            .timeout(3 * 1000)
            .proxy("127.0.0.1", 23334)
            .get()
        val body = doc.body()
        val tagsRows = parseSearchPageForTags(body)
        assert(tagsRows.isNotEmpty())
        tagsRows.forEach {
            println(it)
            assert(it.tags.isNotEmpty())
            it.tags.forEach { tag ->
                assert(tag.tag.isNotEmpty())
            }
            println("-------")
        }
    }

    @Test
    fun parseSearchPageForTagsTest() {
        val uriBuilder = Uri.parse(HAUrls.SEARCH).buildUpon()
            .appendQueryParameter("query", "Genshin")
            .appendQueryParameter("page", "1")
        val doc = Jsoup.connect(uriBuilder.build().toString())
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
            )
            .timeout(3 * 1000)
            .proxy("127.0.0.1", 23334)
            .get()
        val body = doc.body()
        val videos = parseSearchPageForVideos(body)
        assert(videos.isNotEmpty())
        val keySet = mutableSetOf<String>()
        videos.forEach {
            println(it)
            assert(it.id.isNotEmpty())
            assert(it.image.isNotEmpty())
            assert(it.title.isNotEmpty())
            assert(!keySet.contains(it.key))
            keySet.add(it.key)
        }
    }
}