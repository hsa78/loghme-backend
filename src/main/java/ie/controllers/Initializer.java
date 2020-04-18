package ie.controllers;

import ie.logic.Loghme;
import ie.repository.DataManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("serial")
//@WebServlet("/Initializer")
public class Initializer extends HttpServlet {
    @Override
    public void init() throws ServletException{
        System.out.println("in init");
        DataManager.getInstance().setListOfRestaurants();
        super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h4></h4>");
    }
}