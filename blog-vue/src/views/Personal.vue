<template>
    <div>

        <transition name="el-fade-in">
            <div class="illustration">
                <el-image fit="cover" class="img" :src=author.coverImg alt="" />
                <div class="authorInfo">
                    <h1 style="margin: 200px 0 20px 0;color:white;font-size: 50px;"> {{ author.blogName }}
                    </h1>
                    <p style="margin: 10px;">{{ author.blogInfo }}</p>
                </div>
            </div>
        </transition>

        <div class="body">
            <!-- 封面 -->
            <div style="margin: 1%;">
                <div class="setOne">
                    <div class="btOne">
                        <el-upload ref="coverUploadRef" class="upload-demo" accept=".png, .jpg"
                            :http-request="coverUploadHandler" :limit="1" :on-exceed="coverHandleExceed">
                            <template #trigger>
                                <el-button type="primary">设置封面</el-button>
                            </template>
                        </el-upload>
                    </div>
                    <div class="btOne">
                        <el-upload ref="headImgUploadRef" class="upload-demo" accept=".png, .jpg"
                            :http-request="headImgUploadHandler" :limit="1" :on-exceed="headImgHandleExceed">
                            <template #trigger>
                                <el-button type="primary">设置头像</el-button>
                            </template>
                        </el-upload>
                    </div>
                    <div class="btOne">
                        <el-button type="primary" @click="resetPwd">修改密码</el-button>
                    </div>
                </div>
                <div style="margin-top: 40px;width: 150px;">
                    <el-button v-if="changeFlag" type="success" @click="submit"> 点击保存 </el-button>
                    <el-button v-else type="success" :disabled="!changeFlag"> 已保存 </el-button>
                </div>
            </div>
            <!-- 头像 -->
            <div style="margin: 1%;">
                <div class="setTwo">
                    <el-image fit="cover" class="img" :src=author.img alt="" />
                </div>
            </div>

            <!-- 博客信息 -->
            <div style="margin: 1%;">
                <div class="setThree">
                    <el-form :model="author">
                        <el-form-item label="博客名称" class="inputBox">
                            <el-input v-model="author.blogName" placeholder="" maxlength="20"></el-input>
                        </el-form-item>
                        <el-form-item label="博客简介" class="inputBox">
                            <el-input v-model="author.blogInfo" placeholder="" maxlength="20"></el-input></el-form-item>
                        <el-form-item label="作者名称" class="inputBox">
                            <el-input v-model="author.authorName" placeholder=""
                                maxlength="20"></el-input></el-form-item>
                        <el-form-item label="作者简介" class="inputBox">
                            <el-input v-model="author.authorInfo" placeholder=""
                                maxlength="20"></el-input></el-form-item>
                    </el-form>

                </div>
            </div>
            <!-- 管理分类和标签 -->
            <div style="margin: 1%;">
                <div class="setFour">
                    <div style="position: absolute;margin-left: 40px;">
                        分类
                        <el-checkbox v-model="selectAllCa" :indeterminate="isIndeterminateCa"
                            @change="selectAllChangeCa">全选</el-checkbox>
                        <span v-if="catChange" class="deleteBut" @click="deleteCa">删除</span>
                    </div>
                    <div style="margin-top: 40px;height: calc(100% - 40px); overflow-y: auto;overflow-x: hidden;">
                        <el-checkbox-group v-model="selectedCa" @change="selectChangeCa">
                            <el-checkbox v-for="ca in categories" :key="ca" :label="ca"
                                style="display: block;font-size: 20px !important;text-align: left;margin-left: 20px;">
                                {{ ca.category }} ({{ ca.articleNum }})</el-checkbox>
                        </el-checkbox-group>
                        <div style="margin: 0 20px 5px 20px;">
                            <el-input v-model="newCategory" placeholder="新建分类" type="text" @keyup.enter="addCategory">
                            </el-input>
                        </div>
                    </div>
                </div>
            </div>
            <div style="margin: 1%;">
                <div class="setFour">
                    <div style="position: absolute;margin-left: 40px;">
                        标签
                        <el-checkbox v-model="selectAllTag" :indeterminate="isIndeterminateTag"
                            @change="selectAllChangeTag">全选</el-checkbox>
                        <span v-if="tagChange" class="deleteBut" @click="deleteTag">删除</span>
                    </div>
                    <div style="margin-top: 40px;height: calc(100% - 40px); overflow-y: auto;overflow-x: hidden;">
                        <el-checkbox-group v-model="selectedTag" @change="selectChangeTag">
                            <el-checkbox v-for="ca in tags" :key="ca" :label="ca"
                                style="display: block;font-size: 20px !important;text-align: left;margin-left: 20px;">
                                {{ ca }}</el-checkbox>
                        </el-checkbox-group>
                        <div style="margin: 0 20px 5px 20px;">
                            <el-input v-model="newTag" placeholder="新建标签" type="text" @keyup.enter="addTag">
                            </el-input>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 改密dialog -->
        <el-dialog v-model="resetPwdDialog" width="400px" top="20vh" modal :append-to-body="true"
            style=" border-radius: 20px;">
            <template #header>
                <div style="font-size: 25px;margin-top: 10px;text-align: center;">
                    修&#160;&#160;改&#160;&#160;密&#160;&#160;码</div>
            </template>
            <div style="width: 300px;">
                <el-form ref="resetRef" :model="pwdFrom" :rules="rules" label-width="80px" class="demo-ruleForm"
                    @keyup.enter="submitForm">
                    <el-form-item label="原密码" prop="oldPwd">
                        <el-input v-model="pwdFrom.oldPwd" type="password" autocomplete="off" placeholder="请输入原密码" />
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPwd">
                        <el-input v-model="pwdFrom.newPwd" type="password" autocomplete="off" placeholder="请输入新密码" />
                    </el-form-item>
                    <el-form-item label="新密码" prop="reNewPwd">
                        <el-input v-model="pwdFrom.reNewPwd" type="password" autocomplete="off"
                            placeholder="请再次输入新密码" />
                    </el-form-item>
                    <el-form-item label="验证码" prop="code">
                        <div style="width:108px">
                            <el-input v-model="pwdFrom.code" type="text" autocomplete="off" placeholder="请输入验证码" />
                        </div>
                        <div style="cursor: pointer;margin-left:10px ; width: 100px;height: 50px;" @click="getCode">
                            <img :src="codeSrc" alt="" id="code" />
                        </div>

                    </el-form-item>
                    <el-form-item style="margin-top:30px">
                        <el-button type="primary" @click="submitForm">修改密码</el-button>
                        <el-button @click="resetPwdDialog = false">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>

    </div>


