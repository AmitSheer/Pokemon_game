package gameClient2;

import Server.Game_Server_Ex2;
import api.*;
import gameClient2.GameLogic.GameManager;
import gameClient2.GameLogic.Pokemon;
import gameClient2.GameLogic.PokemonTrainer;
import gameClient2.gui.MyFrame;
import gameClient2.gui.GamePanel;
import gameClient2.gui.MyFrame;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Ex2{
    private static GameManager _gm;
    private static GamePanel _gp;
    public static boolean Flag = false;
    static int counter = 0;
    private static game_service game;
    //private static Thread client = new Thread(new Ex2());
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        frame.setSize(1000,700);
        frame.show();
//        if(args.length!=2) {
//            int scenario_num = 11;
//            game = Game_Server_Ex2.getServer(scenario_num);
//            client.start();
//        }else{
//
//        }
    }
//    @Override
//    public void run() {
//// you have [0,23] games
//        //	int id = 999;
//        //	game.login(id);
//        String g = game.getGraph();
//        String pks = game.getPokemons();
////        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
//        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
//
//        init(game);
//        game.startGame();
//        _frame.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
//        int ind=0;
//        long dt;
//        if(_gm.getTrainers().size()>=3){
//            dt=98;
//        }else{
//            dt = 50;
//        }
//        boolean first = true;
//        System.out.println(game.getAgents().toString());
//        //_frame.repaint();
//        while(game.isRunning()) {
//            _frame.setTitle("Ex2 - OOP: (NONE trivial Solution) "+_gm.getGameStatus().toString());
//            _gm.getGameStatus().update(game.toString());
//            moveAgants(game, gg);
//            try {
//                if(ind%1==0)_frame.repaint();
//                Thread.sleep(dt);
//                ind++;
//            }
//            catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//        String res = game.toString();
//
//        System.out.println(res);
//        System.out.println(counter);
//        System.exit(0);
//    }
//
//
//
//
//    private static void moveAgants(game_service game, directed_weighted_graph gg) {
//        String lg = game.move();
//        String fs =  game.getPokemons();
//        _gm.updateAgents(lg);
//        List<Pokemon> ffs = GameManager.json2Pokemons(fs);
//        ffs.forEach(p->GameManager.updateEdge(p,gg));
//        _gm.setPokemons(ffs);
////        if (!ffs.equals(_gm.getPokemons())){
////            _gm.findShortestForAgents();
////            counter++;
////        }
//        boolean flag = true;
//        for(int i=0;i<_gm.getTrainers().size();i++) {
//            PokemonTrainer ag = _gm.getTrainers().get(i);
//            int id = ag.getID();
//            int dest = ag.getNextNode();
//            int src = ag.get_curr_node();
//            double v = ag.get_money();
//            if(dest==-1&flag) {
//                flag = false;
//               // counter++;
//                _gm.findShortestForAgents();
//                game.chooseNextEdge(ag.getID(),ag.getNextNode());
//            }else{
//                game.chooseNextEdge(ag.getID(),ag.getNextNode());
//            }
//            System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
//        }
//    }
//    private static int nextNode(directed_weighted_graph g, int src) {
//        int ans = -1;
//        Collection<edge_data> ee = g.getE(src);
//        Iterator<edge_data> itr = ee.iterator();
//        int s = ee.size();
//        int r = (int)(Math.random()*s);
//        int i=0;
//        while(i<r) {itr.next();i++;}
//        ans = itr.next().getDest();
//        return ans;
//    }
//
//    private void init(game_service game) {
//        loadGameData(game);
//        _frame = new GameFrame();
//        //loadGui();
//    }
//    private void loadGameData(game_service game){
//        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
//        _gm = new GameManager();
//        _gm.setGameStatus(game.toString());
//        _gm.setGraph(gg);
//        _gm.setPokemons(game.getPokemons());
//        _gm.getAlgo().init(gg);
//        String info = game.toString();
//        JSONObject line;
//        List<PokemonTrainer> pts;
//        try {
//            line = new JSONObject(info);
//            JSONObject ttt = line.getJSONObject("GameServer");
//            int rs = ttt.getInt("agents");
//            System.out.println(info);
//            System.out.println(game.getPokemons());
//            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
//            ArrayList<Pokemon> cl_fs = GameManager.json2Pokemons(game.getPokemons());
//            for(int a = 0;a<cl_fs.size();a++) { GameManager.updateEdge(cl_fs.get(a),gg);}
//            for(int a = 0;a<rs;a++) {
//                int ind = a%cl_fs.size();
//                Pokemon c = cl_fs.get(ind);
//                int nn = c.getEdge().getSrc();
//                //if(c.getType()<0 ) {nn = c.getEdge().getSrc();}
//                game.addAgent(nn);
//            }
//            pts = GameManager.getTrainers(game.getAgents(),gg);
//            _gm.setTrainers(pts);
//           // _gm.findShortestForAgents();
//        }
//        catch (JSONException e) {e.printStackTrace();}
//
//    }
//    private void loadGui(){
//        int width = 1000,height = 700;
////        _frame.setSize(1000,700);
////        try {
////            _gp = new GamePanel();
////            _gp.setSize(_frame.getWidth(),_frame.getHeight());
////            _gp.update(_gm);
////        }catch(Exception e){
////            System.out.println(e.toString());;
////        }
////        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
////        _frame.add(_gp, BorderLayout.CENTER);
////        _frame.show();
////        _frame.repaint();
//    }
}
