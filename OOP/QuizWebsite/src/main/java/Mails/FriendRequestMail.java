package Mails;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

public class FriendRequestMail extends Mail {

    public FriendRequestMail(int mailId, int fromId, int toId, Date sentDate) {
        super(mailId, fromId, toId, sentDate);

    }

    @Override
    public String getType() {
        return "FRT";
    }

    @Override
    public String getTitle() {
        return "New Friend Request";
    }

    @Override
    public String getContent() {
        return null;
        //return yes or no
    }



}