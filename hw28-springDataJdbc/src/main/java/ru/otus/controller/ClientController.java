package ru.otus.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.ClientService;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/clients")
    public String showClients(Model model) {
        List<Client> clientList = clientService.findAll();
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

        model.addAttribute("clientList", data);
        return "clients";
    }

    @PostMapping("/clients")
    public String addClient(HttpServletRequest req, Model model) {
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");

        Client newClient = new Client(null, name, new Address(null, address), Set.of(new Phone(null, phone)));
        clientService.saveClient(newClient);
        return "redirect:/clients";
    }
}
