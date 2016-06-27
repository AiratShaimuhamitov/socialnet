package Servlets;


import Services.UserService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SessionDeleteServlet extends HttpServlet{

    private UserService userService;

    public SessionDeleteServlet(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        userService.deleteUserSession(sessionId);
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("status", "");
        resp.getWriter().print(PageGenerator.instance().getPage("index.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
