package HomePage;

import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserSearchServlet", value = "/UserSearchServlet")
public class UserSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("searchQuery");
        List<User> allUsers;
        try {
            allUsers = DatabaseManager.GetAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<User> filteredUsers = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            for (User friend : allUsers) {
                if (friend.getUsername().toLowerCase().startsWith(search.toLowerCase())) {
                    filteredUsers.add(friend);
                }
            }
        } else {
            filteredUsers = allUsers;
        }
        request.setAttribute("allUsers", filteredUsers);
        RequestDispatcher dis = request.getRequestDispatcher("/HTML/Searching.jsp");
        dis.forward(request, response);
    }
}
