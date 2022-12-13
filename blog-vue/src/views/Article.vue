<template>
    <div>
        <div class="headtool">
            <div>
                <div style="width: 200px;display: inline-block;opacity: 0.5;">
                    <el-input v-model="query" />
                </div>
                <div style="margin-left: 20px;display: inline-block;opacity: 0.5;">
                    <el-button color="#626aef" @click="queryHandler">查找</el-button>
                </div>
            </div>

            <div style="margin-left: 20px;opacity: 0.7;" v-if="showFlag">
                <el-button color="#ffae19" @click="addHandler">写文章</el-button>
            </div>
        </div>
        <el-row>
            <div v-for="(article, index) in articleList" :key="article.id" :data-index="index" class="articleCard">
                <transition name="el-zoom-in-top">
                    <div v-show="article.show" style="height: 100%;width: 100%;">
                        <div>
                            <el-image fit="cover" class="cover" :src="article.img" lazy />
                        </div>
                        <div class="content">
                            <div class="title" @click="articleDeail(article.id)">{{ article.title }} &#160;
                            </div>
                            <div class="tagStyle">
                                发表时间: {{ article.createTime }}
                            </div>
                            <div class="tagStyle">
                                分类: {{ article.category }}
                            </div>
                            <div class="tagStyle">
                                标签:
                                <span v-for=" (item, index) in article.tag" :key="index">
                                    {{ item }}
                                    <span style="color: #00c6a5;"> </span>
                                </span>
                            </div>
                        </div>
                        <div class="but">
                            <div class="buttom" v-if="showFlag">
                                <el-button type="primary" @click="editHandler(article.id)">编辑</el-button>
                            </div>
                            <div class="buttom" v-if="showFlag">
                                <el-button type="danger" @click="deleteHandler(article.id)">删除</el-button>
                            </div>
                        </div>
                    </div>
                </transition>
            </div>
        </el-row>
        <div class="endmsg">{{ endmsg }}</div>
        <ArticleEditorVue :categories="categories" :tags="tags" :editFlag=editFlag></ArticleEditorVue>
    </div>

</template>

<script>

