package com.bhgeek.mishopapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bhgeek.mishopapi.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bhgeek.mishopapi.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-24
 */
public interface IGoodsService extends IService<Goods> {

    void addGoods(GoodsVo vo);

    Page<Goods> getGoods(String keyword, Integer page, Integer size , Integer isDelete);
}
