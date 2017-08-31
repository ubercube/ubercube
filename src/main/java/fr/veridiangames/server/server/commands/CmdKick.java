/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.server.server.commands;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.KickPacket;
import fr.veridiangames.server.server.NetworkServer;

public class CmdKick extends Command
{
    public CmdKick()
    {
        super("kick", "Kicks a player out of the server.");
    }

    @Override
	public void process(NetworkServer server, String[] params)
    {
        if (params.length == 2)
        {
            try
            {
                int id = this.getPlayerByName(params[1]);
                if (id == -1)
                {
                    server.log("Player not found !");
                    return;
                }
                server.tcpSendToAll(new KickPacket(id, "You where kicked by the server !"));
                server.log(params[1] + " has been kicked !");
                GameCore.getInstance().getGame().remove(id);
            } catch (Exception e)
            {
                server.log("Player ID not found !");
            }
        } else
        {
            server.log("Incorrect syntax: kick [username]");
        }
    }
}
