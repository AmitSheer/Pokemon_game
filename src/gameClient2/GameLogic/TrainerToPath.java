package gameClient2.GameLogic;

import api.EdgeData;
import api.node_data;

import java.util.List;

public class TrainerToPath extends EdgeData {
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
}
