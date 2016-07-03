package fr.veridiangames.core.network.packets;

import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by trexr on 03/07/2016.
 */
public class KickPacket extends Packet {

    private int id;

    public KickPacket()

    {
        super(KICK);
    }

    public KickPacket(int id) {
        super(KICK);
        this.id = id;
        data.put(id);
    }

    public KickPacket(KickPacket packet) {
        super(KICK);
        data.put(packet.getId());
    }

    @Override
    public void read(DataBuffer buffer) {
        this.id = buffer.getInt();
    }

    @Override
    public void process(NetworkableServer server, InetAddress address, int port) {

    }

    @Override
    public void process(NetworkableClient client, InetAddress address, int port) {
        String name = ((ECName) client.getCore().getGame().getEntityManager().get(id).get(EComponent.NAME)).getName();
        client.getCore().getGame().remove(id);
        client.log(name + " kicked...");
    }

    public int getId() {
        return id;
    }
}
