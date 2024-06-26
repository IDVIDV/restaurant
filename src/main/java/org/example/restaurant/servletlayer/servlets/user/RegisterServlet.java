package org.example.restaurant.servletlayer.servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProviderImpl;
import org.example.restaurant.datalayer.dto.user.AddUserDto;
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
import java.util.Objects;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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

    //TODO
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!Objects.isNull(req.getSession().getAttribute("user"))) {
            resp.sendRedirect(req.getContextPath());
            return;
        }

        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    //TODO DTO
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OperationResult<UserDto> result;

        try {
            AddUserDto addUserDto = new AddUserDto();
            addUserDto.setLogin(req.getParameter("login"));
            addUserDto.setPassword(req.getParameter("password"));
            addUserDto.setPhoneNumber(req.getParameter("phoneNumber"));

            result = userService.register(addUserDto);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/register");
        }

        if (result.isSuccess()) {
            req.getSession().setAttribute("user", result.getResult());
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", result.getFailReason());
            doGet(req, resp);
        }
    }
}
