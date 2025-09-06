package Admin.Servlets;

import Database.DatabaseManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ClearHistoryServlet", value = "/ClearHistoryServlet")
public class ClearHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quizId = Integer.valueOf(request.getParameter("quizId"));
        try {
            DatabaseManager.clearQuizHistory(quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("ManageQuizzesServlet");
    }
}
