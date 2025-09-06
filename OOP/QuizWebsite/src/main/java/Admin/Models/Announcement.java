package Admin.Models;

public class Announcement {
    private int adminId;
    private String announement;

    public Announcement(int adminId, String announcement) {
        this.adminId = adminId;
        this.announement = announcement;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAnnounement() {
        return announement;
    }

    public void setAnnounement(String announement) {
        this.announement = announement;
    }
}
