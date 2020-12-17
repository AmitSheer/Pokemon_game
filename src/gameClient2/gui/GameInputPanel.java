package gameClient2.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameInputPanel extends JPanel {
    private static JTextField _id;
    private static JTextField _scenario;
    private static JButton _startRunButton;
    private static MyFrame _frame;
    private static JPanel _scenarioPanel;
    private static JPanel _idPanel;

    public GameInputPanel(MyFrame frame) {
        _frame = frame;
        _idPanel = new JPanel();
        _scenarioPanel = new JPanel();
        //spaces to make sure the ids start at the right place
        JLabel idLabel = new JLabel("             ID");
        JLabel scenarioLabel = new JLabel("Scenario");

        _id = new JTextField();
        _id.setName("id");
        _id.setColumns(9);
        _id.setSize(10,10);
        IdFilter();
        _idPanel.add(idLabel);
        _idPanel.add(_id);
        _idPanel.setSize(20,30);


        _scenario = new JTextField();
        _scenario.setName("scenario");
        _scenario.setColumns(9);
        _scenario.setSize(10,10);
        scenarioFilter();
        _scenarioPanel.add(scenarioLabel);
        _scenarioPanel.add(_scenario);
        _scenarioPanel.setSize(20,30);

        _startRunButton = new JButton("Start Scenario");
        _startRunButton.setSize(10,10);
        _startRunButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectionButtonPressed();
            }
        });



        this.add(_idPanel);
        this.add(_scenarioPanel);
        this.add(_startRunButton);
    }

    /**
     * Filter any non numerical values from field
     */
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

    /**
     * Filter any non numerical values from field
     */
    private void scenarioFilter() {
        ((AbstractDocument)_scenario.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }else if(offset>String.valueOf(Integer.MAX_VALUE).length()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    /**
     * when pressed the button will tell the game to start
     */
    private void selectionButtonPressed() {
        try {
            int scenarioNum = Integer.parseInt(_scenario.getText());
            int idNum = Integer.parseInt(_id.getText());
            MyFrame.startGame(scenarioNum, idNum);
            _scenarioPanel.getComponent(1).setBackground(Color.white);
        }catch(Exception e){
            _scenarioPanel.getComponent(1).setBackground(Color.red);
        }
    }
}
