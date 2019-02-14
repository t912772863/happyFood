package com.tian.happyfood.service.wechatutil.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public class NewsResponse extends WXResponseData{
    /**
     * 消息的数量, 微信一次可以回复多个消息
     * 大多数情况下只有一个消息, 给个默认值, 需要的话再修改
     * */
    private String ArticleCount = "1";
    /** 具体每个消息*/
    private List<item> Articles;
    /** 标题*/
    private String Title;
    /** 消息的主题描述*/
    private String Description;
    /** 消息中大图的链接*/
    private String PicUrl;
    /** 点击后跳的链接*/
    private String Url;

    public String getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(String articleCount) {
        ArticleCount = articleCount;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public List<item> getArticles() {
        return Articles;
    }

    public void setArticles(List<item> articles) {
        Articles = articles;
    }

    public static class item {
        private String Title;
        private String Description;
        private String PicUrl;
        private String Url;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getPicUrl() {
            return PicUrl;
        }

        public void setPicUrl(String picUrl) {
            PicUrl = picUrl;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }
    }
}
