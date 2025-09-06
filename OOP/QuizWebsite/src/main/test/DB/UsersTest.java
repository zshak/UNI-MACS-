package DB;

import Database.DatabaseManager;
import OAuth.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersTest {
    @BeforeEach
    public void setUp() {
        DatabaseManager.runDatabaseCreationScript("tank_database_test");
    }

    @Test
    public void testAddUser() throws SQLException {
        assertEquals(DatabaseManager.addUser("A", "shengelia"), 1);
        assertEquals(DatabaseManager.addUser("B", "shengelia"), 2);
        assertEquals(DatabaseManager.addUser("C", "shengelia"), 3);
        assertEquals(DatabaseManager.addUser("D", "shengelia"), 4);
        assertEquals(DatabaseManager.addUser("E", "shengelia"), 5);
        assertEquals(DatabaseManager.addUser("F", "shengelia"), 6);
        assertEquals(DatabaseManager.addUser("G", "shengelia"), 7);
        assertEquals(DatabaseManager.addUser("H", "shengelia"), 8);
        assertEquals(DatabaseManager.addUser("I", "shengelia"), 9);
        assertEquals(DatabaseManager.addUser("A", "shengelia"), -1);
        assertEquals(DatabaseManager.addUser("B", "shengelia"), -1);
        assertEquals(DatabaseManager.addUser("C", "shengelia"), -1);
        assertEquals(DatabaseManager.addUser("D", "shengelia"), -1);
    }

    @Test
    public void testUserIsRegistered() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        assertTrue(DatabaseManager.userIsRegistered("A", "shengelia") != null);
        assertTrue(DatabaseManager.userIsRegistered("B", "shengelia") != null);
        assertTrue(DatabaseManager.userIsRegistered("C", "shengelia") != null);
        assertTrue(DatabaseManager.userIsRegistered("D", "shengelia") != null);
        assertTrue(DatabaseManager.userIsRegistered("E", "shengelia") == null);
        assertTrue(DatabaseManager.userIsRegistered("F", "shengelia") == null);
        assertTrue(DatabaseManager.userIsRegistered("G", "shengelia") == null);
    }

    @Test
    public void testGetAdminStatusAndPromoteUser() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        assertEquals(DatabaseManager.getUserAdminStatus(new User(1, "A")), false);
        assertEquals(DatabaseManager.getUserAdminStatus(new User(2, "B")), false);
        assertEquals(DatabaseManager.getUserAdminStatus(new User(3, "C")), false);
        assertEquals(DatabaseManager.getUserAdminStatus(new User(4, "D")), false);

        DatabaseManager.promoteUser(1);
        DatabaseManager.promoteUser(2);

        assertEquals(DatabaseManager.getUserAdminStatus(new User(1, "A")), true);
        assertEquals(DatabaseManager.getUserAdminStatus(new User(2, "B")), true);
        assertEquals(DatabaseManager.getUserAdminStatus(new User(3, "C")), false);
        assertEquals(DatabaseManager.getUserAdminStatus(new User(4, "D")), false);
    }

    @Test
    public void testDeleteUser() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        DatabaseManager.DeleteUser(1);
        DatabaseManager.DeleteUser(2);

        assertTrue(DatabaseManager.userIsRegistered("A", "shengelia") == null);
        assertTrue(DatabaseManager.userIsRegistered("B", "shengelia") == null);
        assertTrue(DatabaseManager.userIsRegistered("C", "shengelia") != null);
        assertTrue(DatabaseManager.userIsRegistered("D", "shengelia") != null);
    }

    @Test
    public void testGetNumberOfUsers() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        assertEquals(DatabaseManager.getNumberOfUsers(), 4);

        DatabaseManager.addUser("E", "shengelia");

        assertEquals(DatabaseManager.getNumberOfUsers(), 5);

        DatabaseManager.DeleteUser(5);

        assertEquals(DatabaseManager.getNumberOfUsers(), 4);

        DatabaseManager.DeleteUser(1);
        DatabaseManager.DeleteUser(2);
        DatabaseManager.DeleteUser(3);
        DatabaseManager.DeleteUser(4);

        assertEquals(DatabaseManager.getNumberOfUsers(), 0);
    }

    @Test
    public void testGetUser() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        assertEquals(DatabaseManager.getUser(1).getUsername(), "A");
        assertEquals(DatabaseManager.getUser(2).getUsername(), "B");
        assertEquals(DatabaseManager.getUser(3).getUsername(), "C");
        assertEquals(DatabaseManager.getUser(4).getUsername(), "D");
    }

    @Test
    public void testGetAllUser() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        var users = DatabaseManager.GetAllUsers();

        assertEquals(users.size(), 4);

        assertEquals(users.get(0).getUsername(), "A");
        assertEquals(users.get(1).getUsername(), "B");
        assertEquals(users.get(2).getUsername(), "C");
        assertEquals(users.get(3).getUsername(), "D");

        DatabaseManager.addUser("E", "shengelia");
        DatabaseManager.addUser("F", "shengelia");

        var updatedUsers = DatabaseManager.GetAllUsers();

        assertEquals(updatedUsers.get(4).getUsername(), "E");
        assertEquals(updatedUsers.get(5).getUsername(), "F");
    }

    @Test
    public void testGetAllNonAdminUsers() throws SQLException {
        DatabaseManager.addUser("A", "shengelia");
        DatabaseManager.addUser("B", "shengelia");
        DatabaseManager.addUser("C", "shengelia");
        DatabaseManager.addUser("D", "shengelia");

        var nonAdmins = DatabaseManager.GetAllNonAdminUsers();

        assertEquals(nonAdmins.size(), 4);

        assertEquals(nonAdmins.get(0).getUsername(), "A");
        assertEquals(nonAdmins.get(1).getUsername(), "B");
        assertEquals(nonAdmins.get(2).getUsername(), "C");
        assertEquals(nonAdmins.get(3).getUsername(), "D");

        DatabaseManager.promoteUser(1);
        DatabaseManager.promoteUser(2);

        var updatedNonAdmins = DatabaseManager.GetAllNonAdminUsers();

        assertEquals(updatedNonAdmins.size(), 2);

        assertEquals(updatedNonAdmins.get(0).getUsername(), "C");
        assertEquals(updatedNonAdmins.get(1).getUsername(), "D");
    }
}
