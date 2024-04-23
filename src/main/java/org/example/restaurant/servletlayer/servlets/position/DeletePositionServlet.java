package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProviderImpl;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.PositionMapper;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.io.IOException;

@WebServlet(value = "/admin/delete-position")
public class DeletePositionServlet extends HttpServlet {
    private PositionService positionService;

    @Override
    public void init() throws ServletException {
        positionService = new PositionService(
                new PositionValidator(),
                new PositionMapper(),
                new PositionRepository(new ConnectionProviderImpl())
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long positionId = Long.valueOf(req.getParameter("positionId"));
        OperationResult<Boolean> result;

        try {
            result = positionService.delete(positionId);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", result.getFailReason());
            req.getRequestDispatcher(req.getContextPath() + "/position").forward(req, resp);
        }
    }
}
