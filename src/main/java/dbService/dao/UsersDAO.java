package dbService.dao;

import dbService.dataSets.FriendsDataSet;
import dbService.dataSets.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;


public class UsersDAO {

    private Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet get(long id) throws HibernateException {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public UsersDataSet get(String email) throws HibernateException{
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return (UsersDataSet) criteria.add(Restrictions.eq("email", email)).uniqueResult();
    }

    public long getUserId(String email) throws HibernateException {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria.add(Restrictions.eq("email", email)).uniqueResult()).getId();
    }


    public long insertUser(String email, String pass, String name, String lastName, int age) throws HibernateException {
        return (Long) session.save(new UsersDataSet(-1, name, lastName, age, email, pass));
    }

    public long insertFriend(long user_id, long friend_id) throws HibernateException {
        return (Long) session.save(new FriendsDataSet(-1 ,user_id, friend_id));
    }

    //TODO kostyl
    public List<FriendsDataSet> getFriends(long id) throws HibernateException{
        Criteria criteria = session.createCriteria(FriendsDataSet.class);
        List<FriendsDataSet> dataSets= criteria.list();
        List<FriendsDataSet> friendsDataSets = new ArrayList<>();
        for(FriendsDataSet fds: dataSets){
            if(fds.getUserId() == id){
                friendsDataSets.add(fds);
            }
        }
        return  friendsDataSets;
    }
}
