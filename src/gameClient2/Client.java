package gameClient2;

import gameClient.MyFrame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Client {
    private static MyFrame _frame;
    public Client(){
        _frame = new MyFrame("game Frame");
        _frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

            }
        });

    }

}
