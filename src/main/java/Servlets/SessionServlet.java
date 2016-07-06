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


public class SessionServlet extends HttpServlet {

    private UserService userService;

    public SessionServlet(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession().getId();

        Map<String, Object> pageVariables = new HashMap<>();

        if(userService.checkSessionId(sessionId)){
            UserProfile userProfile = userService.getUserBySessionId(sessionId);
            pageVariables = getPageVariables(userProfile);
            resp.getWriter().print(PageGenerator.instance().getPage("main.html", pageVariables));
        } else {
            pageVariables.put("status", "");
            resp.getWriter().print(PageGenerator.instance().getPage("index.html", pageVariables));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        UserProfile userProfile = userService.getUserByEmail(email);


        if(userProfile != null && userProfile.getPass().equals(pass)){

            String sessionId = req.getSession().getId();
            userService.addUserSession(sessionId, userProfile);
            Map<String, Object> pageVariables = getPageVariables(userProfile);
            resp.getWriter().print(PageGenerator.instance().getPage("main.html", pageVariables));
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("status", "Wrong email or password!");
            resp.getWriter().print(PageGenerator.instance().getPage("index.html", pageVariables));
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<String, Object> getPageVariables(UserProfile userProfile){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", userProfile.getName());
        pageVariables.put("lastName", userProfile.getLastName());
        pageVariables.put("age", userProfile.getAge());
        pageVariables.put("email", userProfile.getEmail());
        pageVariables.put("friends_value", userProfile.getFriends().size());
        return pageVariables;
    }
}
