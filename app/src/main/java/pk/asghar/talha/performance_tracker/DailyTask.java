package pk.asghar.talha.performance_tracker;

public class DailyTask {
    private int id;
    private String startTime;
    private String endTime;
    private String date;
    private String name;
    private String description;

    public DailyTask(int id, String name, String date, String startTime, String endTime, String description) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.name = name;
        this.description = description;
    }

    public DailyTask(String name, String date, String startTime, String endTime, String description) {
        this(-1, name, date, startTime, endTime, description);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("Task Id: %d\nName:%s\nDate: %s\nStarted: %s\nCompleted: %s\nDetails: %s\n\n",
                getId(), getName(), getDate(), getStartTime(), getEndTime(), getDescription());
    }
}
