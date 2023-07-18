package com.ssi.devicemonitor.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.ssi.devicemonitor.controller.DeviceMonitorController;


public class DeviceMonitor {
    private List<Device> devices;
    private Timer statusUpdateTimer;
    private LocalDateTime lastUpdatedTime;
	public static final long REFRESH_TIME = 2000;

    public DeviceMonitor(DeviceMonitorController deviceMonitorController) {
        devices = new ArrayList<>();

        // Start the timer to simulate status updates every few seconds
        statusUpdateTimer = new Timer();
		statusUpdateTimer.schedule(new StatusUpdateTask(deviceMonitorController), 0, REFRESH_TIME); // Update every 5 seconds
    }

    public List<Device> getDevices() {
        return devices;
    }

    public synchronized void addDevice(Device device) {
        this.devices.add(device);
    }

    public synchronized void removeDevice(Device device) {
        this.devices.remove(device);
    }

    public LocalDateTime getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	private class StatusUpdateTask extends TimerTask {
        private Random random = new Random();
		private DeviceMonitorController controller;

		public StatusUpdateTask(DeviceMonitorController controller) {
		    this.controller = controller;
		}
        @Override
        public void run() {
        	setLastUpdatedTime(LocalDateTime.now());
            synchronized (devices) {
                Iterator<Device> iterator = devices.iterator();
                while (iterator.hasNext()) {
                    Device device = iterator.next();
                    // Simulate random status updates
                    boolean isOnline = random.nextBoolean();
                    device.setStatus(isOnline ? "Online" : "Offline");
                }
            }
            controller.updateListView();
            controller.setLastUpdatedTime(getLastUpdatedTime());
        }
    }
}
