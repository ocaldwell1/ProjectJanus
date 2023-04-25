package com.example.janus;

import java.text.ParseException;

public interface PriorityCalculator {
    public double calculatePriority(int weight, String dueDate) throws ParseException;
}
