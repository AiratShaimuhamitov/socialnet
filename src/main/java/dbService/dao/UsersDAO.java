package dbService.dao;

import dbService.dataSets.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


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

}
