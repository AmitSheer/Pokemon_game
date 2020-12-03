package api;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GraphParser {
    public static directed_weighted_graph Json2Graph(JsonReader reader){
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        return getDirected_weighted_graph(jsonObject);
    }

    public static directed_weighted_graph Json2Graph(String json){
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return getDirected_weighted_graph(jsonObject);
    }

    @NotNull
    private static directed_weighted_graph getDirected_weighted_graph(JsonObject jsonObject) {
        directed_weighted_graph graph = new DWGraph_DS();
        for (JsonElement element : jsonObject.getAsJsonArray("Nodes")) {
            NodeData n = new NodeData(element.getAsJsonObject().get("id").getAsInt());
            String [] geoPos = element.getAsJsonObject().get("pos").getAsString().split(",");
            n.setLocation(new GeoLocations(Double.parseDouble(geoPos[0]),Double.parseDouble(geoPos[1]),Double.parseDouble(geoPos[2])));
            graph.addNode(n);
        }
        for (JsonElement element : jsonObject.getAsJsonArray("Edges")) {
            graph.connect(element.getAsJsonObject().get("src").getAsInt(),element.getAsJsonObject().get("dest").getAsInt(),element.getAsJsonObject().get("w").getAsDouble());
        }
        return graph;
    }

    public static JsonObject Graph2Json(directed_weighted_graph graph){
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
        JsonObject jsonGraph = new JsonObject();
        List<JsonObject> Nodes = new ArrayList<>();
        List<edge_data> Edges = new ArrayList<>();
        for (node_data node : graph.getV()) {
            JsonObject jsonNode = new JsonObject();
            jsonNode.addProperty("id",node.getKey());
            jsonNode.addProperty("pos",node.getLocation().toString());
            Nodes.add(jsonNode);
            Edges.addAll(graph.getE(node.getKey()));
        }
        jsonGraph.add("Nodes", gson.toJsonTree(Nodes.toArray()));
        jsonGraph.add("Edges",gson.toJsonTree(Edges));
        return jsonGraph;
    }
}
