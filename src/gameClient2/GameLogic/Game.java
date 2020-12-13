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
    private game_service game;
    private GameManager _gm;
    private  GamePanel _gp;
    private Thread server;
    private List<Pokemon> badPokemon;
    private boolean isCloseWhenDone;

    public Game(boolean isCloseWhenDone) {
        this.isCloseWhenDone = isCloseWhenDone;
    }

    public void startGame(GamePanel panel){
        _gp = panel;
        game = Game_Server_Ex2.getServer(11);
        server = new Thread(this);
        server.start();
    }

    public void startGame(GamePanel panel,int scenario,int id){
        _gp = panel;
        game = Game_Server_Ex2.getServer(scenario);
        game.login(id);
        server = new Thread(this);
        server.start();
    }

    @Override
    public void run() {
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
        badPokemon = new LinkedList<>();
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
            badPokemon.removeIf(p -> !_gm.getPokemons().contains(p));
            if(badPokemon.size()>0){
                dt=20;
                //System.out.println(dt);
            }else{
                dt=100;
//                System.out.println(dt);
            }
            moveTrainers(game, gg);
            try {
                _gp.repaint();
                //System.out.println(_gm.getTime());
                Thread.sleep(dt);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        _gp.setShowTotal(true);
        if(isCloseWhenDone){
            System.exit(0);
        }
    }

    /**
     * choose move trainer to the next node to go to
     * @param trainer trainer to change
     */
    private void nextEdge( PokemonTrainer trainer){
        int id = trainer.getID();
        int dest = trainer.get_dest();
        int src = trainer.get_curr_node();
        double v = trainer.get_money();
        if(dest==-1) {
            if(trainer.getPathToPokemon().size()==0){
                if(_gm.getPokemons().contains(trainer.getNextPoke())&&trainer.getEndNodeId()== trainer.get_curr_node()){
                    badPokemon.add(trainer.getNextPoke());
                }
                findShortestForAgents();
            }
            if(trainer.getPathToPokemon().size()==0){
                System.out.println("asd");
            }
            trainer.set_next_node();
            game.chooseNextEdge(trainer.getID(),trainer.get_dest());
        }
        //System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
    }

    /**
     * updates agent location and tell server to move trainers to new location
     * @param game current game
     * @param gg current graph
     */
    private void moveTrainers(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        String fs =  game.getPokemons();
        _gm.updateAgents(lg);
        HashSet<Pokemon> ffs = GameManager.json2Pokemons(fs);
        ffs.forEach(p->GameManager.updateEdge(p,gg));
        _gm.setPokemons(ffs);
        for(PokemonTrainer trainer: _gm.getTrainers()) {
            nextEdge(trainer);
        }
    }

    /**
     * loads the game data and the trainers
     */
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
            for(int a = 0;a<rs;a++) {
                int ind = a%_gm.getPokemons().size();
                Pokemon c = _gm.getPokemons().get(ind);
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

    /**
     * stops the server and game
     */
    public synchronized void gameStop(){
        server.stop();
        game.stopGame();
    }

    /**
     * Find the shortest path to Pokemon
     */
    private void findShortestForAgents(){
//        ExecutorService executor = Executors.newFixedThreadPool(5);
////        System.out.println(Instant.now().toString());
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
                List<node_data> path;
                if(trainer.get_dest()==-1){
                    path = _gm.getAlgo().shortestPath(trainer.get_curr_node(),_gm.getPokemons().get(i).getEdge().getSrc());
                }else {
                    path = _gm.getAlgo().shortestPath(trainer.get_dest(),_gm.getPokemons().get(i).getEdge().getSrc());
                }
                path.add(_gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getDest()));
                double dist = _gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getSrc()).getWeight() + _gm.getPokemons().get(i).getEdge().getWeight();
                trainersToPokemonsDist.add(new TrainerToPath(trainer.getID(),_gm.getPokemons().get(i).getEdge().getSrc(),dist,path,_gm.getPokemons().get(i)));
            }
        }

        HashSet<Integer> trainersFilled = new HashSet<>();
        HashSet<Pokemon> pokemonFilled = new HashSet<>();

        while(pokemonFilled.size()< _gm.getPokemons().size()&&trainersFilled.size()< _gm.getTrainers().size()&&trainersToPokemonsDist.size()!=0){
            TrainerToPath trainerToPokemon = trainersToPokemonsDist.remove();
            while((trainersToPokemonsDist.size()>0&&trainersFilled.contains(trainerToPokemon.getSrc())||pokemonFilled.contains(trainerToPokemon.get_pokemon())||trainerToPokemon.getWeight()==Integer.MAX_VALUE)&&trainersToPokemonsDist.size()!=0){
                try{
                    trainerToPokemon = trainersToPokemonsDist.remove();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            boolean flag = true;
            while(flag&&trainersToPokemonsDist.size()!=0){
                flag=false;
                for (Pokemon pokeId : pokemonFilled) {
                    if(pokeId.getEdge().equals(trainerToPokemon.get_pokemon().getEdge())&&trainersToPokemonsDist.size()>0){
                        flag=true;
                        trainerToPokemon = trainersToPokemonsDist.remove();
                        break;
                    }
                }
            }


//            flag = true;
//            while(flag&&trainersToPokemonsDist.size()!=0){
//                flag=false;
//                for (Integer trainerId : trainersFilled) {
//                    if(_gm.getTrainer(trainerId).getPathToPokemon().containsAll(trainerToPokemon.get_path())&&trainersToPokemonsDist.size()>0){
//                        flag=true;
//                        trainerToPokemon = trainersToPokemonsDist.remove();
//                        break;
//                    }
//                }
//            }
            trainersFilled.add(trainerToPokemon.getSrc());
            pokemonFilled.add(trainerToPokemon.get_pokemon());
            _gm.updateTrainerPath(trainerToPokemon.get_path(),trainerToPokemon.getSrc());
            _gm.getTrainer(trainerToPokemon.getSrc()).setNextPoke(trainerToPokemon.get_pokemon());
//            _gm.getPokemon(trainerToPokemon.get_pokemonId()).setTrainerId(trainerToPokemon.getSrc());
        }
    }
}
