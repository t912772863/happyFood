package com.tian.happyfood.service.wechatutil.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public class NewsResponse extends WXResponseData{
    private String ArticleCount;
    private List<item> Articles;
    private String Title;
    private String Description;
    private String PicUrl;
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
