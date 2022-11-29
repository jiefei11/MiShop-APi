package com.bhgeek.mishopapi.dto;

import com.bhgeek.mishopapi.entity.Goods;
import com.bhgeek.mishopapi.entity.GoodsAttrValue;
import lombok.Data;

@Data
public class CartDto {
    private GoodsAttrValue attrInfo;

    private Goods storeInfo;

    private Integer id;

    private String type;

    private Integer productId;

    private Integer cartNum;
}
