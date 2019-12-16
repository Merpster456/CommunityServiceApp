package Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {

    private StringProperty id;
    private StringProperty hours;
    private StringProperty date;
    private StringProperty desc;

    public Event(String id, String hours, String date, String desc) {

        this.id = new SimpleStringProperty(id);
        this.hours = new SimpleStringProperty(hours);
        this.date = new SimpleStringProperty(date);
        this.desc = new SimpleStringProperty(desc);
    }
    public Event(String id, String hours, String date) {

        this.id = new SimpleStringProperty(id);
        this.hours = new SimpleStringProperty(hours);
        this.date = new SimpleStringProperty(date);
    }

    public String getId() {
        return id.get();
    }
    public StringProperty idProperty() {
        return id;
    }
    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }
    public String getHours() {
        return hours.get();
    }
    public StringProperty hoursProperty() {
        return hours;
    }
    public void setHours(String hours) {
        this.hours = new SimpleStringProperty(hours);
    }
    public String getDate() {
        return date.get();
    }
    public StringProperty dateProperty() {
        return date;
    }
    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }
    public String getDesc() {
        return desc.get();
    }
    public StringProperty descProperty() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = new SimpleStringProperty(desc);
    }

}
