package Servlets;

import Models.UserProfile;
import Services.UserService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    private UserService userService;

    public RegistrationServlet(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("reg_status", "");
        pageVariables.put("tx_color", "#000000");

        resp.getWriter().print(PageGenerator.instance().getPage("registration.html", pageVariables));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        String status = "Successful";
        UserProfile userProfile;

        try {
            userProfile = userService.getUser(req);
            if(!userService.registration(userProfile)){
                throw new RuntimeException("This user already register.");
            }
            pageVariables.put("reg_status", status);
            pageVariables.put("tx_color", "#2fd34a");
        } catch (RuntimeException e) {
            status = e.getMessage();
            pageVariables.put("reg_status", status);
            pageVariables.put("tx_color", "#c60909");
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(PageGenerator.instance().getPage("registration.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
