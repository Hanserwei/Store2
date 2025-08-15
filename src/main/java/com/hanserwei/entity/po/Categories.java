package com.hanserwei.entity.po;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品分类表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categories implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类唯一标识
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID
     */
    private Long parentId;
}