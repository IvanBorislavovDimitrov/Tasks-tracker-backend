package com.tracker.taskstracker.model.response;

public class ProjectTaskStatesResponseModel {

    private int backlogItems;
    private int selectedItems;
    private int inProgressItems;
    private int blockedItems;
    private int completedItems;

    private ProjectTaskStatesResponseModel(Builder builder) {
        backlogItems = builder.backlogItems;
        selectedItems = builder.selectedItems;
        inProgressItems = builder.inProgressItems;
        blockedItems = builder.blockedItems;
        completedItems = builder.completedItems;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getBacklogItems() {
        return backlogItems;
    }

    public void setBacklogItems(int backlogItems) {
        this.backlogItems = backlogItems;
    }

    public int getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(int selectedItems) {
        this.selectedItems = selectedItems;
    }

    public int getInProgressItems() {
        return inProgressItems;
    }

    public void setInProgressItems(int inProgressItems) {
        this.inProgressItems = inProgressItems;
    }

    public int getBlockedItems() {
        return blockedItems;
    }

    public void setBlockedItems(int blockedItems) {
        this.blockedItems = blockedItems;
    }

    public int getCompletedItems() {
        return completedItems;
    }

    public void setCompletedItems(int completedItems) {
        this.completedItems = completedItems;
    }

    public static class Builder {
        private int backlogItems;
        private int selectedItems;
        private int inProgressItems;
        private int blockedItems;
        private int completedItems;

        public Builder setBacklogItems(int backlogItems) {
            this.backlogItems = backlogItems;
            return this;
        }

        public Builder setSelectedItems(int selectedItems) {
            this.selectedItems = selectedItems;
            return this;
        }

        public Builder setInProgressItems(int inProgressItems) {
            this.inProgressItems = inProgressItems;
            return this;
        }

        public Builder setBlockedItems(int blockedItems) {
            this.blockedItems = blockedItems;
            return this;
        }

        public Builder setCompletedItems(int completedItems) {
            this.completedItems = completedItems;
            return this;
        }

        public ProjectTaskStatesResponseModel build() {
            return new ProjectTaskStatesResponseModel(this);
        }
    }
}
