package gameClient.GameLogic;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.gui.GamePanel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * responsible to managing the game run
 */
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
        _gm = new GameManager();
    }

    public void startGame(GamePanel panel,int scenario,int id){
        game = Game_Server_Ex2.getServer(scenario);
        _gp = panel;
        _gm = new GameManager();
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
        dt=108;
        System.out.println(game.getAgents());
        game.startGame();
        Date a = new Date();
        a.setTime(game.timeToEnd());
        while(game.isRunning()) {
            a.setTime(game.timeToEnd());
            _gm.setTime(a);
            badPokemon.removeIf(p -> !_gm.getPokemons().contains(p));
            moveTrainers(game, gg);
            try {
                _gp.repaint();
                Thread.sleep(dt);
                dt = 108;
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

        if(badPokemon.size()>0) {
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
        List<Pokemon> ffs = GameManager.json2Pokemons(fs,gg);
        ffs.forEach(p->GameManager.updateEdge(p,gg));
        if(_gm.getPokemons().equals(ffs))
            ShortestPathAlgo.findShortestForAgents(_gm);
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
//        if(finishedPath)
//            ShortestPathAlgo.findShortestForAgents(_gm);
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
        markPokemonGroups();
        try {
            String info = game.toString();
            JSONObject line= new JSONObject(info);;
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            loadTrainers(rs);
            GameManager.getTrainers(game.getAgents(),gg);
        }
        catch (JSONException e) {e.printStackTrace();}
    }

    /**
     * finds pokemons in close proximity, by the number of edges between them
     */
    private void markPokemonGroups(){
        for (Pokemon currentPokemon : _gm.getPokemons()) {
            for (Pokemon pokemon : _gm.getPokemons()) {
                if(!pokemon.equals(currentPokemon)){
                    List<node_data> dist = _gm.getAlgo().shortestPath(pokemon.getEdge().getSrc(),currentPokemon.getEdge().getSrc());
                    if(dist!=null) {
                        if (dist.size() < 3) {
                            currentPokemon.getClosePokemons().add(pokemon);
                            pokemon.getClosePokemons().add(currentPokemon);
                        }
                    }
                }
            }
        }
    }

    /**
     * adds trainers to the graph
     * if graph is connected then it is added according to markPokemonGroups
     * if graph is connected then it is added based on the scc and other factors
     * @param rs number of trainers to add
     */
    private void loadTrainers(int rs) {
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
            int index = 0;
            PriorityQueue<List<Integer>> sccBySize = new PriorityQueue<>(new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> o1, List<Integer> o2) {
                    return o2.size()- o1.size();
                }
            });
            sccBySize.addAll(Tarjan.getSccNodes());
            List<List<Integer>> connections = new LinkedList<>(sccBySize.stream().collect(Collectors.toList()));
            //this for loop runs under the assumption that there are at least enough agents for all scc
            int pokemonIndex;
            for (; index < sccBySize.size() && index < rs; index++) {
                pokemonIndex = -1;
                for (Pokemon pok : pokemonQueue) {
                    if (connections.get(index).contains(pok.getEdge().getSrc())&&!pok.isMarked()) {
                        pokemonQueue.removeIf(p->pok.getClosePokemons().contains(p));
                        pokemonIndex = pok.getEdge().getSrc();
                        game.addAgent(pok.getEdge().getSrc());
                        pok.setMarked(true);
                        break;
                    }
                }
                if (pokemonIndex == -1 && connections.get(index).size() > 1) {
                    game.addAgent(connections.get(index).get(0));
                }
            }
            pokemonIndex = -1;
            //this will add agents to the graph according to the relative size of scc to the size of the graph
            for (List<Integer> scc : connections) {
                int numOfAgents = Math.floorDiv(scc.size(),_gm.getGraph().nodeSize());
                for (int i = 0; i < numOfAgents; i++) {
                    for (Pokemon p : _gm.getPokemons()) {
                        if (!p.isMarked() && scc.contains(p.getEdge().getSrc())) {
                            pokemonIndex = p.getEdge().getSrc();
                            game.addAgent(p.getEdge().getSrc());
                            p.setMarked(true);
                            break;
                        }
                    }
                    if (pokemonIndex == -1) {
                        int ind = index%scc.size();
                        game.addAgent(scc.get(ind));
                    }
                    index++;
                }
            }
            for (;  index < rs; index++) {
                int ind = index%connections.get(0).size();
                game.addAgent(connections.get(0).get(ind));
            }
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
