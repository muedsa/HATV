package com.muedsa.hatv.util

import android.net.UrlQuerySanitizer
import androidx.compose.ui.util.fastJoinToString
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.model.VideosRowModel
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator

val VideoSourceUrlPattern = "const source = 'https://(.*?)';".toRegex()

fun parseHomePageBody(body: Element): List<VideosRowModel> {
    val list = mutableListOf<VideosRowModel>()
    val rowsWrapper = body.selectFirst(Evaluator.Id("home-rows-wrapper"))!!
    var i = 0
    while (i < rowsWrapper.childrenSize()) {
        val child = rowsWrapper.child(i)
        i++
        if (child.tagName() == "a" && child.childrenSize() > 0) {
            val rowTitle = child.selectFirst("h3")
            if (rowTitle != null) {
                val videosWrapper =
                    child.nextElementSibling()?.selectFirst(".home-rows-videos-wrapper")
                if (videosWrapper != null) {
                    list.add(parseRow(rowTitle, videosWrapper))
                    i++
                }
            }
        }
    }
    return list
}

private fun parseRow(rowTitle: Element, wrapper: Element): VideosRowModel {
    val title = rowTitle.text()
    val horizontal = wrapper.selectFirst(".home-rows-videos-div") == null
    val videos = if (horizontal)
        parseRowHorizontalItems(wrapper, ".search-doujin-videos")
    else
        parseRowVerticalItems(wrapper)
    return VideosRowModel(
        title = title,
        videos = videos,
        horizontalVideoImage = horizontal
    )
}

private fun parseRowHorizontalItems(
    wrapper: Element,
    otherCssQuery: String = ""
): List<VideoInfoModel> {
    val videos = mutableListOf<VideoInfoModel>()
    val elements = wrapper.select(".multiple-link-wrapper$otherCssQuery")
    val videoIdSet = mutableSetOf<String>()
    for (el in elements) {
        val id = el.selectFirst("a[href^=\"https://hanime1.me/watch?v=\"]")
            ?.attr("href")
            ?.let {
//                it.removePrefix("https://hanime1.me/watch?v=")
                UrlQuerySanitizer(it).getValue("v")
            }
        val img = el.select("img").last()?.attr("src")
        val title = el.selectFirst(".card-mobile-title")?.text()
        val author = el.selectFirst(".card-mobile-user")?.text()
        val desc = el.select(".card-mobile-duration").map {
            it.text()
        }.fastJoinToString(separator = " ")
        if (id != null && img != null && title != null && !videoIdSet.contains(id)) {
            videos.add(
                VideoInfoModel(
                    id = id,
                    image = img,
                    title = title,
                    author = author,
                    desc = desc
                )
            )
            videoIdSet.add(id)
        }
    }
    return videos
}

private fun parseRowVerticalItems(wrapper: Element): List<VideoInfoModel> {
    val videos = mutableListOf<VideoInfoModel>()
    val elements = wrapper.select(".home-rows-videos-div")
    val videoIdSet = mutableSetOf<String>()
    for (el in elements) {
        if (el.parent() != null && el.parent()!!.`is`("a[href^=\"https://hanime1.me/watch?v=\"]")) {
            val id = el.parent()!!.attr("href").let {
//                it.removePrefix("https://hanime1.me/watch?v=")
                UrlQuerySanitizer(it).getValue("v")
            }
            val img = el.selectFirst("img")?.attr("src")
            val title = el.selectFirst(".home-rows-videos-title")?.text()
            if (id != null && img != null && title != null && !videoIdSet.contains(id)) {
                videos.add(
                    VideoInfoModel(
                        id = id,
                        image = img,
                        title = title
                    )
                )
                videoIdSet.add(id)
            }
        }
    }
    return videos
}

fun parseWatchPageBody(body: Element): VideoDetailModel {
    val id = body.selectFirst(Evaluator.Id("video-id"))?.`val`()!!

    val videoEl = body.selectFirst(Evaluator.Id("player"))!!
    val posterImage = videoEl.attr("poster")
    val videoSourceELs = videoEl.select("source[src]")
    val playUrl = if (videoSourceELs.isNotEmpty()) {
        val videoSourceEl = videoEl.select("source[src]").maxBy {
            it.attr("size").toIntOrNull() ?: 0
        }
        videoSourceEl?.attr("src")!!
    } else if (!videoEl.attr("src").isNullOrBlank()) {
        videoEl.attr("src")
    } else {
        VideoSourceUrlPattern.find(videoEl.nextElementSiblings().select("script").html())!!
            .groups[1]!!
            .value
    }

    val author = body.selectFirst(Evaluator.Id("video-artist-name"))?.text() ?: ""

    val detailEl = body.selectFirst(".video-details-wrapper .video-description-panel")!!
    val title = detailEl.child(1).text()
    val desc = detailEl.child(2).text()

    val playlistEl = body.selectFirst(Evaluator.Id("playlist-scroll"))
    val videos = if (playlistEl != null)
        parseRowHorizontalItems(playlistEl, ".related-watch-wrap")
    else
        emptyList()
    return VideoDetailModel(
        id = id,
        image = posterImage,
        title = title,
        author = author,
        desc = desc,
        playUrl = playUrl,
        videoList = videos,
    )
}