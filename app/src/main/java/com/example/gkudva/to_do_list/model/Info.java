package com.example.gkudva.to_do_list.model;

/**
 * Created by gkudva on 06/08/17.
 */

public class Info {
    int ToDoID;
    String ToDoTaskDetails;
    String ToDoTaskPrority;
    String ToDoTaskStatus;
    String ToDoNotes;

    public void setToDoDate(String toDoDate) {
        ToDoDate = toDoDate;
    }

    public String getToDoDate() {

        return ToDoDate;
    }

    String ToDoDate;

    public int getToDoID() {
        return ToDoID;
    }

    public void setToDoID(int toDoID) {
        ToDoID = toDoID;
    }

    public String getToDoTaskDetails() {
        return ToDoTaskDetails;
    }

    public void setToDoTaskDetails(String toDoTaskDetails) {
        ToDoTaskDetails = toDoTaskDetails;
    }

    public String getToDoTaskPrority() {
        return ToDoTaskPrority;
    }

    public void setToDoTaskPrority(String toDoTaskPrority) {
        ToDoTaskPrority = toDoTaskPrority;
    }

    public String getToDoTaskStatus() {
        return ToDoTaskStatus;
    }

    public void setToDoTaskStatus(String toDoTaskStatus) {
        ToDoTaskStatus = toDoTaskStatus;
    }

    public String getToDoNotes() {
        return ToDoNotes;
    }

    public void setToDoNotes(String toDoNotes) {
        ToDoNotes = toDoNotes;
    }

    @Override
    public String toString() {
        return "Info {id-" + ToDoID + ", taskDetails-" + ToDoTaskDetails + ", propity-" + ToDoTaskPrority + ", status-" + ToDoTaskStatus + ", notes-" + ToDoNotes + "}";
    }

}
