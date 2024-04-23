package org.example.restaurant.servletlayer.servlets.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProviderImpl;
import org.example.restaurant.datalayer.dto.order.PositionInOrderDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.OrderMapper;
import org.example.restaurant.datalayer.mappers.PositionInOrderMapper;
import org.example.restaurant.datalayer.repositories.OrderRepository;
import org.example.restaurant.datalayer.repositories.PositionInOrderRepository;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.datalayer.repositories.TableRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.OrderService;
import org.example.restaurant.servicelayer.validators.OrderValidator;

import java.io.IOException;

@WebServlet("/user/add-to-order")
public class AddToOrderServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        TableRepository tableRepository = new TableRepository(new ConnectionProviderImpl());
        PositionRepository positionRepository = new PositionRepository(new ConnectionProviderImpl());
        OrderRepository orderRepository = new OrderRepository(new ConnectionProviderImpl(), tableRepository);
        PositionInOrderRepository positionInOrderRepository = new PositionInOrderRepository(new ConnectionProviderImpl(),
                positionRepository, orderRepository);

        orderService = new OrderService(
                new OrderValidator(),
                new OrderMapper(),
                new PositionInOrderMapper(),
                positionRepository,
                orderRepository,
                positionInOrderRepository
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        Long positionId = Long.parseLong(req.getParameter("positionId"));
        OperationResult<PositionInOrderDto> result;

        try {
            result = orderService.addPositionToOrder(user.getId(), positionId);
        } catch (DataBaseException e) {
            result = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath() + "/user/opened-order");
        } else {
            req.setAttribute("error", result.getFailReason());
            req.getRequestDispatcher("").forward(req, resp);
        }
    }
}
