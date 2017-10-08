package com.stc07.gubarkovag.jaxbwrappers;

import com.stc07.gubarkovag.pojo.User;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class Users {
    @XmlElement(name = "user")
    private List<User> users;

    public Users() {

    }

    public Users(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.toString()).append(" ");
        }

        return sb.toString();
    }
}
