import{_ as w,o as l,f as u,h as t,g as a,t as c,p as D,n as A,i as v,w as r,T as y,m as E,v as L,L as I,j as k,l as x,F as b,A as f,k as C}from"./index.de08f477.js";import{E as S,a as q,b as N}from"./el-image-viewer.660ee909.js";const V={data(){return{}},computed:{author(){return this.$store.state.author}}},T=e=>(D("data-v-ce9d6af4"),e=e(),A(),e),U={class:"authorStyle"},F={class:"author"},j={class:"author-name"},J={style:{"font-size":"90%",color:"#858585"}},O=T(()=>t("div",null,[t("span",{class:"tag1"},"\u6587\u7AE0"),t("span",{class:"tag1"},"\u5206\u7C7B"),t("span",{class:"tag1"},"\u8BBF\u95EE")],-1)),R={class:"tag",style:{color:"#6300ff"}},G={class:"tag",style:{color:"#6300ff"}},K={class:"tag",style:{color:"#6300ff"}},M=T(()=>t("div",{class:"category"}," \u6587\u7AE0\u5206\u7C7B ",-1));function Q(e,n,s,_,o,i){const h=S;return l(),u("div",U,[t("div",F,[a(h,{class:"img",fit:"cover",src:i.author.img,alt:""},null,8,["src"]),t("p",j,c(i.author.blogName),1),t("p",J,c(i.author.blogInfo),1),O,t("div",null,[t("span",R,c(i.author.articleNum),1),t("span",G,c(i.author.categoryNum),1),t("span",K,c(i.author.visitNum),1)])]),M])}const z=w(V,[["render",Q],["__scopeId","data-v-ce9d6af4"]]);const W={components:{},props:{article:{id:Number,title:String,createTime:String,category:String,tag:String,digest:String,url:String,img:String}},data(){return{ok:!1}},methods:{articleDeail(){this.$router.push({path:"/ArticleDetail/"+this.article.id})}},computed:{},watch:{},mounted(){this.article.img=this.baseUrl+this.article.img,this.article.tag=JSON.parse(this.article.tag),this.article.digest=this.article.digest.replace(/#*.*#/g,"").replace(/[^a-z0-9\u4e00-\u9fa5]/,"").substring(0,200),this.ok=!0}},H=e=>(D("data-v-9ca90d4c"),e=e(),A(),e),X={class:"timebar"},Y={class:"time_line"},Z=H(()=>t("div",{class:"time_node"},null,-1)),tt={class:"time_info"},et={class:"article_card"},st={style:{"font-size":"80%"}},at={style:{color:"#d63a3a"}},ot=H(()=>t("hr",{width:"400px",align:"left"},null,-1));function it(e,n,s,_,o,i){const h=q,d=S,g=N;return l(),v(y,{name:"el-zoom-in-top"},{default:r(()=>[a(g,null,{default:r(()=>[a(h,{class:"timebarCol"},{default:r(()=>[t("div",X,[t("div",Y,[Z,t("div",tt,c(s.article.createTime),1)])])]),_:1}),a(h,{class:"articleCol"},{default:r(()=>[a(y,{name:"el-zoom-in-top"},{default:r(()=>[E(t("div",et,[t("div",{class:I({illustration_l:s.article.id%2==0,illustration_r:s.article.id%2!=0})},[a(d,{class:"img",fir:"cover",src:s.article.img,alt:"img",onClick:i.articleDeail},null,8,["src","onClick"])],2),t("div",{class:I({content_r:s.article.id%2==0,content_l:s.article.id%2!=0})},[t("h3",{onClick:n[0]||(n[0]=(...m)=>i.articleDeail&&i.articleDeail(...m))},c(s.article.title),1),t("span",st,[k(" \u5206\u7C7B: "),t("span",at,c(s.article.category),1),k(" | \u6807\u7B7E: "),(l(!0),u(b,null,x(s.article.tag.tags,(m,p)=>(l(),u("span",{style:{color:"#d63a3a"},key:p},c(m)+"\xA0 ",1))),128))]),ot,t("p",null,c(s.article.digest),1)],2)],512),[[L,o.ok]])]),_:1})]),_:1})]),_:1})]),_:1})}const P=w(W,[["render",it],["__scopeId","data-v-9ca90d4c"]]);const ct={name:"Home",props:["authorId"],components:{vArticleCard:P,vAuthorCard:z},data(){return{endmsg:"\u4E0B\u62C9\u52A0\u8F7D\u66F4\u591A",articleList:[],show:!1,ok:0,timeout:null,queryData:{authorId:"",startPage:0,pageSize:5},coverImg:""}},methods:{handleScroll(){clearTimeout(this.timeout),this.timeout=setTimeout(()=>{let e=document.documentElement.scrollTop,n=document.documentElement.clientHeight,s=document.documentElement.scrollHeight;n+e-s>-10&&(this.endmsg="\u6B63\u5728\u52A0\u8F7D...",f.get("init/getArticles",this.queryData).then(_=>{_.code==200&&(_.data.forEach(o=>{this.articleList.push(o)}),_.data.length<this.queryData.pageSize&&(this.endmsg="\u6CA1\u6709\u66F4\u591A\u4E86...",window.removeEventListener("scroll",this.handleScroll)),this.queryData.startPage=this.queryData.startPage+5)}))},100)}},computed:{isLogin:{set(e){this.$store.state.isLogin=e},get(){return this.$store.state.isLogin}}},watch:{},mounted(){console.log(),window.addEventListener("scroll",this.handleScroll),f.get("init/getAuthor",{authorId:this.authorId}).then(e=>{e.code==200&&(e.data.img=this.baseUrl+e.data.img,this.coverImg=this.baseUrl+e.data.coverImg,this.$store.commit("setAuthorInfo",e.data),this.ok++)}),this.queryData.authorId=this.authorId,f.get("init/getArticles",this.queryData).then(e=>{e.code==200&&(e.data.forEach(n=>{this.articleList.push(n)}),e.data.length<this.queryData.pageSize&&(this.endmsg="\u6CA1\u6709\u66F4\u591A\u4E86...",window.removeEventListener("scroll",this.handleScroll)),this.ok++,this.queryData.startPage=this.queryData.startPage+5)}),setTimeout(()=>{this.show=!0},200)},unmounted(){window.removeEventListener("scroll",this.handleScroll)}},lt={class:"home-page"},rt={class:"endmsg"};function nt(e,n,s,_,o,i){const h=S,d=q,g=P,m=z,p=N;return l(),u("div",null,[t("div",lt,[a(y,{name:"el-fade-in"},{default:r(()=>[E(a(h,{class:"img",fit:"cover",src:o.coverImg},null,8,["src"]),[[L,o.show]])]),_:1})]),o.ok>1?(l(),v(p,{key:0},{default:r(()=>[a(d,{class:"space"}),a(d,{class:"ArticleCard"},{default:r(()=>[(l(!0),u(b,null,x(o.articleList,($,B)=>(l(),u("div",{key:B,style:{transition:"all 0.5s"}},[a(g,{article:$},null,8,["article"])]))),128))]),_:1}),a(d,{class:"space"}),a(d,{class:"AuthorCard"},{default:r(()=>[o.ok>1?(l(),v(m,{key:0})):C("",!0)]),_:1}),a(d,{class:"space"})]),_:1})):C("",!0),t("div",rt,c(o.endmsg),1)])}const ht=w(ct,[["render",nt],["__scopeId","data-v-8a10dec7"]]);export{ht as default};