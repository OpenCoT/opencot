package com.github.opencot.io;

import com.github.opencot.data.DeviceData;

public interface Gateway {
	
	
	public int Init();
	public int Start();
	//public int Configure( Configuration cfg );
	public int Stop();
	//public int DeInit();
	public void addReceiver(DeviceData devdata);
	public void sendData(DeviceData devdata);
}
