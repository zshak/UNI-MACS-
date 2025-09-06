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
import java.sql.Date;
import java.sql.SQLException;


@WebServlet(name = "AddFriendServlet", value = "/AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String referer = request.getHeader("referer");
        User currentUser = (User) request.getSession().getAttribute("user");
        int toUserId = Integer.parseInt(request.getParameter("friendId"));
        Date currentDateTime = new Date(System.currentTimeMillis());
        try {
            DatabaseManager.SendMail(currentUser.getId(), toUserId, "FRT", "", "", currentDateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (referer != null && referer.contains("/HTML/AddFriendPage.jsp")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/AddFriendPage.jsp");
            dispatcher.forward(request, response);
        } else if (referer != null && referer.contains("/HTML/ViewProfile.jsp")) {
            String url = "/HTML/ViewProfile.jsp?userId=" + currentUser.getId();
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/HTML/AddFriendPage.jsp");
            dispatcher.forward(request, response);
        }
    }
}
