package gameClient2;

import api.EdgeData;
import api.node_data;

import java.util.List;

public class TrainerToPath extends EdgeData {
    private List<node_data> _path;
    private Integer _pokemonId;
    public TrainerToPath(int src, int dest, double w, List<node_data> path, int pokemonId) {
        super(src, dest, w);
        this._path = path;
        this._pokemonId=pokemonId;
    }

    public List<node_data> get_path() {
        return _path;
    }

    public void set_path(List<node_data> _path) {
        this._path = _path;
    }

    public Integer get_pokemonId() {
        return _pokemonId;
    }

    public void set_pokemonId(Integer _pokemonId) {
        this._pokemonId = _pokemonId;
    }
}
