import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main
{
	private static JTextField urval = new JTextField("255", 3);
	private static JTextField ugval = new JTextField("255", 3);
	private static JTextField ubval = new JTextField("255", 3);	
	private static JTextField orval = new JTextField(3);
	private static JTextField ogval = new JTextField(3);
	private static JTextField obval = new JTextField(3);
	private static JPanel mcPanel = new JPanel();
	private static JPanel ccPanel = new JPanel(new BorderLayout());


	public static void main(String[] args)
	{
		createGUI();
	}

	public static int[] getCompColour(int h, int s, int l)
	{
		int b = h-180;

		if(b < 0)
			b += 360;

		int[] comp = {b, s, l};
		return comp;
	}

	public static int[] convertRGBtoHSL(int r, int g, int b)
	{		
		double rpercent = normaliseRGBVal(r);
		double gpercent = normaliseRGBVal(g);
		double bpercent = normaliseRGBVal(b);
		char a = ' ';	
		double max = 0;
		double min = 1;

		if(max < rpercent)
		{
			max = rpercent;
			a = 'r';
		}
		if(max < gpercent)
		{
			max = gpercent;
			a = 'g';
		}
		if(max < bpercent)
		{
			max = bpercent;
			a = 'b';
		}

		if(min > rpercent)
			min = rpercent;
		if(min > gpercent)
			min = gpercent;
		if(min > bpercent)
			min = bpercent;

		double L = (max + min) / 2;
		double S = 0;
		double H = 0;

		if(max != min)
		{
			if(L < 0.5)
				S = (max - min) / (max + min);
			else
				S = (max- min) / (2 - max - min);
		}

		switch(a)
		{
		case 'r':	H = (gpercent - bpercent) / (max + min);
		break;
		case 'g':	H = 2 + (bpercent - rpercent) / (max - min);
		break;
		case 'b':	H = 4 + (rpercent - gpercent) / (max - min);
		}

		H = H * 60;
		if(H < 0)
			H += 360;

		DecimalFormat d = new DecimalFormat("0");
		int[] hsl = {(int)(double)Double.valueOf(d.format(H)), 
				(int)(double)Double.valueOf(d.format(S*100)), 
				(int)(double)Double.valueOf(d.format(L*100))};
		return hsl;
	}

	public static int[] convertHSLtoRGB(int h, int s, int l)
	{	
		if(s == 0)
		{
			int[] rgb = {l, l, l};
			return rgb;
		}
				
		double normalisedLum = l/100.0;
		double normalisedSat = s/100.0;
		
		double temp2 = 0;
		if(normalisedLum < 0.5)
			temp2 = normalisedLum*(1.0 + normalisedSat);
		else
			temp2 = normalisedLum + normalisedSat - normalisedLum*normalisedSat;

		double temp1 = 2 * normalisedLum - temp2;
		double normalisedHue = h/360.0;

		double rtemp = normalisedHue + (1/3.0);
		double gtemp = normalisedHue;
		double btemp = normalisedHue - (1/3.0);

		rtemp = putInRange0to1(rtemp);
		gtemp = putInRange0to1(gtemp);
		btemp = putInRange0to1(btemp);

		double r = calcSingleRGBVal(temp2, temp1, rtemp);
		double g = calcSingleRGBVal(temp2, temp1, gtemp);
		double b = calcSingleRGBVal(temp2, temp1, btemp);
		
		return new int[]{scaleToRGB(r), scaleToRGB(g), scaleToRGB(b)};
	}

	/**
	 * NOT A SCALING METHOD
	 * 
	 * @param valToRange
	 * @return
	 */
	private static double putInRange0to1(double valToRange) 
	{
		double newVal = valToRange;
		
		if(valToRange < 0)
			newVal += 1;
		else if(valToRange > 1)
			newVal -= 1;
		return newVal;
	}
	
	/**
	 * Normalise an R, G, or B value
	 * @param value
	 * @return the normalised value
	 */
	private static double normaliseRGBVal(int value)
	{
		return (value/255.0);
	}
	
	/**
	 * 
	 * @param value the RGB value to scale to range [0, 255]
	 * @return the scaled value
	 */
	private static int scaleToRGB(double value)
	{
		return (int)Math.round(value*255);
	}

	/**
	 * @param temp2
	 * @param temp1
	 * @param currentColourVal
	 * @return the final r, g, or b colour value
	 */
	private static double calcSingleRGBVal(double temp2, double temp1,
			double currentColourVal) 
	{
		double r;
		if(6*currentColourVal < 1)
			r = temp1 + (temp2-temp1)*6.0*currentColourVal;
		else if(2*currentColourVal < 1)
			r = temp2;
		else if(3*currentColourVal < 2)
			r = temp1 + (temp2-temp1)*((2/3.0)-currentColourVal)*6.0;
		else
			r = temp1;
		return r;
	}

	/**
	 * @throws HeadlessException
	 */
	@SuppressWarnings("unused")
	private static void createGUI() throws HeadlessException 
	{
		JPanel mainColourPanel = new JPanel(new BorderLayout());
		JPanel compColourPanel = new JPanel(new BorderLayout());
	
		JLabel rl = new JLabel("\tr:");
		JLabel gl = new JLabel("\tg:");
		JLabel bl = new JLabel("\tb:");
		JLabel rl2 = new JLabel("r:");
		JLabel gl2 = new JLabel("g:");
		JLabel bl2 = new JLabel("b:");
	
		JPanel urpane = new JPanel(new BorderLayout());
		JPanel ugpane = new JPanel(new BorderLayout());
		JPanel ubpane = new JPanel(new BorderLayout());
	
		urpane.add(rl, BorderLayout.WEST);
		urpane.add(urval, BorderLayout.EAST);
	
		ugpane.add(gl, BorderLayout.WEST);
		ugpane.add(ugval, BorderLayout.EAST);
	
		ubpane.add(bl, BorderLayout.WEST);
		ubpane.add(ubval, BorderLayout.EAST);
	
		JPanel userInputPanel = new JPanel(new BorderLayout());
	
		userInputPanel.add(urpane, BorderLayout.NORTH);
		userInputPanel.add(ugpane, BorderLayout.CENTER);
		userInputPanel.add(ubpane, BorderLayout.SOUTH);
	
	
		mcPanel.setBackground(Color.white);
		mcPanel.setPreferredSize(new Dimension(100, 100));
	
		mainColourPanel.add(mcPanel, BorderLayout.NORTH);
		mainColourPanel.add(userInputPanel, BorderLayout.SOUTH);
	
		JPanel outputPanel = new JPanel(new BorderLayout());
		JPanel orpane = new JPanel(new BorderLayout());
		JPanel ogpane = new JPanel(new BorderLayout());
		JPanel obpane = new JPanel(new BorderLayout());
	
		orpane.add(rl2, BorderLayout.WEST);
		orpane.add(orval, BorderLayout.EAST);
	
		ogpane.add(gl2, BorderLayout.WEST);
		ogpane.add(ogval, BorderLayout.EAST);
	
		obpane.add(bl2, BorderLayout.WEST);
		obpane.add(obval, BorderLayout.EAST);
	
		outputPanel.add(orpane, BorderLayout.NORTH);
		outputPanel.add(ogpane, BorderLayout.CENTER);
		outputPanel.add(obpane, BorderLayout.SOUTH);
	
		ccPanel.setPreferredSize(new Dimension(100, 100));
	
		compColourPanel.add(ccPanel, BorderLayout.NORTH);
		compColourPanel.add(outputPanel, BorderLayout.SOUTH);
	
		JPanel buttonPanel = new JPanel(new BorderLayout());
	
		JButton compButton = new JButton("Get complementary colour");
		compButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r = Integer.parseInt(urval.getText());
				int g = Integer.parseInt(ugval.getText());
				int b = Integer.parseInt(ubval.getText());
	
				if(r > 255)
					r -= 255;
				if(g > 255)
					g -= 255;
				if(b > 255)
					b -= 255;
	
				int hsl[] = convertRGBtoHSL(r, g, b);
				int cc[] = getCompColour(hsl[0], hsl[1], hsl[2]);
				int rgb[] = convertHSLtoRGB(cc[0], cc[1], cc[2]);
				ccPanel.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
				orval.setText("" + rgb[0]);
				ogval.setText("" + rgb[1]);
				obval.setText("" + rgb[2]);
			}
		});
	
		JButton enterColourButton = new JButton("Enter colour");
		enterColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int r = Integer.parseInt(urval.getText());
				int g = Integer.parseInt(ugval.getText());
				int b = Integer.parseInt(ubval.getText());
	
				if(r > 255)
					r -= 255;
				if(g > 255)
					g -= 255;
				if(b > 255)
					b -= 255;
	
				mcPanel.setBackground(new Color(r, g, b));
			}
	
		});
		buttonPanel.add(enterColourButton, BorderLayout.WEST);
		buttonPanel.add(compButton, BorderLayout.EAST);
	
		//Create the frame
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(450, 300));
	
		Container c = frame.getContentPane();
		c.add(mainColourPanel, BorderLayout.WEST);
		c.add(compColourPanel, BorderLayout.EAST);
		c.add(buttonPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}
}