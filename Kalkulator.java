import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.function.DoubleBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Kalkulator {

	private static String calculateLine(String line) {
		String patternPa = "\\(([\\d\\.\\+\\-\\*\\/]+)\\)";
		Pattern pattern = Pattern.compile(patternPa);
		Matcher m;
		while ((m = pattern.matcher(line)).find())
			line = line.replaceFirst(patternPa, calculatePart(m.group(1)));
		return calculatePart(line);
	}

	private static String calculatePart(String line) {
		String patternMu = "(-?[\\d\\.]+)\\*(-?[\\d\\.]+)";
		String patternDi = "(-?[\\d\\.]+)\\/(-?[\\d\\.]+)";
		String patternAd = "(-?[\\d\\.]+)\\+(-?[\\d\\.]+)";
		String patternSu = "(-?[\\d\\.]+)\\-(-?[\\d\\.]+)";
		String patternPo = "(-?[\\d\\.]+)\\^(-?[\\d\\.]+)";
		line = execute(line, patternPo, (d1, d2) -> Math.pow(d1, d2));
		line = execute(line, patternMu, (d1, d2) -> d1 * d2);
		line = execute(line, patternDi, (d1, d2) -> d1 / d2);
		line = execute(line, patternAd, (d1, d2) -> d1 + d2);
		line = execute(line, patternSu, (d1, d2) -> d1 - d2);
		return line;
	}

	private static String execute(String line, String patternString, DoubleBinaryOperator func) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher m;
		while ((m = pattern.matcher(line)).find()) {
			Double result = func.applyAsDouble(Double.valueOf(m.group(1)), Double.valueOf(m.group(2)));
			line = line.replaceFirst(patternString, result.toString());
		}
		return line;
	}

	public static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setTitle("Kalkulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setResizable(false);
		JTextField myText = new JTextField("", 30);
		{
			myText.addActionListener(event -> myText.setText(calculateLine(myText.getText())));
		}
		frame.add(myText, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		{
			JButton buttonWyczysc = new JButton("Wyczyœæ");
			buttonWyczysc.addActionListener(event -> {
				myText.setText("");
				myText.grabFocus();
			});
			buttonWyczysc.setMnemonic(KeyEvent.VK_W);
			buttonPanel.add(buttonWyczysc);

			JButton buttonPrzelicz = new JButton("Przelicz");
			buttonPrzelicz.setMnemonic(KeyEvent.VK_P);
			buttonPrzelicz.addActionListener(event -> {
				myText.setText(calculateLine(myText.getText()));
				myText.grabFocus();
			});
			buttonPanel.add(buttonPrzelicz);

			JButton buttonKoniec = new JButton("Koniec");
			buttonKoniec.setMnemonic(KeyEvent.VK_K);
			buttonKoniec.addActionListener(event -> SwingUtilities.getWindowAncestor(buttonKoniec).dispose());
			buttonPanel.add(buttonKoniec);
		}
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> runGUI());
	}
}
