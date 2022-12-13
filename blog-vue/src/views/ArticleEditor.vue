<template>
    <el-dialog style="border-radius: 10px; z-index: 2;margin-bottom: 100px;" v-model="editDialog" top="60px" width="90%"
        :close-on-click-modal=false :close-on-press-escape=false @close="closeHandler" :append-to-body="true">
        <el-form id="myform" :model="article" ref="articleRef" :rules="rules">
            <div class="header">
                <el-row>
                    <el-col :span="12">
                        <div class="title">
                            <el-form-item prop="title">
                                <el-input style="font-size: 20px;" size="large" v-model="article.title"
                                    placeholder="标题">
                                </el-input>
                            </el-form-item>
                        </div>
                        <div class="info">
                            <el-row>
                                <el-col :span="7">
                                    <el-form-item prop="category">
                                        <el-select v-model="article.category" placeholder="选择分类">
                                            <el-option v-for="(c, index) in categories" :key="index" :label="c"
                                                :value="c">
                                            </el-option>
                                            <div style="margin: 0 15px;">
                                                <el-input v-model="newCategory" placeholder="新建分类" type="text"
                                                    @keyup.enter="addCategory"></el-input>
                                            </div>
                                        </el-select>
                                    </el-form-item>
                                </el-col>

                                <el-col :span="1"></el-col>
                                <el-col :span="7">
                                    <el-form-item prop="tag">
                                        <el-select v-model="article.tag" placeholder="选择标签" collapse-tags
                                            :multiple="true" clearable>
                                            <el-option v-for="(c, index) in tags" :key="index" :label="c" :value="c">
                                            </el-option>
                                            <div style="margin: 0 15px;">
                                                <el-input v-model="newTag" placeholder="新建标签" type="text"
                                                    @keyup.enter="addTag">
                                                </el-input>
                                            </div>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="1"></el-col>
                                <el-col :span="8">
                                    <el-upload ref="uploadRef" class="upload-demo" name="illustrate" accept=".png, .jpg"
                                        :http-request="uploadHandler" :limit="1" :on-exceed="handleExceed">
                                        <template #trigger>
                                            <el-button type="primary">选择插图</el-button>
                                        </template>
                                    </el-upload>
                                </el-col>
                            </el-row>
                        </div>
                    </el-col>

                    <el-col :span="1"></el-col>

                    <el-col :span="6">
                        <transition name="el-zoom-in-top">
                            <div style="height:150px;width: 250px; translate:0 -30px ;">
                                <el-image class="img" fit="cover" :src="article.img" />
                            </div>
                        </transition>
                    </el-col>
                    <el-col :span="4">
                        <div>
                            <el-button type="primary" @click="saveToScript">保存到草稿</el-button>
                        </div>
                        <div style="margin: 10px 0;">
                            <el-button type="primary" @click="saveAndIssue">发表</el-button>
                        </div>
                        <div style="margin: 10px 0;">
                            <el-button type="danger" @click="closeAndSaveToScript">关闭</el-button>
                        </div>
                    </el-col>

                </el-row>
            </div>
            <div class="content">
                <div class="input-body">
                    <el-input style="font-size: 16px;height: 100%;" type="textarea" v-model="article.content"
                        resize="none" placeholder="文章内容"></el-input>
                </div>
                <div class="resize" @mousedown="drapContent" @dblclick="resetSize"></div>
                <div class="markdown-body" v-html="htmlContent"></div>
            </div>
        </el-form>
        <el-button type="primary" @click="saveToScript">保存到草稿</el-button>
        <el-button type="primary" @click="saveAndIssue">发表</el-button>
        <el-button type="danger" @click="closeAndSaveToScript">关闭</el-button>

    </el-dialog>
</template>

<script>

