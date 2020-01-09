package com.thtf.common.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ---------------------------
 * 分页查询结果
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 13:59
 * 版本：  v1.0
 * ---------------------------
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pager<T> {
    // 数据总数
    private long total;
    // 数据列表
    private List<T> records;
}
