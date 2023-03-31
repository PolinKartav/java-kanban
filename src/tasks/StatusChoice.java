package tasks;

public enum StatusChoice {
    NEW,
    IN_PROGRESS,
    DONE;

    public static StatusChoice leadToStatusChoice(String status){
        if(status.equals("NEW")) return NEW;
        else if (status.equals("IN_PROGRESS")) return IN_PROGRESS;
        else if (status.equals("DONE")) return DONE;
        else return null;
    }
}

