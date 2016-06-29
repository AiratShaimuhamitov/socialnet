package Main;

import Services.UserService;
import Servlets.SessionDeleteServlet;
import Servlets.SessionServlet;
import Servlets.RegistrationServlet;
import dbService.DBService;
import dbService.dataSets.FriendsDataSet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.List;

class Main{
    public static void main(String []args) throws Exception{

        DBService dbService = new DBService();

        UserService userService = new UserService(dbService);

        SessionServlet sessionServlet = new SessionServlet(userService);
        RegistrationServlet registrationServlet = new RegistrationServlet(userService);
        SessionDeleteServlet sessionDeleteServlet = new SessionDeleteServlet(userService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(sessionServlet), "/*");
        context.addServlet(new ServletHolder(registrationServlet), "/reg");
        context.addServlet(new ServletHolder(sessionDeleteServlet), "/exit");

        Server server = new Server(8080);

        server.setHandler(context);

        server.start();
        server.join();
    }
}