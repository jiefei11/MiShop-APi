package com.bhgeek.mishopapi.entity;

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
 * 城市表
 * </p>
 *
 * @author bhgeek
 * @since 2022-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("system_city")
public class SystemCity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 城市id
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 省市级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 区号
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 合并名称
     */
    @TableField("merger_name")
    private String mergerName;

    /**
     * 经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 是否展示
     */
    @TableField("is_show")
    private Boolean isShow;


}
