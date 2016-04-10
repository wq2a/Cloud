package cloud.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class Config{
	public final static Dimension SCREENDIM = Toolkit.getDefaultToolkit().getScreenSize();
	public final static CompoundBorder BLACKBORDER = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), 
        	BorderFactory.createEmptyBorder(10, 10, 10, 10));
    public final static CompoundBorder REDBORDER = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red), 
        	BorderFactory.createEmptyBorder(10, 10, 10, 10));
    private static Config instance;
    
    public static Config getInstance(){
    	if(instance == null){
    		return new Config();
    	}
    	return instance;
    }

}