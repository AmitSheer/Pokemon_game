package gameClient;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Client {
    private static MyFrame _frame;
    private static GameManager _manager;
    public Client(GameManager manager){
    }

    public static GameManager get_manager() {
        return _manager;
    }

    public static void set_manager(GameManager _manager) {
        Client._manager = _manager;
    }
}
