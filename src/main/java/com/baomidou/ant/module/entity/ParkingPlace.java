package com.baomidou.ant.module.entity;

import 你自己的父类实体,没有就不用设置!;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 充电桩所在位置表
 * </p>
 *
 * @author baixi
 * @since 2023-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParkingPlace extends 你自己的父类实体,没有就不用设置! {

    private static final long serialVersionUID = 1L;

    /**
     * 充电桩所在地点描述
     */
    private String location;

    /**
     * 记录创建时间
     */
    private Integer createTime;

    /**
     * 记录修改时间
     */
    private Integer updateTime;

    /**
     * 记录是否被删除
     */
    private Integer isDeleted;


}
