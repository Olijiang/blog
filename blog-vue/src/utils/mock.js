import Mock from "mockjs";

const baseurl = "";

function login(data) {
    console.log(data);
    let username = data.username
    let password = data.password
    if (username === "4050000" && password === "123") {
        return {
            code: 200,
            message: "登录成功",
            author: {
                name: "高西维尔",
                desc: "这是我的blog",
                articleN: 999,
                categoryN: 999,
                tagN: 999,
                imgUrl: "src/img/2.png"
            }
        }
    } else {
        return {
            code: 400,
            message: "用户名或密码错误",
        }
    }
}

Mock.mock("/login", (data) => login(JSON.parse(data.body)));


function initiate() {
    console.log("mock init:");
    return {
        code: 200,
        message: "",
        author: {
            name: "神华里绫",
            desc: "这是我的blog",
            articleN: 99,
            categoryN: 99,
            tagN: 99,
            imgUrl: "src/img/1.png"
        },
        articles: [
            { index: 1, title: "段落标题段落标题段落标题段落标题段落标题", createTime: "2012-01-23", category: "Vue", tag: "vuex", digest: "段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要", url: "xxx", imgUrl: "src/img/1.png" },
            { index: 2, title: "段落标题", createTime: "2014-02-21", category: "Java", tag: "多线程", digest: "段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要段落摘要", url: "xxx", imgUrl: "src/img/2.png" },
            { index: 3, title: "段落标题", createTime: "2015-01-27", category: "Vue", tag: "router", digest: "段落摘要段落摘要", url: "xxx", imgUrl: "src/img/3.png" },
            { index: 4, title: "段落标题", createTime: "2016-02-15", category: "Redis", tag: "redis", digest: "段落摘要段落摘要", url: "xxx", imgUrl: "src/img/1.png" },
            { index: 5, title: "段落标题", createTime: "2017-11-23", category: "Python", tag: "CNN", digest: "段落摘要段落摘要", url: "xxx", imgUrl: "src/img/2.png" },
            { index: 6, title: "段落标题", createTime: "2018-05-29", category: "Vue", tag: "mock", digest: "段落摘要段落摘要", url: "xxx", imgUrl: "src/img/3.png" },
            { index: 7, title: "段落标题", createTime: "2019-08-01", category: "Java", tag: "springboot", digest: "段落摘要段落摘要", url: "xxx", imgUrl: "src/img/1.png" },
            { index: 8, title: "段落标题", createTime: "2021-04-29", category: "Vue", tag: "vuex", digest: "段落摘要段落摘要", url: "xxx", imgUrl: "src/img/2.png" }
        ]
    }
}
Mock.mock("/init", initiate);


function getArticle() {
    return {
        code: 200,
        message: "文章细节",
        data: `# AQS 简单介绍\n123123\n\n## bia`

    }
}

Mock.mock(RegExp("/article/id=" + "\d*"), getArticle)



