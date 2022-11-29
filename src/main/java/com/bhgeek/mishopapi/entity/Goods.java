package com.bhgeek.mishopapi.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    @TableField("cid")
    private Integer cid;

    /**
     * 商品名称
     */
    @TableField("store_name")
    private String storeName;

    /**
     * 商品信息
     */
    @TableField("store_info")
    private String storeInfo;

    /**
     * 商品图片
     */
    @TableField("image")
    private String image;

    /**
     * 商品轮播图
     */
    @TableField("image_arr")
    private String imageArr;

    /**
     * 商品描述
     */
    @TableField("description")
    private String description;

    /**
     * 商品价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 商品库存
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 商品销量
     */
    @TableField("sale")
    private Integer sale;

    /**
     * 商品关键字
     */
    @TableField("keyword")
    private String keyword;

    /**
     * 商品状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否删除 1是 0 否
     */
    @TableField("is_delete")
    private Integer isDelete;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
