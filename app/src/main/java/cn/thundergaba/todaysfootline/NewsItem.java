package cn.thundergaba.todaysfootline;

import java.util.Date;

public class NewsItem {
    private String newsitem_id;

    private NewsUserInfo newsUserInfo;

    private Integer NewsPublishTime;

    public String getNewsItem_id() {
        return newsitem_id;
    }

    public void setNewsItem_id(String item_id) {
        this.newsitem_id = item_id;
    }

    public NewsUserInfo getNewsUserInfo() {
        return newsUserInfo;
    }

    public void setNewsUserInfo(NewsUserInfo userInfo) {
        this.newsUserInfo = userInfo;
    }

    public Integer getNewsPublishTime() {
        return NewsPublishTime;
    }

    public void setNewsPublishTime(Integer publishTime) {
        NewsPublishTime = publishTime;
    }
}
