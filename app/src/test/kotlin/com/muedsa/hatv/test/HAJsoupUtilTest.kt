package com.muedsa.hatv.test

import android.net.Uri
import com.muedsa.hatv.repository.HAUrls
import com.muedsa.hatv.util.parseHomePageBody
import com.muedsa.hatv.util.parseWatchPageBody
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class HAJsoupUtilTest {

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
    }

    @Test
    fun parseWatchPageBodyTest() {
        val uriBuilder = Uri.parse(HAUrls.WATCH).buildUpon()
            .appendQueryParameter("v", "13667")
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