package Admin.Models;

public class AnnouncementUserModel {
    private String adminUsername;
    private String announement;

    public AnnouncementUserModel(String adminUsername, String announement) {
        this.adminUsername = adminUsername;
        this.announement = announement;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAnnounement() {
        return announement;
    }

    public void setAnnounement(String announement) {
        this.announement = announement;
    }
}
