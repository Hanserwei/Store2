package com.hanserwei.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7350725223100338485L;

    /**
     * 父分类名称
     */
    private String fatherName;

    /**
     * 子分类集合
     */
    private List<CategoryChildrenVO> childNames;
}
