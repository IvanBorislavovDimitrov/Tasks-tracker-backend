package com.tracker.taskstracker.model.response;

import java.util.Date;
import java.util.List;

public class UserResponseModelExtended extends UserResponseModel {

    private List<LoginRecord> loginRecords;
    private List<TaskResponseModel> tasks;

    public List<LoginRecord> getLoginRecords() {
        return loginRecords;
    }

    public void setLoginRecords(List<LoginRecord> loginRecords) {
        this.loginRecords = loginRecords;
    }

    public List<TaskResponseModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskResponseModel> tasks) {
        this.tasks = tasks;
    }

    public static class LoginRecord {

        private Date createdAt;

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }
}
