package gameClient2;

import api.GeoLocations;
import api.game_service;
import gameClient2.GameLogic.GameManager;
import gameClient2.GameLogic.Pokemon;
import gameClient2.gui.GamePanel;
import gameClient2.gui.MyFrame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
