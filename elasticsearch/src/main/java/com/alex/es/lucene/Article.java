package com.alex.es.lucene;

import org.apache.lucene.document.*;

public class Article {
    private Long id;//1：文章id
    private String title;//2：文章名称
    private String content;// 3:文章内容
    private String author;// 4：文章作者
    private String url;//5：文章url地址
    public Article(){}

    public Article(Long id, String title, String content, String author,
            String url) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.url = url;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "id : " + id + " , title : " + title + " , content : " + content + " , author : " + author + " , url : " + url;
    }
}
