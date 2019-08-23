package com.changyou.community.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by xuxingping on 2019/8/23.
 */

public class TagDTO {
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private List<String> tags;
}
