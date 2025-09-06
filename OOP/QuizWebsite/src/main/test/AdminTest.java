import Admin.Models.Announcement;
import Admin.Models.AnnouncementUserModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    public void testConstructorAndGetters() {
        int adminId = 1;
        String announcement = "Test Announcement";
        Announcement ann = new Announcement(adminId, announcement);

        assertEquals(adminId, ann.getAdminId());
        assertEquals(announcement, ann.getAnnounement());
    }

    @Test
    public void testSetter() {
        int adminId = 1;
        String announcement = "Test Announcement";
        Announcement ann = new Announcement(adminId, announcement);

        int newAdminId = 2;
        String newAnnouncement = "Updated Announcement";

        ann.setAdminId(newAdminId);
        ann.setAnnounement(newAnnouncement);

        assertEquals(newAdminId, ann.getAdminId());
        assertEquals(newAnnouncement, ann.getAnnounement());
    }

    @Test
    public void testConstructorAndGettersAnnouncementUserModel() {
        String adminUsername = "admin";
        String announcement = "Test Announcement";
        AnnouncementUserModel userModel = new AnnouncementUserModel(adminUsername, announcement);

        assertEquals(adminUsername, userModel.getAdminUsername());
        assertEquals(announcement, userModel.getAnnounement());
    }

    @Test
    public void testSetterAnnouncementUserModel() {
        String adminUsername = "admin";
        String announcement = "Test Announcement";
        AnnouncementUserModel userModel = new AnnouncementUserModel(adminUsername, announcement);

        String newAdminUsername = "newadmin";
        String newAnnouncement = "Updated Announcement";

        userModel.setAdminUsername(newAdminUsername);
        userModel.setAnnounement(newAnnouncement);

        assertEquals(newAdminUsername, userModel.getAdminUsername());
        assertEquals(newAnnouncement, userModel.getAnnounement());
    }
}
