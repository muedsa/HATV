package com.muedsa.hatv.util

import android.net.UrlQuerySanitizer
import androidx.compose.ui.util.fastJoinToString
import androidx.core.text.isDigitsOnly
import com.muedsa.hatv.model.PagedVideoInfoModel
import com.muedsa.hatv.model.SearchOptionsModel
import com.muedsa.hatv.model.SearchTagsRowModel
import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.model.VideosRowModel
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator

val VideoSourceUrlPattern = "const source = '(https://.*?)';".toRegex()

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

    val tagsWrapperEl = body.selectFirst(".video-details-wrapper.video-tags-wrapper")
    val tags = tagsWrapperEl?.let {
        it.select(".single-video-tag:not([data-toggle])")
            .map { el ->
                el.text()
            }.filter { text ->
                text.isNotEmpty()
            }
    } ?: emptyList()
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
        tags = tags,
        playUrl = playUrl,
        videoList = videos,
    )
}

fun parseSearchOptionsFromSearchPage(body: Element): SearchOptionsModel {
    val genres = parseGenresFromSearchPage(body)
    val tagsRows = parseTagsFromSearchPage(body)
    return SearchOptionsModel(
        genres = genres,
        tagsRows = tagsRows
    )
}

fun parseGenresFromSearchPage(body: Element): List<String> {
    val genresEl = body.selectFirst("#genre-modal .modal-body")!!
    return genresEl.select(".hentai-sort-options").map {
        it.text()
    }
}

fun parseTagsFromSearchPage(body: Element): List<SearchTagsRowModel> {
    val list = mutableListOf<SearchTagsRowModel>()
    val tagsEl = body.selectFirst("#tags .modal-body")!!
    tagsEl.children().toList().filter {
        it.`is`("h5") || it.`is`("label")
    }.forEach {
        if (it.`is`("h5")) {
            list.add(
                SearchTagsRowModel(
                    title = it.text(),
                    tags = mutableListOf()
                )
            )
        } else {
            (list.last().tags as MutableList<String>)
                .add(it.selectFirst("input[name=\"tags[]\"]")!!.`val`())
        }
    }
    return list
}

fun parsePagedVideosFromSearchPage(body: Element): PagedVideoInfoModel {
    val wrapper = body.selectFirst(Evaluator.Id("home-rows-wrapper"))!!
    val horizontal = wrapper.selectFirst(".home-rows-videos-div") == null
    val videos = if (horizontal)
        parseRowHorizontalItems(wrapper, ".search-doujin-videos")
    else
        parseRowVerticalItems(wrapper)
    val paginationEl = body.selectFirst("ul.pagination[role=\"navigation\"]")
    var page = 1
    var maxPage = 1
    paginationEl?.let {
        it.selectFirst("li.page-item.active")?.let { activeEl ->
            activeEl.text().let { text ->
                if (text.isDigitsOnly()) {
                    page = text.toInt()
                    maxPage = page
                }
            }
        }
        it.select("li.page-item").toList()
            .map { i -> i.text() }
            .filter { i -> i.isDigitsOnly() }
            .map { i -> i.toInt() }
            .let { pageNoList ->
                if (pageNoList.isNotEmpty()) {
                    maxPage = pageNoList.max()
                }
            }
    }
    return PagedVideoInfoModel(
        page = page,
        maxPage = maxPage,
        videos = videos,
        horizontalVideoImage = horizontal
    )
}