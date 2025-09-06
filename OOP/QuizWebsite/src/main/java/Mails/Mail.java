package Mails;
import java.util.Date;

public abstract class Mail {

    private int mailId;
    private int fromId;
    private int toId;
    private Date sentDate;

    public Mail(int mailId, int fromId, int toId, Date sentDate) {
        this.mailId = mailId;
        this.fromId = fromId;
        this.toId = toId;
        this.sentDate = sentDate;
    }

    public int getId() {
        return mailId;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public Date getDate() {
        return sentDate;
    }

    @Override
    public boolean equals(Object object){
        Mail o= (Mail) object;
        if (this.fromId!=o.fromId )return false;
        if (this.toId!=o.toId) return false;
        if (!this.getType().equals(o.getType())) return false;
        //if (this.getDate().compareTo(o.getDate())!=0)return false;
        if (!this.getContent().equals(o.getContent())) return false;
        return true;
    }

    public abstract String getType();
    public abstract String getTitle();
    public abstract String getContent();


}