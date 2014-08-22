package iscas.seagochen.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainGUI extends JFrame implements ActionListener{

	/**
	 * generated serial version UID
	 */
	private static final long serialVersionUID = -5439430427760860819L;
	
	private JButton button;
	private Label   lable;
	private Frame   frame;
	
	public MainGUI()
	{
		frame = new Frame("personal frame GUI");
		lable = new Label("this is lable");
		
		lable.setBackground(Color.blue);
		
		frame.setLayout(new FlowLayout());
		
		frame.add(lable);
		
		frame.setSize(200, 100);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[])
	{
		new MainGUI();
	}

}
