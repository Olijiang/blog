<template>
    <div>
        <transition name="el-fade-in">
            <div class="illustration">
                <el-image fit="cover" class="img" :src=author.coverImg alt="" />
                <div class="authorInfo">
                    <h1 style="margin: 200px 0 0 20px;color:burlywood;font-size: 50px;"> {{ author.blogName }}
                    </h1>
                    <p style="margin: 10px;">{{ author.blogInfo }}</p>
                </div>
            </div>
        </transition>

        <el-row v-if="ok > 1">
            <el-col class="space"></el-col>
            <el-col class="ArticleCard">
                <!-- 文章部分 -->
                <div v-for="(aricle, index) in articleList" :key=index style="transition:all 0.5s">
                    <vArticleCard :article=aricle />
                </div>
            </el-col>
            <el-col class="space"></el-col>
            <el-col class="AuthorCard">
                <vAuthorCard v-if="ok > 1"></vAuthorCard>
            </el-col>
            <el-col class="space"></el-col>
        </el-row>
        <div class="endmsg">{{ endmsg }}</div>
    </div>
</template>



<script>

import vArticleCard from '@/components/v-articleCard.vue'
import vAuthorCard from '@/components/v-authorCard.vue'
import API from '../utils/API'

export default {
    name: 'Home',
    props: ["authorId"],
    components: {
        vArticleCard,
        vAuthorCard
    },
    data() {
        return {
            endmsg: "下拉加载更多",
            articleList: [],
            ok: 0,
            timeout: null,
            queryData: {
                authorId: "",
                startPage: 0,
                pageSize: 5,
                queryWord: ""
            },
            author: {},
            coverImg: "",
        }
    },
    methods: {
        handleScroll() {
            clearTimeout(this.timeout)
            this.timeout = setTimeout(() => {
                let scrollTop = document.documentElement.scrollTop;//滚动高度
                let clientHeight = document.documentElement.clientHeight;//可视高度
                let scrollHeight = document.documentElement.scrollHeight;//内容高度
                if (clientHeight + scrollTop - scrollHeight > -10) {
                    this.endmsg = "正在加载..."
                    let api = (this.isAuthor) ? ("article/getArticles") : ("init/getPublicArticles")
                    API.get(api, this.queryData)
                        .then(res => {
                            if (res.code == 200) {
                                // console.log(res.data);
                                res.data.forEach((element, index) => {
                                    element.index = this.queryData.startPage + index
                                    this.articleList.push(element)
                                });
                                if (res.data.length < this.queryData.pageSize) {
                                    this.endmsg = "没有更多了..."
                                    window.removeEventListener('scroll', this.handleScroll)
                                }
                                this.queryData.startPage = this.queryData.startPage + 5
                            }
                        })
                }
            }, 100);
        }
    },
    computed: {
        isLogin: {
            set(value) {
                this.$store.state.isLogin = value;
            },
            get() {
                return this.$store.state.isLogin;
            }
        },
        isAuthor() {
            // 登录并且当前访问的authorId 等于登录 Id
            return this.$store.getters.isAuthor
        }
    },
    watch: {

    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll)
        // 查询作者信息
        API.get('init/getAuthor', { authorId: this.authorId })
            .then(res => {
                if (res.code == 200) {
                    res.data.img = this.baseUrl + res.data.img
                    res.data.coverImg = this.baseUrl + res.data.coverImg
                    this.author = res.data
                    this.$store.commit("setVisitAuthor", res.data)
                    this.ok++
                }
            })
        // 查询作者文章 5篇
        this.queryData.authorId = this.authorId
        // 等待100ms请求, 等待VisitAuthor更新
        setTimeout(() => {
            let api = (this.isAuthor) ? ("article/getArticles") : ("init/getPublicArticles")
            API.get(api, this.queryData)
                .then(res => {
                    if (res.code == 200) {
                        // console.log(res.data);
                        res.data.forEach((element, index) => {
                            element.index = this.queryData.startPage + index
                            this.articleList.push(element)
                        });
                        if (res.data.length < this.queryData.pageSize) {
                            this.endmsg = "没有更多了..."
                            window.removeEventListener('scroll', this.handleScroll)
                        }
                        this.ok++
                        this.queryData.startPage = this.queryData.startPage + 5
                    }
                })
        }, 100);




        // 更新访问信息
        API.get('init/visit', { authorId: this.authorId })
    },
    unmounted() {
        window.removeEventListener('scroll', this.handleScroll)
    }
}


</script>


<style lang="less" scoped>
.illustration {
    position: relative;
    width: 100%;
    height: 500px;
    margin-bottom: 20px;
    transition: all 0.5s;

    .img {
        display: block;
        height: 100%;
        width: 100%;
        position: absolute;
        transition: all 0.5s ease-in-out;
        border-radius: 0px 0px 10px 10px;
    }

    .authorInfo {
        background-color: rgba(137, 133, 133, 0.5);
        border-radius: 0px 0px 10px 10px;
        position: absolute;
        width: 100%;
        height: 100%;
        transition: all 0.3s ease-in-out;
        user-select: none;
    }
}


.endmsg {
    user-select: none;
    position: relative;
    margin: 0 0 10px;
    width: 100%;
    font-size: 90%;
    color: #858585;
}

.ArticleCard {
    max-width: 70%;
    flex: 0 0 70%;
    transition: all 0.5s ease;

    div {
        transition: all 0.5s ease;
    }
}

.AuthorCard {
    display: block;
    transition: all 0.5s ease;
    max-width: 20%;
    flex: 0 0 20%;
}

.space {
    max-width: 3%;
    flex: 0 0 3%;
}

@media (max-width: 1100px) {
    .ArticleCard {
        max-width: 65%;
        flex: 0 0 65%;
        transition: all 0.5s ease;
    }
}

@media (max-width: 900px) {

    .ArticleCard {
        max-width: 95%;
        flex: 0 0 95%;
        transition: all 0.5s ease;
    }

    .AuthorCard {
        max-width: 0%;
        // flex: 0 0 0%;
        transition: all 0.5s ease;

        * {
            display: none;
        }
    }
}
</style>