import API from '../utils/API'
import ArticleEditorVue from './ArticleEditor.vue'
export default {
    components: {
        ArticleEditorVue,

    },
    props: ["authorId"],
    data() {
        return {
            endmsg: '下拉加载更多',
            editFlag: false,
            query: "",
            articleList: [],
            queryData: {
                authorId: "",
                startPage: 0,
                pageSize: 12
            },
            categories: [],
            tags: []
        }
    },
    methods: {

        queryHandler() {

        },
        addHandler() {
            //清除arcticle
            this.$store.commit("clearArticle")
            this.editDialog = true
            this.editFlag = false
        },
        editHandler(aricleId) {
            // 文章信息
            let data = { "ArticleId": aricleId }
            API.get('init/getArticle', data)
                .then(res => {
                    if (res.code == 200) {
                        res.data.img = this.baseUrl + res.data.img
                        this.$store.commit("setArticle", res.data)
                        setTimeout(() => {
                            this.editFlag = true
                            this.editDialog = true
                        }, 100);
                    }
                })
        },
        deleteHandler(articleId) {
            ElMessageBox.confirm('确认删除?', '提示框',
                {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '是',
                    cancelButtonText: '否',
                    type: 'warning',
                    customStyle: { top: "-20% !important", position: "relative" },
                }).then(() => {
                    // 删除
                    API.get('article/delete', { articleId: articleId })
                        .then(res => {
                            if (res.code == 200) {
                                this.articleList.forEach((e, index) => {
                                    if (e.id == articleId) {
                                        e.show = false
                                        setTimeout(() => {
                                            this.articleList.splice(index, 1)
                                            ElMessage({
                                                showClose: true,
                                                message: "删除成功",
                                                type: 'success',
                                            })
                                        }, 500);
                                        return
                                    }
                                })

                            }
                        })
                }).catch((action) => {
                    if (action == 'cancel') {
                        // 点击关闭 关闭弹窗回到主页面
                    } else {
                        // 按ESC 啥也不干
                    }
                })

        },
        articleDeail(value) {
            this.$router.push({
                path: "/ArticleDetail/" + value
            })
        },
        handleScroll() {
            clearTimeout(this.timeout)
            this.timeout = setTimeout(() => {
                let scrollTop = document.documentElement.scrollTop;//滚动高度
                let clientHeight = document.documentElement.clientHeight;//可视高度
                let scrollHeight = document.documentElement.scrollHeight;//内容高度
                if (clientHeight + scrollTop - scrollHeight > -10) {
                    this.endmsg = "正在加载..."
                    API.get('init/getArticles', this.queryData)
                        .then(res => {
                            if (res.code == 200) {
                                // console.log(res.data);
                                res.data.forEach(element => {
                                    element.tag = JSON.parse(element.tag).tags
                                    element.digest = element.digest.replace(/#*.*#/g, '').replace(/[^a-z0-9\u4e00-\u9fa5]/, '').substring(0, 200) // 除去标题部分，截取200个字用来显示
                                    element.show = false
                                    this.articleList.push(element)
                                });
                                if (res.data.length < this.queryData.pageSize) {
                                    this.endmsg = "没有更多了..."
                                    window.removeEventListener('scroll', this.handleScroll)
                                }
                                setTimeout(() => {
                                    for (let i = this.queryData.startPage; i < this.queryData.startPage + res.data.length; i++) {
                                        this.articleList[i].show = true
                                    }
                                    this.queryData.startPage = this.queryData.startPage + 16
                                }, 100);
                            }
                        })
                }
            }, 100);
        }
    },
    computed: {
        editDialog: {
            get() {
                return this.$store.state.editDialog
            },
            set(value) {
                this.$store.state.editDialog = value
            }
        },
        showFlag() {
            // 登录并且当前访问的authorId 等于登录 Id
            if (this.$store.state.isLogin && this.authorId == this.$store.state.author.username)
                return true
            else
                return false
        }
    },
    watch: {

    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll)
        // 获取文章
        this.queryData.authorId = this.authorId
        API.get('init/getArticles', this.queryData)
            .then(res => {
                res.data.forEach(element => {
                    element.img = this.baseUrl + element.img
                    element.tag = JSON.parse(element.tag).tags
                    element.digest = element.digest.replace(/#*.*#/g, '').replace(/[^a-z0-9\u4e00-\u9fa5]/, '').substring(0, 200) // 除去标题部分，截取200个字用来显示
                    element.show = true
                    this.articleList.push(element)
                });
                this.queryData.startPage = this.queryData.startPage + 16
                if (res.data.length < this.queryData.pageSize) {
                    this.endmsg = "没有更多了..."
                    window.removeEventListener('scroll', this.handleScroll)
                }
            })

        let data = { "authorId": this.authorId }
        API.get('init/getTags', data)
            .then(res => {
                this.tags = res.data
                if (this.tags.length == 0) {
                    this.tags = ['默认']
                }
            })
        API.get('init/getCategories', data)
            .then(res => {
                this.categories = res.data
                if (this.categories.length == 0) {
                    this.categories = ['默认']
                }
            })
    },
    unmounted() {
        window.removeEventListener('scroll', this.handleScroll)
    }
}

</script>

<style lang='less' scoped>
.headtool {
    display: flex;
    justify-content: space-between;
    text-align: left;
    margin: 60px 3% 10px 3%;
}

.tagStyle {
    color: #ffffff;
    font-size: 90%
}

.endmsg {
    user-select: none;
    position: relative;
    margin: 0 0 10px;
    text-align: center;
    width: 100%;
    font-size: 90%;
    color: #858585;
}

.articleCard {
    display: inline-block;
    position: relative;
    border-radius: 10px;
    height: 200px;
    max-width: 20%;
    flex: 0 0 20%;
    min-width: 200px !important;
    margin: 20px 2.5% 20px 2.5%;
    transition: all 0.5s;

    .cover {
        opacity: 0.5;
        z-index: -1;
        top: 0;
        left: 0;
        position: absolute;
        height: 100%;
        width: 100%;
        transition: all 0.5s ease-in-out;
        border-radius: 10px;
    }

    .content {
        width: 100%;
        height: 100%;
        overflow: hidden;
        background-color: rgba(137, 133, 133, 0.5);
        border-radius: 10px;
        user-select: none;
    }

    .title {
        margin: 20px 10px;
        text-align: center;
        font-size: 25px;
        color: #ffffff;
        display: inline-block;
        cursor: pointer;
        transition: all 0.2s;

        &:hover {
            color: #04d6a2;
        }
    }

    .but {
        margin: auto;
        width: 160px;
        opacity: 0;
        transition: all 0.5s;

        .buttom {
            opacity: 0.7;
            display: inline-block;
            margin: 5px;
        }
    }

    &:hover {
        box-shadow: 0 0 10px #5ae7af;
        transition: all 0.2s ease-in-out;

        .but {
            transition-delay: 0.5s;
            opacity: 1;
        }
    }
}


@media(max-width: 1010px) {
    .articleCard {
        max-width: 28%;
        flex: 0 0 30%;
        min-width: 200px !important;
        margin: 20px 2.5% 20px 2.5%;
        transition: all 0.5s;
    }
}

@media(max-width: 720px) {
    .articleCard {
        max-width: 45%;
        flex: 0 0 45%;
        min-width: 200px !important;
        margin: 20px 2.5% 20px 2.5%;
        transition: all 0.5s;
    }
}
</style>