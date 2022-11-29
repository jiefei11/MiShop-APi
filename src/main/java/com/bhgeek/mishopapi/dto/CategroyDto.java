package com.bhgeek.mishopapi.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bhgeek.mishopapi.entity.Goods;
import lombok.Data;

import java.util.List;

@Data
public class CategroyDto {

    private Integer id;


    private String cateName;

    private List<Goods> goods;
}
