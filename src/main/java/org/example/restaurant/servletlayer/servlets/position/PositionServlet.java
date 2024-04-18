package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProviderImpl;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.PositionMapper;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.io.IOException;

@WebServlet(value = "/position")
public class PositionServlet extends HttpServlet {
    private PositionService positionService;

    @Override
    public void init() {
        positionService = new PositionService(PositionValidator.getInstance(),
                PositionMapper.getInstance(),
                new PositionRepository(ConnectionProviderImpl.getInstance()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long positionId = Long.valueOf(req.getParameter("positionId"));
        OperationResult<PositionDto> result;

        try {
            result = positionService.getById(positionId);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (result.isSuccess()) {
            req.setAttribute("position", result.getResult());
            req.getRequestDispatcher("position.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", result.getFailReason());
            req.getRequestDispatcher("").forward(req, resp);
        }
    }
}
