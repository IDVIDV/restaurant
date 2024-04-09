package org.example.restaurant.servletlayer.servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.repositories.UserRepository;

import java.io.IOException;

@WebServlet(value = "/user")
public class UserServlet extends HttpServlet {
    UserRepository userRepository = new UserRepository(ConnectionProvider.getInstance());
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("userinfo.jsp").forward(req, resp);
    }
}
