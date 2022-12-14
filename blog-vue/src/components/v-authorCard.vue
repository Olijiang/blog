<template>
    <div class=authorStyle>
        <!-- 作者信息 -->
        <div class="author">
            <el-image class="img" fit="cover" :src=author.img alt="" />

            <p class="author-name">{{ author.authorName }}
            </p>
            <p style="font-size: 90%;color: #858585;user-select: none;"> {{ author.authorInfo }}</p>

            <div>
                <span class="tag1">文章</span>
                <span class="tag1">分类</span>
                <span class="tag1">访问</span>
            </div>
            <div>
                <span class="tag" style="color: #6300ff;">{{ author.articleNum }}</span>
                <span class="tag" style="color: #6300ff;">{{ author.categoryNum }}</span>
                <span class="tag" style="color: #6300ff;">{{ author.visitNum }}</span>
            </div>
        </div>
        <!-- 文章分类 -->
        <div class="category">
            <div
                style="font-size: 18px;width: 100%; margin: 10px auto;text-align: center; user-select: none;position: absolute;">
                文章分类
            </div>

            <div style="overflow-y: auto;margin-top: 40px;height: calc(100% - 40px)">

                <div class="item" v-for="(cat, index) in categories" :key="index">
                    {{ index + 1 }}、{{ cat.category }} ({{ cat.articleNum }})
                </div>
                <div style="font-size: 15px;width: 100%; margin: 10px auto;text-align: center; user-select: none;"
                    v-if="categories.length == 0">
                    暂无分类
                </div>
            </div>
        </div>
    </div>
</template>



<script>
import API from '../utils/API'


// Object.keys(author).forEach(key => { this.author[key] = author[key] })
export default {
    data() {
        return {
            categories: []
        }
    },
    computed: {
        author() {
            return this.$store.state.visitAuthor
        },
    },
    mounted() {
        // 分类
        API.get('init/getCategories', { authorId: this.author.username })
            .then(res => {
                this.categories = res.data
                if (this.categories.length == 0) {
                    this.categories = []
                }
            })
    }
}
</script>


<style lang="less" scoped>
.authorStyle {
    margin-bottom: 20px;
    position: sticky;
    top: 20px;
    width: 250px;
    transition: 0.2s all ease-in-out;
}

.card {
    background-color: rgba(255, 255, 255, 0.5);
    border: 1px solid #278b63;
    border-radius: 20px;
    transition: all 0.2s ease-in-out;


    &:hover {
        box-shadow: 0 0 10px rgb(219, 219, 219);
        transition: all 0.2s ease-in-out;
    }
}

.author {
    width: 250px;
    height: 300px;
    padding-bottom: 20px;
    .card();

    .tag {
        user-select: none;
        display: inline-block;
        width: 30%;
        height: 30px;
    }

    .tag1 {
        user-select: none;
        display: inline-block;
        width: 30%;
        height: 30px;
        transition: 0.2s all ease-in-out;
        color: #4b9797;
    }

    .img {
        margin-top: 20px;
        width: 150px;
        height: 150px;
        border-radius: 50%;
        object-fit: cover;
        transition: all 0.8s ease;

        &:hover {
            transform: rotate(360deg);
            transition: all 0.8s ease;
            transition-delay: 0.5s;
        }
    }
}

.author-name {
    user-select: none;
    line-height: 10px;
    color: #ff0000;
    margin: 20px 0 5px 0;
    font-size: 20px;
}

.category {
    top: 350px;
    width: 100%;
    margin-top: 20px;
    height: 250px;
    overflow: hidden;

    ::-webkit-scrollbar {
        width: 5px !important;
        /*高宽分别对应横竖·滚动条的尺寸*/
        height: 5px !important;
    }

    ::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 10px rgba(255, 255, 255, 0.497);
    }

    .card();

    .item {
        user-select: none;
        cursor: pointer;
        margin-left: 40px;
        height: 28px;
        text-align: left;
        overflow: hidden;

        &:hover {
            color: #4b9797;
        }
    }
}
</style>

