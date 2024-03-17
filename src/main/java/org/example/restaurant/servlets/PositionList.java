package org.example.restaurant.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.data.entities.Entity;
import org.example.restaurant.data.entities.Position;
import org.example.restaurant.data.repositories.PositionRepository;
import org.example.restaurant.data.repositories.Repository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "positionList", value = "/position-list")
public class PositionList extends HttpServlet {

    private Repository positionRepository;

    @Override
    public void init() {
        positionRepository = new PositionRepository();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        List<Entity> positions = positionRepository.getAll();

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<style>" +
                ".positions {" +
                "border: 1px solid;" +
                "}" +
                "</style>");
        out.println("<body><table class = \"positions\">");
        out.println("<tr>" +
                "<th>Наименование</th>" +
                "<th>Цена</th>" +
                "<th>Вес</th>" +
                "<th>Белки</th>" +
                "<th>Жиры</th>" +
                "<th>Углеводы</th>" +
                "<th>Веганское</th>" +
                "</tr>");

        for (Entity position : positions) {
            Position pos = (Position) position;
            out.println("<tr>" +
                    "<th>" + pos.getPositionName() + "</th>" +
                    "<th>" + pos.getPrice() + "</th>" +
                    "<th>" + pos.getWeight() + "</th>" +
                    "<th>" + pos.getProtein()+ "</th>" +
                    "<th>" + pos.getFat() + "</th>" +
                    "<th>" + pos.getCarbohydrate() + "</th>" +
                    "<th>" + pos.isVegan() + "</th>" +
                    "</tr>");
        }
        out.println("</table></body></html>");
    }
}