</template>

<script>

import API from '../utils/API';
import md5 from 'js-md5'
export default {

    components: {

    },
    props: { authorId: Number },
    data() {
        return {
            //改密
            resetPwdDialog: false,
            pwdFrom: {
                oldPwd: "",
                newPwd: "",
                reNewPwd: "",
                code: ""
            },
            rules: {
                oldPwd: { required: true, message: "请输入原密码", trigger: 'blur' },
                newPwd: [{ required: true, message: "请输入新密码", trigger: 'blur' },
                        {min:5, max:16, message:"密码长度在 5 到 16 个字符", trigger:'blur'}],
                reNewPwd: { validator: this.validateReNewPwd, required: true, trigger: 'blur' },
                code: { required: true, message: "请输入验证码", trigger: 'blur' }
            },
            // 验证码src
            codeSrc: "",
            key:"",
            //
            author: {},
            changeFlag: false,
            tagChange: false,
            catChange: false,
            categories: [],
            selectedCa: [],
            selectAllCa: false,
            isIndeterminateCa: false,
            newCategory: "",
            newTag: "",
            tags: [],
            selectedTag: [],
            selectAllTag: false,
            isIndeterminateTag: false,
            cachedCat: []
        }
    },
    methods: {
        validateReNewPwd(rule, value, callback) {
            if (value === '') {
                callback('请再次输入新密码')
            }else if (value !== this.pwdFrom.newPwd) {
                callback("两次输入的密码不一致")
            }else callback()
        },
        resetForm() {
            this.$refs['resetRef']?.resetFields()
        },
        resetPwd(){
            this.resetPwdDialog = true,
            this.getCode()
            this.resetForm()
        },
        getCode() {
            this.pwdFrom.code = ""
            API.get("getCode")
                .then(res => {
                    if (res.code == 200) {
                        this.codeSrc = "data:image/jpg;base64," + res.data
                        this.key = res.data.slice(1000,1020)
                    }
                })
        },
        makePW(PW) {
            let md5Str = md5(PW)
            let pd1 = md5Str.slice(1, 8)
            let pd2 = md5Str.slice(9, 16)
            let pd3 = pd1 + PW + pd2
            return md5(pd3).slice(1, 16)
        },
        // 改密
        submitForm() {
            this.$refs['resetRef'].validate((valid) => {
                if (valid) {
                    let data = {
                        code: this.pwdFrom.code,
                        oldPwd: md5(this.makePW(this.pwdFrom.oldPwd) + this.key).slice(1, 30),
                        newPwd: this.makePW(this.pwdFrom.newPwd),
                    }
                    API.post('user/resetPwd', data)
                        .then(res => {
                            if (res.code === 200) {
                                ElMessage({
                                    showClose: true,
                                    message: "修改成功",
                                    type: 'success',
                                })
                                this.resetPwdDialog = false
                            } else {
                                // 刷新验证码
                                setTimeout(() => {
                                    this.getCode()
                                }, 100);
                            }
                        })
                }
            })
        },
        selectAllChangeCa(value) {
            console.log(value);
            this.selectedCa = value ? this.categories : []
            this.isIndeterminateCa = false
            this.catChange = value
        },
        selectChangeCa(value) {
            this.catChange = (value.length > 0)
            this.selectAllCa = value.length === this.categories.length
            this.isIndeterminateCa = value.length > 0 && value.length < this.categories.length
        },
        addCategory() {
            if (this.newCategory == "") return
            for (let i = 0; i < this.categories.length; i++) {
                if (this.categories[i].category == this.newCategory) {
                    // 分类已存在
                    this.newCategory = ""
                    return
                }
            }
            let data = {
                id: -1,
                authorId: null,
                category: this.newCategory,
                articleNum: 0,
            }
            this.categories.push(data)
            this.newCategory = ""
            this.changeFlag = true
        },
        deleteCa() {
            let data = []
            this.categories.forEach(e => {
                if (!this.selectedCa.includes(e)) {
                    data.push(e)
                }
            })
            this.cachedCat = this.categories
            this.categories = data
            this.changeFlag = true
            this.catChange = false
            this.isIndeterminateCa = false
        },
        selectAllChangeTag(value) {
            this.selectedTag = value ? this.tags : []
            this.isIndeterminateTag = false
            this.tagChange = value
        },
        selectChangeTag(value) {
            this.tagChange = value.length > 0
            this.selectAllTag = value.length === this.tags.length
            this.isIndeterminateTag = value.length > 0 && value.length < this.tags.length
        },
        addTag() {
            if (this.newTag == "") return
            for (let i = 0; i < this.tags.length; i++) {
                if (this.tags[i] == this.newTag) {
                    // 标签已存在
                    this.newTag = ""
                    return
                }
            }
            this.tags.push(this.newTag)
            this.newTag = ""
            this.changeFlag = true
        },
        deleteTag() {
            let data = []
            this.tags.forEach(e => {
                if (!this.selectedTag.includes(e)) {
                    data.push(e)
                }
            })
            this.tags = data
            this.changeFlag = true
            this.tagChange = false
            this.isIndeterminateTag = false
        },
        coverUploadHandler(file) {
            let reader = new FileReader()
            reader.readAsDataURL(file.file)
            reader.onload = async e => {
                this.author.coverImg = await this.dealImage(e.target.result, 1000)
                this.author.coverImgChange = true
            }
            this.changeFlag = true
        },
        coverHandleExceed(files) {
            this.$refs.coverUploadRef.clearFiles()
            this.$refs.coverUploadRef.handleStart(files[0])
            let reader = new FileReader()
            reader.readAsDataURL(files[0])
            reader.onload = async e => {
                this.author.coverImg = await this.dealImage(e.target.result, 1000)
            }
        },
        headImgUploadHandler(file) {
            let reader = new FileReader()
            reader.readAsDataURL(file.file)
            reader.onload = async e => {
                this.author.img = await this.dealImage(e.target.result, 1000)
                this.author.imgChange = true
            }
            this.changeFlag = true
        },
        headImgHandleExceed(files) {
            this.$refs.headImgUploadRef.clearFiles()
            this.$refs.headImgUploadRef.handleStart(files[0])
            let reader = new FileReader()
            reader.readAsDataURL(files[0])
            reader.onload = async e => {
                this.author.img = await this.dealImage(e.target.result, 1000)
            }
        },
        submit() {
            // 检查封面和头像是否修改过
            let au = JSON.parse(JSON.stringify(this.author));
            if (!au.coverImgChange) au.coverImg = ""
            if (!au.imgChange) au.img = ""
            let data = {
                author: au,
                tags: this.tags,
                categories: this.categories
            }
            API.post('/user/update', data)
                .then(res => {
                    if (res.code == 200) {
                        // 修改store的作者信息
                        API.get('init/getAuthor', { authorId: this.author.username })
                            .then(res => {
                                if (res.code == 200) {
                                    res.data.img = this.baseUrl + res.data.img
                                    res.data.coverImg = this.baseUrl + res.data.coverImg
                                    this.$store.commit("login", res.data)
                                }
                            })
                        ElMessage({
                            showClose: true,
                            message: "修改成功",
                            type: 'success',
                        })
                        this.changeFlag = false
                    }
                    if (res.code == 499) {
                        // 分类下有文章不允许删除
                        this.categories = this.cachedCat
                    }
                })
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
                    } else {
                        canvas.width = imgWidth
                        canvas.height = imgHeight
                        quality = 1
                    }
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(this, 0, 0, canvas.width, canvas.height);
                    var base64 = canvas.toDataURL("image/webp", quality);
                    let newSize = base64.length * 0.75 / 1024
                    while (newSize > size) {
                        quality -= 0.02;
                        base64 = canvas.toDataURL("image/webp", quality);
                        newSize = base64.length * 0.75 / 1024
                    }
                    resolve(base64)
                }
            })
        },
    },
    computed: {

    },
    watch: {
        author: {
            deep: true,
            handler() {
                this.changeFlag = true
            }
        }
    },
    mounted() {
        this.author = JSON.parse(JSON.stringify(this.$store.state.author));
        let data = { "authorId": this.author.username }
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
        setTimeout(() => {
            this.changeFlag = false
        }, 200);
    },

}

