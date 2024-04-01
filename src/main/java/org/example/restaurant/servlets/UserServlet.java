package org.example.restaurant.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.data.entities.User;
import org.example.restaurant.data.repositories.UserRepository;

import java.io.IOException;

@WebServlet(value = "/user")
public class UserServlet extends HttpServlet {
    UserRepository userRepository = new UserRepository("user", )
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
