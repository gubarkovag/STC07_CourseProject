package com.stc07.gubarkovag.pojo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    private Integer id;
    @XmlElement
    private String name;
    @XmlElement
    private String genre;

    public Book() {

    }

    public Book(Integer id, String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book: [id = ").append(id).append(", name = ")
                .append(name).append(", genre = ").append(genre).append("]");

        return sb.toString();
    }
}
