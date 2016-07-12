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

public class ChatServlet extends HttpServlet{

    private UserService userService;

    public ChatServlet(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();

        UserProfile userProfile = userService.getUserBySessionId(sessionId);

        Map<String, Object> pageVariables = new HashMap<>();
        if(userProfile == null){
            pageVariables.put("status", " ");
            resp.getWriter().print(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            pageVariables.put("name", userProfile.getName());
            resp.getWriter().print(PageGenerator.instance().getPage("chat.html", pageVariables));
        }
    }

}
