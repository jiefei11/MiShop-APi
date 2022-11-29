package com.bhgeek.mishopapi.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsVo {

    private Integer cid;
    private String storeName;
    private String storeInfo;
    private String image;
    private String imageArr;
    private String describe;
    private Integer price;
    private Integer stock;
    private String keyword;
    private String skuInfo;

}
