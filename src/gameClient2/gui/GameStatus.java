package gameClient2.gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameStatus {
    private int _pokemonNum;
    private int _moves;
    private double _grade;
    private int _gameLevel;
    private String _graph;
    private int _trainersNum;
    private boolean _isLogin;
    private int _id;
    private int _maxUserLevel;

    public GameStatus(String data) {
        update(data);
    }

    public GameStatus() {

    }

    public void update(String data){
        JsonObject jsonData = new JsonParser().parse(data).getAsJsonObject().get("GameServer").getAsJsonObject();
        set_pokemonNum(jsonData.get("pokemons").getAsInt());
        set_isLogin(jsonData.get("is_logged_in").getAsBoolean());
        set_moves(jsonData.get("moves").getAsInt());
        set_grade(jsonData.get("grade").getAsDouble());
        set_gameLevel(jsonData.get("game_level").getAsInt());
        set_maxUserLevel(jsonData.get("max_user_level").getAsInt());
        set_id(jsonData.get("id").getAsInt());
        set_graph(jsonData.get("graph").getAsString());
        set_trainersNum(jsonData.get("agents").getAsInt());
    }

    public int get_pokemonNum() {
        return _pokemonNum;
    }

    public void set_pokemonNum(int _pokemonNum) {
        this._pokemonNum = _pokemonNum;
    }

    public int get_moves() {
        return _moves;
    }

    public void set_moves(int _moves) {
        this._moves = _moves;
    }

    public double get_grade() {
        return _grade;
    }

    public void set_grade(double _grade) {
        this._grade = _grade;
    }

    public int get_gameLevel() {
        return _gameLevel;
    }

    public void set_gameLevel(int _gameLevel) {
        this._gameLevel = _gameLevel;
    }

    public String get_graph() {
        return _graph;
    }

    public void set_graph(String _graph) {
        this._graph = _graph;
    }

    public int get_trainersNum() {
        return _trainersNum;
    }

    public void set_trainersNum(int _trainersNum) {
        this._trainersNum = _trainersNum;
    }

    public boolean is_isLogin() {
        return _isLogin;
    }

    public void set_isLogin(boolean _isLogin) {
        this._isLogin = _isLogin;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_maxUserLevel() {
        return _maxUserLevel;
    }

    public void set_maxUserLevel(int _maxUserLevel) {
        this._maxUserLevel = _maxUserLevel;
    }

    @Override
    public String toString() {
        return "GameStatus{" +
                "pokemons=" + _pokemonNum +
                ", is_logged_in:" + _isLogin +
                ", moves:" + _moves +
                ",\n\n grade:" + _grade +
                ", game_level:" + _gameLevel +
                ", _maxUserLevel=" + _maxUserLevel +
                ", _id=" + _id +
                ", graph:" + _graph +
                ", agents:" + _trainersNum +
                '}';
    }
}
