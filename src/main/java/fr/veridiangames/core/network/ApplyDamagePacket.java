package fr.veridiangames.core.network;

import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

public class ApplyDamagePacket extends Packet
{
    private int id;
    private int damage;

    public ApplyDamagePacket()
    {
        super(APPLY_DAMAGE);
    }

    public ApplyDamagePacket(Player p, int damage)
    {
        super(APPLY_DAMAGE);

        data.put(p.getID());
        data.put(damage);

        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        this.id = buffer.getInt();
        this.damage = buffer.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {

    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        ClientPlayer p = client.getCore().getGame().getPlayer();
        p.setLife(p.getLife() - this.damage);
    }
}
