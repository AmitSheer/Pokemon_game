package gameClient2.gui;

import api.*;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient2.GameManager;
import gameClient2.Pokemon;
import gameClient2.PokemonTrainer;
import gameClient2.util.Range;
import gameClient2.util.Range2D;
import gameClient2.util.Range2Range;

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
    private static String _pikachuImgPath = "src/gameClient2/assets/Pikachu.png";
    private static String _miauImgPath = "src/gameClient2/assets/Miau.png";
    private static String _agentsImgPath = "src/gameClient2/assets/Boaz.png";
    private static BufferedImage _pika, _miau, _agent;
    private int _ind;
    private GameManager _gm;
    private Range2Range _w2f;

    public GamePanel() throws IOException {
        super();
        this.setOpaque(false);
        this.setBackground(Color.WHITE);
        //BufferedImage img = ImageIO.read(getAssets(),_pikachuImgPath);
        _pika = ImageIO.read(new File(_pikachuImgPath));
        _miau = ImageIO.read(new File(_miauImgPath));
        _agent = ImageIO.read(new File(_agentsImgPath));
    }

    public void update(GameManager gm) {
        this._gm = gm;
        updatePanel();
    }
    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g2d = bufferedImage.createGraphics();
        int w = this.getWidth();
        int h = this.getHeight();
        g2d.clearRect(0, 0, w, h);
        g2d.fillRect(0,0,w,h);
        updatePanel();
        drawGraph(g2d);
        drawPokemons(g2d);
        drawAgants(g2d);
        drawInfo(g2d);
        Graphics2D g2dComponent = (Graphics2D) g;

        g2dComponent.drawImage(bufferedImage, null, 0, 0);
    }

    private void updatePanel() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _gm.getGraph();
        _w2f = GameManager.w2f(g, frame);
    }

    private void drawInfo(Graphics g) {
        List<String> str = _gm.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _gm.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        g.setColor(Color.gray);
        while (iter.hasNext()) {
            node_data n = iter.next();
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                drawEdge(e, g);
            }
        }
        iter = gg.getV().iterator();
        g.setColor(Color.blue);

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
                GeoLocations c = new GeoLocations(f.getLocation().toString());
                int r = 10;
                g.setColor(Color.green);
//                if (f.getType() < 0) {
//                    //g.setColor(Color.orange);
//                }
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(f.getLocation());
                    if(f.getType()<0) {
                        g.drawImage(_pika.getScaledInstance(2 * r, 2 * r, Image.SCALE_SMOOTH), (int) fp.x() - r, (int) fp.y() - r, null);
                    }else{
                        g.drawImage(_miau.getScaledInstance(2 * r, 2 * r, Image.SCALE_SMOOTH), (int) fp.x() - r, (int) fp.y() - r, null);
                    }
                    //g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<PokemonTrainer> rs = _gm.getTrainers();
        //	Iterator<OOP_Point3D> itr = rs.iterator();

        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.drawImage(_agent.getScaledInstance(2 * r, 2 * r, Image.SCALE_SMOOTH), (int) fp.x() - r, (int) fp.y() - r, null);
                //g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        //g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _gm.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }
}
