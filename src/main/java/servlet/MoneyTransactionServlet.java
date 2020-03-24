/*
package servlet;

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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>());
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", null));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        BankClient sender = new BankClientService().getClientByName(req.getParameter("senderName"));
        try {
            if (new BankClientService().sendMoneyToClient(sender, req.getParameter("nameTo"),Long.valueOf(req.getParameter("count")))) {
                pageVariables.put("message", "The transaction was successful");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                pageVariables.put("message", "transaction rejected");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
*/

package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    PageGenerator pageGenerator = PageGenerator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(pageGenerator.getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        Long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        BankClient sender;
        HashMap<String, Object> messages = new HashMap<>();
        try {
            sender = bankClientService.getClientByName(senderName);
            if (sender.getName().equals(senderName) & sender.getPassword().equals(senderPass)) {
                if (bankClientService.sendMoneyToClient(sender, nameTo, count)) {
                    resp.setStatus(200);
                    messages.put("message", "The transaction was successful");
                    resp.getWriter().println(pageGenerator.getPage("resultPage.html", messages));
                } else {
                    resp.setStatus(200);
                    messages.put("message", "transaction rejected");
                    resp.getWriter().println(pageGenerator.getPage("resultPage.html", messages));
                }
            } else {
                resp.setStatus(200);
                messages.put("message", "transaction rejected");
                resp.getWriter().println(pageGenerator.getPage("resultPage.html", messages));
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

}