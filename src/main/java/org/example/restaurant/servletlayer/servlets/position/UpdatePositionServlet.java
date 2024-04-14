package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.dto.position.UpdatePositionDto;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.PositionMapper;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@WebServlet(value = "/admin/update-position")
public class UpdatePositionServlet extends HttpServlet {
    private PositionService positionService;

    @Override
    public void init() throws ServletException {
        positionService = new PositionService(PositionValidator.getInstance(),
                PositionMapper.getInstance(),
                new PositionRepository(ConnectionProvider.getInstance()));
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
            req.getRequestDispatcher("update-position.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", result.getFailReason());
            req.getRequestDispatcher("").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OperationResult<PositionDto> result;

        try {
            UpdatePositionDto updatePositionDto = new UpdatePositionDto();
            updatePositionDto.setId(Long.parseLong(req.getParameter("positionId")));
            updatePositionDto.setPositionName(req.getParameter("positionName"));
            updatePositionDto.setPrice(BigDecimal.valueOf(Long.parseLong(req.getParameter("price"))));
            updatePositionDto.setWeight(Double.parseDouble(req.getParameter("weight")));
            updatePositionDto.setProtein(Double.parseDouble(req.getParameter("protein")));
            updatePositionDto.setFat(Double.parseDouble(req.getParameter("fat")));
            updatePositionDto.setCarbohydrate(Double.parseDouble(req.getParameter("carbohydrate")));
            updatePositionDto.setVegan(!Objects.isNull(req.getParameter("isVegan")));
            updatePositionDto.setIngredients(req.getParameter("ingredients"));

            result = positionService.update(updatePositionDto);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", result.getFailReason());
            doGet(req, resp);
        }
    }
}
