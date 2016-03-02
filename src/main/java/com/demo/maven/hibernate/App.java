package com.demo.maven.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App {

    private static SessionFactory factory;
    
    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
        
        Integer employeeId1 = addEmployee("Zara", "Ali", 1000);
        Integer employeeId2 = addEmployee("Daisy", "Das", 5000);
        Integer employeeId3 = addEmployee("Jonn", "Paul", 10000);
        System.out.println("employeeId1=" + employeeId1 + ", employeeId2=" + employeeId2
                + ", employeeId3=" + employeeId3);
        
        listEmployees();
    }
    
    public static Integer addEmployee(String fName, String lName, int salary) {
        Session session = factory.openSession();
        Integer employeeId = null;
        
        try {
            session.beginTransaction();
            Employee employee = new Employee(fName, lName, salary);
            employeeId = (Integer) session.save(employee);
            session.getTransaction().commit();
        } catch (HibernateException e) {
           if (session.getTransaction() != null) {
               session.getTransaction().rollback();
           }
           e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        return employeeId;
    }
    
    public static void listEmployees() {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            List<Employee> employees = session.createQuery("FROM Employee").list();
            Iterator<Employee> iterator = employees.iterator();
            while (iterator.hasNext()) {
                Employee employee = iterator.next();
                System.out.println("FirstName: " + employee.getFirstName());
                System.out.println("LastName: " + employee.getLastName());
                System.out.println("Salary: " + employee.getSalary());
                System.out.println();
            }
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
