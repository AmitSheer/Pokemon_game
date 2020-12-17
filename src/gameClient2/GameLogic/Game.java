package gameClient2.GameLogic;

import Server.Game_Server_Ex2;
import api.*;
import gameClient2.gui.GamePanel;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.attribute.UnmodifiableSetException;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Game implements Runnable{
    private static game_service game;
    private static GameManager _gm;
    private  GamePanel _gp;
    private Thread server;
    private static List<Pokemon> badPokemon = new ArrayList<>();
    private static int dt;
    private boolean isCloseWhenDone;


    public Game(boolean isCloseWhenDone) {
        this.isCloseWhenDone = isCloseWhenDone;
    }

    public void startGame(GamePanel panel){
        _gp = panel;
        _gm = new GameManager();
        game = Game_Server_Ex2.getServer(30);
        server = new Thread(this);
        server.start();
    }

    public void startGame(GamePanel panel,int scenario,int id){
        _gp = panel;
        _gm = new GameManager();
        game = Game_Server_Ex2.getServer(scenario);
        game.login(id);
        server = new Thread(this);
        server.start();
    }

    @Override
    public void run() {
        directed_weighted_graph gg = GraphParser.Json2Graph(game.getGraph());
        badPokemon = new LinkedList<>();
        loadGameData(gg);
        _gp.update(_gm);
        dt=110;
        System.out.println(game.getAgents());
        game.startGame();
        Date a = new Date();
        a.setTime(game.timeToEnd());
        //findShortestForAgents();
        while(game.isRunning()) {
            a.setTime(game.timeToEnd());
            _gm.setTime(a);
            badPokemon.removeIf(p -> !_gm.getPokemons().contains(p));
            //System.out.println(game.getAgents());
            moveTrainers(game, gg);
//            if(badPokemon.size()>0){
//                dt=20;
//                //System.out.println(dt);
//            }else{
//                dt=100;
//                System.out.println(dt);
//            }
            try {
                _gp.repaint();
                //System.out.println(_gm.getTime());
                //System.out.println(dt);
                Thread.sleep(dt);
                dt = 99;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        _gm.setGameStatus(game.toString());
        String res = game.toString();

        System.out.println(res);
        _gp.setShowTotal(true);
        _gp.repaint();
        if(isCloseWhenDone){
            System.exit(0);
        }
    }

    /**
     * choose move trainer to the next node to go to
     * @param trainer trainer to change
     */
    private static void nextEdge( PokemonTrainer trainer){
        int id = trainer.getID();
        int dest = trainer.get_dest();
        int src = trainer.get_curr_node();
        double v = trainer.get_money();
//        findShortestForAgents();
        //System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
//        if (trainer.getPathToPokemon().size() == 1 && dest==-1) {
//            if(badPokemon.stream().anyMatch(p-> p.getEdge().equals(trainer.get_curr_edge())))
//            edge_data ed = _gm.getGraph().getEdge(trainer.get_curr_node(), trainer.getPathToPokemon().get(0).getKey()); ed.getWeight() < (0.001) / 2||

//            else if ((trainer.get_speed() >= 5) && (ed.getWeight() < 1.5) )
//                dt = 50;
//        }
        if(badPokemon.size()>0) {
//            System.out.println(badPokemon.toString());
            for (Pokemon p : badPokemon) {
                if(dest==-1&&trainer.getPathToPokemon().size()==1)
                    if(p.getEdge().getSrc() == trainer.get_curr_node() && p.getEdge().getDest() == trainer.getPathToPokemon().get(0).getKey())
                        dt=30;
                else if (p.getEdge().equals(trainer.get_curr_edge())) {
                    dt = 30;
                }
            }
        }
        if(dest==-1&&trainer.getPathToPokemon().size()!=0) {
            trainer.set_next_node(trainer.getPathToPokemon().remove(0).getKey());
            game.chooseNextEdge(trainer.getID(),trainer.get_dest());
        }
    }

    /**
     * updates agent location and tell server to move trainers to new location
     * @param game current game
     * @param gg current graph
     */
    private static void moveTrainers(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        String fs =  game.getPokemons();
        GameManager.getTrainers(lg,_gm.getGraph());
        HashSet<Pokemon> ffs = GameManager.json2Pokemons(fs,gg);
        ffs.forEach(p->GameManager.updateEdge(p,gg));
        _gm.setPokemons(ffs);
        boolean finishedPath = false;
        for (PokemonTrainer pt : _gm.getTrainers()) {
            if(pt.getPathToPokemon().size()==0&&pt.get_dest()==-1){
                finishedPath=true;
                break;
            }
        }
        for (PokemonTrainer pt : _gm.getTrainers()) {
            if(_gm.getPokemons().contains(pt.getNextPoke())&&pt.getEndNodeId()== pt.get_curr_node()&&!badPokemon.contains(pt.getNextPoke())&&pt.getPathToPokemon().size()==0){
                badPokemon.add(pt.getNextPoke());
            }
        }
        if(finishedPath)
            findShortestForAgents();
        for(PokemonTrainer trainer: _gm.getTrainers()) {
            nextEdge(trainer);
        }
    }

    /**
     * loads the game data and the trainers
     */
    private void loadGameData(directed_weighted_graph gg){
        _gm = new GameManager();
        _gm.setGameStatus(game.toString());
        _gm.setGraph(gg);
        _gm.setPokemons(game.getPokemons());
        _gm.getAlgo().init(gg);
        String info = game.toString();
        JSONObject line;
        List<PokemonTrainer> pts;
        markPokemonGroups();
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
//            PriorityQueue<Pokemon> pkms = new PriorityQueue<Pokemon>(new Comparator<Pokemon>() {
//                @Override
//                public int compare(Pokemon o1, Pokemon o2) {
//                    return o2.get_numOfClosePokemon()-o1.get_numOfClosePokemon();
//                }
//            });
//            markClosePkms(_gm.getPokemons(),pkms);
//            for (int i = 0; i < _gm.getPokemons().size() && i < rs; i++) {
//                int nn = pkms.poll().getEdge().getSrc();
//                game.addAgent(nn);
//                rs--;
//            }
            loadTrainers(rs,_gm.getPokemons());
//            for(int a = 0;a<rs;a++) {
//                int ind = a%_gm.getPokemons().size();
//                Pokemon c = _gm.getPokemons().get(ind);
//                int nn = c.getEdge().getSrc();
//                game.addAgent(nn);
//            }
            GameManager.getTrainers(game.getAgents(),gg);
            //_gm.setTrainers(pts);
        }
        catch (JSONException e) {e.printStackTrace();}

    }

    private void markPokemonGroups(){
        for (Pokemon currentPokemon : _gm.getPokemons()) {
            for (Pokemon pokemon : _gm.getPokemons()) {
                if(!pokemon.equals(currentPokemon)){
                    List<node_data> dist = _gm.getAlgo().shortestPath(pokemon.getEdge().getSrc(),currentPokemon.getEdge().getSrc());
                    if(dist.size()<3){
                        currentPokemon.getClosePokemons().add(pokemon);
                        pokemon.getClosePokemons().add(currentPokemon);
                    }
                }
            }
        }
    }
    private void loadTrainers(int rs, List<Pokemon> cl_fs) {
        PriorityQueue<Pokemon> pokemonQueue = new PriorityQueue<>(new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon o1, Pokemon o2) {
                return o2.getClosePokemons().size()-o1.getClosePokemons().size();
            }
        });
        pokemonQueue.addAll(_gm.getPokemons());
        if (_gm.getAlgo().isConnected()) {
            int index = 0;
            for (;index < rs && index<pokemonQueue.size(); index++) {
                Pokemon nn = pokemonQueue.poll();
                pokemonQueue.removeIf(p->nn.getClosePokemons().contains(p));
                game.addAgent(nn.getEdge().getSrc());
            }
            for (; index < rs; index++) {
                int ind = index%_gm.getPokemons().size();
                Pokemon c = _gm.getPokemons().get(ind);
                int nn = c.getEdge().getSrc();
                game.addAgent(nn);
            }
        } else {
            System.out.println("Not connected");
            List<List<Integer>> connections = Tarjan.getSccNodes();
            int index = 0;
            int [] numOfTrainersForEachScc = new int[connections.size()];
            PriorityQueue<List<Integer>> sccBySize = new PriorityQueue<>(new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> o1, List<Integer> o2) {
                    if (o1.size() == o2.size())
                        return 0;
                    else if (o1.size() < o2.size())
                        return 1;
                    return -1;
                }
            });
            connections = sccBySize.stream().collect(Collectors.toList());
            for (; index < sccBySize.size() && index < rs; index++) {
                int pokemonIndex = -1;
                for (Pokemon p : _gm.getPokemons()) {
                    if (connections.get(index).contains(p.getEdge().getSrc())&&!p.isMarked()) {
                        pokemonIndex = p.getEdge().getSrc();
                        game.addAgent(p.getEdge().getSrc());
                        p.setMarked(true);
                        numOfTrainersForEachScc[index]++;
                        break;
                    }
                }
                if (pokemonIndex == -1 && connections.get(index).size() > 1) {
                    numOfTrainersForEachScc[index]++;
                    game.addAgent(connections.get(index).get(0));
                }
            }
            int agentsLeft = rs-index;
        }
        GameManager.getTrainers(game.getAgents(), _gm.getGraph());
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
    private static void findShortestForAgents(){
        PriorityQueue<TrainerToPath> trainersToPokemonsDist = new PriorityQueue<>(new CompareToEdge());
        //gets all distance from point Trainer to Pokemon and adds it to PriorityQueue
        for (int i = 0; i < _gm.getPokemons().size(); i++) {
            for (PokemonTrainer trainer : _gm.getTrainers()) {
                List<node_data> path;
                //Check if Trainer is on edge
                if(trainer.get_curr_edge()==null){
                    path = _gm.getAlgo().shortestPath(trainer.get_curr_node(),_gm.getPokemons().get(i).getEdge().getSrc());
                }else {
                    path = _gm.getAlgo().shortestPath(trainer.get_dest(),_gm.getPokemons().get(i).getEdge().getSrc());
                }
                path.add(_gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getDest()));
                double dist = _gm.getGraph().getNode(_gm.getPokemons().get(i).getEdge().getSrc()).getWeight() + _gm.getPokemons().get(i).getEdge().getWeight();
                trainersToPokemonsDist.add(new TrainerToPath(trainer.getID(), _gm.getPokemons().get(i).getEdge().getSrc(), dist, path, _gm.getPokemons().get(i)));
            }
        }

        HashSet<Integer> trainersFilled = new HashSet<>();
        HashSet<Pokemon> pokemonFilled = new HashSet<>();
        HashSet<edge_data> edges = new HashSet<>();


        TrainerToPath trainerToPokemon = null;
        while(pokemonFilled.size()< _gm.getPokemons().size()&&trainersFilled.size()< _gm.getTrainers().size()&&trainersToPokemonsDist.size()!=0){
            trainerToPokemon  = trainersToPokemonsDist.remove();
            //check if the trainer already has a new path or not
            //check if pokemon is already chosen
            //check if the queue isn't empty
            //check if the distance is not max Integer, meaning the graph is not scc and trainer cant visit pokemon
            TrainerToPath finalTrainerToPokemon = trainerToPokemon;
            while((trainersToPokemonsDist.size()>0&&trainersFilled.contains(trainerToPokemon.getSrc())||pokemonFilled.contains(trainerToPokemon.get_pokemon())||
                    trainerToPokemon.getWeight()==Integer.MAX_VALUE|| edges.contains(trainerToPokemon.get_pokemon().getEdge()))&&trainersToPokemonsDist.size()!=0){
                try{
                    trainerToPokemon = trainersToPokemonsDist.remove();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }



            //check the path to pokemon isn't contained in path that other trainers chose
//            boolean flag = true;
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
            edges.add(trainerToPokemon.get_pokemon().getEdge());
            _gm.updateTrainerPath(trainerToPokemon.get_path(),trainerToPokemon.getSrc());
            _gm.getTrainer(trainerToPokemon.getSrc()).setNextPoke(trainerToPokemon.get_pokemon());
//            trainerToPokemon.get_pokemon().set_pokemonTrainer(_gm.getTrainer(trainerToPokemon.getSrc()));
        }
        //System.out.println(trainerToPokemon._path.toString());
    }

    /**
     * contains data for path from trainer to a pokemon
     */
     static class TrainerToPath extends EdgeData {
        private List<node_data> _path;
        private Pokemon _pokemon;
        public TrainerToPath(int src, int dest, double w, List<node_data> path, Pokemon pokemonId) {
            super(src, dest, w);
            this._path = path;
            this._pokemon =pokemonId;
        }

        public List<node_data> get_path() {
            return _path;
        }

        public void set_path(List<node_data> _path) {
            this._path = _path;
        }

        public Pokemon get_pokemon() {
            return _pokemon;
        }

        public void set_pokemon(Pokemon _pokemon) {
            this._pokemon = this._pokemon;
        }

        @Override
        public String toString() {
            return "TrainerToPath{" +
                    "_path=" + _path +
                    ", _pokemon=" + _pokemon +
                    '}';
        }
    }

    /**
     * Comparator for edge_data interface based on edge size
     */
    static class CompareToEdge implements Comparator<edge_data> {
        @Override
        public int compare(edge_data o1, edge_data o2) {
            return (int)(o1.getWeight()-o2.getWeight());
        }
    }
}
