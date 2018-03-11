package com.developer.pritampc.goaltracker;

/**
 * Created by pritamPC on 3/4/2018.
 */

public class GoalData {
    String id;
    String startDate;
    String endDate;
    String goal_name;
    String description;
    String status;

    public GoalData() {
    }

    public GoalData(String id,String goal_name,String startDate, String endDate, String description,String status) {
        this.id=id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal_name = goal_name;
        this.description = description;
        this.status =status;
    }


    public String getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getGoal_name() {
        return goal_name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