</script>

<style lang='less' scoped>
@border: 1px solid #278b63;

.btOne {
    margin: 20px;
    opacity: 0.3;
    transition: all 0.3s ease-in-out;

    &:hover {
        opacity: 1;
    }
}

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
        position: absolute;
        width: 100%;
        height: 100%;
        transition: all 0.3s ease-in-out;
        user-select: none;
    }
}

.body {
    display: flex;
    margin-left: 2.5%;
    max-width: 95%;
    flex: 0 0 95%;
    max-height: 280px;

    .setOne {
        width: 150px;
        border: @border;
        border-radius: 10px;
    }

    .setTwo {
        width: 200px;
        height: 200px;
        border: @border;
        border-radius: 10px;


        .img {
            height: 100%;
            width: 100%;
            vertical-align: middle;
            transition: all 0.5s ease-in-out;
            border-radius: 10px;
        }
    }

    .setThree {
        width: 300px;
        height: 200px;
        border: @border;
        border-radius: 10px;
        padding: 20px;
        user-select: none;

        .inputBox {
            width: 270px;
        }
    }

    .setFour {
        position: relative;
        width: 200px;
        height: 260px;
        border: @border;
        border-radius: 10px;
        overflow-y: hidden;
        overflow-x: hidden;
        user-select: none;

        .deleteBut {
            cursor: pointer;
            font-size: 14px;
            margin: -5px 0 0 10px;
            padding: 5px;
            border: 1px solid rgb(227, 131, 6);
            border-radius: 5px;
        }
    }
}

.el-input {
    opacity: 0.5;
    transition: all 0.2s;

    &:hover {
        color: red;
        opacity: 1;
    }
}
</style>