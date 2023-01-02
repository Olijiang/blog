<template>
    <div style="margin-top: 300px;">
        <el-radio-group v-model="op" @change="changeH">
            <el-radio label='0' size="large">设为私密</el-radio>
            <el-radio label='1' size="large">设为公开</el-radio>
        </el-radio-group>

    </div>
</template>

<script>

export default {
    name: name,
    components: {

    },
    props: {},
    data() {
        return {
            op:String(1)
        }
    },
    methods: {
        changeH(){
            console.log(this.op);
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

</style>
