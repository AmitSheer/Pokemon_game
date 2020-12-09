package gameClient2.gui;

import gameClient2.GameLogic.GameManager;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class MyFrame extends JFrame {
    private static GamePanel _gp;
    private static GameInputPanel _gip;
    private static JSplitPane _sp;

    public MyFrame(int id ,int scenario){

    }

    public MyFrame(){
        init();
    }
    private void init(){

        try {
            _gp = new GamePanel(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _gip = new GameInputPanel(this);

        _sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,_gp,_gip);
        //_sp.setOneTouchExpandable(true);
        getContentPane().add(_sp, BorderLayout.CENTER);
        setPreferredSize(new Dimension(1645, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        _sp.setResizeWeight(1);
        _sp.setDividerLocation(1433);
        setVisible(true);
        pack();
        _sp.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent changeEvent) {
                if (changeEvent.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    _gp.setSize(_sp.getWidth() - _sp.getWidth() + _sp.getDividerLocation(),_gp.getHeight());
                }
            }
        });
        startGame();
    }

    public static void startGame(){
        _gp.startGame();
    }
    public static void main(String[] args) {
        MyFrame f= new MyFrame();
    }

}
