package com.ffms.domain.VO;/*
  @auther WJW129
  @date 2019/11/10 - 13:50
*/

import lombok.Data;

/**
 *  分类实体类
 */
@Data
public class CategoryResponse {
    private int id;
    private String name;//分类名称
    private double allPrice;//分类总金额
    private double account;//占比
}
