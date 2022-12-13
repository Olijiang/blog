<template>
    <div>
        <el-dialog v-model="uploadDialog" top="60px" width="64%" style="min-width: 820px;border-radius: 20px;"
            :close-on-click-modal="false" :close-on-press-escape=false :append-to-body="true">
            <div style="margin-left: 2%;">
                <el-upload v-model:file-list="fileList" ref="uploadRef" list-type="picture-card"
                    :on-preview="handlePictureCardPreview" :on-remove="handleRemove" :http-request="uploadHandler"
                    :on-exceed="exceedHandler" multiple :limit="50">
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
                <el-button type="primary" @click="clearAll">清空全部</el-button>
                <el-button type="primary" @click="clearUpload">清空已上传</el-button>
                <el-button type="danger" @click="uploadDialog = false">关闭</el-button>
                <div style="display: inline-block;margin-left: 2%;max-width: 150px;">
                    <el-select v-model="albumName" placeholder="选择相册">
                        <el-option v-for="(c, index) in albums" :key="index" :label="c" :value="c">
                        </el-option>
                        <div style="margin: 0 15px;">
                            <el-input v-model="newAlbum" placeholder="新建相册" type="text" @keyup.enter="addAlbum">
                            </el-input>
                        </div>
                    </el-select>
                </div>
                <div style="margin-top: 2%;">
                    <el-progress :percentage="progress.percentage" v-show="progress.show" :status="progress.status" />
                </div>

            </div>


            <el-dialog v-model="dialogVisible" width="70%" top="60px" style="background-color: transparent;">
                <div class="previewDialog">
                    <img :src="dialogImageUrl" />
                </div>
            </el-dialog>
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
    props: {},
    data() {
        return {

            progress: {
                percentage: 0,
                show: false,
                status: ""
            },
            albumName: "",
            newAlbum: "",
            albums: ["人物", "风景", "科幻"],
            isfull: false,
            uploadDialog: true,
            fileList: [],
            dialogImageUrl: "",
            dialogVisible: false
        }
    },
    methods: {
        addAlbum() {
            this.albums.push(this.newAlbum);
            this.albumName = this.newAlbum
            this.newAlbum = ""
        },
        handleRemove(uploadFile, uploadFiles) {
            console.log(uploadFile, uploadFiles)
        },
        handlePictureCardPreview(uploadFile) {
            this.dialogImageUrl = uploadFile.url
            this.dialogVisible = true
        },
        dealImage(rawbase64) {
            var newImage = new Image();
            var quality = 0.9;    //压缩系数0-1之间
            newImage.src = rawbase64;
            newImage.setAttribute("crossOrigin", 'Anonymous');	//url为外域时需要
            var imgWidth, imgHeight;
            return new Promise(resolve => {
                newImage.onload = async function () {
                    imgWidth = this.width;
                    imgHeight = this.height;
                    var canvas = document.createElement("canvas");
                    var ctx = canvas.getContext("2d");
                    let size = 1920 // 设置压缩尺寸大小
                    if (Math.max(imgWidth, imgHeight) > size) {
                        if (imgWidth > imgHeight) {
                            canvas.width = size;
                            canvas.height = size * imgHeight / imgWidth;
                        } else {
                            canvas.height = size;
                            canvas.width = size * imgWidth / imgHeight;
                        }
                    } else {
                        canvas.width = imgWidth;
                        canvas.height = imgHeight;
                        quality = 1;
                    }
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(this, 0, 0, canvas.width, canvas.height);
                    var base64 = canvas.toDataURL("image/jpeg", quality); //压缩语句
                    // 如想确保图片压缩到自己想要的尺寸
                    while (base64.length / 1024 > 500) {
                        quality -= 0.05;
                        base64 = canvas.toDataURL("image/jpeg", quality);
                    }
                    // 防止最后一次压缩低于最低尺寸，只要quality递减合理，无需考虑
                    // while (base64.length / 1024 < 5) {
                    //     quality += 0.01;
                    //     base64 = canvas.toDataURL("image/jpeg", quality);
                    // }
                    resolve(base64)
                }
            })

        },

        uploadHandler(file) {
            let _file = file.file
            // console.log(_file);
            // let reader = new FileReader()
            // reader.readAsDataURL(_file)
            // reader.onload = async e => {
            //     // let base64 = await this.dealImage(e.target.result)
            //     // console.log(base64);
            // }
        },
        exceedHandler() {
            ElMessage({
                showClose: true,
                message: "图片数量超出限制",
                type: 'warning',
            })
        },
        upload() {
            if (this.albumName == "") {
                ElMessage({
                    showClose: true,
                    message: "请选择相册",
                    type: 'warning',
                })
                return
            }
            this.progress.show = true
            this.progress.percentage = 0
            let unit = Math.round((1 / this.fileList.length) * 100)
            this.fileList.forEach(e => {
                if (e.status == "ready") {
                    e.status = "uploading"
                    e.percentage = Math.round(20 * Math.random())
                    let data = {
                        albumName: this.albumName,
                        simplifyImg: "",
                        originalImg: ""
                    }
                    let reader = new FileReader()
                    reader.readAsDataURL(e.raw)
                    reader.onload = async e => {
                        data.originalImg = e.target.result
                        data.simplifyImg = await this.dealImage(e.target.result)
                    }
                    console.log(data);
                    API.post("album/add", data)
                        .then(res => {
                            if (res.code == 200) {
                                e.status = "success"
                                if (this.progress.percentage + unit > 95) {
                                    this.progress.percentage = 100
                                    this.progress.status = "success"
                                }
                                else {
                                    this.progress.percentage += unit
                                }
                            } else {
                                e.status = "fail"
                                this.progress.status = "warning"
                            }
                        }).catch(err => {
                            e.status = "fail"
                            this.progress.status = "exception"
                        })
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
        }
    },
    computed: {

    },
    watch: {

    },
    mounted() {

    },
}

</script>

<style lang='less' scoped>
.previewDialog {
    z-index: 2;
    width: 100%;
    height: 80%;

    img {
        z-index: 10;
        width: 100%;
        object-fit: cover;
    }
}
</style>