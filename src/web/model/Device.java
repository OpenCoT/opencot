/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.model;

import com.github.opencot.DeviceData;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.ClientEndpoint;

/**
 *
 * @author Saulius
 */
@ClientEndpoint
public class Device {

   
    private String name;
    private String status;
    private String type;
    private String description;
    protected boolean valid;
	protected int id;
	
	protected List<DeviceData> dataentries;

    public Device() {
    }
    public Device(int uid, String name, String description) {
		this.id = uid;
		this.name = name;
		this.description = description;
		dataentries = new ArrayList<>();
		valid = true;
	}
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public int getID() {
		return id;
	}

	public boolean isValid() {
		return valid;
	}
	public void setInvalid() {
		valid = false;
	}

	
	
	public void addData(DeviceData newdata) {
		dataentries.add(newdata);
	}
	/**
	 * Returns data object by the entry name
	 * @param dataname Name of data entry
	 * @return data object if found, null otherwise
	 */
	public DeviceData getData( String dataname ) {
		for (DeviceData dd : dataentries) {
			if (dd.getName().equalsIgnoreCase(dataname))
				return dd;
		}
		return null;
	}
}