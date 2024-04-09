package org.example.restaurant.servletlayer.servlets.position;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.entities.Position;
import org.example.restaurant.datalayer.exceptions.DataLayerException;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.servicelayer.exceptions.ServiceLayerException;
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
        positionService = new PositionService(new PositionRepository(ConnectionProvider.getInstance()),
                PositionValidator.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("add-position.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Position position = new Position();
            position.setPositionName(req.getParameter("positionName"));
            position.setPrice(BigDecimal.valueOf(Long.parseLong(req.getParameter("price"))));
            position.setWeight(Double.parseDouble(req.getParameter("weight")));
            position.setProtein(Double.parseDouble(req.getParameter("protein")));
            position.setFat(Double.parseDouble(req.getParameter("fat")));
            position.setCarbohydrate(Double.parseDouble(req.getParameter("carbohydrate")));
            position.setVegan(Objects.isNull(req.getParameter("isVegan")));
            position.setIngredients(req.getParameter("ingredients"));

            positionService.add(position);
            resp.sendRedirect(req.getContextPath());
        } catch (DataLayerException e) {
            resp.sendError(500, e.getMessage());
        } catch (ServiceLayerException e) {
            resp.sendError(400, e.getMessage());
        }   //TODO ошибки
    }
}
