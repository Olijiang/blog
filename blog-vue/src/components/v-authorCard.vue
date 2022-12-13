<template>
    <div class=authorStyle>
        <!-- 作者信息 -->
        <div class="author">
            <el-image class="img" fit="cover" :src=author.img alt="" />

            <p class="author-name">{{ author.authorName }}
            </p>
            <p style="font-size: 90%;color: #858585;"> {{ author.authorInfo }}</p>

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
            文章分类
        </div>
    </div>
</template>



<script>

// Object.keys(author).forEach(key => { this.author[key] = author[key] })
export default {
    data() {
        return {
        }
    },
    computed: {
        author() {
            return this.$store.state.visitAuthor
        },
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
    // position: absolute;

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
        display: inline-block;
        width: 30%;
        height: 30px;
    }

    .tag1 {
        cursor: pointer;
        display: inline-block;
        width: 30%;
        height: 30px;
        transition: 0.2s all ease-in-out;

        &:hover {
            color: #4b9797;
        }
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

        }
    }
}

.author-name {
    line-height: 10px;
    color: #ff0000;
    margin: 20px 0 5px 0;
    font-size: 20px;
    transition: 0.3s all ease-in-out;
    cursor: pointer;

    &:hover {
        color: #40b5ad;
    }
}

.category {
    top: 350px;
    width: 100%;
    margin-top: 20px;
    height: 250px;
    .card()
}
</style>

