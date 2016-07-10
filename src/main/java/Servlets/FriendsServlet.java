package Servlets;

import Models.UserProfile;
import Services.UserService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

public class FriendsServlet extends HttpServlet {

    private UserService userService;

    public FriendsServlet(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserProfile userProfile = userService.getUserBySessionId(req.getSession().getId());

        if(userProfile == null){
            resp.getWriter().print("Error: user was not founded!");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();
            List<Long> friends = userProfile.getFriends();
            int i = 1;
            for (Long friendsId: friends){
                UserProfile friendProfile = userService.getUserById(friendsId);
                stringBuilder.append("<a href=\"user?id="+ friendProfile.getId() +"\">" + i + ". " + friendProfile.getName() + " " + friendProfile.getLastName() + "</a> <br>");
                i++;
            }
            HashMap<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("friends", stringBuilder);
            resp.getWriter().print(PageGenerator.instance().getPage("friends.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
