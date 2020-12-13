package gameClient2;

import api.GeoLocations;
import api.game_service;
import gameClient2.GameLogic.GameManager;
import gameClient2.GameLogic.Pokemon;
import gameClient2.gui.GamePanel;
import gameClient2.gui.MyFrame;

import java.util.ArrayList;
import java.util.List;

public class Ex2{
    public static void main(String[] args) {
//        List<Pokemon> a = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            a.add(new Pokemon(i,i,new GeoLocations(),i,null));
//        }
//        Pokemon b = a.get(0);
//        a.remove(0);
//        System.out.println(b);
        if(args.length==0) {
            MyFrame frame = new MyFrame();
            frame.show();
        }else{
            MyFrame frame = new MyFrame(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            frame.show();
        }
    }
}
