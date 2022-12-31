
import { createStore } from "vuex";
import API from '../utils/API'
const store = createStore({
  state() {
    return {
      albums: [],
      token:'',
      author: undefined,
      visitAuthor: undefined,
      editDialog: false, //控制文章编辑窗口
      isLogin: false,  //控制是否登录
      article: {
        authorId: "",
        title: "",
        category: "",
        tag: [],
        content: "",
        img: ""
      }
    }
  },
  mutations: {
    login(state, author) {
      state.token = author.password
      state.author = author
      state.isLogin = true
    },
    logout(state) {
      state.isLogin = false
      state.author = undefined
    },
    setVisitAuthor(state, author) {
      state.visitAuthor = author
    },
    setArticle(state, article) {
      state.article = article
      // 解析tag
      state.article.tag = JSON.parse(article.tag).tags
      // 请求文章内容
      let data = { "filePath": article.content }
      API.get('init/getContent', data)
        .then(res => {
          if (res.code == 200) {
            state.article.content = res.data
          }
        })
    },
    clearArticle(state) {
      for (let key in state.article) {
        if (key == "tag") state.article[key] = []
        state.article[key] = ""
      }
    },
    setAlbums(state, albums) {
      state.albums = albums
    }
  }
})

export default store;

