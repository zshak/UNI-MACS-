import Database.DatabaseManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

@WebListener(value = "/HTML/AuthorizationPage.html")
public class Initializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent even) {
        try {
            DatabaseManager.connect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // DatabaseManager.runDatabaseCreationScript("tank_database");
    }
}