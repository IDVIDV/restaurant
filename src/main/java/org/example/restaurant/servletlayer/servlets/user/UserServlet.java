package org.example.restaurant.servletlayer.servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.dto.user.UpdateUserDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.entities.User;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.UserMapper;
import org.example.restaurant.datalayer.repositories.UserRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.UserService;
import org.example.restaurant.servicelayer.validators.UserValidator;

import java.io.IOException;

@WebServlet(value = "/user")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService(UserValidator.getInstance(),
                UserMapper.getInstance(),
                new UserRepository(ConnectionProvider.getInstance()));
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
