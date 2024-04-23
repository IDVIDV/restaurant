package org.example.restaurant.servletlayer.servlets.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.datalayer.ConnectionProvider;
import org.example.restaurant.datalayer.ConnectionProviderImpl;
import org.example.restaurant.datalayer.dto.order.CloseUnfinishedOrderDto;
import org.example.restaurant.datalayer.dto.order.OrderDto;
import org.example.restaurant.datalayer.dto.order.PositionInOrderDto;
import org.example.restaurant.datalayer.dto.table.TableDto;
import org.example.restaurant.datalayer.dto.user.UserDto;
import org.example.restaurant.datalayer.exceptions.DataBaseException;
import org.example.restaurant.datalayer.mappers.OrderMapper;
import org.example.restaurant.datalayer.mappers.PositionInOrderMapper;
import org.example.restaurant.datalayer.mappers.TableMapper;
import org.example.restaurant.datalayer.repositories.OrderRepository;
import org.example.restaurant.datalayer.repositories.PositionInOrderRepository;
import org.example.restaurant.datalayer.repositories.PositionRepository;
import org.example.restaurant.datalayer.repositories.TableRepository;
import org.example.restaurant.servicelayer.OperationResult;
import org.example.restaurant.servicelayer.services.OrderService;
import org.example.restaurant.servicelayer.services.TableService;
import org.example.restaurant.servicelayer.validators.OrderValidator;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/user/opened-order")
public class OpenedOrderServlet extends HttpServlet {
    private OrderService orderService;
    private TableService tableService;

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

        tableService = new TableService(new TableMapper(), tableRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto user = (UserDto) req.getSession().getAttribute("user");
        OperationResult<OrderDto> resultOrder;
        OperationResult<List<PositionInOrderDto>> resultPositions;

        try {
            resultOrder = orderService.getUnfinishedOrderByUserId(user.getId());
            resultPositions = orderService.getUnfinishedOrderPositionsByUserId(user.getId());
        } catch (DataBaseException e) {
            resultOrder = new OperationResult<>("DataBase error: " + e.getMessage());
            resultPositions = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (resultPositions.isSuccess() && resultOrder.isSuccess()) {
            req.setAttribute("order", resultOrder.getResult());
            req.setAttribute("tables", tableService.getAll());
            req.setAttribute("orderPositions", resultPositions.getResult());
            req.getRequestDispatcher("opened-order.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", resultOrder.getFailReason() + resultPositions.getFailReason());
            req.getRequestDispatcher("").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OperationResult<Boolean> closeResult;
        OperationResult<TableDto> tableResult;

        try {
            tableResult = tableService.getById(Long.parseLong(req.getParameter("tableId")));
            if (tableResult.isSuccess()) {
                CloseUnfinishedOrderDto closeUnfinishedOrderDto = new CloseUnfinishedOrderDto();
                closeUnfinishedOrderDto.setId(Long.parseLong(req.getParameter("orderId")));
                closeUnfinishedOrderDto.setUserId(Long.parseLong(req.getParameter("userId")));
                closeUnfinishedOrderDto.setOrderDate(Date.valueOf(req.getParameter("orderDate")));
                closeUnfinishedOrderDto.setTable(tableResult.getResult());

                closeResult = orderService.closeOpenedOrder(closeUnfinishedOrderDto);
            } else {
                closeResult = new OperationResult<>("Table not found");
            }
        } catch (DataBaseException e) {
            tableResult = new OperationResult<>("DataBase error: " + e.getMessage());
            closeResult = new OperationResult<>("DataBase error: " + e.getMessage());
        }

        if (tableResult.isSuccess() && closeResult.isSuccess()) {
            resp.sendRedirect(req.getContextPath());
        } else {
            req.setAttribute("error", closeResult.getFailReason());
            req.getRequestDispatcher("").forward(req, resp);
        }
    }
}
