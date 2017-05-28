package com.github.opencot.io;


public interface Gateway {
	
	
	public int Init();
	public int Run();
	//public int Configure( Configuration cfg );
	public int Stop();
	//public int DeInit();
}
