/*package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>());
//        PageGenerator.getInstance().getPage("registrationPage.html", null);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", null));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        BankClient bc = new BankClient(req.getParameter("name"), req.getParameter("password"), Long.valueOf(req.getParameter("money")));
        try {
            if (new BankClientService().addClient(bc)) {
                pageVariables.put("message", "Add client successful");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                pageVariables.put("message", "Client not add");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            };
        } catch (DBException e) { }
    }


}*/

package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    BankClientService service = new BankClientService();
    PageGenerator pageGenerator = PageGenerator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(pageGenerator.getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> messages = new HashMap<>();
        String name = req.getParameter("name");
        try {
            if (service.getClientByName(name) == null) {
                String password = req.getParameter("password");
                Long money = Long.parseLong(req.getParameter("money"));
                BankClient client = new BankClient(name, password, money);
                service.addClient(client);

                resp.setStatus(200);
                messages.put("message", "Add client successful");
                resp.getWriter().println(pageGenerator.getPage("resultPage.html", messages));
            } else {
                resp.setStatus(200);
                messages.put("message", "Client not add");
                resp.getWriter().println(pageGenerator.getPage("resultPage.html", messages));
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}

