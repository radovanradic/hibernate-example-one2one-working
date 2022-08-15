package com.example.hibernate.timestamp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class MainTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testFind() throws Exception {

    Configuration configuration = new Configuration();
    configuration.configure("hibernate.h2.cfg.xml");
    configuration.addAnnotatedClass(User.class);
    configuration.addAnnotatedClass(UserProfile.class);

    SessionFactory sessionFactory = configuration.buildSessionFactory();
    Session session = sessionFactory.openSession();

    User user = new User();
    user.setName("my name");
    session.beginTransaction();
    session.save(user);
    session.getTransaction().commit();

    UserProfile userProfile = new UserProfile();
    userProfile.setAddress("my addr");
    userProfile.setUser(user);
    session.beginTransaction();
    session.save(userProfile);
    session.getTransaction().commit();

    Query<User> query = session.createQuery("FROM User WHERE userProfile = :p ");
    query.setMaxResults(1);
    query.setParameter("p", userProfile);

    User obj;
    Iterator<User> iterator = query.list().iterator();
    if (iterator.hasNext()) {
      obj = iterator.next();
    } else {
      obj = null;
    }
    Assert.assertNotNull(obj);

  }
}
