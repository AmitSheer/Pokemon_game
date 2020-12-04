package gameClient2;

import api.NodeData;
import api.edge_data;
import api.geo_location;
import api.node_data;

import java.util.List;


/**
 * goes to catch all pokemon
 */
public class PokemonTrainer extends NodeData {
    private edge_data _curr_edge;
    private List<node_data> _pathToPokemon;
    private double _speed;

    public PokemonTrainer(int key, geo_location e) {
        super(key, e);
    }

    public void setPathToPokemon(List<node_data> pathToPokemon) {
        this._pathToPokemon = pathToPokemon;
    }

    public void moveTrainer(){
        if(_pathToPokemon.size()!=0) _pathToPokemon.remove(0);
    }

    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        } else {

            ans = this._curr_edge.getDest();
        }
        return ans;
    }
}
