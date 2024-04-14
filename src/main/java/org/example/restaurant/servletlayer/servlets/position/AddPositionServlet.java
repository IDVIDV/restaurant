package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.dto.position.AddPositionDto;
import org.example.restaurant.datalayer.dto.position.PositionDto;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.PositionMapper;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.PositionService;
import org.example.restaurant.servicelayer.validators.PositionValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@WebServlet(value = "/admin/add-position")
public class AddPositionServlet extends HttpServlet {
    private PositionService positionService;

    @Override
    public void init() throws ServletException {
        positionService = new PositionService(PositionValidator.getInstance(),
                PositionMapper.getInstance(),
                new PositionRepository(ConnectionProvider.getInstance()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("add-position.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OperationResult<PositionDto> result;

        try {
            AddPositionDto addPositionDto = new AddPositionDto();
            addPositionDto.setPositionName(req.getParameter("positionName"));
            addPositionDto.setPrice(BigDecimal.valueOf(Long.parseLong(req.getParameter("price"))));
            addPositionDto.setWeight(Double.parseDouble(req.getParameter("weight")));
            addPositionDto.setProtein(Double.parseDouble(req.getParameter("protein")));
            addPositionDto.setFat(Double.parseDouble(req.getParameter("fat")));
            addPositionDto.setCarbohydrate(Double.parseDouble(req.getParameter("carbohydrate")));
            addPositionDto.setVegan(!Objects.isNull(req.getParameter("isVegan")));
            addPositionDto.setIngredients(req.getParameter("ingredients"));

            result = positionService.add(addPositionDto);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: "+ e.getMessage());
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", result.getFailReason());
            doGet(req, resp);
        }
    }
}
