package Services;

import Models.UserProfile;
import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.FriendsDataSet;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


public class UserService {
    private final Map<String, UserProfile> sessionIdToProfile;

    private DBService dbService;

    public UserService(DBService dbService) {
        this.dbService = dbService;
        this.sessionIdToProfile = new HashMap<>();
    }

    public boolean checkSessionId(String sessionId){
       return sessionIdToProfile.containsKey(sessionId);
    }

    public void addUserSession(String sessionId, UserProfile profile){
        sessionIdToProfile.put(sessionId, profile);
    }

    public void deleteUserSession(String sessionId){
        sessionIdToProfile.remove(sessionId);
    }

    public UserProfile getUserByEmail(String email){
        try {
            return dbService.getUser(email);
        } catch (DBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserProfile getUserBySessionId(String sessionId){
        return sessionIdToProfile.get(sessionId);
    }

    public UserProfile getUserById(Long id){
        try {
            return dbService.getUser(id);
        } catch (DBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserProfile getUser(HttpServletRequest request){

        String name = request.getParameter("name");
        String lastName = request.getParameter("lastName");

        String email = request.getParameter("email");
        if(!checkEmail(email)) {
            throw new RuntimeException("Invalid Email");
        }

        String password = request.getParameter("password");
        if(!checkPass(password)){
            throw new RuntimeException("Invalid Password");
        }

        int age;
        try {
            age = Integer.valueOf(request.getParameter("age"));
            if(!checkAge(age)){
                throw new RuntimeException("Invalid age");
            }
        }catch (NumberFormatException e){
            throw new RuntimeException("Invalid age");
        }

        return new UserProfile(0, name, lastName, age, email, password);
    }

    private boolean checkEmail(String email) {
        char a[] = email.toCharArray();
        if(a[0] < 97 || a[0] > 122)
            return false;
        if(email.indexOf('@') == -1 || email.indexOf('.') == -1 || email.indexOf('@') > email.indexOf('.')){
            return false;
        }
        return true;
    }

    private boolean checkPass(String pass){
        if(pass.length() < 6){
            return false;
        }
        char a[] = pass.toCharArray();
        for (char ch : a) {
            if (ch < 48 || ch > 122)
                return false;
        }
        return true;
    }

    private boolean checkAge(int age){
        return !(age < 0 || age > 110);
    }

    public boolean registration(UserProfile userProfile) {
        try {
            dbService.addUser(userProfile.getEmail(), userProfile.getPass(), userProfile.getName(), userProfile.getLastName(), userProfile.getAge());
        } catch (DBException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void addFriend(UserProfile userProfile, long userId){
        try {
            dbService.addFriendId(userProfile.getId(), userId);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(UserProfile userProfile, long userId){
        try {
            dbService.clearFriendId(userProfile.getId(), userId);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getUserVariables(UserProfile userProfile){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", userProfile.getName());
        pageVariables.put("lastName", userProfile.getLastName());
        pageVariables.put("age", userProfile.getAge());
        pageVariables.put("email", userProfile.getEmail());
        pageVariables.put("friends_value", userProfile.getFriends().size());
        return pageVariables;
    }
}
