package edu.gsu.bbb.willdo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Task {
    @Id
    private String id;

    private String summary;
    private String description;
    private String date;
    private boolean state;

	@Indexed
    private String group;

    public Task() {}

    public String getId() { return id; }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

	public String getGroup() {
		return this.group;
	}
}