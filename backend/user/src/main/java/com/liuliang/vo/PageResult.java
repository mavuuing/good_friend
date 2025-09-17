package com.liuliang.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> list;
    private int page;
    private int size;
    private int total;
}


