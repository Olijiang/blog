<template>
    <!-- 时间进度条 -->
    <transition name="el-zoom-in-top">
        <el-row>
            <el-col class="timebarCol">
                <div class="timebar">
                    <div class="time_line">
                        <div class="time_node"></div>
                        <div class="time_info">{{ article.createTime }}</div>
                    </div>
                </div>
            </el-col>
            <!-- <el-col :span="1"></el-col> -->

            <el-col class="articleCol">
                <transition name="el-zoom-in-top">
                    <div class="article_card" v-show="ok">
                        <div :class="{ illustration_l: (article.id % 2 == 0), illustration_r: (article.id % 2 != 0) }">
                            <el-image class="img" fir="cover" :src=article.img alt="img" @click="articleDeail" />
                        </div>
                        <div :class="{ content_r: (article.id % 2 == 0), content_l: (article.id % 2 != 0) }">
                            <h3 @click="articleDeail">{{ article.title }}</h3>
                            <span style="font-size: 80%;user-select: none;">
                                分类: <span style="color: #d63a3a;">{{ article.category }}</span> |

                                标签:
                                <span style="color: #d63a3a;" v-for=" (item, index) in article.tag.tags" :key="index">
                                    {{ item }}&#160;
                                </span>
                            </span>

                            <hr width="400px" align="left" />
                            <p style="user-select: none;">{{ article.digest }}</p>
                        </div>
                    </div>
                </transition>
            </el-col>
        </el-row>
    </transition>
</template>

<script>

export default {
    components: {

    },
    props: {
        article: {
            id: Number,
            title: String,
            createTime: String,
            category: String, // 分类
            tag: String,  // 标签
            digest: String, //摘要
            url: String, //正文地址
            img: String, //插图地址
        },
    },
    data() {
        return {
            ok: false
        }
    },
    methods: {
        articleDeail() {
            this.$router.push({
                path: "/ArticleDetail/" + this.article.id
            })
        }
    },
    computed: {

    },
    watch: {

    },
    mounted() {
        this.article.img = this.baseUrl + this.article.img
        this.article.tag = JSON.parse(this.article.tag)
        this.article.digest = this.article.digest.replace(/#*.*#/g, '').replace(/[^a-z0-9\u4e00-\u9fa5]/, '').substring(0, 200) // 除去标题部分，截取200个字用来显示
        this.ok = true
    },
}

</script>


<style lang="less" scoped>
.timebar {
    min-width: 150px;
    height: 270px;
    user-select: none;
}

.time_node {
    position: absolute;
    background-color: #3f00ff;
    position: absolute;
    top: 100px;
    left: -6px;
    width: 11px;
    height: 11px;
    border-radius: 11px;
}

.time_line {
    position: relative;
    display: inline-block;
    border-left: 1px solid #3f00ff;
    height: 100%;
    text-align: center;
    width: 50%;
}

.time_info {
    margin-top: 100px;
    margin-left: 5%;
    font-size: 80%;
    color: #3f00ff;
}

.article_card {

    width: 100%;
    min-width: 600px;
    background-color: rgba(255, 255, 255, 0.5);
    text-align: left;
    height: 250px;
    border-radius: 20px;
    border: 1px solid #278b63;
    transition: all 0.2s ease-in-out;
    margin-bottom: 20px;

    .img {
        cursor: pointer;
        height: 100%;
        width: 100%;
        object-fit: cover;
        vertical-align: middle;
        transition: all 0.5s ease-in-out;

        &:hover {
            transition: all 0.5s ease-in-out;
            transform: scale(1.1, 1.1);
        }
    }

    &:hover {
        border: 1px solid #2dcf8e;
        box-shadow: 0 0 10px #5ae7af;
        transition: all 0.2s ease-in-out;
    }
}

.illustration {
    height: 100%;
    width: 45%;
    overflow: hidden;
    transition: all 0.5s;
}

.illustration_l {
    float: left;
    border-radius: 20px 10px 10px 20px;
    .illustration()
}

.illustration_r {
    border-radius: 10px 20px 20px 10px;
    float: right;
    .illustration()
}

.content {
    padding-top: 10px;
    width: 50%;
    height: 210px;
    margin: 0 2% 0;
    overflow: hidden;
    transition: all 0.5s;
}

.content_l {
    float: left;
    .content()
}

.content_r {
    float: right;
    .content()
}

h3 {
    color: #4b9797;
    cursor: pointer;
    height: 25px;
    line-height: 1.2;
    margin: 10px 5px 10px 0;
    font-size: 20px;
    overflow: hidden;
    transition: all 0.5s ease-in-out;

    &:hover {
        font-size: 22px;
        transition: all 0.5s ease-in-out;
    }
}

p {
    // font-size:%;
    margin: 10px 0 10px;
    text-indent: 2em; //首行缩进
    line-height: 1.6;
}

.timebarCol {
    max-width: 20%;
    flex: 0 0 20%;
    transition: all 0.5s ease;
}

.articleCol {
    max-width: 80%;
    flex: 0 0 80%;
    transition: all 0.5s ease;
}

@media (max-width: 1100px) {
    .timebarCol {
        max-width: 0%;
        transition: all 0.5s ease;

        * {
            display: none;
        }
    }

    .articleCol {
        max-width: 100%;
        flex: 0 0 100%;
        transition: all 0.5s ease;
    }
}

@media (max-width: 600px) {
    .article_card {
        min-width: 400px;
        height: 380px;
        overflow: hidden;
    }

    .illustration {
        height: 61%;
        width: 100%;
        overflow: hidden;
        border-radius: 19px 19px 10px 10px;
    }

    .illustration_l {
        .illustration()
    }

    .illustration_r {
        .illustration()
    }

    .content {
        padding-top: 10px;
        width: 90%;
        height: 50%;
        margin: 0 5% 0;
        overflow: hidden;
    }

    .content_l {
        .content()
    }

    .content_r {
        .content()
    }
}
</style>