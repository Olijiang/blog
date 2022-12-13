<template>
    <header @mousemove="onMousemove" :class="headerStyle">

        <div class="author" @click="routerHandler('/')">
            <!-- {{ authorInfo.name }} -->
            {{ blogName }}
        </div>
        <div class="nav">

            <div v-for="(item, index) in navItems" :key="index" class="nav_item" @click="routerHandler(item.path)">
                <div v-if="visitAuthorId != undefined">
                    <span>{{ item.label }}</span>
                    <div class="item_line"></div>
                </div>
            </div>
            <template v-if="isLogin">
                <div class="nav_item">
                    <span v-if="isAuthor" @click="routerHandler('/Personal')">个人中心</span>
                    <span v-else @click.prevent="goHome()">回家</span>
                    <div class="item_line"></div>
                </div>
                <div class="nav_item" @click="logoutHandler">
                    退出登录
                    <div class="item_line"></div>
                </div>
            </template>

            <template v-else>
                <div class="nav_item" @click="loginDialog = true">
                    <span>登录</span>
                    <div class="item_line"></div>
                </div>
            </template>
        </div>
    </header>
    <!-- 登录dialog -->
    <el-dialog v-model="loginDialog" width="400px" top="20vh" modal :append-to-body="true" style="border-radius: 20px;">
        <template #header>
            <div style="font-size: 25px;margin-top: 10px;text-align: center;">
                登&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;录</div>
        </template>
        <div style="width: 300px;">
            <el-form ref="loginRef" :model="loginForm" :rules="rules" label-width="80px" class="demo-ruleForm"
                @keyup.enter="loginHandler">
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
                    <el-button type="primary" @click="loginHandler">登录</el-button>
                    <el-button @click="resetForm">清空</el-button>
                    <div class="regtxt" @click="toRegister">注册账号</div>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
    <!-- 注册dialog -->
    <el-dialog v-model="registDialog" width="400px" top="20vh" modal :append-to-body="true"
        style=" border-radius: 20px;">
        <template #header>
            <div style="font-size: 25px;margin-top: 10px;text-align: center;">
                注&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;册</div>
        </template>
        <div style="width: 300px;">
            <el-form ref="rigisterRef" :model="loginForm" :rules="rules" label-width="80px" class="demo-ruleForm"
                @keyup.enter="registerHanlder">
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
                    <el-button type="primary" @click="registerHanlder">注册</el-button>
                    <el-button @click="toLogin">返回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </el-dialog>
</template>

