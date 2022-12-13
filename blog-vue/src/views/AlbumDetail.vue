<template>
    <div>
        <transition name="el-fade-in">
            <div class="illustration">
                <el-image fit="cover" class="img" :src=coverImg alt="" lazy />
                <div class="ArticleInfo">
                    <h1 style="margin: 20px;color:rgb(9, 214, 180);font-size: 100px;">{{ albumName }} </h1>
                    <router-link :to="'/Album/' + authorId">
                        <span style="color:#06a0e9;font-size: 30px;">
                            相册
                        </span>
                    </router-link>

                </div>
            </div>
        </transition>
        <el-row style="margin-top: 30px;">
            <el-col class="space"></el-col>
            <el-col class="container">
                <el-row>
                    <div style="margin:0 0 1% 2.5%;height: 30px; opacity: 0.7;">
                        <el-button v-if="showFlag" type="primary" @click="management">管理</el-button>
                    </div>
                    <transition name="el-zoom-in-top">
                        <div style="margin:0 0 1% 1%;height: 30px; opacity: 0.7;" v-if="editFlag">
                            <el-button type="warning" @click="moveImage">移动</el-button>
                            <el-button type="danger" @click="deleteImage">删除</el-button>
                            <el-button type="success" @click="selectAll">全选</el-button>
                        </div>
                    </transition>
                </el-row>

                <el-row class="body">
                    <div class="item" v-for=" (item, index) in images" :key="index">
                        <div v-show="editFlag" @click="selectImage(item)">
                            <el-icon class="selectIcon" v-show="!item.select">
                                <CircleCheckFilled />
                            </el-icon>
                            <el-icon class="selectIcon1" v-show="item.select">
                                <CircleCheckFilled />
                            </el-icon>
                        </div>
                        <transition name="el-zoom-in-top">
                            <div class="photoCloak" v-show="editFlag" @click="selectImage(item)">
                            </div>
                        </transition>
                        <transition name="el-zoom-in-top">
                            <el-image class="photo" :src="item.simplifyImg" fit="cover" v-show="item.ok"
                                @click="previewImage(item.originalImg)" />
                        </transition>
                    </div>
                </el-row>
            </el-col>
            <el-col class="space"></el-col>
        </el-row>
        <div class="endmsg">{{ endmsg }}</div>
        <!-- 预览Dialog -->
        <el-dialog v-model="previewDialog" top="60px" :append-to-body="true" style="background-color: transparent;"
            width="60%">
            <div class="imgPreviewBox">
                <img class="img" :src="dialogImageUrl" />
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { CircleCheckFilled } from '@element-plus/icons-vue'
import API from '../utils/API'


