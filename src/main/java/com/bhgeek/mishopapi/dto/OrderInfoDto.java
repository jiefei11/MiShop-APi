package com.bhgeek.mishopapi.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bhgeek.mishopapi.entity.Goods;
import com.bhgeek.mishopapi.entity.GoodsAttrValue;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orderId;

    private Integer uid;

    private String realName;

    private String phone;

    private String address;

    private String cartId;

    private BigDecimal totalPrice;

    private Integer totalNum;

    private Integer status;

    private Integer isDel;

    private String unique;

    private Integer totalSize;

    private List<GoodsAttrValueDto> attrInfo;

    private LocalDateTime createTime;
}
