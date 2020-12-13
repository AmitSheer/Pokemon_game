package gameClient2.GameLogic;

import Server.Game_Server_Ex2;
import api.*;
import gameClient2.gui.GamePanel;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game implements Runnable{
    private static game_service game;
    private static GameManager _gm;
    private  GamePanel _gp;
    private static Thread server;
    private static List<Pokemon> badPokemon;

    public void startGame(GamePanel panel){
        _gp = panel;
        game = Game_Server_Ex2.getServer(11);
        server = new Thread(this);
        server.start();
    }

    public void startGame(GamePanel panel,int scenario,int id){
        _gp = panel;
        game = Game_Server_Ex2.getServer(scenario);
        //game.login(id);
        server = new Thread(this);
        server.start();
//        game.login(id);
    }

    @Override
    public void run() {
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
        loadGameData();
        _gp.update(_gm);
        long dt=98;
        System.out.println(game.getAgents());
        game.startGame();
        Date a = new Date();
        a.setTime(game.timeToEnd());
        while(game.isRunning()) {
            a.setTime(game.timeToEnd());
            _gm.setTime(a);
            moveAgants(game, gg);
            try {
                _gp.repaint();
                if(badPokemon.size()>0){
                    dt=50;
                }else{
                    dt=100;
                }
                Thread.sleep(dt);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
    }

    /**
     * choose move trainer to the next node to go to
     * @param trainer trainer to change
     */
    private static void nextEdge( PokemonTrainer trainer){
        int id = trainer.getID();
        int dest = trainer.getNextNode();
        int src = trainer.get_curr_node();
        double v = trainer.get_money();
        if(dest==-1) {
            findShortestForAgents();
            game.chooseNextEdge(trainer.getID(),trainer.getNextNode());
        }else{
            game.chooseNextEdge(trainer.getID(),trainer.getNextNode());
        }
        //System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
    }

    private void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        String fs =  game.getPokemons();
        _gm.updateAgents(lg);
        List<Pokemon> ffs = GameManager.json2Pokemons(fs);
        ffs.forEach(p->GameManager.updateEdge(p,gg));
        System.out.println(ffs.toString());
        _gm.setPokemons(ffs);
        System.out.println(_gm.getPokemons().toString());
        boolean flag = true;
        for(PokemonTrainer trainer: _gm.getTrainers()) {
            nextEdge(trainer);
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

    public static void findShortestForAgents(){
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        System.out.println(Instant.now().toString());
//        List<Callable<Void>> threads = new LinkedList<>();
//        for (PokemonTrainer pt :
//               _gm.getTrainers()) {
//            for (Pokemon p :
//                    _gm.getPokemons()) {
//                threads.add(new PathFinder(pt,p,_gm.getAlgo().copy()));
//            }
//        }
//        try {
//            executor.invokeAll(threads);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        PriorityQueue<TrainerToPath> trainersToPokemonsDist = PathFinder.trainersToPokemonsDist;
        PriorityQueue<TrainerToPath> trainersToPokemonsDist = new PriorityQueue<>(new CompareToEdge());
        for (int i = 0; i < _gm.getPokemons().size(); i++) {
            for (PokemonTrainer trainer : _gm.getTrainers()) {
                List<node_data> path = _gm.getAlgo().shortestPath(trainer.get_curr_node(),_gm.getPokemons().get(i).getEdge().getSrc());
                path.add(_gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getDest()));
                double dist = _gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getSrc()).getWeight() + _gm.getPokemons().get(i).getEdge().getWeight();
                trainersToPokemonsDist.add(new TrainerToPath(trainer.getID(),_gm.getPokemons().get(i).getEdge().getSrc(),dist,path,_gm.getPokemons().get(i).get_id()));
            }
        }

        HashSet<Integer> trainersFilled = new HashSet<>();
        HashSet<Integer> pokemonFilled = new HashSet<>();

        while(pokemonFilled.size()< _gm.getPokemons().size()&&trainersFilled.size()< _gm.getTrainers().size()){
            TrainerToPath trainerToPokemon = trainersToPokemonsDist.remove();
            while(trainersToPokemonsDist.size()>0&&trainersFilled.contains(trainerToPokemon.getSrc())||pokemonFilled.contains(trainerToPokemon.get_pokemonId())||trainerToPokemon.getWeight()==Integer.MAX_VALUE){
                trainerToPokemon = trainersToPokemonsDist.remove();
            }
//            boolean flag = true;
//            while(flag){
//                flag=false;
//                for (Integer pokeId : pokemonFilled) {
//                    if(_gm.getPokemon(pokeId).getEdge().equals(_gm.getPokemon(trainerToPokemon.get_pokemonId()).getEdge())&&trainersToPokemonsDist.size()>0){
//                        flag=true;
//                        trainerToPokemon = trainersToPokemonsDist.remove();
//                        break;
//                    }
//                }
//            }

//
//            flag = true;
//            while(flag){
//                flag=false;
//                for (Integer trainerId : trainersFilled) {
//                    if(_gm.getTrainer(trainerId).get_pathToPokemon().containsAll(trainerToPokemon.get_path())&&trainersToPokemonsDist.size()>0){
//                        flag=true;
//                        trainerToPokemon = trainersToPokemonsDist.remove();
//                        System.out.println("bllalalalala");
//                        break;
//                    }
//                }
//            }
            trainersFilled.add(trainerToPokemon.getSrc());
            pokemonFilled.add(trainerToPokemon.get_pokemonId());
            _gm.updateTrainerPath(trainerToPokemon.get_path(),trainerToPokemon.getSrc());
            _gm.getTrainer(trainerToPokemon.getSrc()).setNextPoke(_gm.getPokemon(trainerToPokemon.get_pokemonId()));
            _gm.getPokemon(trainerToPokemon.get_pokemonId()).setTrainerId(trainerToPokemon.getSrc());
        }
    }
}
