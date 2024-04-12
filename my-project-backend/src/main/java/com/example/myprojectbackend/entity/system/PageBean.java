package com.example.myprojectbackend.entity.system;

import lombok.Data;
import lombok.Getter;
@Data

public class PageBean {



    private int pageNum; // 第幾頁
    private int pageSize; // 每頁紀錄數
    private int start;  // 起始頁
    private String query; // 參數

    public PageBean() {
    }

    public PageBean(int pageNum, int pageSize, String query) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.query = query;
    }

    public PageBean(int pageNum, int pageSize) {
        super();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }





    public int getStart() {
        return (pageNum-1)*pageSize;
    }


}