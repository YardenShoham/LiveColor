// LiveColor
// Yarden Shoham 2018

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.Hashtable;
import gnu.io.*;
import java.io.*;

/**
* Frame and main. Contains 3 sliders (RGB).
*/
public class LiveColor extends JFrame implements ChangeListener
{
	private JSlider redSlider, greenSlider, blueSlider; // the color sliders
	private JLabel redLabel, greenLabel, blueLabel; // the label
	private OutputStream port; // the port through which we'll comunicate with the arduino

	/**
	* Creating the sliders and placing them.
	*/
	public LiveColor()
	{
		super("Changing colors"); // setting title

		// creating sliders. rgb 0-255
		redSlider = new JSlider(JSlider.VERTICAL, 0, 255, 0);
		greenSlider = new JSlider(JSlider.VERTICAL, 0, 255, 0);
		blueSlider = new JSlider(JSlider.VERTICAL, 0, 255, 0);


		// creating label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		for (int i = 0; i < 250; i += 20) labelTable.put(i, new JLabel("" + i));
		labelTable.put(255, new JLabel("" + 255));

		// setting labels
		redSlider.setLabelTable(labelTable);
		greenSlider.setLabelTable(labelTable);
		blueSlider.setLabelTable(labelTable);
		redSlider.setPaintLabels(true);
		greenSlider.setPaintLabels(true);
		blueSlider.setPaintLabels(true);

		// creating the slider and label panels
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		// setting layout
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));


		// adding sliders
		centerPanel.add(redSlider);
		centerPanel.add(greenSlider);
		centerPanel.add(blueSlider);
		add(centerPanel);

		// creating labels
		Font font = new Font("Serif", Font.BOLD, 20);
		redLabel = new JLabel("Red");
		redLabel.setForeground(Color.RED);
		redLabel.setFont(font);
		greenLabel = new JLabel("Green");
		greenLabel.setForeground(Color.GREEN);
		greenLabel.setFont(font);
		blueLabel = new JLabel("Blue");
		blueLabel.setForeground(Color.BLUE);
		blueLabel.setFont(font);

		// adding labels
		southPanel.add(redLabel);
		southPanel.add(greenLabel);
		southPanel.add(blueLabel);
		add(southPanel, BorderLayout.SOUTH);

		// initializing the serial port
		initSerialPort();

		// adding handlers
		redSlider.addChangeListener(this);
		greenSlider.addChangeListener(this);
		blueSlider.addChangeListener(this);
	}

	/**
	* The handler for the sliders.
	* @param event The event that is being fired
	*/
	@Override
	public void stateChanged(ChangeEvent event)
	{
		String serialData = "R" + redSlider.getValue() + "G" + greenSlider.getValue() + "B" + blueSlider.getValue();
		try
		{
			port.write(serialData.getBytes());
			Thread.sleep(20); // so the arduino wouldn't receive so much data in such little time
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	/**
	* Creating the serial communication channel.
	*/
	public void initSerialPort()
	{
		try
		{
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier("COM3");
			SerialPort serialPort = (SerialPort) portId.open("Live Color", 5000);
			int baudRate = 9600;
			serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port = serialPort.getOutputStream();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	/**
	* The main method creates the frame.
	* @param args The arguments from the cmd. Not used in this program.
	*/
	public static void main(String[] args)
	{
		LiveColor frame = new LiveColor();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 270);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // center the frame
		frame.setVisible(true);
	}
}