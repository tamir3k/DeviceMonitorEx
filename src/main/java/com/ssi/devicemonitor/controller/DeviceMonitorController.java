package com.ssi.devicemonitor.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.TimerTask;

import com.ssi.devicemonitor.entity.Device;
import com.ssi.devicemonitor.entity.Device.DeviceType;
import com.ssi.devicemonitor.entity.DeviceMonitor;
import com.ssi.devicemonitor.entity.HardwareDevice;
import com.ssi.devicemonitor.entity.SoftwareDevice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class DeviceMonitorController {
    @FXML
    private ListView<Device> deviceListView;

    @FXML
    private TextField deviceNameTextField;

    @FXML
    private Button addHardwareDeviceButton;

    @FXML
    private Button addSoftwareDeviceButton;

    @FXML
    private Button removeDeviceButton;

    @FXML
    private TableView<Device> deviceTableView; // New TableView

    
    @FXML
    private CheckBox showPropertiesCheckbox;
    
    private DeviceMonitor deviceMonitor;

    private ObservableList<Device> observableList;

	private LocalDateTime lastUpdatedTime;



    public void initialize() {
        deviceMonitor = new DeviceMonitor(this);

        deviceMonitor.addDevice(new HardwareDevice("Device 1"));
        deviceMonitor.addDevice(new HardwareDevice("Device 2"));
        deviceMonitor.addDevice(new HardwareDevice("Device 3"));

        observableList = FXCollections.observableList(deviceMonitor.getDevices());
        deviceListView.setItems(observableList);
        deviceListView.setCellFactory(deviceListView -> new DeviceListCell());

        // Add context menu to ListView	
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove");

        removeItem.setOnAction(event -> {
            Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
            if (selectedDevice != null) {
                deviceMonitor.removeDevice(selectedDevice);
                deviceListView.getSelectionModel().clearSelection();

            }
            refreshListView();
        });

        contextMenu.getItems().addAll(removeItem);
        deviceListView.setContextMenu(contextMenu);

        
        // Set up the TableView columns

        deviceTableView.setVisible(false);
        deviceTableView.setManaged(false);
    }

    @FXML
    private void showPropertiesChanged() {
        boolean showProperties = showPropertiesCheckbox.isSelected();
        deviceTableView.setVisible(showProperties);
        deviceTableView.setManaged(showProperties);
    }
    
	private class DataUpdateTask extends TimerTask {
        private Random random = new Random();

        @Override
        public void run() {
            refreshListView();
        }
    }

    @FXML
    private void addHardwareDevice() {
    	addDevice(DeviceType.HARDWARE);
    }
    @FXML
    private void addSoftwareDevice() {
    	addDevice(DeviceType.SOFTWARE);
    }
    
    @FXML
    private void updateTime() {
        
        String formattedTime = lastUpdatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        Duration duration = Duration.ofMillis(DeviceMonitor.REFRESH_TIME);
        LocalDateTime nextUpdateTime = lastUpdatedTime.plus(duration);
        String next = nextUpdateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Last/next Updated Time");
        alert.setHeaderText(null);
        alert.setContentText("Last Updated Time: " + formattedTime +" \nNext Update Time: " + next);
        alert.showAndWait();
    }
    
    public void updateListView() {
        Platform.runLater(() -> {
            deviceListView.refresh();
        });
    }
    
    private void addDevice(DeviceType deviceType) {
        String deviceName = deviceNameTextField.getText();
        Device newDevice = null;
	
        if (deviceType.equals(DeviceType.HARDWARE)) {
            newDevice = new HardwareDevice(deviceName);
        } else {
             //Create a SoftwareDevice 
             newDevice = new SoftwareDevice(deviceName);
        }
        observableList.add(newDevice);
        deviceNameTextField.clear();
        refreshListView();
    }
    
    @FXML
    private void removeDevice() {
        Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
        if (selectedDevice != null) {
            deviceMonitor.removeDevice(selectedDevice);
            deviceListView.getSelectionModel().clearSelection();
            refreshListView();
        }
    }

    public void refreshListView() {
        deviceListView.refresh();
    }

    private class DeviceListCell extends ListCell<Device> {
        @Override
        protected void updateItem(Device device, boolean empty) {
            super.updateItem(device, empty);

            if (device == null || empty) {
                setText(null);
                setGraphic(null);
                setTooltip(null); // Clear the tooltip
                setStyle(""); // Reset the cell style
            } else {
                setText("(" + device.getDeviceType() + ") " + device.getName() + " - " + device.getStatus());
                // Set the tooltip for the device
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Device Type: " + device.getDeviceType().toString() + "\nStatus: " + device.getStatus());
                tooltip.setShowDelay(new javafx.util.Duration(100)); // Set the show delay to 100 milliseconds
                setTooltip(tooltip);

                // Set the cell style based on the device status
                if (device.getStatus().equals("Online")) {
                    setStyle("-fx-text-fill: green;");
                } else if (device.getStatus().equals("Offline")) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle(""); // Reset the cell style
                }
            }
        }
    }

	public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void saveDevice() {
	    Device selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
	    if (selectedDevice != null) {
	        StringBuilder deviceData = new StringBuilder();
	        deviceData.append(selectedDevice.getDeviceType()).append(";")
	                 .append(selectedDevice.getName()).append(";")
	                 .append(selectedDevice.getStatus()).append(";")
	                 .append(selectedDevice.getManufacturer()).append(";")
	                 .append(selectedDevice.getVersion()).append(";")
	                 ;

	        if (selectedDevice instanceof HardwareDevice) {
	            HardwareDevice hardwareDevice = (HardwareDevice) selectedDevice;
	            deviceData.append(hardwareDevice.getManufacturer()).append(";")
	                     .append(hardwareDevice.getLocation()).append(";");
	        } else if (selectedDevice instanceof SoftwareDevice) {
	            SoftwareDevice softwareDevice = (SoftwareDevice) selectedDevice;
	            deviceData.append(softwareDevice.getInstallationDate()).append(";");
	        }
	        
	        String filePath = System.getProperty("user.dir") + "/target/devices.txt";
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
	            writer.write(deviceData.toString());
	            writer.newLine();
	            writer.flush();
	            showAlert("Device Saved", "The device has been saved successfully.");
	        } catch (IOException e) {
	            showAlert("Error", "An error occurred while saving the device.");
	            e.printStackTrace();
	        }
	    } else {
	        showAlert("No Device Selected", "Please select a device to save.");
	    }
	}

	
	private void showAlert(String title, String content) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(content);
	    alert.showAndWait();
	}
}
