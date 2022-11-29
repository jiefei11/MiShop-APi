package com.bhgeek.mishopapi.mapper;

import com.bhgeek.mishopapi.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-25
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}
