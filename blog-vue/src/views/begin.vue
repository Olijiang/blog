<template>

    <div class="bigTitle">BLOG</div>

</template>

<script>

export default {
    data() {
        return {

        }
    },
}

</script>

<style lang='less' scoped>
.bigTitle {
    margin: 10% auto;
    text-align: center;
    width: 900px;
    // background-image: linear-gradient(45deg, hsl(0, 70%, 70%, 1), hsl(60, 60%, 90%, 1), hsl(120, 60%, 90%, 1), hsl(180, 60%, 90%, 1), hsl(240, 60%, 90%, 1), hsl(320, 60%, 90%, 1), hsl(360, 60%, 90%, 1));
    background-image: linear-gradient(45deg, hsl(0, 70%, 80%, 1), hsl(60, 70%, 80%, 1), hsl(120, 70%, 80%, 1), hsl(180, 70%, 80%, 1), hsl(240, 70%, 80%, 1), hsl(320, 70%, 80%, 1), hsl(360, 70%, 80%, 1));

    color: transparent;
    background-size: 200% 200%;
    -webkit-background-clip: text;
    background-clip: text;
    font-size: 300px;
    font-family: 'Times New Roman', Times, serif;
    user-select: none;
    transition: all 0.5s;
    animation: Gradient 15s ease infinite;

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
}
</style>