module com.ssi.devicemonitor {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ssi.devicemonitor.controller to javafx.fxml;

    exports com.ssi.devicemonitor;
}
	