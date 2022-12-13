<template>
    <footer>
        <div style="margin: auto;">@2020</div>
    </footer>
</template>

<script>

export default {
    data() {
        return {

        }
    },
    methods: {

    },
    computed: {

    },
}

</script>

<style lang='less' scoped>
footer {
    user-select: none;
    display: flex;
    height: 80px;
    text-align: center;
    vertical-align: middle;
    box-shadow: 0px 0px 1px #dadaad;
    background: linear-gradient(45deg, hsl(0, 30%, 90%, 0.8), hsl(60, 60%, 90%, 0.8), hsl(120, 60%, 90%, 0.8), hsl(180, 60%, 90%, 0.8), hsl(240, 60%, 90%, 0.8), hsl(320, 60%, 90%, 0.8), hsl(360, 60%, 90%, 0.8));
    background-size: 400% 400%;
    font-family: 'Times New Roman', Times, serif;
    // font-size: 20px;
    transition: all 0.5s;
    animation: Gradient 15s ease infinite running;

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