package gameClient2.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameInputPanel extends JPanel {
    //private static List<Integer> SCENARIO_LIST = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
    private static JTextField _id;
    private static JSpinner _scenario;
    private static JButton _startRunButton;
    private static MyFrame _frame;

    public GameInputPanel(MyFrame frame) {
        _frame = frame;
        _id = new JTextField();
        IdFilter();
        _id.setColumns(9);
        _id.setSize(10,10);
        _scenario = new JSpinner(new SpinnerNumberModel(0,0,23,1));
        _scenario.setSize(10,10);
        _startRunButton = new JButton("blablabla");
        _startRunButton.setSize(10,10);
        _startRunButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectionButtonPressed();
            }
        });
        this.add(_id);
        this.add(_scenario);
        this.add(_startRunButton);
        this.setSize(20,30);
    }

    private void IdFilter() {
        ((AbstractDocument)_id.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                } else if(offset>9){
                    return;
                }

                super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    private void selectionButtonPressed() {
        MyFrame.startGame(Integer.parseInt(_scenario.getValue().toString()),Integer.parseInt(_id.getText()));
    }
}
