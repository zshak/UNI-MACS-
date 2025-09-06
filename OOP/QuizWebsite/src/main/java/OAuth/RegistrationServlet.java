package OAuth;


import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name="register", value = "/HTML/register")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        var session = req.getSession(true);
        try {
            int id = DatabaseManager.addUser(username, password);
           if(id != -1) {
               User currentUser = new User(id, username);
               RequestDispatcher dis = req.getRequestDispatcher("/HTML/Homepage.jsp");
               req.getSession().setAttribute("user", currentUser);
               req.getSession().setAttribute("is_admin", DatabaseManager.getUserAdminStatus(currentUser));
               dis.forward(req, resp);
           } else {
               RequestDispatcher dis = req.getRequestDispatcher("/HTML/TryAgainRegistration.jsp");
               req.setAttribute("username", username);
               dis.forward(req, resp);
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
