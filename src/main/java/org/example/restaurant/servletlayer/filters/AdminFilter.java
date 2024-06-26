package org.example.restaurant.servletlayer.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.dto.user.UserDto;

import java.io.IOException;
import java.util.Objects;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UserDto user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            servletRequest.setAttribute("error", "Login as admin required to perform action");
            servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
        } else if (!user.getRole().equals("admin")) {
            servletRequest.setAttribute("error", "Login as admin required to perform action");
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
