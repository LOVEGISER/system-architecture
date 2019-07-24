package hello.java.designpattern.visitor;

import java.util.Date;

//项目计划表
public class ProjectElement implements Element {

    private String projectName  ;
    private String projectContent ;
    private String visitorName ;
    private Date visitorTime ;

    public ProjectElement(String projectName, String projectContent) {
        this.projectName = projectName;
        this.projectContent = projectContent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    public void signature(String visitorName,Date visitorTime) {
        this.visitorName =  visitorName;
        this.visitorTime = visitorTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public Date getVisitorTime() {
        return visitorTime;
    }

    public void setVisitorTime(Date visitorTime) {
        this.visitorTime = visitorTime;
    }
}
