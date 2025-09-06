package Friends;

import Database.DatabaseManager;
import OAuth.Models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchFriendsServlet", value = "/SearchFriendsServlet")
public class SearchFriendsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        try {
            User currentUser = (User) request.getSession().getAttribute("user");
            List<User> userFriends = DatabaseManager.GetUserFriends(currentUser.getId());

            List<User> filteredFriends = new ArrayList<>();
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                for (User friend : userFriends) {
                    if (friend.getUsername().toLowerCase().startsWith(searchQuery.toLowerCase())) {
                        filteredFriends.add(friend);
                    }
                }
            } else {
                filteredFriends = userFriends;
            }

            request.setAttribute("filteredFriends", filteredFriends);
            RequestDispatcher dis = request.getRequestDispatcher("/HTML/Friends.jsp");
            dis.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
