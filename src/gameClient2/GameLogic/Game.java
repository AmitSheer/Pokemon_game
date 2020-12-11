package gameClient2.GameLogic;

import Server.Game_Server_Ex2;
import api.*;
import gameClient2.gui.GamePanel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable{
    private  game_service game;
    private  GameManager _gm;
    private  GamePanel _gp;
    private static Thread server;

    public void startGame(GamePanel panel){
        _gp = panel;
        game = Game_Server_Ex2.getServer(11);
        server = new Thread(this);
        server.start();
    }

    public void startGame(GamePanel panel,int scenario,int id){
        _gp = panel;
        game = Game_Server_Ex2.getServer(scenario);
        server = new Thread(this);
        server.start();
//        game.login(id);
    }

    @Override
    public void run() {
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
        loadGameData();
        _gp.update(_gm);
        game.startGame();
        long dt=95;
        System.out.println(game.getAgents());
        while(game.isRunning()) {
            _gm.getGameStatus().update(game.toString());
            moveAgants(game, gg);
            try {
                _gp.repaint();
                Thread.sleep(dt);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
    }
    private void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        String fs =  game.getPokemons();
        _gm.updateAgents(lg);
        List<Pokemon> ffs = GameManager.json2Pokemons(fs);
        ffs.forEach(p->GameManager.updateEdge(p,gg));
        _gm.setPokemons(ffs);
        boolean flag = true;
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
    private void loadGameData(){
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
        _gm = new GameManager();
        _gm.setGameStatus(game.toString());
        _gm.setGraph(gg);
        _gm.setPokemons(game.getPokemons());
        _gm.getAlgo().init(gg);
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
                //if(c.getType()<0 ) {nn = c.getEdge().getSrc();}
                game.addAgent(nn);
            }
            pts = GameManager.getTrainers(game.getAgents(),gg);
            _gm.setTrainers(pts);
        }
        catch (JSONException e) {e.printStackTrace();}

    }

    private void loadAgents(game_service game, int rs, ArrayList<Pokemon> cl_fs, directed_weighted_graph gg) {
        if (_gm.getAlgo().isConnected()) {
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                Pokemon c = cl_fs.get(ind);
                int nn = c.getEdge().getSrc();
                game.addAgent(nn);
            }
        } else {
            System.out.println("Not connected");
            List<Integer> nodeKeys = Tarjan.getSccNodes();
            int index = 0;
            for (; index < nodeKeys.size() && index < rs; index++) {
                for (Pokemon p : _gm.getPokemons()) {
                    if (_gm.getAlgo().shortestPathDist(nodeKeys.get(index), p.getEdge().getSrc()) != 0) {
                        game.addAgent(p.getEdge().getSrc());
                        break;
                    }
                }
            }
            _gm.setTrainers(GameManager.getTrainers(game.getAgents(), gg));
        }
    }
    public boolean isRunning(){
        return game.isRunning();
    }
    public synchronized void gameStop(){
        server.stop();
        game.stopGame();
    }
}
