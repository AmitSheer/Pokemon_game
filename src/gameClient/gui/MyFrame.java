package gameClient.gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class MyFrame extends JFrame {
    private static GamePanel _gp;
    private static GameInputPanel _gip;
    private static JSplitPane _sp;

    public MyFrame(int id ,int scenario,boolean isCloseWhenFinishRun){
        this.setSize(1000,700);
        init();
        _gp.startGame(scenario,id,isCloseWhenFinishRun);
    }

    public MyFrame(){
        this.setSize(1000,700);
        init();
    }

    private void init(){
        try {
            _gp = new GamePanel(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _gip = new GameInputPanel(this);

        //adds a splitter easy way to input
        _sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,_gp,_gip);
        _sp.setOneTouchExpandable(true);
        getContentPane().add(_sp, BorderLayout.CENTER);
        setPreferredSize(new Dimension(1000, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        _sp.setResizeWeight(1);
        //_sp.setToolTipText("Tap to Expand/Minimize");
        _sp.setDividerLocation(800);
        _sp.setBackground(Color.white);
        //_sp.getLeftComponent().setMaximumSize(new Dimension(250,this.getHeight()));

        setVisible(true);
        _sp.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent changeEvent) {
                if (changeEvent.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    _gp.setSize(_sp.getDividerLocation(),_gp.getHeight());
                }
            }
        });
        pack();
        //startGame();
    }

    public static GamePanel get_gp() {
        return _gp;
    }

    /**
     * tells the game to start running
     * @param scenario to run
     * @param id to login with
     */
    public static void startGame(int scenario,int id){
        _gp.startGame(scenario, id,false);
    }

}
