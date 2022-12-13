<template>
    <div>
        <el-row style="margin-top: 60px;">
            <el-col class="space"></el-col>
            <el-col class="container">
                <el-row style="display: flex;justify-content: space-between;">
                    <div style="margin:0 0 1% 2.5%;height: 30px; opacity: 0.7;">
                        <el-button v-if="showFlag" type="primary" @click="uploadDialog = true">上传</el-button>
                    </div>
                    <div style="margin:0 2.5% 1% 0;height: 30px; opacity: 0.7;">
                        <el-button v-if="showFlag" type="warning" @click="editAlbumDialog = true">管理相册</el-button>
                    </div>
                </el-row>
                <el-row class="header">
                    <router-link :to="'/AlbumDetail/' + authorId + '_' + item.albumName" class="item"
                        v-for="item in albums" :key="item.id">
                        <el-image fit="cover" class="cover" :src="item.coverImg" lazy />
                        <span class="fenlei">分类</span>
                        <hr class="line" align="center" />
                        <span class="jutifenlei">{{ item.albumName }}</span>
                    </router-link>
                </el-row>
                <el-row class="fengexian">
                    <el-col :span="1"></el-col>
                    <el-col :span="10">
                        <hr width="100%" align="left" />
                    </el-col>
                    <el-col :span="2" style="color: deeppink;">
                        <span>精选</span>
                    </el-col>
                    <el-col :span="10">
                        <hr width="100%" align="right" />
                    </el-col>
                    <el-col :span="1"></el-col>
                </el-row>

                <el-row class="body">
                    <div class="item" v-for=" (item, index) in simplifyImg" :key="index">
                        <el-image class="photo" :src="item" fit="cover" @click="previewImage(index)" />
                    </div>
                </el-row>
            </el-col>
            <el-col class="space"></el-col>
        </el-row>
        <!-- 上传dialog -->
        <el-dialog ref="uploadRef" v-model="uploadDialog" top="60px" width="64%"
            style="min-width: 820px;border-radius: 20px;" :close-on-click-modal="false" :close-on-press-escape=false
            :append-to-body="true">
            <div style="margin-left: 2%;">
                <el-upload v-model:file-list="fileList" ref="uploadRef" list-type="picture-card"
                    :on-preview="handlePictureCardPreview" :on-remove="handleRemove" :http-request="uploadHandler"
                    :on-exceed="exceedHandler" multiple :limit="50" accept=".png, .jpg">
                    <el-icon>
                        <Plus />
                    </el-icon>
                </el-upload>
            </div>
            <!-- <div style="width: 150px;height: 150px;">
                <img :src="test" alt="" style="width: 100%;">
            </div> -->
            <div style="margin: 2% 0 0 2%;">
                <el-button type="primary" @click="upload">上传</el-button>
                <el-button type="info" @click="clearAll">清空全部</el-button>
                <el-button type="success" @click="clearUpload">清空已上传</el-button>
                <el-button type="warning" @click="stopUpload">停止</el-button>
                <el-button type="danger" @click="closeUpload">关闭</el-button>
                <div style="display: inline-block;margin-left: 2%;max-width: 150px;">
                    <el-select v-model="albumName" placeholder="选择相册">
                        <el-option v-for="(item, index) in albumsB" :key="index" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </div>
                <div style="margin-top: 2%;">
                    <el-progress :percentage="progress.percentage" v-show="progress.show" :status="progress.status" />
                </div>
            </div>
        </el-dialog>
        <!-- 相册编辑Dialog -->
        <el-dialog v-model="editAlbumDialog" top="60px" width="50%" style="min-width: 500px;border-radius: 20px;"
            :close-on-click-modal="false" :close-on-press-escape='false' :append-to-body="true" @open="albumDialogOpen">
            <div class="albumItem" v-for="(item, index) in albumsA" :key="index">
                <el-row>
                    <el-col class="albumCover">
                        <el-image class="albumCoverImg" fit="cover" :src="item.coverImg" alt="" lazy />
                    </el-col>
                    <el-col class="albumInfo">
                        <div style="width: 200px;margin-top: 10px;">
                            <el-form ref="albumRef" :model="albumsA[index]" :rules="rules">
                                <el-form-item label="相册名" prop="albumName">
                                    <el-input v-model="albumsA[index].albumName" placeholder=""
                                        :disabled="item.albumName == '全部'">
                                    </el-input>
                                </el-form-item>
                            </el-form>
                        </div>
                        <div style="display: inline-block;width: 150px;margin-top: 10px;">
                            <el-upload ref="editAlbumUploadRef" class="upload-demo" accept=".png, .jpg"
                                :http-request="(file) => { editAlbumUploadHandler(file, index) }" :limit="1"
                                :on-exceed="(files) => { editAlbumHandleExceed(files, index) }">
                                <template #trigger>
                                    <el-button type="primary">选择封面</el-button>
                                </template>
                            </el-upload>
                        </div>
                        <div style="display: inline-block;width: 100px;margin-top: 10px;vertical-align: top;">
                            <el-button type="danger" @click="deleteAlbum(index)" :disabled="item.albumName == '全部'">删除
                            </el-button>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <div style="margin: 2% 0 0 2%;">
                <el-button type="primary" @click="saveEditAlbum">保存</el-button>
                <el-button type="danger" @click="closeEditAlbum">关闭</el-button>
                <el-button type="warning" @click="addAlbum">新增相册</el-button>
            </div>
        </el-dialog>
        <!-- 预览 -->
        <el-dialog v-model="previewDialog" top="60px" :append-to-body="true" style="background-color: transparent;"
            width="60%">
            <div class="imgPreviewBox">
                <img class="img" :src="dialogImageUrl" alt="" />
            </div>
        </el-dialog>


    </div>
