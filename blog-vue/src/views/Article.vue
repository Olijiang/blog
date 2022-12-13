<template>
    <div class="body">
        <div style="display: flex;justify-content: space-between;">
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

        <div v-for="(article, index) in articleList" :key="article.id" :data-index="index">
            <transition name="el-zoom-in-top">
                <div class="article" v-show="article.show">
                    <div class="content">
                        <div>
                            <span class="title" @click="articleDeail(article.id)">{{ article.title }} &#160;</span>
                            <span> 时间:</span>
                            <span style="color: #d63a3a;font-size: 90%;">{{ article.createTime }} &#160;</span>
                            <span> 分类:</span>
                            <span style="color: #d63a3a;font-size: 90%;"> {{ article.category }} &#160;</span>
                            <span> 标签:</span>
                            <span style="color: #d63a3a;font-size: 90%;" v-for=" (item, index) in article.tag"
                                :key="index">
                                {{ item }}
                                <span style="color: #00c6a5;"> </span>
                            </span>

                        </div>
                        <div style="margin: 10px 0; text-indent: 2em;line-height: 20px;">{{ article.digest }}
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
                pageSize: 7
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
                                    this.queryData.startPage = this.queryData.startPage + 7
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
                    element.tag = JSON.parse(element.tag).tags
                    element.digest = element.digest.replace(/#*.*#/g, '').replace(/[^a-z0-9\u4e00-\u9fa5]/, '').substring(0, 200) // 除去标题部分，截取200个字用来显示
                    element.show = true
                    this.articleList.push(element)
                });
                this.queryData.startPage = this.queryData.startPage + 7
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
.body {
    text-align: left;
    margin: 60px 3% 30px 3%;
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

.article {
    display: flex;
    justify-content: space-between;

    border: 1px solid rgba(220, 220, 220, 0.6);
    // box-shadow: 0 0px 5px rgba(221, 221, 221, 0.6);
    border-radius: 10px;
    overflow: hidden;
    margin: 15px 0;
    background-color: rgba(255, 255, 255, 0.3);
    padding: 0 10px;
    height: 80px !important;
    transition: all 0.5s;

    &:hover {
        border: 1px solid #5dfaff;
        box-shadow: 0 0 10px #5dfaff;
        transition: all 0.2s ease-in-out;
    }

    .title {
        margin-top: 5px;
        font-size: 20px;
        color: #4b9797;
        display: inline-block;
        cursor: pointer;
        transition: all 0.2s;

        &:hover {
            color: #04d6a2;
        }
    }
}

.content {
    width: 85%;
}

.but {
    margin: auto 0;
    width: 160px;

    .buttom {
        opacity: 0.7;
        display: inline-block;
        margin: 0 0 0 10px;
    }
}

@media(max-width: 650px) {
    .article {
        padding-left: 20px;
        height: 170px !important;
        justify-content: flex-end;

        .title {
            display: block;
            margin-top: 10px;
            margin-bottom: 10px;
        }
    }

    .but {
        margin: auto 0;
        width: 80px;

        .buttom {
            display: block;
            margin: 10px 0 0 20px;
        }
    }
}
</style>