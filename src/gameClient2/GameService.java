package gameClient;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.game_service;

public class GameService implements game_service {
    private int _gameType;
    private dw_graph_algorithms _algo;
    private World _world;
    public  GameService(int type){
        _gameType = type;
        _algo = new DWGraph_Algo();
    }

    @Override
    public String getGraph() {
        return _algo.getGraph().toString();
    }

    @Override
    public String getPokemons() {
        return _world.getPokemons().toString();
    }

    @Override
    public String getAgents() {
        return null;
    }

    @Override
    public boolean addAgent(int start_node) {
        return false;
    }

    @Override
    public long startGame() {
        return 0;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public long stopGame() {
        return 0;
    }

    @Override
    public long chooseNextEdge(int id, int next_node) {
        return 0;
    }

    @Override
    public long timeToEnd() {
        return 0;
    }

    @Override
    public String move() {
        return null;
    }

    @Override
    public boolean login(long id) {
        return false;
    }
}
