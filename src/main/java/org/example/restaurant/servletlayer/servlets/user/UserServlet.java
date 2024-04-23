package org.example.restaurant.servletlayer.servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProviderImpl;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.UserMapper;
import org.example.restaurant.datalayer.repositories.UserRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.PasswordHasherImpl;
import org.example.restaurant.servicelayer.services.UserService;
import org.example.restaurant.servicelayer.validators.UserValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet(value = "/user")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService(
                    new PasswordHasherImpl(),
                    new UserValidator(),
                    new UserMapper(),
                    new UserRepository(new ConnectionProviderImpl())
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/userinfo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        OperationResult<UserDto> result;

        try {
            UpdateUserDto updateUserDto = new UpdateUserDto();
            updateUserDto.setId(userDto.getId());
            updateUserDto.setLogin(req.getParameter("login"));
            updateUserDto.setPhoneNumber(req.getParameter("phoneNumber"));

            result = userService.update(updateUserDto);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath() + "/logout");
        } else {
            req.setAttribute("error", result.getFailReason());
            doGet(req, resp);
        }
    }
}
