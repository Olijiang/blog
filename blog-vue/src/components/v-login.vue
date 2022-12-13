<template>
    <el-dialog v-model="loginDialog" width="400px" top="20vh" modal style=" border-radius: 20px;">
        <template class=" card-header" #header>
            <h3>登&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;录</h3>
        </template>
        <div class="card-body">
            <el-form ref="loginRef" :model="loginForm" :rules="rules" label-width="80px" class="demo-ruleForm"
                @keyup.enter="submitForm">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="loginForm.username" type="text" autocomplete="off" placeholder="请输入账户" />
                </el-form-item>
                <el-form-item label="密&#160;&#160;码" prop="password">
                    <el-input v-model="loginForm.password" type="password" autocomplete="off" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item label="验证码" prop="code">
                    <div style="width:108px">
                        <el-input v-model="loginForm.code" type="text" autocomplete="off" placeholder="请输入验证码" />
                    </div>
                    <div style="cursor: pointer;margin-left:10px ; width: 100px;height: 50px;" @click="getCode">
                        <img :src="src" alt="" id="code" />
                    </div>

                </el-form-item>
                <el-form-item style="margin-top:30px">
                    <el-button type="primary" @click="submitForm">登录</el-button>
                    <el-button @click="resetForm">清空</el-button>
                </el-form-item>
            </el-form>
        </div>

    </el-dialog>
</template>

<script>
import API from '../utils/API'

export default {

    components: {

    },
    props: {},
    data() {
        function validateUserName(rule, value, callback) {
            if (value === '') {
                callback('请输入用户名')
            } else {
                let idReg = /^\d{4,10}$/
                if (idReg.test(value)) {
                    callback()
                } else {
                    callback('请输入正确的用户名')
                }
            }
        }
        function validatePassword(rule, value, callback) {
            if (value === '') {
                callback('请输入密码')
            } else {
                callback()
            }
        }
        return {
            vscodeFlag: false,
            src: "",
            rules: {
                username: { validator: validateUserName, required: true, trigger: 'blur' },
                password: { validator: validatePassword, required: true, trigger: 'blur' },
                code: { required: true, message: "请输入验证码", trigger: 'blur' }
            },
            loginForm: {
                username: '',
                password: '',
                code: "",
                timeStamp: ""
            }
        }
    },
    methods: {
        getCode() {
            this.loginForm.timeStamp = new Date().getTime()
            let data = { timeStamp: this.loginForm.timeStamp }
            this.loginForm.code = ""
            API.get("getCode", data)
                .then(res => {
                    if (res.code == 200) {
                        this.src = "data:image/jpg;base64," + res.data
                    }
                })
        },
        submitForm() {
            this.$refs['loginRef'].validate((valid) => {
                if (valid) {
                    API.post('login', this.loginForm)
                        .then(res => {
                            if (res.code === 200) {
                                // 同步vuex
                                res.data.img = import.meta.env.VITE_BASE_URL + res.data.img
                                res.data.coverImg = import.meta.env.VITE_BASE_URL + res.data.coverImg
                                this.$store.commit("login", res.data)
                                this.$store.state.loginDialog = false
                                if (this.$route.path == "/") {
                                    this.$router.push({
                                        path: '/' + res.data.username
                                    })
                                }
                            }
                        })
                    // 刷新验证码
                    setTimeout(() => {
                        this.getCode()
                    }, 100);
                }
            })
        },
        resetForm() {
            this.$refs['loginRef'].resetFields()
        }
    },
    computed: {
        loginDialog: {
            set(value) {
                this.$store.state.loginDialog = value;
            },
            get() {
                return this.$store.state.loginDialog;
            }
        }
    },
    watch: {
        loginDialog(newValue) {
            if (newValue) {
                this.getCode()
            } else {
                this.resetForm()
            }
        }
    },
    mounted() {
    },
    unmounted() {
        this.resetForm()
    }
}

</script>

<style lang='less' scoped>
el-dialog {
    border-radius: 20px !important;
}

el-input::placeholder {
    /* placeholder颜色  */
    color: #0068fa !important;
    /* placeholder字体大小  */
    font-size: 10px;
    /* placeholder位置  */
    text-align: right;
}

.card-body {
    width: 300px;
    margin: auto;
    z-index: 20;
    padding-right: 5%
}

.card-header {
    font-size: 20px;
    font-family: "宋体";
    align-items: center;
}
</style>