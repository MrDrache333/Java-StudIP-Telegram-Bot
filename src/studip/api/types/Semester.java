package studip.api.types;

import java.util.Date;

public class Semester {

    private String id;

    private String title;
    private String token;
    private Date begin;
    private Date end;

    public Semester() {

    }

    public Semester(String id, String title, String token, long begin, long end) {
        this.id = id;
        this.title = title;
        this.token = token;
        this.begin = new Date(begin);
        this.end = new Date(end);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
