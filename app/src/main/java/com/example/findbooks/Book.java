package com.example.findbooks;

public class Book {

    private String image,title,author,publishedDate,publisher,language,description,read,buy;
    private double price;

    public Book(String image,String title,String author,String publishedDate,String publisher,String language,String description,String read,String buy,double price){
        this.image=image;
        this.title=title;
        this.author=author;
        this.publishedDate=publishedDate;
        this.publisher=publisher;
        this.language=language;
        this.description=description;
        this.read=read;
        this.buy=buy;
        this.price=price;
    }
    public String getImage(){
        return image;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getPublishedDate(){
        return publishedDate;
    }
    public String getPublisher(){
        return publisher;
    }
    public String getLanguage(){return language;}
    public String getDescription(){
        return description;
    }
    public String getRead(){
        return read;
    }
    public String getBuy(){
        return buy;
    }
    public double getPrice(){
        return price;
    }
}
