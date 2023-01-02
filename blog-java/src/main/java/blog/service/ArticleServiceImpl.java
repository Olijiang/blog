package blog.service;

import blog.config.Result;
import blog.config.LocalCache;
import blog.config.PathConfig;
import blog.entity.Article;
import blog.entity.ArticleDTO;
import blog.entity.Tag;
import blog.entity.User;
import blog.mapper.ArticleMapper;
import blog.mapper.TagMapper;
import blog.utils.myUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGB
 * @since 2022-11-06
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>{


	@Resource
	private CategoryServiceImpl categoryService;
	@Resource
	private ArticleMapper articleMapper;
	@Resource
	private TagMapper tagMapper;
	@Resource
	private UserServiceImpl userService;

	public Result getArticleContent(String filePath){
		String article = getContent(filePath);
		if (article==null) return Result.error("获取文章内容失败");
		return Result.success("获取文章内容成功",article);
	}

	public List<Article> getPublicArticles(String authorId, String queryWord,int startPage, int pageSize) {
		List<Article> articles;
		String key = "articleList" + authorId + "-public"+ queryWord + startPage + "-" + (startPage+pageSize);
		if ((articles = (List<Article>) LocalCache.get(key)) == null) {
			QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
					.eq("author_id",authorId)
					.eq("is_public",1)
					.like(!queryWord.equals(""),"title",queryWord)
					.orderByDesc("create_time")
					.last("limit "+startPage+","+pageSize);
			articles = articleMapper.selectList(wrapper);
			LocalCache.put(key, articles);
			return articles;
		}
		return articles;
	}

	public List<Article> getAllArticles(String authorId, String queryWord, int startPage, int pageSize) {
		List<Article> articles;
		String key = "articleList" + authorId + "-All-" + queryWord + startPage + "-" + (startPage+pageSize);
		if ((articles = (List<Article>) LocalCache.get(key)) == null) {
			QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
					.eq("author_id",authorId)
					.orderByDesc("create_time")
					.like(!queryWord.equals(""),"title",queryWord)
					.last("limit "+startPage+","+pageSize);
			articles = articleMapper.selectList(wrapper);
			LocalCache.put(key, articles);
			return articles;
		}
		return articles;
	}

	public List<Article> getPrivateArticles(String authorId, String queryWord, int startPage, int pageSize) {
		List<Article> articles;
		String key = "articleList" + authorId + "-private-" + startPage + "-" + (startPage+pageSize);
		if ((articles = (List<Article>) LocalCache.get(key)) == null) {
			QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
					.eq("author_id",authorId)
					.eq("is_public",0)
					.orderByDesc("create_time")
					.last("limit "+startPage+","+pageSize);
			articles = articleMapper.selectList(wrapper);
			LocalCache.put(key, articles);
			return articles;
		}
		return articles;
	}

	public Article getPublicArticle(Integer articleId){
		Article article;
		String key = "article" + articleId;
		if ((article = (Article) LocalCache.get(key))==null){
			QueryWrapper<Article> wrapper = new QueryWrapper<Article>()
					.eq("id",articleId)
					.eq("is_public",1);
			article = articleMapper.selectOne(wrapper);
			if (article==null) return null;
			LocalCache.put(key,article);
			return article;
		}
		return article;
	}

	public Article getArticle(Integer articleId){
		Article article;
		String key = "article" + articleId;
		if ((article = (Article) LocalCache.get(key))==null){
			article = articleMapper.selectById(articleId);
			if (article==null) return null;
			LocalCache.put(key,article);
			return article;
		}
		return article;
	}

	public Result deleteArticle(Integer articleId, String authorId) {
		Article article = getArticle(articleId);
		if (article == null || !article.getAuthorId().equals(authorId)) return Result.error("非法操作");
		// 删除文章
		myUtil.deleteFile(article.getContent());
		// 删除图片
		myUtil.deleteFile(article.getImg());
		// 删除记录
		articleMapper.deleteById(articleId);
		// 清除缓存
		LocalCache.removeByPre("articleList"+authorId);
		LocalCache.remove("article"+articleId);
		// 更新作者文章信息
		User user = userService.getUserById(authorId);
		user.setArticleNum(user.getArticleNum()-1);
		LocalCache.put("user"+authorId,user);
		userService.updateById(user);
		categoryService.articleMinusOne(authorId,article.getCategory());
		log.info("删除文章成功 id"+ article.getAuthorId() + article.getTitle());
		return Result.success();
	}

	public Result addArticle(ArticleDTO articleDTO, String authorId) {
		User user = userService.getUserById(authorId);
		Article article = new Article();
		// 图片
//		System.out.println(articleDTO);
		String imgPath = myUtil.saveImg(articleDTO.getImg(),authorId);
		if (imgPath==null) return Result.error("文章发表失败, 原因：插图");

		article.setImg(imgPath);
		// 文章
		String articlePath = saveContent(articleDTO.getContent(),authorId);
		if (articlePath==null) return Result.error("文章发表失败, 原因：文章内容");

		article.setContent(articlePath);
		// 设置标题
		article.setTitle(articleDTO.getTitle());
		// 设置分类
		article.setCategory(articleDTO.getCategory());
		// 刷新作者的 category 信息
		user.setCategoryNum(categoryService.addCategory(authorId, articleDTO.getCategory()));
		categoryService.articleAddOne(authorId,articleDTO.getCategory());
		// 刷新作者的 tag 库
		updateTags(authorId, articleDTO.getTag());
		// 设置 本文章的 tag
		JSONObject thisTagJson = new JSONObject();
		thisTagJson.put("tags", articleDTO.getTag());
		article.setTag(thisTagJson.toJSONString());
		// authorId
		article.setAuthorId(authorId);
		// public
		article.setIsPublic(articleDTO.getIsPublic());
		//digest
		if (articleDTO.getContent().length()>180){
			article.setDigest(articleDTO.getContent().substring(0,180)+"...");
		}else {
			article.setDigest(articleDTO.getContent());
		}

		//CreatTime
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US); // 格式化时间
		String date = dtf.format(LocalDateTime.now());
		article.setCreateTime(date);
		//updateTime
		article.setUpdateTime(date);
		log.info("成功添加新文章:"+article.getTitle());
		articleMapper.insert(article);
		LocalCache.removeByPre("articleList"+authorId);
		log.info("添加文章成功 id："+ article.getAuthorId()+" title:" + article.getTitle());
		// 更新作者文章信息
		user.setArticleNum(user.getArticleNum()+1);
		LocalCache.put("user"+authorId,user);
		userService.updateById(user);
		return Result.success("添加成功");
	}

	public Result updateArticle(ArticleDTO articleDTO, String authorId) {
		User user = userService.getUserById(authorId);
		Article article = getArticle(articleDTO.getId());
		if (article==null) return Result.error("文章不存在");
		// 验证作者和文章是否对应
		if (!Objects.requireNonNull(getArticle(articleDTO.getId())).getAuthorId().equals(authorId))
			return Result.error("非法操作");
		// 保存图片
		if (!articleDTO.getImg().equals("")){
			// 若图片修改过 先删除原图片
			String rawPath = Objects.requireNonNull(getArticle(articleDTO.getId())).getImg();
			myUtil.deleteFile(rawPath);
			String imgPath = myUtil.saveImg(articleDTO.getImg(),authorId);
			if (imgPath==null){
				return Result.error("文章保存失败, 原因：插图");
			}
			article.setImg(imgPath);
		}
		// 删除原文章
		String rawPath = Objects.requireNonNull(getArticle(articleDTO.getId())).getContent();
		myUtil.deleteFile(rawPath);
		// 保存文章
		String articlePath = saveContent(articleDTO.getContent(),authorId);
		if (articlePath==null){
			return Result.error("文章保存失败, 原因：文章内容");
		}
		article.setContent(articlePath);
		// title
		article.setTitle(articleDTO.getTitle());
		// category
		article.setCategory(articleDTO.getCategory());
		// 刷新作者的 category 信息
		user.setCategoryNum(categoryService.addCategory(authorId, articleDTO.getCategory()));
		categoryService.articleAddOne(authorId,articleDTO.getCategory());
		categoryService.articleMinusOne(authorId,article.getCategory());
		// 刷新作者的 tag 库
		updateTags(authorId, articleDTO.getTag());
		// 设置 本文章的 tag
		JSONObject thisTagJson = new JSONObject();
		thisTagJson.put("tags", articleDTO.getTag());
		article.setTag(thisTagJson.toJSONString());
		// authorId
		article.setAuthorId(authorId);
		// public
		article.setIsPublic(articleDTO.getIsPublic());
		//digest
		if (articleDTO.getContent().length()>180){
			article.setDigest(articleDTO.getContent().substring(0,180)+"...");
		}else {
			article.setDigest(articleDTO.getContent());
		}
		//updateTime
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US); // 格式化时间
		String date = dtf.format(LocalDateTime.now());
		article.setUpdateTime(date);

		article.setId(articleDTO.getId());
		articleMapper.updateById(article);
		LocalCache.put("article"+article.getId(),article);
		LocalCache.removeByPre("articleList"+authorId);
		// 更新作者文章信息
		userService.updateById(user);
		LocalCache.put("user"+authorId,user);
		log.info("修改文章成功 id："+ article.getAuthorId()+" title:" + article.getTitle());
		return Result.success("修改成功");
	}

	public Result getTagsById(String authorId){
		String[] tags= getTags(authorId);
		return Result.success("获取标签成功",tags);
	}
	/**
	 * @description: 以下部分为内部封装的方法 方便操作  private
	 */

	//保存错误时返回null,  成功返回相对路径
	public String saveContent(String contentData,String authorId) {
		String projectPath = System.getProperty("user.dir");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS", Locale.US); // 格式化时间
		String date = dtf.format(LocalDateTime.now());
		// 文件名
		String fileName = date + "_" + authorId+".txt";
		String storagePath = projectPath + File.separator + PathConfig.dataPath + File.separator + PathConfig.articlePath + File.separator + fileName;
		String relativePath = PathConfig.dataPath + File.separator + PathConfig.articlePath + File.separator + fileName;
		if (myUtil.writeTxt(contentData, storagePath)){
			log.info("文章保存成功");
			return relativePath;
		}else {
			log.warn("文章保存失败");
			return null;
		}
	}

	//读取错误时返回空字符串
	private String getContent(String filePath) {
		String articleContent;
		String key = "articleContent"+filePath;
		if ((articleContent = (String) LocalCache.get(key))==null) {
			String projectPath = System.getProperty("user.dir");
			String storagePath = projectPath + File.separator+ filePath;
			articleContent =  myUtil.readTxt(storagePath);
			if (articleContent==null) return null;
			LocalCache.put(key, articleContent);
			return articleContent;
		}
		return articleContent;
	}


	// 根据author 获取 tag 数据
	private String[] getTags(String authorId){
		String[] tags;
		String key = "tags"+authorId;
		if ((tags = (String[]) LocalCache.get(key))==null) {
			Tag tag = tagMapper.selectById(authorId);
			if (tag==null) return new String[] {};
			JSONArray jsonArray = (JSONArray) JSON.parseObject(tag.getTags()).get("tags");
			tags = jsonToArray(jsonArray);
			LocalCache.put(key, tags);
			return tags;
		}
		return tags;
	}
	// tags, 根据authorId刷新用户的 tags 数据
	private void updateTags(String authorId, String[] tags){
		String[] rawTags = getTags(authorId);
		String[] newTags = myUtil.union(rawTags, tags);
		LocalCache.put("tags"+authorId,newTags);
		Tag tag = new Tag();
		tag.setAuthorId(authorId);
		JSONObject json = new JSONObject();
		json.put("tags", newTags);
		tag.setTags(json.toJSONString());
		tagMapper.updateById(tag);
	}

	private String[] jsonToArray(JSONArray jsonArray){
		ArrayList<String > list = new ArrayList<>();
		jsonArray.forEach((object)->{
			list.add(object.toString());
		});
		return list.toArray(new String[0]);
	}


}
