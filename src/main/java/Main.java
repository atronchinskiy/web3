
import exception.DBException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.BankClientService;
import model.BankClient;
import servlet.ApiServlet;
import servlet.MoneyTransactionServlet;
import servlet.RegistrationServlet;
import servlet.ResultServlet;

public class Main {
    public static void main(String[] args) throws Exception{
        ApiServlet apiServlet = new ApiServlet();
        MoneyTransactionServlet mtS = new MoneyTransactionServlet();
        RegistrationServlet regS = new RegistrationServlet();
        ResultServlet resultS = new ResultServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(resultS), "/result");
        context.addServlet(new ServletHolder(mtS), "/transaction");
        context.addServlet(new ServletHolder(regS), "/registration");
        context.addServlet(new ServletHolder(apiServlet), "/api/*");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();

/*        ApiServlet apiServlet = new ApiServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(apiServlet), "/api/*");
        context.addServlet(new ServletHolder(new RegistrationServlet()), "/registration");
        context.addServlet(new ServletHolder(new MoneyTransactionServlet()), "/transaction");
        context.addServlet(new ServletHolder(new ResultServlet()), "/result");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();*/
    }
}
