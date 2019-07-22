package com.changyou.community.model;
public class LogItem {
    private Long id;
    private String itemId;
    private String itemName;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemName() {
        return itemName;
    }
    @Override
    public String toString() {
        return "LogItem [id=" + id + ", itemId=" + itemId + ", itemName="
                + itemName + "]";
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