</template>

<script>

import { Plus } from '@element-plus/icons-vue'
import API from '../utils/API';



export default {
    components: {
        Plus
    },
    props: ["authorId"],
    data() {
        return {
            //去除全部的相册名称数组
            albumsB: [],
            // 请求得到的相册数组
            albums: [],
            // 用于暂存相册编辑数据
            albumsA: [],
            originalImg: [],
            simplifyImg: [],
            // dialog
            progress: {
                percentage: 0,
                show: false,
                status: ""
            },
            albumName: "",
            fileList: [],

            previewDialog: false,
            dialogImageUrl: "",
            uploadDialog: false,

            editAlbumDialog: false,
            rules: {
                albumName: { required: true, message: "请输入相册名", trigger: 'blur' }
            },
            // 验证相册编辑的表单
            OKFlag: true

        }
    },
    methods: {
        previewImage(index) {
            this.dialogImageUrl = this.originalImg[index]
            this.previewDialog = true
        },
        // 上传图片部分
        handleRemove(uploadFile, uploadFiles) {
            // console.log(uploadFile, uploadFiles)
        },
        handlePictureCardPreview(uploadFile) {
            this.dialogImageUrl = uploadFile.url
            this.previewDialog = true
        },
        dealImage(rawbase64, size) {
            // size 单位KB
            var newImage = new Image();
            newImage.src = rawbase64;
            return new Promise(resolve => {
                newImage.onload = function () {
                    let imgWidth = this.width;
                    let imgHeight = this.height;
                    let canvas = document.createElement("canvas");
                    let ctx = canvas.getContext("2d");
                    let maxLenth = imgWidth > imgHeight ? imgWidth : imgHeight
                    // 小于 1920 的图片不压缩 压缩系数0-1之间
                    let quality = 1;
                    quality = quality < 0.7 ? 0.7 : quality // 最小定到0.7
                    // 设置画布大小 
                    if (maxLenth > size * 2) {
                        let rate = imgWidth / imgHeight
                        canvas.width = size * 2;
                        canvas.height = size * 2 / rate;
                        // quality = 1 / (maxLenth / (size * 2));
                    } else {
                        canvas.width = imgWidth
                        canvas.height = imgHeight
                        quality = 1
                    }
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(this, 0, 0, canvas.width, canvas.height);
                    var base64 = canvas.toDataURL("image/webp", quality);
                    // 如想确保图片压缩 size 尺寸
                    console.log("size:", base64.length * 0.75 / 1024, " targetSize:", size, " base64Size:", base64.length, " quality:", quality);

                    let newSize = base64.length * 0.75 / 1024
                    while (newSize > size) {
                        quality -= 0.02;
                        base64 = canvas.toDataURL("image/webp", quality);
                        newSize = base64.length * 0.75 / 1024
                        console.log("size:", base64.length * 0.75 / 1024, " targetSize:", size, " base64Size:", base64.length, " quality:", quality);
                    }

                    // 压缩过度 适当提高一点 性能权衡 
                    // while (newSize < size - 100) {
                    //     quality += 0.05
                    //     if (quality > 0.9) break
                    //     base64 = canvas.toDataURL("image/webp", quality);
                    //     newSize = base64.length * 0.75 / 1024
                    //     // console.log("size:", base64.length * 0.75 / 1024, " targetSize:", size, " base64Size:", base64.length);
                    // }
                    // console.log(base64);
                    resolve(base64)
                }
            })
        },
        uploadHandler() {
            this.progress.unit = Math.round((1 / this.fileList.length) * 100)
        },
        exceedHandler() {
            ElMessage({
                showClose: true,
                message: "图片数量超出限制",
                type: 'warning',
            })
        },
        stopUpload() {
            this.fileList.forEach(e => {
                this.$refs.uploadRef.abort(e.raw)
            })
        },
        // 上传图片操作
        async upload() {
            if (this.albumName == "") {
                ElMessage({
                    showClose: true,
                    message: "请选择相册",
                    type: 'warning',
                })
                return
            }

            let n = 0
            this.fileList.forEach(e => {
                if (e.status == "ready") {
                    e.status = "uploading"
                    e.percentage = 0
                    n++
                }
            })
            this.progress.status = ""
            this.progress.show = true
            this.progress.percentage = 0
            this.progress.unit = Math.round((1 / n) * 100)
            let time = new Date().getTime()
            n = 1
            for (let e of this.fileList) {
                if (e.status == "uploading") {
                    e.id = n++
                    await this.uploadFunc(e)
                    // this.uploadFunc(e)
                }
            }
            this.initImages()
            // console.log("total time:", new Date().getTime() - time);
        },
        async uploadFunc(e) {
            return new Promise(resolve => {
                let data = {
                    albumName: this.albumName,
                    originalImg: "",
                    simplifyImg: "",
                }
                let time = new Date().getTime()
                var reader = new FileReader()
                reader.readAsDataURL(e.raw)
                reader.onload = async re => {
                    console.log("任务", e.id, "  begin time:", time, "  load time", new Date().getTime() - time);
                    time = new Date().getTime()
                    data.originalImg = await this.dealImage(re.target.result, 1000)
                    data.simplifyImg = await this.dealImage(data.originalImg, 200)
                    console.log("canvas time", new Date().getTime() - time);
                    await API.post("image/add", data,
                        (progressEvent) => {
                            e.percentage = (progressEvent.loaded / progressEvent.total * 100 | 0)
                        })
                        .then(res => {
                            if (res.code == 200) {
                                e.status = "success"
                                if (this.progress.percentage + this.progress.unit > 95) {
                                    this.progress.percentage = 100
                                    this.progress.status = "success"
                                }
                                else {
                                    this.progress.percentage += this.progress.unit
                                }
                            } else {
                                e.status = "ready"
                                this.progress.status = "warning"
                            }
                            resolve()
                        }).catch(err => {
                            e.status = "ready"
                            this.progress.status = "exception"
                            resolve()
                        })
                    resolve()
                }
            })

        },
        clearAll() {
            this.fileList = []
        },
        clearUpload() {
            let len = this.fileList.length
            for (let i = 0; i < len; i++) {
                if (this.fileList[i].status == "success") {
                    this.fileList.splice(i, 1)
                    i--;
                    len--;
                }
            }
        },
        closeUpload() {
            this.uploadDialog = false
            this.progress.show = false
            this.progress.status = ""
            this.progress.percentage = 0
        },
        // 以下为 相册部分功能函数
        saveEditAlbum() {
            let data = []
            this.albumsA.forEach(e => {
                let a = {};
                a.id = e.id
                a.author = ""
                a.albumName = e.albumName
                if (!e.imgChange) a.coverImg = ""
                else a.coverImg = e.coverImg
                data.push(a)
            })
            this.OKFlag = true
            this.$refs.albumRef.forEach(e => {
                e.validate(valid => {
                    if (!valid) this.OKFlag = false
                });
            })
            setTimeout(() => {
                if (this.OKFlag) {
                    API.post('album/update', data)
                        .then(res => {
                            if (res.code == 200) {
                                ElMessage({
                                    showClose: true,
                                    message: "编辑成功",
                                    type: 'success',
                                })
                                console.log(res.data);
                                this.editAlbumDialog = false
                                // 添加到本地数据，就不刷新页面了
                                this.initAlbums()
                                this.initImages()
                            }
                        })
                }
            }, 100);
        },
        closeEditAlbum() {
            this.editAlbumDialog = false
        },
        editAlbumUploadHandler(file, index) {
            let reader = new FileReader()
            reader.readAsDataURL(file.file)
            reader.onload = async e => {
                this.albumsA[index].coverImg = await this.dealImage(e.target.result, 1000)
                this.albumsA[index].imgChange = true
            }
        },
        editAlbumHandleExceed(files, index) {
            this.$refs.editAlbumUploadRef[index].clearFiles()
            this.$refs.editAlbumUploadRef[index].handleStart(files[0])
            let reader = new FileReader()
            reader.readAsDataURL(files[0])
            reader.onload = async e => {
                this.albumsA[index].coverImg = await this.dealImage(e.target.result, 1000)
            }
        },
        deleteAlbum(index) {
            // 本地删除，点击保存提交更改
            ElMessageBox.confirm('删除相册会删除相册全部相片，是否确认?', '提示框',
                {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '是',
                    cancelButtonText: '否',
                    type: 'warning',
                    customStyle: { top: "-20% !important", position: "relative" },
                }).then(() => {
                    this.albumsA.splice(index, 1)
                }).catch((action) => {
                    if (action == 'cancel') {
                        // 点击关闭 关闭弹窗回到主页面
                    } else {
                        // 按ESC 啥也不干
                    }
                })
        },
        addAlbum() {
            let album = {
                id: -1,
                authorId: 0,
                albumName: "新增分类",
                coverImg: ""
            }
            this.albumsA.push(album)
        },
        albumDialogOpen() {
            this.albumsA = []
            this.albums.forEach(e => {
                // 深度复制一份
                e.imgChange = false
                this.albumsA.push(JSON.parse(JSON.stringify(e)))
            })
        },
        initAlbums() {
            // 获取相册信息
            let data = {
                authorId: this.authorId,
            }
            API.get("init/getAlbums", data)
                .then(res => {
                    if (res.code == 200) {
                        // 刷新信息有关信息
                        this.albums = []
                        this.albumsB = []
                        res.data.forEach(e => {
                            e.coverImg = this.baseUrl + e.coverImg
                            if (e.albumName != "全部") {
                                this.albumsB.push(e.albumName)
                            }
                            this.albums.push(e)
                        })
                        // 扔到 store
                        this.$store.commit("setAlbums", res.data)
                    }
                })
        },
        initImages() {
            // 获取初始图片
            let data = {
                authorId: this.authorId,
                startPage: 0,
                pageSize: 8
            }
            this.originalImg = []
            this.simplifyImg = []
            API.get("init/getImages", data)
                .then(res => {
                    if (res.code == 200) {
                        res.data.forEach(e => {
                            this.originalImg.push(this.baseUrl + e.originalImg)
                            this.simplifyImg.push(this.baseUrl + e.simplifyImg)
                        })
                    }
                })
        }
    },
    computed: {
        showFlag() {
            // 登录并且当前访问的authorId 等于登录 Id
            return (this.$store.state.isLogin && this.authorId == this.$store.state.author.username)
        }
    },
    watch: {

    },
    mounted() {
        this.initAlbums()
        this.initImages()
    },
}
</script>

