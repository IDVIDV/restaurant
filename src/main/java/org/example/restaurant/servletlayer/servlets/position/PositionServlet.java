package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.exceptions.DataLayerException;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.io.IOException;

@WebServlet(value = "/position")
public class PositionServlet extends HttpServlet {
    private PositionService positionService;

    @Override
    public void init() {
        positionService = new PositionService(new PositionRepository(ConnectionProvider.getInstance()),
                PositionValidator.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long positionId = Long.valueOf(req.getParameter("positionId"));
        try {
            req.setAttribute("position", positionService.getById(positionId));
            req.getRequestDispatcher("position.jsp").forward(req, resp);
        } catch (DataLayerException e) {
            resp.sendError(404, e.getMessage());
        }
    }
}
