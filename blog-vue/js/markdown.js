import MarkdownIt from "markdown-it";
import hljs from "highlight.js";
import markdownItAbbr from "markdown-it-abbr";
import markdownItDeflist from "markdown-it-deflist";
import markdownItFootnote from "markdown-it-footnote";
import markdownItIns from "markdown-it-ins";
import markdownItKatexExternal from "markdown-it-katex-external";
import markdownItMark from "markdown-it-mark";
import markdownItSup from "markdown-it-sup";
import markdownItTaskLists from "markdown-it-task-lists";
import markdownitEmoji from "markdown-it-emoji";
import MarkdownItContainer from "markdown-it-container";
import markdownItSub from "markdown-it-sub";
export default function markdownToHtml(content) {
  const md = new MarkdownIt({
    html: true,
    linkify: true,
    typographer: true,
    breaks: true,
    highlight: function (str, lang) {
      if (lang && hljs.getLanguage(lang)) {
        try {
          return '<pre class="hljs"><code>' + hljs.highlightAuto(str).value + '</code></pre>';
        } catch (__) { }
      }
    }
  })
    .use(markdownItSup)
    .use(markdownItSub)
    .use(markdownItMark)
    .use(markdownItAbbr)
    .use(MarkdownItContainer)
    .use(markdownItDeflist)
    .use(markdownitEmoji)
    .use(markdownItFootnote)
    .use(markdownItIns)
    .use(markdownItKatexExternal)
    .use(markdownItTaskLists);
  // 将markdown替换为html标签
  return md.render(content);
}

//   npm i markdown-it-task-lists markdown-it-katex-external markdown-it-ins markdown-it-sub markdown-it-sup markdown-it-mark markdown-it-abbr markdown-it-container markdown-it-deflist markdown-it-emoji markdown-it-footnote