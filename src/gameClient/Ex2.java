package gameClient;

import gameClient.gui.MyFrame;

/**
 * Main game run
 */
public class Ex2{
    public static void main(String[] args) {
        if(args.length==0) {
            MyFrame frame = new MyFrame();
            frame.show();
        }else{
            MyFrame frame = new MyFrame(Integer.parseInt(args[0]),Integer.parseInt(args[1]),true);
            frame.show();
        }
    }
}
