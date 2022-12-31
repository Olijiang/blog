package blog.service;

import blog.mapper.AlbumMapper;
import blog.mapper.ArticleMapper;
import blog.mapper.ImageMapper;
import blog.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ZGB
 * @version 1.0
 * @description: 代理所有跟数据库交互的操作
 * @date 2022/12/26 21:27
 */
@Service
@Slf4j
public class SqlAgent {
	@Resource
	private AlbumMapper albumMapper;
	@Resource
	private ImageMapper imageMapper;
	@Resource
	private CategoryServiceImpl categoryService;
	@Resource
	private ArticleMapper articleMapper;
	@Resource
	private TagMapper tagMapper;
	@Resource
	private UserServiceImpl userService;



}
