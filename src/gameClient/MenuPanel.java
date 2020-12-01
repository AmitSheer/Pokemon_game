package gameClient;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

public class MenuPanel extends JPanel {
    private static JPanel _panel;
    private static final int ID_LENGTH = 9;

    public static void main(String args[]) {
        NumberFormatter formatter = new NumberFormatter() {
            @Override
            public Object stringToValue(String text)
                    throws ParseException {
                try {
                    return Double.valueOf(text);
                } catch (NumberFormatException ne) {
                    return super.stringToValue(text);
                }
            }
        };
        JFrame frame = new JFrame("Number Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("SansSerif", Font.BOLD, 16);

        JLabel label;
        JFormattedTextField input;
        JPanel panel;

        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        frame.setLayout(layout);

        label = new JLabel("Raw Number:");
        input = new JFormattedTextField(formatter);
        //input.setValue(2424.50);
        input.setColumns(20);
        input.setFont(font);
//        input.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent ke) {
//                String value = input.getText();
//                int l = value.length();
//                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
//                    input.setEditable(true);
//                } else {
//                    input.setEditable(false);
//                }
//            }
//        });

        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(label);
        panel.add(input);
        frame.add(panel);

        frame.add(new JTextField());
        frame.pack();
        frame.setVisible(true);
    }
    public MenuPanel(){
        super(new GridBagLayout());
        JLabel label = new JLabel("User Id");

    }

    private void UserIdComponent(){

    }
}
