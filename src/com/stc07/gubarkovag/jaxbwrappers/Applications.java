package com.stc07.gubarkovag.jaxbwrappers;

import com.stc07.gubarkovag.pojo.Application;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "applications")
@XmlAccessorType(XmlAccessType.FIELD)
public class Applications {
    @XmlElement(name = "application")
    private List<Application> applications;

    public Applications() {

    }

    public Applications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Application> getApplications() {
        return applications;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Application application : applications) {
            sb.append(application.toString()).append(" ");
        }

        return sb.toString();
    }
}
