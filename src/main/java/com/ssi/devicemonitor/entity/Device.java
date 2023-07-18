package com.ssi.devicemonitor.entity;

public abstract class Device {
	
	
    private String name;
    private String status;
    
    //general device fields
    private String manufacturer;
    private DeviceType deviceType;
    private String version;
    
    
    public enum DeviceType {
        HARDWARE("Hardware"),
        SOFTWARE("Software");

        private final String displayName;

        DeviceType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
    
    public Device(String name) {
        this.name = name;
        this.status = "Offline"; // Set initial status to Offline
    }

    public Device(String name,DeviceType type) {
    	this.name = name;
    	this.deviceType = type;
    	this.status = "Offline"; // Set initial status to Offline
    	
    }

    
    public String getName() {
        return name;
    }

    public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public DeviceType getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
}
