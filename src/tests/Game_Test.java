package tests;

import api.Tarjan;
import gameClient.GameLogic.Game;
import gameClient.GameLogic.GameManager;
import gameClient.GameLogic.Pokemon;
import gameClient.GameLogic.ShortestPathAlgo;
import gameClient.gui.GamePanel;
import gameClient.gui.MyFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Game_Test extends BaseTest{
    GameManager _gm = new GameManager();

    @BeforeEach
    void BeforeEach(){
        _gm = new GameManager();
        File f = new File(PATH_GRAPH_DATA+"A4");
        f.renameTo(new File(f.getParent(),"A4Backup"));
        f = new File(PATH_GRAPH_DATA+"A0_notSCC");
        f.renameTo(new File(f.getParent(),"A4"));
    }

    @Test
    void GameWithNotSCCGraph() {

        Game g = new Game(true);
        try {
            g.startGame(new GamePanel(new MyFrame()),16,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        g.gameStop();
        int currNodeTrainer_One = _gm.getTrainers().get(0).get_curr_node(), trainerOne_SCC;
        int currNodeTrainer_Two = _gm.getTrainers().get(1).get_curr_node(), trainerTwo_SCC;
        if((Tarjan.getSccNodes().get(0).contains(currNodeTrainer_Two)&&Tarjan.getSccNodes().get(0).contains(currNodeTrainer_One))||
                (Tarjan.getSccNodes().get(1).contains(currNodeTrainer_Two)&&Tarjan.getSccNodes().get(1).contains(currNodeTrainer_One)))
            fail();
        if(Tarjan.getSccNodes().get(0).contains(currNodeTrainer_Two)){
            trainerTwo_SCC = 0;
            trainerOne_SCC=1;
        }else{
            trainerTwo_SCC = 1;
            trainerOne_SCC=0;
        }
        List<Pokemon> pokemon = _gm.getPokemons();
        List<Pokemon> scc_1 = new ArrayList<>();
        List<Pokemon> scc_2 = new ArrayList<>();
        for (Pokemon p :
                pokemon) {
            if (Tarjan.getSccNodes().get(0).contains(p.getEdge().getSrc())) {
                scc_1.add(p);
            }else{
                scc_2.add(p);
            }
        }
        _gm.setPokemons(new ArrayList<Pokemon>());
        List<Pokemon> pokemonsToAdd = new ArrayList<>();
        pokemonsToAdd.addAll(scc_1);
        _gm.setPokemons(pokemonsToAdd);
        ShortestPathAlgo.findShortestForAgents(_gm);
        assertTrue(Tarjan.getSccNodes().get(trainerOne_SCC).contains(_gm.getTrainers().get(0).getEndNodeId()));
        assertTrue(Tarjan.getSccNodes().get(trainerTwo_SCC).contains(_gm.getTrainers().get(1).getEndNodeId()));
        _gm.setPokemons(new ArrayList<Pokemon>());
        pokemonsToAdd = new ArrayList<>();
        pokemonsToAdd.addAll(scc_2);
        _gm.setPokemons(pokemonsToAdd);
        ShortestPathAlgo.findShortestForAgents(_gm);
        assertTrue(Tarjan.getSccNodes().get(trainerOne_SCC).contains(_gm.getTrainers().get(0).getEndNodeId()));
        assertTrue(Tarjan.getSccNodes().get(trainerTwo_SCC).contains(_gm.getTrainers().get(1).getEndNodeId()));
    }

    @Test
    void GameWithNotSCCGraphAfter20Seconds() {

        Game g = new Game(true);
        try {
            g.startGame(new GamePanel(new MyFrame()),16,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
            int currNodeTrainer_Two = _gm.getTrainers().get(1).get_curr_node(), trainerTwo_SCC;
            int currNodeTrainer_One = _gm.getTrainers().get(0).get_curr_node(), trainerOne_SCC;
            if((Tarjan.getSccNodes().get(0).contains(currNodeTrainer_Two)&&Tarjan.getSccNodes().get(0).contains(currNodeTrainer_One))||
                    (Tarjan.getSccNodes().get(1).contains(currNodeTrainer_Two)&&Tarjan.getSccNodes().get(1).contains(currNodeTrainer_One)))
                fail();
            if(Tarjan.getSccNodes().get(0).contains(currNodeTrainer_Two)){
                trainerTwo_SCC = 0;
                trainerOne_SCC=1;
            }else{
                trainerTwo_SCC = 1;
                trainerOne_SCC=0;
            }
            Thread.sleep(20000);
            g.gameStop();
            assertTrue(Tarjan.getSccNodes().get(trainerOne_SCC).contains(_gm.getTrainers().get(0).get_curr_node()));
            assertTrue(Tarjan.getSccNodes().get(trainerTwo_SCC).contains(_gm.getTrainers().get(1).get_curr_node()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @AfterEach
    void AfterEach(){
        File f = new File(PATH_GRAPH_DATA+"A4");
        f.renameTo(new File(f.getParent(),"A0_notSCC"));
        f = new File(PATH_GRAPH_DATA+"A4Backup");
        f.renameTo(new File(f.getParent(),"A4"));
    }
}
