package dbService;

import Models.UserProfile;
import dbService.dao.UsersDAO;
import dbService.dataSets.FriendsDataSet;
import dbService.dataSets.UsersDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;


public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getMySqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public UserProfile getUser(long id) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet = dao.get(id);
            session.close();
            List<Long> friendsId =getFriendsId(dataSet.getId());
            return new UserProfile(dataSet.getId(), dataSet.getName(),
                    dataSet.getLastName(), dataSet.getAge(), dataSet.getEmail(),
                    dataSet.getPass(), friendsId);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UserProfile getUser(String email) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet = dao.get(email);
            session.close();
            if(dataSet == null) {
                return null;
            }
            List<Long> friendsId =getFriendsId(dataSet.getId());
            return new UserProfile(dataSet.getId(), dataSet.getName(),
                    dataSet.getLastName(), dataSet.getAge(), dataSet.getEmail(),
                    dataSet.getPass(), friendsId);
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addUser(String email, String pass, String name, String lastName, int age) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            long id = dao.insertUser(email, pass, name, lastName, age);
            transaction.commit();
            session.close();
            return id;
        }
        catch (HibernateException e) {
            throw  new DBException(e);
        }
    }

    public long addFriendId(long userId, long friendId) throws  DBException{
        try{
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            long id = dao.insertFriend(userId, friendId);
            transaction.commit();
            session.close();
            return id;
        }
        catch (HibernateException e){
            throw new DBException(e);
        }
    }

    public void clearFriendId(long userId, long friendId) throws DBException{
        try{
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            dao.clearFriendId(userId, friendId);
            transaction.commit();
            session.close();
        }
        catch (HibernateException e){
            throw new DBException(e);
        }
    }

    private List<Long> getFriendsId(long userId) throws DBException{
        Session session = sessionFactory.openSession();
        UsersDAO dao = new UsersDAO(session);
        List<FriendsDataSet> friendsDataSets = dao.getFriends(userId);
        session.close();

        if(friendsDataSets == null){
            return null;
        }

        List<Long> friendsId = new ArrayList<>();
        for(FriendsDataSet dataSet: friendsDataSets){
            friendsId.add(dataSet.getFriendId());
        }

        return friendsId;
    }

    public Boolean containsUser(String email){
        try (Session session = sessionFactory.openSession()){
            if(new UsersDAO(session).get(email) != null){
                return true;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }


    @SuppressWarnings("UnusedDeclaration")
    private Configuration getMySqlConfiguration() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.addAnnotatedClass(FriendsDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/user_profiles");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "211997Airat");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }


    private static org.hibernate.SessionFactory createSessionFactory(org.hibernate.cfg.Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
