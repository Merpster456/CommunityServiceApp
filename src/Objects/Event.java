package Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {

    private StringProperty hours;
    private StringProperty date;

    public Event(String hours, String date) {

        this.hours = new SimpleStringProperty(hours);
        this.date = new SimpleStringProperty(date);
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
}
