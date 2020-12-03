package gameClient;

import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Ex2 implements Runnable{
    public static void main(String[] args) {

    }
    @Override
    public void run() {
        int scenario = 2;
        GameManager gameManager = new GameManager();
        game_service game = Game_Server_Ex2.getServer(scenario);
        gameManager.setPokemons(game.getPokemons());
        gameManager.setTrainers(JsonParser.parseString(game.toString()).getAsJsonObject().get("GameServer").getAsJsonObject().get("agents").getAsInt());
    }
}
