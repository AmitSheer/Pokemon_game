package gameClient.gui;

import api.*;
import gameClient.GameLogic.Game;
import gameClient.GameLogic.GameManager;
import gameClient.GameLogic.Pokemon;
import gameClient.GameLogic.PokemonTrainer;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GamePanel extends JPanel {

    private static String _pikachuImgPath = "assets/Pikachu.png";
    private static String _miauImgPath = "assets/Miau.png";
    private static String _agentsImgPath = "assets/Boaz.png";
    private static String _backgroundImgPath = "assets/grass.jpg";
    private static String _townImgPath = "assets/town.jpg";
    private static Image _pika, _miau, _agent,_background, _town;
    private static MyFrame _frame;
    private static Game game;
    private int _infoText;
    private GameManager _gm;
    private Range2Range _w2f;
    private boolean showTotal;

    public GamePanel() throws IOException {
        super();
        this.setOpaque(false);
        this.setBackground(Color.WHITE);
        //BufferedImage img = ImageIO.read(getAssets(),_pikachuImgPath);
        _pika = ImageIO.read(new File(_pikachuImgPath));
        _miau = ImageIO.read(new File(_miauImgPath));
        _agent = ImageIO.read(new File(_agentsImgPath));
        _town = ImageIO.read(new File(_townImgPath));
        _gm = new GameManager();
    }

    public GamePanel(MyFrame frame) throws IOException {
        _frame = frame;
        this.setOpaque(false);
        this.setBackground(Color.WHITE);
        this.setSize(frame.getWidth(), frame.getHeight());
        _pika = ImageIO.read(new File(_pikachuImgPath));
        _miau = ImageIO.read(new File(_miauImgPath));
        _agent = ImageIO.read(new File(_agentsImgPath));
        _background = ImageIO.read(new File(_backgroundImgPath));
        _town = ImageIO.read(new File(_townImgPath));
        _gm = new GameManager();
        showTotal= false;
    }

    public void startGame(int scenario,int id,boolean isCloseWhenDone){
        try{
            if(game.isRunning()){
                game.gameStop();
            }
        }catch(Exception ignore ){}
        game = new Game(isCloseWhenDone);
        showTotal = false;
        game.startGame(this,scenario,id);

    }


    public void update(GameManager gm) {
        this._gm = gm;
        updatePanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g2d = bufferedImage.createGraphics();
        super.paintComponent(g);
        int w = this.getWidth();
        int h = this.getHeight();
        g2d.clearRect(0, 0, w, h);
        g2d.fillRect(0,0,w,h);
        Dimension d = _frame.getSize();
        updatePanel();
        g2d.drawImage(_background,0,0,d.width,d.height,null);
        this._infoText = (this.getHeight() / 20)*5;
        drawInfo(g2d);
        drawGraph(g2d);
        drawAgants(g2d);
        drawPokemons(g2d);
        g.drawImage(bufferedImage,0,0,null);
    }

    private void updatePanel() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight()-50, _infoText);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _gm.getGraph();
        _w2f = GameManager.w2f(g, frame);
    }

    private void drawInfo(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.white);
        g2D.setFont(new Font(Font.MONOSPACED, Font.BOLD, (this.getHeight() + this.getWidth()) / 80));
        int x0 = this.getWidth() / 70;
        int y0 = this.getHeight() / 20;

        if(showTotal){
            g2D.drawString("Total: " +_gm.getGameStatus().get_grade(), (int) x0 * 5, (int) y0);
        }else{
            g2D.drawString("Time to finish: " +_gm.getTime(), (int) x0 * 5, (int) y0);
        }
        y0 = y0 +this.getHeight() / 20;
        int counter=0;
        for (PokemonTrainer trainer : _gm.getTrainers()) {
            counter++;
            g2D.drawString("Agent" + trainer.getID() + " : " + trainer.get_money(), (int) x0 * 5, (int) y0);
            y0 = y0 +this.getHeight() / 20;
            if(counter%3==0){
                y0 =  this.getHeight()/20+this.getHeight()/20;
                x0 = x0 * 5+x0;
            }
        }
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _gm.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        g.setColor(Color.white);
        while (iter.hasNext()) {
            node_data n = iter.next();
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                drawEdge(e, g);
            }
        }
        iter = gg.getV().iterator();
        g.setColor(Color.red);
        while(iter.hasNext()){
            drawNode(iter.next(), 5, g);
        }
    }

    private void drawPokemons(Graphics g) {
        List<Pokemon> fs = _gm.getPokemons().stream().collect(Collectors.toList());
        if (fs != null) {
            Iterator<Pokemon> itr = fs.iterator();
            while (itr.hasNext()) {
                Pokemon f = itr.next();
                GeoLocation c = new GeoLocation(f.getLocation().toString());
                int r = 10;
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(f.getLocation());
                    if(f.getType()<0) {
                        g.drawImage(_pika.getScaledInstance(2 * r+this.getWidth()/100, 2 * r+this.getHeight()/100, Image.SCALE_SMOOTH), (int) fp.x() - r-this.getWidth()/100, (int) fp.y() - r-this.getHeight()/200, null);
                    }else{
                        g.drawImage(_miau.getScaledInstance(2 * r+this.getWidth()/100, 2 * r+this.getHeight()/100, Image.SCALE_SMOOTH), (int) fp.x() - r-this.getWidth()/100, (int) fp.y() - r-this.getHeight()/200, null);
                    }
                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<PokemonTrainer> rs = _gm.getTrainers();
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.drawImage(_agent.getScaledInstance(2 * r+this.getWidth()/100, 2 * r+this.getHeight()/100, Image.SCALE_SMOOTH), (int) fp.x() -r-_frame.getWidth()/200, (int) fp.y() - r-_frame.getHeight()/200, null);
                try {
                   // g.drawString("" + rs.get(i).getID(), (int) fp.x(), (int) fp.y() - 4 * r);
                    //g.drawString("" + rs.get(i).getNextNode(), (int) fp.x(), (int) fp.y() - 3 * r);
                }catch (Exception Ignore){}
            }
            i++;
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.drawImage(_town.getScaledInstance(2 * r+this.getWidth()/100, 2 * r+this.getHeight()/100, Image.SCALE_SMOOTH),(int)fp.x()-(r+this.getWidth()/300), (int) fp.y()-(r+this.getHeight()/200),null);
        //g.fillOval((int) fp.x() - r, (int) fp.y() - r, this.getWidth()/100, this.getHeight()/100);
//        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _gm.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
    }

    public boolean getShowTotal() {
        return showTotal;
    }

    public void setShowTotal(boolean showTotal) {
        this.showTotal = showTotal;
    }
}
