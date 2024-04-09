package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.io.IOException;

@WebServlet(name = "positionList", value = {"/position-list", ""})
public class PositionListServlet extends HttpServlet {
    private PositionService positionService;

    @Override
    public void init() {
        positionService = new PositionService(new PositionRepository(ConnectionProvider.getInstance()),
                PositionValidator.getInstance());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String positionName = request.getParameter("positionName");
        request.setAttribute("positions", positionService.getByName(positionName));
        request.getRequestDispatcher("positions.jsp").forward(request, response);
    }
}
