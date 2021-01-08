import api.DWGraph_Algo;
import api.GraphParser;
import api.Tarjan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.stream.JsonReader;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RunTimeTest {
    public static void main(String[] args) {
        List<String> graphs = Arrays.asList("G_10_80_0.json", "G_100_800_0.json", "G_1000_8000_0.json",
                "G_10000_80000_0.json","G_20000_160000_0.json","G_30000_240000_0.json");
        List<Integer> sccNumber = Arrays.asList(1,1,1,12,16,30);
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        JsonElement json = new JsonObject();
        DWGraph_Algo algo = new DWGraph_Algo();
        for (int i=0;i< graphs.size();i++) {
            JsonObject g = new JsonObject();
            algo.load("./Graphs_no_pos/"+graphs.get(i));
            System.out.println(graphs.get(i));
            double t1 = ((double)new Date().getTime())/1000;
            algo.shortestPath(1,5);
            double t2 = ((double)new Date().getTime())/1000;
            g.addProperty("shortest_path", t2-t1);
            t1 = ((double)new Date().getTime())/1000;
            Tarjan a = new Tarjan();
            List<Integer> comp;
            comp = Tarjan.connectedComponent(algo.getGraph(), algo.getGraph().getNode(1));
            t2 = ((double)new Date().getTime())/1000;
            g.addProperty("java_connected_component", (t2-t1));
            g.addProperty("connected_component", comp.contains(1));
            t1 = ((double)new Date().getTime())/1000;
            List<List<Integer>> scc = Tarjan.connectedComponents(algo.getGraph());
            t2 = ((double)new Date().getTime())/1000;
            g.addProperty("java_connected_components", (t2-t1));
            g.addProperty("connected_components", scc.size()==sccNumber.get(i));
            json.getAsJsonObject().add(graphs.get(i), gson.toJsonTree(g));

        }
        try{
            FileOutputStream outputStream = new FileOutputStream("results.json");
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
            gson.toJson((JsonElement) json,writer);
            writer.close();
        }catch (Exception exception){
            System.out.println("asdasd");
        }
    }
}
