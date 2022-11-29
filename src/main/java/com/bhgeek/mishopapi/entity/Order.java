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
 * @since 2022-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  
     */
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

    @TableField("`unique`")
    private String unique;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
