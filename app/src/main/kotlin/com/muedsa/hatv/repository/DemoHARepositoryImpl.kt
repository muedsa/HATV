package com.muedsa.hatv.repository

import com.muedsa.hatv.model.ha.PagedVideoInfoModel
import com.muedsa.hatv.model.ha.SearchOptionsModel
import com.muedsa.hatv.model.ha.SearchTagsRowModel
import com.muedsa.hatv.model.ha.VideoDetailModel
import com.muedsa.hatv.model.ha.VideoInfoModel
import com.muedsa.hatv.model.ha.VideosRowModel
import java.util.Random

class DemoHARepositoryImpl : IHARepository {

    override fun fetchHomeVideosRows(): List<VideosRowModel> {
        mockWaitNetwork()
        return listOf(
            VideosRowModel(
                title = "Title One",
                videos = getDemoVideos(false),
                horizontalVideoImage = false
            ),
            VideosRowModel(
                title = "Title Two",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
            VideosRowModel(
                title = "Title Three",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
            VideosRowModel(
                title = "Title Four",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
            VideosRowModel(
                title = "Title Five",
                videos = getDemoVideos(false),
                horizontalVideoImage = false
            ),
            VideosRowModel(
                title = "Title 6",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
            VideosRowModel(
                title = "Title 7",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
            VideosRowModel(
                title = "Title 8",
                videos = getDemoVideos(true),
                horizontalVideoImage = true
            ),
        )
    }

    override fun fetchVideoDetail(videoId: String): VideoDetailModel {
        mockWaitNetwork()
        val videos = getDemoVideos()
        val index = RANDOM.nextInt(videos.size - 1)
        val video = videos[index]
        return VideoDetailModel(
            id = video.id,
            image = video.image,
            title = video.title,
            author = video.author,
            desc = video.desc,
            tags = getTags("Tag", RANDOM.nextInt(10)),
            playUrl = "https://media.w3.org/2010/05/sintel/trailer.mp4",
            videoList = videos
        )
    }

    override fun fetchSearchOptions(): SearchOptionsModel {
        mockWaitNetwork()
        return SearchOptionsModel(
            genres = listOf("全部", "123", "233", "HAHAHAHA", "WOW"),
            tagsRows = listOf(
                SearchTagsRowModel(
                    title = "Tag Group One",
                    tags = getTags("TagOne", 5)
                ),
                SearchTagsRowModel(
                    title = "Tag Group Two",
                    tags = getTags("TagTwo", 10)
                ),
                SearchTagsRowModel(
                    title = "Tag Group Three",
                    tags = getTags("TagThree", 15)
                ),
                SearchTagsRowModel(
                    title = "Tag Group Four",
                    tags = getTags("TagFour", 30)
                ),
                SearchTagsRowModel(
                    title = "Tag Group Five",
                    tags = getTags("TagFive", 20)
                ),
                SearchTagsRowModel(
                    title = "Tag Group Six",
                    tags = getTags("TagSix", 1)
                )
            )
        )
    }

    override fun fetchSearchVideos(
        query: String,
        genre: String,
        tags: Set<String>,
        page: Int
    ): PagedVideoInfoModel {
        mockWaitNetwork()
        val horizontal = genre != "123" && genre != "2333"
        return PagedVideoInfoModel(
            page = page,
            maxPage = 5,
            videos = getDemoVideos(horizontal, titlePrefix = "P$page-"),
            horizontalVideoImage = horizontal
        )
    }

    private fun getDemoVideos(
        horizontal: Boolean = true,
        titlePrefix: String = ""
    ): List<VideoInfoModel> {
        val sizePath =
            if (horizontal) "$IMAGE_WIDTH/$IMAGE_HEIGHT" else "$IMAGE_HEIGHT/$IMAGE_WIDTH"
        return listOf(
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "$titlePrefix【散人】国产悬疑惊悚《三伏》 旧时代三眼神童之谜（已更新至P4 明镜台）",
                if (horizontal)
                    "逍遥散人"
                else null,
                if (horizontal)
                    "试玩视频BV1qF411x7ER 前作烟火BV15U4y1x7YS\r\n三伏终于上线了，期待很久的国产悬疑游戏，剧情优秀，情节震撼。\r\n喜欢的朋友欢迎收藏三连分享，去steam上购买支持下作者，非常感谢！"
                else null,
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}汽车安全锤",
                if (horizontal)
                    "鉴货兄弟"
                else null,
                if (horizontal)
                    "这东西关键时刻真能保命吗？#测评 #汽车安全锤 #有车必备"
                else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "$titlePrefix【某幻】国产悬疑《三伏》全流程实况 1P三眼神童",
                if (horizontal)
                    "某幻君"
                else null,
                if (horizontal) "游戏：三伏\n结尾给我刀瞎了" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}超燃打戏！功夫皇帝李连杰，22年前豪取15亿票房！",
                if (horizontal) "摩斯神探" else null,
                if (horizontal) "超燃打戏！功夫皇帝李连杰，22年前豪取15亿票房！《龙之吻》" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "$titlePrefix《明日方舟》EP - Miss You",
                if (horizontal) "明日方舟" else null,
                if (horizontal)
                    "8月1日 Miss You 正式上架塞壬唱片官网，网易云音乐及QQ音乐等平台\n塞壬唱片官网链接：https://monster-siren.hypergryph.com/music/514540\n\n【专辑介绍】\nMagma is formed from molten rocks.\nThough separated by vast distances, \nthey all converge into a singular life force of molten heat.\nWith the long da"
                else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}“他就想活命，他有什么罪!”",
                if (horizontal) "听云up" else null,
                if (horizontal) "电影:我不是药神\nBGM:用什么把你留住" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}网络热门生物鉴定49",
                if (horizontal) "无穷小亮的科普日常" else null,
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}家人们这么多天没更新就是去做了这个事情.想你们了！稍微休息两天马上更新视频！",
                if (horizontal) "李炮炮儿" else null,
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}当 代 年 轻 人 消 费 现 状",
                if (horizontal) "进击的金厂长" else null,
                if (horizontal) "兄弟们觉得现在一个月消费多少钱正常？让我在评论区康康大家的情况\\n点赞投币会带来好运哦～" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}给奶奶做了一个电动轮椅",
                if (horizontal) "手工耿" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}伍佰郑州演唱会，伍佰不在观众都开始唱了，网友：伍佰演唱会不需要伍佰了",
                if (horizontal) "四川观察" else null,
                if (horizontal)
                    "9月16日，河南郑州。伍佰演唱会散场后，歌迷在场馆内久久不愿离去自发合唱，甚至在离场通道内继续大合唱。网友：事态严重了，伍佰的演唱会，伍佰在不在都开始唱了。"
                else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}如何在家用西瓜制造白砂糖",
                if (horizontal) "嘤武罗" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}花2200买了一只摆烂鸟，到家天天睡大觉",
                if (horizontal) "一只小皮皮呀丶" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}当班级里有人讲黄色……我开诚布公地教育了",
                if (horizontal) "圆脸然老师" else null
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}家人们这么多天没更新就是去做了这个事情.想你们了！稍微休息两天马上更新视频！",
                if (horizontal) "李炮炮儿" else null,
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}【每天早晨5点起床】我的身体发生了什么变化?!",
                if (horizontal) "帅soserious" else null,
                if (horizontal)
                    "大家喜欢这个视频的话别忘了一键三连哦！\\n新来的小伙伴，别忘了点击\\\"关注\\\"，与【沃尔沃 XC60】一起，早睡早起，共享健康生活！"
                else null,
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}每日早餐，张嘴即可！",
                if (horizontal) "Joseph的机器" else null,
                if (horizontal) "这条传送带给我喂早餐！草莓蘸酸奶、鳄梨吐司、咖啡、冰沙和相当大的糕点。可口的！" else null,
            ),
            VideoInfoModel(
                RANDOM.nextInt(99999).toString(),
                "https://picsum.photos/$sizePath?r=${RANDOM.nextInt(99999)}",
                "${titlePrefix}火车进藏保姆级攻略，需要知道的小常识希望能帮到你！",
                if (horizontal) "中国旅游攻略" else null
            )
        ).shuffled()
    }

    private fun getTags(label: String, num: Int = 10): List<String> {
        return buildList(capacity = num) {
            for (i in 0..num) {
                add("$label-$i")
            }
        }
    }

    private fun mockWaitNetwork() {
        if (RANDOM.nextInt(10) > 8) {
            throw RuntimeException("just mock http request error! ＞﹏＜")
        }
        Thread.sleep(
            MIN_SIMULATE_REQUEST_DELAY_MS +
                    RANDOM.nextInt((MAX_SIMULATE_REQUEST_DELAY_MS - MIN_SIMULATE_REQUEST_DELAY_MS).toInt())
        )
    }

    companion object {
        private const val MIN_SIMULATE_REQUEST_DELAY_MS = 300L
        private const val MAX_SIMULATE_REQUEST_DELAY_MS = 2300L
        private const val IMAGE_WIDTH = 1920
        private const val IMAGE_HEIGHT = 1080
        private val RANDOM = Random()
    }
}