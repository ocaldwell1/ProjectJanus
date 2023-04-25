package com.example.janus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LongTasksPriorityCalculator implements PriorityCalculator {
    public double calculatePriority(int weight, String dueDate) throws ParseException {
        Date now = new Date(System.currentTimeMillis());
        Date due = new SimpleDateFormat("MM/dd/yyyy").parse(dueDate); //duedate - now
        //Date due = dueDate; // TODO: Replace this after verifying that it works fine
        double timeDiff = due.getTime() - now.getTime();
        double priority = weight * Math.pow(2, (-0.5 * (timeDiff/8.64e+7) + 1));
        if(priority > 1) {
            return 1;
        }
        else {
            return priority;
        }
    }
}
