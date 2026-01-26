package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.TemplateProcessor;

@RequiredArgsConstructor
@SuppressWarnings({"java:S1989"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENT_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENT_LIST = "clientList";

    private final transient DBServiceClient dbServiceClient;
    private final transient TemplateProcessor templateProcessor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clientList = dbServiceClient.findAll();

        List<Map<String, String>> data = clientList.stream()
                .map(client -> Map.of(
                        "id",
                        client.getId().toString(),
                        "name",
                        client.getName(),
                        "address",
                        Optional.of(client.getAddress()).map(Address::getStreet).orElse(""),
                        "phone",
                        client.getPhones().stream()
                                .findFirst()
                                .map(Phone::getNumber)
                                .orElse("")))
                .toList();

        paramsMap.put(TEMPLATE_ATTR_CLIENT_LIST, data);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");

        Client newClient =
                new Client(null, name, password, new Address(null, address), List.of(new Phone(null, phone)));
        dbServiceClient.saveClient(newClient);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.sendRedirect("/clients");
    }
}
