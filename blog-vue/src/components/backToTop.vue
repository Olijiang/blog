
<template>
    <div :class="boxStyle">
        <div class="box" @click="toTop">
            <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                <path fill="#efa228"
                    d="M572.235 205.282v600.365a30.118 30.118 0 1 1-60.235 0V205.282L292.382 438.633a28.913 28.913 0 0 1-42.646 0 33.43 33.43 0 0 1 0-45.236l271.058-288.045a28.913 28.913 0 0 1 42.647 0L834.5 393.397a33.43 33.43 0 0 1 0 45.176 28.913 28.913 0 0 1-42.647 0l-219.618-233.23z">
                </path>
            </svg>
        </div>
    </div>
</template>

<script setup>

import { onMounted, ref } from 'vue';

// 添加监听事件控制模块的显示
const boxStyle = ref("noshow")
onMounted(() => {
    window.addEventListener('scroll', () => {
        var y = document.body.scrollTop || document.documentElement.scrollTop
        boxStyle.value = (y > 800) ? "show" : "noshow"
    });
})

const myTimer = ref()
function interrupt() {
    window.clearInterval(myTimer.value)
    window.removeEventListener('mousewheel', interrupt)
}

function toTop() {
    // 检测用户滚动则打断回到顶部
    // window.addEventListener('mousewheel', interrupt)
    // let y = document.documentElement.scrollTop || document.body.scrollTop
    // // 步长按二次函数变化
    // // 大致步数定位100步, 但实际中会少一些, 因为当前高度小于步长时会直接到顶部
    // let n = 100
    // let step = y / (n / 2)    //起始步长
    // let a = step / n        //步长变化率
    // let t = 0
    // step -= a / 2
    // myTimer.value = setInterval(() => {
    //     // console.log(t++, "步长：", step);
    //     y -= step
    //     step -= a
    //     if (y <= 0 || step < 0) {
    //         y = 0
    //         window.clearInterval(myTimer)
    //     }
    //     window.scrollTo(0, y) //这是值是指离开网页顶部的距离
    // }, 1);
    document.querySelector("#app").scrollIntoView({ behavior: "smooth" });
}

</script>

<style lang="less" scoped>
.show {
    position: fixed;
    bottom: 100px;
    right: 5px;
    z-index: 5;
    opacity: 0.2;
    transition: all 0.5s;

    &:hover {

        opacity: 1;
    }

    .box {
        background: linear-gradient(45deg, rgba(251, 255, 0, 0.6), rgba(51, 255, 0, 0.6), rgba(0, 251, 255, 0.6), rgba(0, 60, 255, 0.6), rgba(230, 0, 255, 0.6));
        animation: Gradient 5s ease infinite;
        background-size: 400% 400%;
        box-shadow: 0px 0px 2px #efa228;
        padding: 10px;
        width: 30px;
        height: 30px;
        border-radius: 10px;

        @keyframes Gradient {
            0% {
                background-position: 0 50%;
            }

            50% {
                background-position: 100% 50%;
            }

            100% {
                background-position: 0 50%;
            }
        }

        &:hover {
            box-shadow: 0px 0px 8px #efa228;
            opacity: 1;
        }
    }
}

.noshow {
    .show;
    right: -55px;
}
</style>
