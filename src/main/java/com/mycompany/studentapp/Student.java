/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.studentapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Station
 */
@Named("studentBean")
@RequestScoped
public class Student {
    int id;
    String firstName;
    String lastName;
    String county;
    String username;
    String password;
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;
    ArrayList usersList ;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    //connection to the database
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_java","root","");
        } catch (Exception e) {
            System.out.println(e);
        }
        return  connection;
    }
    
    public String save(){
        int result = 0;
        try{
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into details(first_name,last_name,county,username,password) values(?,?,?,?,?)");
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, county);
            stmt.setString(4, username);
            stmt.setString(5, password);
            result = stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }
        if(result !=0)
            return "index.xhtml?faces-redirect=true";
        else return "register.xhtml?faces-redirect=true";
    }
      // Used to fetch all records
    public ArrayList usersList(){
        try{
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from details");  
            while(rs.next()){
                Student std = new Student();
                std.setId(rs.getInt("id"));
                std.setFirstName(rs.getString("first_name"));
                std.setLastName(rs.getString("last_name"));
                std.setCounty(rs.getString("county"));
                std.setUsername(rs.getString("username"));
                std.setPassword(rs.getString("password"));
                usersList.add(std);
            }
            connection.close();        
        }catch(Exception e){
            System.out.println(e);
        }
        return usersList;
    }
    // Used to fetch record to update
    public String edit(int id){
        Student std = null;
        System.out.println(id);
        try{
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from details where id = "+(id));
            rs.next();
            std = new Student();
                std.setId(rs.getInt("id"));
                std.setFirstName(rs.getString("first_name"));
                std.setLastName(rs.getString("last_name"));
                std.setCounty(rs.getString("county"));
                std.setUsername(rs.getString("username"));
                std.setPassword(rs.getString("password")); 
            System.out.println(rs.getString("password"));
            sessionMap.put("editUser", std);
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }       
        return "/edit.xhtml?faces-redirect=true";
    }
    // Used to update user record
    public String update(Student u){
        //int result = 0;
        try{
            connection = getConnection();  
            PreparedStatement stmt=connection.prepareStatement("update details set first_name=?,last_name=?,county=?,username=?,password=? where id=?");  
            stmt.setString(1,u.getFirstName());  
            stmt.setString(2,u.getLastName());  
            stmt.setString(3,u.getCounty());  
            stmt.setString(4,u.getUsername());  
            stmt.setString(5,u.getPassword());  
            stmt.setInt(6,u.getId());  
            stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println();
        }
        return "/dashboard.xhtml?faces-redirect=true";      
    }
    // Used to delete user record
    public void delete(int id){
        try{
            connection = getConnection();  
            PreparedStatement stmt = connection.prepareStatement("delete from details where id = "+id);  
            stmt.executeUpdate();  
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
}