<script>
import API from '@/utils/API'


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
            registDialog: false,
            // 登录dialog
            loginDialog: false,
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
            },
            // navBar
            x: 100,
            navItems: [
                {
                    label: "首页",
                    name: "Home",
                    path: "/"
                },
                {
                    label: "相册",
                    name: "Album",
                    path: "/Album/"
                },
                {
                    label: "文章",
                    name: "Article",
                    path: "/Article/"
                },
            ],
            headerStyle: "header-on",
        }
    },
    methods: {
        toRegister() {
            this.loginDialog = false
            setTimeout(() => {
                this.registDialog = true
            }, 500);

        },
        toLogin() {
            this.registDialog = false
            setTimeout(() => {
                this.loginDialog = true
            }, 500);
        },
        registerHanlder() {
            // 注册
            this.$refs['rigisterRef'].validate((valid) => {
                if (valid) {
                    API.post('register', this.loginForm)
                        .then(res => {
                            if (res.code === 200) {
                                ElMessage({
                                    showClose: true,
                                    message: response.data.message,
                                    type: 'success',
                                })
                                this.toLogin()
                                this.resetForm()
                            }
                            // 刷新验证码
                            setTimeout(() => {
                                this.getCode()
                            }, 100);
                        })
                }
            })
        },
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
        loginHandler() {
            console.log(this.loginForm);
            this.$refs['loginRef'].validate((valid) => {
                if (valid) {
                    API.post('login', this.loginForm)
                        .then(res => {
                            if (res.code === 200) {
                                // 同步vuex
                                res.data.img = this.baseUrl + res.data.img
                                res.data.coverImg = this.baseUrl + res.data.coverImg
                                this.$store.commit("login", res.data)
                                this.loginDialog = false
                                if (this.$route.path == "/") {
                                    this.$router.push({
                                        path: '/' + res.data.username
                                    })
                                }
                            }
                            // 刷新验证码
                            setTimeout(() => {
                                this.getCode()
                            }, 100);
                        })
                }
            })
        },
        resetForm() {
            this.$refs['loginRef'].resetFields()
        },

        // navBar
        logoutHandler() {
            ElMessageBox.confirm('是否确认退出?', '提示框',
                {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '是',
                    cancelButtonText: '否',
                    type: 'warning',
                    customStyle: { top: "-20% !important", position: "relative" },
                }).then(() => {
                    this.$store.commit("logout")
                    ElMessage({
                        type: 'success',
                        message: '退出成功',
                    })
                    this.$router.push({
                        path: "/"
                    })
                }).catch((action) => {
                    if (action == 'cancel') {
                        // 点击关闭 关闭弹窗回到主页面

                    } else {
                        // 按ESC 啥也不干
                    }
                })
        },
        routerHandler(value) {
            if (value === '/Personal') {
                this.$router.push({
                    path: value
                })
                return
            }
            if (this.visitAuthorId != undefined) {
                this.$router.push({
                    path: value + this.visitAuthorId
                })
            }
        },
        goHome() {
            this.$router.push({
                path: "/" + this.authorId
            })
        },
        onMousemove(event) {
            this.x = event.clientX
        }
    },
    computed: {
        isLogin() {
            return this.$store.state.isLogin
        },
        // 
        isAuthor() {
            return (this.$store.state.visitAuthor?.username == this.$store.state.author.username)
        },
        // 正在访问的博客的 username
        authorId() {
            return this.$store.state.author.username;
        },
        visitAuthorId() {
            return this.$store.state.visitAuthor?.username;
        },
        // 显示正在访问的博客的名字
        blogName() {
            return this.$store.state?.visitAuthor?.blogName ?? ""
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
        let oldy = 0;
        window.addEventListener('scroll', () => {
            let y = document.documentElement.scrollTop || document.body.scrollTop
            if (y - oldy > 0) { // 向下滚动
                this.headerStyle = "header-off"
            } else {
                this.headerStyle = "header-on"
            }
            oldy = y;
        });
    },
    unmounted() {
        this.resetForm()
    }
}

</script>

<style lang='less' scoped>
// ------------------------->>>>>  heaer css   <<<<<-------------------------
@borderColor: #989898;
@colortransition: 0.1s background-color ease-in-out;
@transition: 0.3s all ease-in-out;

.header-on {
    position: fixed;
    display: flex;
    justify-content: space-between;
    font-weight: bold;
    font-size: 18px;
    z-index: 3;
    width: 100vw; //包括滚动条的宽度在内
    height: 50px;
    line-height: 50px;
    transition: @colortransition, @transition;
    color: #ffffff
}


.header-off {
    .header-on;
    opacity: 0.2;
    transform: translate(0, -50px);
}

.author {
    cursor: pointer;
    margin-left: 20px;
    color: #ff0000;
    user-select: none;
}

.nav {
    z-index: 3;
    margin-right: 20px;
}

.nav_item {
    position: relative;
    display: inline-block;
    line-height: 20px;
    margin: 0 8px;
    cursor: pointer;
    transition: 0.3s all ease-in-out;
    color: #278b63;
    user-select: none;

    .item_line {
        position: absolute;
        width: 0;
        margin-top: 10px;
        height: 5px;
        border-radius: 2px;
        background-color: rgb(2, 159, 207);
        transition: 0.3s all ease-in-out;

    }

    &:hover {
        color: rgb(2, 159, 207);
        transition: 0.3s all ease-in-out;

        .item_line {
            translate: 0;
            width: 100%;
        }
    }
}

.regtxt {
    margin-left: 30px;
    cursor: pointer;
}
</style>