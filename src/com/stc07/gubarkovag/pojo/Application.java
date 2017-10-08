package com.stc07.gubarkovag.pojo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Application implements Serializable {
    private static final long serialVersionUID = 2L;

    @XmlEnum(String.class)
    public enum Status {
        @XmlEnumValue("approved")
        APPROVED,
        @XmlEnumValue("waiting")
        WAITING,
        @XmlEnumValue("rejected")
        REJECTED
    }

    @XmlElement
    private Integer user_id;
    @XmlElement
    private Integer book_id;
    @XmlElement
    private Status status;

    public Application() {

    }

    public Application(Integer user_id, Integer book_id, Status status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.status = status;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Application: [user id = ").append(user_id).append(", book id = ")
                .append(book_id).append(", status = ").append(status).append("]");

        return sb.toString();
    }
}
