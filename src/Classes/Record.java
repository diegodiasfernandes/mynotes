package Classes;

public class Record {
    private int id;
    private String title;
    private String fullText;
    private java.sql.Date date;
    private java.sql.Time time;
    private int idUser;
    private String type;
    private java.sql.Date edit_date;
    private java.sql.Time edit_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public java.sql.Time getTime() {
        return time;
    }

    public void setTime(java.sql.Time time) {
        this.time = time;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public java.sql.Date getEditDate() {
        return edit_date;
    }

    public void setEditDate(java.sql.Date edit_date) {
        this.edit_date = edit_date;
    }

    public java.sql.Time getEditTime() {
        return edit_time;
    }

    public void setEditTime(java.sql.Time edit_time) {
        this.edit_time = edit_time;
    }
}