<style lang='less' scoped>
@transition: all 0.5s ease-in-out;

.container {
    max-width: 96%;
    flex: 0 0 96%;
}

.header {
    transition: @transition;
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

    .cover {
        opacity: 0.7;
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
    }

    .line {
        border-color: antiquewhite;
        background-color: antiquewhite;
        color: antiquewhite;
        width: 100px;
        transition: @transition;
    }

    .fenlei {
        display: block;
        font-size: 20px;
        margin-top: 20%;
        transition: @transition;
    }

    .jutifenlei {
        color: #17ed97;
        display: block;
        font-size: 30px;
        transition: @transition;
    }

    &:hover {
        .line {
            width: 100%;
        }

        .jutifenlei {
            // color: skyblue;
            font-size: 40px;
        }

        .cover {
            transform: scale(1.1, 1.1);
            opacity: 0.5;
        }

        .photo {
            transform: scale(1.1, 1.1);
        }
    }
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


.albumItem {
    margin: 2%;
    height: 150px;
}

.albumCover {
    border-radius: 10px;
    flex: 0 0 40%;
    display: inline-block;
    height: 150px;
    max-width: 400px;
    min-width: 200px;
}

.albumCoverImg {
    border-radius: 10px;
    height: 100%;
    width: 100%;
    object-fit: cover;
}

.albumInfo {
    margin-left: 8%;
    max-width: 40%;
    flex: 0 0 40%;
}
</style>