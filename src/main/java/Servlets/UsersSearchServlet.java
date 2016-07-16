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
import java.util.List;
import java.util.Map;


public class UsersSearchServlet extends HttpServlet {

    private UserService userService;

    public UsersSearchServlet(UserService userService){
        this.userService = userService;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UserProfile userProfile = userService.getUserBySessionId(sessionId);

        HashMap<String, Object> pageVariables = new HashMap<>();
        if(userProfile == null){
            pageVariables.put("status", " ");
            resp.getWriter().print(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            pageVariables.put("status", " ");
            pageVariables.put("users", " ");
            resp.getWriter().print(PageGenerator.instance().getPage("search.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        String []searchQuery = req.getParameter("search_query").split(" ");
        String name;
        String lastName;
        try {
            name = searchQuery[0];
            lastName = searchQuery[1];
        } catch (ArrayIndexOutOfBoundsException e){
            pageVariables.put("status", "Enter correct name and last name!");
            pageVariables.put("users", " ");
            resp.getWriter().print(PageGenerator.instance().getPage("search.html", pageVariables));
            return;
        }

        List<UserProfile> userProfiles = userService.getUserProfilesByName(name, lastName);

        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (UserProfile userProfile: userProfiles){
            stringBuilder.append("<a href=\"user?id="+ userProfile.getId() +"\">" + i + ". "
                    + userProfile.getName() + " " + userProfile.getLastName() + "</a> <br>");
            i++;
        }


        pageVariables.put("users", stringBuilder);
        pageVariables.put("status", "Was found " + userProfiles.size() + " users:");
        resp.getWriter().print(PageGenerator.instance().getPage("search.html", pageVariables));

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
