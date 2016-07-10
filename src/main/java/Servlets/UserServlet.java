package Servlets;

import Models.UserProfile;
import Services.UserService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class UserServlet extends HttpServlet {

    private UserService userService;

    public UserServlet(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.valueOf(req.getParameter("id"));
        UserProfile userProfile = userService.getUserById(userId);
        if(userProfile == null){
            resp.getWriter().print("User was not found");
            resp.setStatus(HttpServletResponse.SC_FOUND);
        } else {
            Map<String, Object> pageVariables = userService.getUserVariables(userProfile);
            resp.getWriter().print(PageGenerator.instance().getPage("user.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
