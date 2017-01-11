package cn.scxh.hibernate.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
	SessionFactory sessionFactory;
	Transaction transaction;
	Session session;
	@Before
	public void setUp() throws Exception {
		// 读取hibernate.cfg.xml文件
		Configuration config = new Configuration().configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
						.build();
				// 建立SessionFactory
		sessionFactory = config.buildSessionFactory(serviceRegistry);
//		session = sessionFactory.openSession();
		session = sessionFactory.getCurrentSession();
		transaction = session.beginTransaction();
	}

	@After
	public void tearDown() throws Exception {
		transaction.commit();
//		session.close();
//		sessionFactory.close();
	}
/**
 * 运用sql语句实现查询
 */
	@Test
	public void test() {
//		User user = new User(7,"6","test",new Date());
//		session.save(user);
//		session.delete(user);
		String sql = "select * from user where name = 1";
		SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(User.class);
		List<User> list =sqlQuery.list();//java.util包
		for(User user : list){
			System.out.println(user);
		}
	}
	@Test
	public void testSelectAllUser(){
		String sql = "select * from user";
		SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(User.class);
		List<User> list =sqlQuery.list();//java.util包
		for(User user : list){
			System.out.println(user);
		}
	}
	/**
	 * 用hql实现查询 
	 * org.hibernate.hql.internal.ast.QuerySyntaxException: user is not mapped [from user]
	 * user不应该是表单而是User类
	 */
	@Test
	public void testSelectAllUserByHql(){
		String hsql = "from User";
		Query query = session.createQuery(hsql);
		List<User> list =query.list();
		for(User user : list){
			System.out.println(user);
		}
	}
	/**
	 * 查询用户个数
	 * java.lang.ClassCastException: java.lang.Long cannot be cast to java.lang.Integer
	 * 格式转换异常:此处应该用Long型
	 */
	@Test
	public void testSelectNumUser(){
		String hql ="select count(*) from User";
		Query query = session.createQuery(hql);
		long num = (long) query.uniqueResult();//如果查询结果唯一用 uniqueResult
		System.out.println(num);
	}
	/**
	 * 查找指定属性记录
	 * 1.创建包含查询属性构造方法
	 * 2.若查询结果唯一，用uniqueResult()，有多条结果，用list()
	 */
	@Test
	public void testSelectActionById(){
		//如果只查询一部分属性，需要写一个需要查询的构造方法
		String hql = "select new User(u.name,u.passWord) from User as u where u.id=:num";
		Query query = session.createQuery(hql);
		query.setParameter("num", 5);
		User user = (User) query.uniqueResult();
		System.out.println(user);
	}
}
