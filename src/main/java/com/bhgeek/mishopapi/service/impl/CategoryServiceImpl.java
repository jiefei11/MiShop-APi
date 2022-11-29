package com.bhgeek.mishopapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bhgeek.mishopapi.entity.Category;
import com.bhgeek.mishopapi.mapper.CategoryMapper;
import com.bhgeek.mishopapi.service.ICategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
