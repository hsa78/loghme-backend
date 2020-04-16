package ie.controllers;

import ie.logic.Loghme;
import ie.repository.DataManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class FoodPartyTimerListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Food Party Timer Listener has been shutdown");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("Food Party Listener initialized.");

        TimerTask scheduledTask = new StartFoodPartyTask();
        Timer timer = new Timer();
        timer.schedule(scheduledTask, 0, (300 * 1000));
    }

    class StartFoodPartyTask extends TimerTask {

        @Override
        public void run() {
            DataManager.getInstance().startFoodParty();
        }
    }
}