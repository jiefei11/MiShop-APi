package com.bhgeek.mishopapi.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("goods_attr_value")
public class GoodsAttrValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    @TableField("pid")
    private Integer pid;

    /**
     * 规格参数
     */
    @TableField("sku")
    private String sku;

    /**
     * 库存
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 销量
     */
    @TableField("sale")
    private Integer sale;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 图片
     */
    @TableField("image")
    private String image;

    /**
     * 唯一值
     */
    @TableField("`unique`")
    private String unique;


}
