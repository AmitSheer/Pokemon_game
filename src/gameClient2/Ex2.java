package gameClient2;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gameClient.*;
import gameClient.MyFrame;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Ex2 implements Runnable{
    private static GameManager _gm;
    private static JFrame _frame;
    private static GamePanel _gp;
    private static boolean flag=true;
    public static void main(String[] args) {
        Thread client = new Thread(new Ex2());
        client.start();
    }
    @Override
    public void run() {
        _frame = new JFrame();
        int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
//        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());

        init(game);
        game.startGame();
        _frame.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;
        System.out.println(game.getAgents().toString());
        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%1==0) {_frame.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }




    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<PokemonTrainer> log = GameManager.getTrainers(lg, gg);
        _gm.updateAgents(lg);
        String fs =  game.getPokemons();
        List<Pokemon> ffs = GameManager.json2Pokemons(fs);
        ffs.forEach(p->GameManager.updateEdge(p,gg));
        if (!ffs.equals(_gm.getPokemons())){
            _gm.setPokemons(ffs);
            _gm.findShortestForAgents();
        }
        for(int i=0;i<_gm.getTrainers().size();i++) {
            PokemonTrainer ag = _gm.getTrainers().get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.get_curr_node();
            double v = ag.get_money();
            if(dest==-1) {
                _gm.findShortestForAgents();
                game.chooseNextEdge(ag.getID(),ag.getNextNode());
            }else{
                game.chooseNextEdge(ag.getID(),ag.getNextNode());
            }
            System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
        }
    }
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        //directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
        //gg.init(g);
        _gm = new GameManager();
        _gm.setGraph(gg);
        _gm.setPokemons(game.getPokemons());
        _gm.getAlgo().init(gg);
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.setSize(1000,700);
        try {
            _gp = new GamePanel();
            _gp.setSize(_frame.getWidth(),_frame.getHeight());
            _gp.update(_gm);
        }catch(Exception e){
            System.out.println(e.toString());;
        }

        _frame.add(_gp, BorderLayout.CENTER);
        _frame.show();
        String info = game.toString();
        JSONObject line;
        List<PokemonTrainer> pts;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<Pokemon> cl_fs = GameManager.json2Pokemons(game.getPokemons());
            for(int a = 0;a<cl_fs.size();a++) { GameManager.updateEdge(cl_fs.get(a),gg);}
            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                Pokemon c = cl_fs.get(ind);
                int nn = c.getEdge().getSrc();
               // if(c.getType()<0 ) {nn = c.getEdge().getSrc();}
                game.addAgent(nn);
            }
            pts = GameManager.getTrainers(game.getAgents(),gg);
            _gm.setTrainers(pts);
        }
        catch (JSONException e) {e.printStackTrace();}
    }
}
