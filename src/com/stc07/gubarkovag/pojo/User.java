package com.stc07.gubarkovag.pojo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
    private static final long serialVersionUID = 3L;

    @XmlEnum(String.class)
    public enum Role {
        @XmlEnumValue("admin")
        ADMIN,
        @XmlEnumValue("authorizedUser")
        AUTHORIZEDUSER,
        @XmlEnumValue("nonAuthorizedUser")
        NONAUTHORIZEDUSER
    }

    @XmlAttribute
    private Integer id;
    @XmlElement
    private String login;
    @XmlElement
    private String password;
    @XmlElement
    private Role role;

    public User() {

    }

    public User(Integer id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: [id = ").append(id).append(", login = ").append(login)
                .append(", password = ").append(password)
                .append(", role = ").append(role).append("]");

        return sb.toString();
    }
}
