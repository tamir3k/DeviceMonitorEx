package com.ssi.devicemonitor;

import com.ssi.devicemonitor.controller.DeviceMonitorController;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DeviceMonitorApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(DeviceMonitorApp.class.getResource("device_monitor.fxml"));
        DeviceMonitorController controller = new DeviceMonitorController();
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Device Monitor");
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				System.exit(0);				
			}
		});
    }

    public static void main(String[] args) {
        launch(args);
    }
}
