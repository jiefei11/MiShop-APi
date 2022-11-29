package com.bhgeek.mishopapi.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bhgeek.mishopapi.entity.Goods;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GoodsAttrValueDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private String sku;

    private Integer stock;

    private Integer sale;

    private BigDecimal price;

    private String image;

    private String unique;

    private Integer CartNum;

    private Goods storeInfo;

}
