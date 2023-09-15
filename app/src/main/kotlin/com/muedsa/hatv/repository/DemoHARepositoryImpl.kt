package com.muedsa.hatv.repository

import com.muedsa.hatv.model.VideoDetailModel
import com.muedsa.hatv.model.VideoInfoModel
import com.muedsa.hatv.model.VideosRowModel
import io.reactivex.rxjava3.core.Single
import java.util.Random
import java.util.concurrent.TimeUnit

class DemoHARepositoryImpl : IHARepository {

    override fun fetchHomeVideosRows(): Single<List<VideosRowModel>> {
        return Single.just(listOf(
            VideosRowModel(
                title = "Title One",
                videos = getDemoVideos(false),
                horizontalVideoImage = false
            ),
            VideosRowModel(
                title = "Title Two",
                videos = getDemoVideos(false),
                horizontalVideoImage = false
            ),
            VideosRowModel(
                title = "Title Three",
                videos = getDemoVideos(false),
                horizontalVideoImage = false
            ),
            VideosRowModel(
                title = "Title Four",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
            VideosRowModel(
                title = "Title Five",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            )
        )).delay(SIMULATE_REQUEST_DELAY_MS, TimeUnit.MILLISECONDS)
    }

    override fun fetchVideoDetail(videoId: String): Single<VideoDetailModel> {
        val videos = getDemoVideos()
        val rand = Random()
        val index = rand.nextInt(videos.size - 1)
        val video = videos[index]
        return Single.just(
            VideoDetailModel(
                id = video.id,
                image = video.image,
                title = video.title,
                author = video.author,
                desc = video.desc,
                playUrl = "https://media.w3.org/2010/05/sintel/trailer.mp4",
                videoList = videos
            )
        )
    }

    override fun fetchSearchVideos(): Single<List<VideoInfoModel>> {
        return Single.just(getDemoVideos()).delay(SIMULATE_REQUEST_DELAY_MS, TimeUnit.MILLISECONDS)
    }

    private fun getDemoVideos(horizontal: Boolean = true): List<VideoInfoModel> {
        val rand = Random()
        val sizePath =
            if (horizontal) "$IMAGE_WIDTH/$IMAGE_HEIGHT" else "$IMAGE_HEIGHT/$IMAGE_WIDTH"
        return listOf(
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "【散人】国产悬疑惊悚《三伏》 旧时代三眼神童之谜（已更新至P4 明镜台）",
                "逍遥散人",
                "试玩视频BV1qF411x7ER 前作烟火BV15U4y1x7YS\r\n三伏终于上线了，期待很久的国产悬疑游戏，剧情优秀，情节震撼。\r\n喜欢的朋友欢迎收藏三连分享，去steam上购买支持下作者，非常感谢！"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "汽车安全锤",
                "鉴货兄弟",
                "这东西关键时刻真能保命吗？#测评 #汽车安全锤 #有车必备"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "【某幻】国产悬疑《三伏》全流程实况 1P三眼神童",
                "某幻君",
                "游戏：三伏\n结尾给我刀瞎了"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "超燃打戏！功夫皇帝李连杰，22年前豪取15亿票房！",
                "摩斯神探",
                "超燃打戏！功夫皇帝李连杰，22年前豪取15亿票房！《龙之吻》"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "《明日方舟》EP - Miss You",
                "明日方舟",
                "8月1日 Miss You 正式上架塞壬唱片官网，网易云音乐及QQ音乐等平台\n塞壬唱片官网链接：https://monster-siren.hypergryph.com/music/514540\n\n【专辑介绍】\nMagma is formed from molten rocks.\nThough separated by vast distances, \nthey all converge into a singular life force of molten heat.\nWith the long da"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "“他就想活命，他有什么罪!”",
                "听云up",
                "电影:我不是药神\nBGM:用什么把你留住"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "网络热门生物鉴定49",
                "无穷小亮的科普日常",
                ""
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "【散人】国产悬疑惊悚《三伏》 旧时代三眼神童之谜（已更新至P4 明镜台）",
                "逍遥散人",
                "试玩视频BV1qF411x7ER 前作烟火BV15U4y1x7YS\r\n三伏终于上线了，期待很久的国产悬疑游戏，剧情优秀，情节震撼。\r\n喜欢的朋友欢迎收藏三连分享，去steam上购买支持下作者，非常感谢！"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "汽车安全锤",
                "鉴货兄弟",
                "这东西关键时刻真能保命吗？#测评 #汽车安全锤 #有车必备"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "【某幻】国产悬疑《三伏》全流程实况 1P三眼神童",
                "某幻君",
                "游戏：三伏\n结尾给我刀瞎了"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "超燃打戏！功夫皇帝李连杰，22年前豪取15亿票房！",
                "摩斯神探",
                "超燃打戏！功夫皇帝李连杰，22年前豪取15亿票房！《龙之吻》"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "《明日方舟》EP - Miss You",
                "明日方舟",
                "8月1日 Miss You 正式上架塞壬唱片官网，网易云音乐及QQ音乐等平台\n塞壬唱片官网链接：https://monster-siren.hypergryph.com/music/514540\n\n【专辑介绍】\nMagma is formed from molten rocks.\nThough separated by vast distances, \nthey all converge into a singular life force of molten heat.\nWith the long da"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "“他就想活命，他有什么罪!”",
                "听云up",
                "电影:我不是药神\nBGM:用什么把你留住"
            ),
            VideoInfoModel(
                rand.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${rand.nextInt(99999)}",
                "网络热门生物鉴定49",
                "无穷小亮的科普日常",
                ""
            ),
        ).shuffled()
    }

    companion object {
        private const val SIMULATE_REQUEST_DELAY_MS = 300L
        private const val IMAGE_WIDTH = 1920
        private const val IMAGE_HEIGHT = 1080
    }
}