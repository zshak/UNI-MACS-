package Quiz.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet(name = "temp", value = "/temp")
public class tempServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().getAttribute("question");
        String chosenOption = request.getParameter("chosenOption"); // Get the value of the chosen radio button
        String[] inputtedOptions = request.getParameterValues("optionsArray");

        String[] inputArray = request.getParameterValues("strings[]");
        String[] correctArray = request.getParameterValues("correct[]");

        String t = null;
    }
}
