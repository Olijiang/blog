---
页面跳转备忘录
---

## article

## article --> articleEditor

直接传递参数

- tags：作者的全部标签
- categories: 作者的全部分类
- editFlag: 区分编辑文字和新增文章的标志, 用于选择 API 地址

间接传递参数

- article 信息由 article 页面请求放入 store 中, 文章的主题 content 由 store 自动请求

## article --> articleDetail
