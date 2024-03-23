package org.example.restaurant.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.restaurant.data.ConnectionFactory;
import org.example.restaurant.data.entities.Entity;
import org.example.restaurant.data.entities.Position;
import org.example.restaurant.data.repositories.PositionRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "positionList", value = "/position-list")
public class PositionList extends HttpServlet {

    private PositionRepository positionRepository;

    @Override
    public void init() {
        positionRepository = new PositionRepository("position", new ConnectionFactory());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        List<Position> positions = positionRepository.getAll();

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<style>" +
                ".positions {" +
                "border-collapse: collapse;" +
                "}" +
                ".positions td, .positions th {" +
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

        for (Position pos : positions) {
            out.println("<tr>" +
                    "<th>" + pos.getPositionName() + "</th>" +
                    "<th>" + pos.getPrice() + "</th>" +
                    "<th>" + pos.getWeight() + "</th>" +
                    "<th>" + pos.getProtein() + "</th>" +
                    "<th>" + pos.getFat() + "</th>" +
                    "<th>" + pos.getCarbohydrate() + "</th>" +
                    "<th>" + pos.isVegan() + "</th>" +
                    "</tr>");
        }

        Position pos = positions.get(0);

        pos.setPositionName("ChangeTestName");

        positionRepository.update(pos);

        pos.setPositionName("AddTestName");
        positionRepository.add(pos);

        positionRepository.delete(2);

        positions = positionRepository.getAll();

        for (Position pos1 : positions) {
            out.println("<tr>" +
                    "<th>" + pos1.getPositionName() + "</th>" +
                    "<th>" + pos1.getPrice() + "</th>" +
                    "<th>" + pos1.getWeight() + "</th>" +
                    "<th>" + pos1.getProtein() + "</th>" +
                    "<th>" + pos1.getFat() + "</th>" +
                    "<th>" + pos1.getCarbohydrate() + "</th>" +
                    "<th>" + pos1.isVegan() + "</th>" +
                    "</tr>");
        }


        out.println("</table></body></html>");
    }
}