import markdownToHtml from '@/utils/markdown'
import { Bottom } from '@element-plus/icons-vue';
import API from '../utils/API';
export default {
    components: {
    },
    props: {
        tags: Array,
        categories: Array,
        editFlag: Bottom,
    },
    data() {
        return {
            imgChange: false,
            newCategory: "",
            newTag: "",
            rules: {
                title: { required: true, message: "请输入标题", trigger: 'blur' },
                category: { required: true, message: "请选择分类", trigger: 'blur' },
                tag: { required: true, message: "请选择标签", trigger: 'blur' }
            }
        };
    },
    methods: {
        test() {

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
                    while (base64.length / 1024 > 1000) {
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
            let reader = new FileReader()
            reader.readAsDataURL(file.file)
            reader.onload = async e => {
                this.article.img = await this.dealImage(e.target.result)
            }
            this.imgChange = true
        },
        handleExceed(files) {
            this.$refs.uploadRef.clearFiles()
            this.$refs.uploadRef.handleStart(files[0])
            let reader = new FileReader()
            reader.readAsDataURL(files[0])
            reader.onload = async e => {
                this.article.img = await this.dealImage(e.target.result)
            }
        },
        addCategory() {
            console.log("add new category", this.newCategory);
            this.categories.push(this.newCategory)
            this.article.category = this.newCategory
            this.newCategory = ""
        },
        addTag() {
            console.log("add new tag", this.newTag);
            this.tags.push(this.newTag)
            this.article.tag.push(this.newTag)
            this.newTag = ""
        },
        drapContent(e) {
            let left = document.getElementsByClassName("input-body")[0];
            let right = document.getElementsByClassName('markdown-body')[0];

            let leftWid = window.getComputedStyle(left).getPropertyValue('width')
            let rightWid = window.getComputedStyle(right).getPropertyValue('width')

            let oldX = e.clientX;
            document.onmousemove = function (e) {
                // 移动的距离
                let diffX = (e.clientX - oldX);
                if (leftWid.slice(0, -2) > 100 && rightWid.slice(0, -2) > 100) {
                    // 左右宽度大于100时可拉动
                    left.style.width = `calc(${leftWid} + ${diffX}px)`;
                    right.style.width = `calc(${rightWid} - ${diffX}px)`;
                    // console.log("left:", leftWid, "         rirgt:", rightWid, "  diffX", diffX);
                }
            }
            document.onmouseup = function () {
                document.onmousemove = null;
                document.onmouseup = null;
            }
        },
        resetSize() {
            let left = document.getElementsByClassName("markdown")[0];
            let right = document.getElementsByClassName('html')[0];
            left.style.width = `50%`;
            right.style.width = `calc(50% - 20px)`;
            document.onmouseup = function () {
                document.onmousemove = null;
                document.onmouseup = null;
            }
        },
        saveToScript() {

            // this.editDialog = false
        },
        saveAndIssue() {
            this.$refs['articleRef'].validate((valid) => {
                if (valid) {
                    if (this.article.img == "") {
                        ElMessage({
                            showClose: true,
                            message: "请选择一张插图",
                            type: 'warning',
                        })
                        return
                    }
                    if (this.article.content == "") {
                        ElMessage({
                            showClose: true,
                            message: "内容不能为空",
                            type: 'warning',
                        })
                        return
                    }
                    if (this.editFlag) {
                        // 修改
                        let article = this.article
                        if (!this.imgChange) {
                            // 图片未改动
                            article.img = ""
                        }
                        API.post("article/update", article)
                            .then(res => {
                                if (res.code == 200) {
                                    ElMessage({
                                        type: 'success',
                                        message: '修改成功',
                                    })
                                }
                            })
                    } else {
                        // 新增
                        API.post("article/add", this.article)
                            .then(res => {
                                if (res.code == 200) {
                                    ElMessage({
                                        type: 'success',
                                        message: '发表成功',
                                    })
                                }
                            })
                    }
                    this.editDialog = false
                    setTimeout(() => {
                        location.reload()
                    }, 500);
                }
            })

        },
        resetForm() {
            this.$refs['articleRef'].resetFields()
        },
        closeAndSaveToScript() {
            this.editDialog = false
        },
        closeHandler() {
            this.$refs.uploadRef.clearFiles()
            //清除验证结果
            setTimeout(() => {
                this.resetForm()
            }, 100);
        }
    },
    computed: {
        htmlContent() {
            return markdownToHtml(this.article.content)
        },
        editDialog: {
            get() {
                return this.$store.state.editDialog
            },
            set(value) {
                this.$store.state.editDialog = value
            }
        },
        article() {
            return this.$store.state.article
        }
    },
    watch: {

    },
    mounted() {

    }
}

</script>

<style lang='less' scoped>
.header {
    .title {
        margin: 0 0 30px 0;
        width: 80%;
    }

    .info {
        margin: 0 0 20px 0;
    }

    .img {
        height: 100%;
        width: 100%;
        object-fit: cover;
        vertical-align: middle;
    }
}

.content {
    z-index: 20;
    position: relative;
    justify-content: space-between;
    display: flex;
    width: 100%;
    min-height: 500px;
    margin: 0 0 20px 0;

    .input-body {
        width: 50%;

    }

    .resize {
        position: sticky;
        top: 25vh;
        cursor: col-resize;
        width: 8px;
        height: 100px;
        margin: calc(25% - 100px) 5px;
        background: linear-gradient(45deg, rgba(255, 13, 0, 0.6), rgba(51, 255, 0, 0.6), rgba(0, 251, 255, 0.6), rgba(0, 60, 255, 0.6), rgba(230, 0, 255, 0.6));
        border-radius: 5px;
    }

    .markdown-body {
        overflow-y: auto;
        overflow-x: hidden;
        border-radius: 5px;
        border: 1px rgb(214, 214, 214) solid;
        font-size: 16px;
        width: 48%;
        min-width: 180px;
        padding: 0 5px;
    }
}
</style>