export default {
    components: {
        CircleCheckFilled
    },
    props: ["data"],
    data() {
        return {
            previewDialog: false,
            dialogImageUrl: "",
            editFlag: false,
            authorId: "",
            albumName: "",
            images: [],
            originalImg: [],
            endmsg: "下拉加载更多...",
            queryData: {
                authorId: "",
                albumName: "",
                startPage: 0,
                pageSize: 4
            },
            // 存选择图片的id
            selectedImage: [],
            selectAllFlag: true
        }
    },
    methods: {
        previewImage(src) {
            this.dialogImageUrl = src
            this.previewDialog = true
        },
        handleScroll() {
            clearTimeout(this.timeout)
            this.timeout = setTimeout(() => {
                let scrollTop = document.documentElement.scrollTop;//滚动高度
                let clientHeight = document.documentElement.clientHeight;//可视高度
                let scrollHeight = document.documentElement.scrollHeight;//内容高度
                if (clientHeight + scrollTop - scrollHeight > -10) {
                    this.endmsg = "正在加载..."
                    API.get('init/getImagesByAlbum', this.queryData)
                        .then(res => {
                            if (res.code == 200) {
                                res.data.forEach(e => {
                                    e.ok = false
                                    e.simplifyImg = this.baseUrl + e.simplifyImg
                                    e.originalImg = this.baseUrl + e.originalImg
                                    this.images.push(e)
                                    this.originalImg.push(e.originalImg)
                                })
                                setTimeout(() => {
                                    let len = this.images.length
                                    let i = len - res.data.length
                                    for (i; i < len; i++) {
                                        this.images[i].ok = true
                                    }
                                }, 100);
                                if (res.data.length < this.queryData.pageSize) {
                                    this.endmsg = "没有更多了..."
                                    window.removeEventListener('scroll', this.handleScroll)
                                }
                            }
                            this.queryData.startPage = this.queryData.startPage + 8

                        })
                }
            }, 100);
        },
        management() {
            this.selectedImage = []
            this.editFlag = !this.editFlag
            this.images.forEach(e => {
                e.select = false
            })
        },
        selectImage(item) {
            item.select = !item.select
            if (item.select) {
                this.selectedImage.push(item.id)
            } else {
                this.selectedImage.forEach((e, index) => {
                    if (e.id == item.id) {
                        this.selectedImage.splice(index, 1)
                        return
                    }
                })
            }
        },
        moveImage() {

        },
        deleteImage() {
            if (this.selectedImage.length == 0) {
                ElMessage({
                    showClose: true,
                    message: "请选择图片",
                    type: 'warning',
                })
                return
            }
            ElMessageBox.confirm('确认删除?', '提示框',
                {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '是',
                    cancelButtonText: '否',
                    type: 'warning',
                    customStyle: { top: "-20% !important", position: "relative" },
                }).then(() => {
                    // 删除
                    API.post('image/delete', this.selectedImage)
                        .then(res => {
                            if (res.code == 200) {
                                // 移除本地的图片
                                this.selectedImage.forEach(id => {
                                    this.images.forEach((e, index) => {
                                        if (e.id == id) {
                                            this.images.splice(index, 1)
                                            return
                                        }
                                    })
                                })
                                ElMessage({
                                    showClose: true,
                                    message: res.message,
                                    type: 'success',
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
        selectAll() {
            this.selectedImage = []
            if (this.selectAllFlag) {
                this.images.forEach((e) => {
                    e.select = true
                    this.selectedImage.push(e.id)
                })
            } else {
                this.images.forEach((e) => {
                    e.select = false
                })
            }
            this.selectAllFlag = !this.selectAllFlag
        },
    },
    computed: {
        showFlag() {
            // 登录并且当前访问的authorId 等于登录 Id
            if (this.$store.state.isLogin && this.authorId == this.$store.state.author.username)
                return true
            else
                return false
        },
        coverImg() {
            let list = this.$store.state.albums
            let len = list.length
            for (let i = 0; i < len; i++) {
                if (this.albumName == list[i].albumName) {
                    return list[i].coverImg
                }
            }
        }
    },
    watch: {

    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll)
        this.authorId = this.data.split("_")[0]
        this.albumName = this.data.split("_")[1]

        this.queryData.authorId = this.authorId
        this.queryData.albumName = this.albumName
        console.log(this.queryData);
        API.get("init/getImagesByAlbum", this.queryData)
            .then(res => {
                if (res.code == 200) {
                    res.data.forEach(e => {
                        e.ok = false
                        e.simplifyImg = this.baseUrl + e.simplifyImg
                        e.originalImg = this.baseUrl + e.originalImg
                        this.images.push(e)
                        this.originalImg.push(e.originalImg)
                    })
                    setTimeout(() => {
                        let len = this.images.length
                        let i = len - res.data.length
                        for (i; i < len; i++) {
                            this.images[i].ok = true
                        }
                    }, 100);
                    if (res.data.length < this.queryData.pageSize) {
                        this.endmsg = "没有更多了..."
                        window.removeEventListener('scroll', this.handleScroll)
                    }
                    this.queryData.startPage = this.queryData.startPage + 4
                    this.queryData.pageSize = 8
                }
            })
    },
    unmounted() {
        window.removeEventListener('scroll', this.handleScroll)
    }
}

</script>

<style lang='less' scoped>
@transition: all 0.5s ease-in-out;

.illustration {
    position: relative;
    width: 100%;
    height: 600px;
    margin-bottom: 20px;
    transition: all 0.5s;

    .img {
        height: 100%;
        width: 100%;
        object-fit: cover;
        vertical-align: middle;
        transition: all 0.5s ease-in-out;
        border-radius: 0px 0px 10px 10px;
    }

    .ArticleInfo {
        user-select: none;
        position: absolute;
        width: 100%;
        // height: 100%;
        // color: white;
        top: 250px;
        margin: 0 auto;
        transition: all 0.3s ease-in-out;
    }
}

.container {
    max-width: 96%;
    flex: 0 0 96%;
}

.body {
    transition: @transition;
}


.space {
    height: 100%;
    max-width: 2%;
    flex: 0 0 2%;
}

.item {
    display: inline-block;
    position: relative;
    cursor: pointer;
    border-radius: 5px;
    height: 200px;
    max-width: 20%;
    flex: 0 0 20%;
    min-width: 200px !important;
    margin: 1% 2.5% 1% 2.5%;
    // border: 1px solid skyblue;
    transition: @transition;
}

@media (max-width: 1060px) {
    .item {
        height: 150px;
        max-width: 20%;
        flex: 0 0 20%;
        min-width: 150px !important;
    }

}

@media (max-width: 800px) {
    .item {
        height: 150px;
        max-width: 45%;
        flex: 0 0 45%;
        min-width: 200px !important;
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

.photo {
    z-index: -1;
    top: 0;
    left: 0;
    position: absolute;
    height: 100%;
    width: 100%;
    // object-fit: cover;
    // vertical-align: middle;
    transition: all 0.5s ease-in-out;
    border-radius: 10px;

    &:hover {
        transform: scale(1.1, 1.1);
    }
}



.photoCloak {
    opacity: 0.5;
    background-color: #b6b1b1;
    z-index: 2;
    top: 0;
    left: 0;
    position: absolute;
    height: 100%;
    width: 100%;
    transition: all 0.5s ease-in-out;
    border-radius: 10px;
}

.selectIcon {
    margin-top: 10%;
    cursor: auto;
    z-index: 3;
    opacity: 1;
    font-size: 50px;
    size: 50px;
}

.selectIcon1 {
    .selectIcon;
    color: #409EFC;
}
</style>