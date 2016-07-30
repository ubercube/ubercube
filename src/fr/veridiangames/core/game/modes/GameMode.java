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

package fr.veridiangames.core.game.modes;

import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec4;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.DataBuffer;

import java.util.List;

/**
 * Created by Jimi Vacarians on 24/07/2016.
 */
public interface GameMode
{
    void update();

    Vec3 getPlayerSpawn(Player p);
    Team getPlayerTeam(Player p);

    /**
     *  All event need a NetworkableServer param
     */

    void onPlayerConnect(Player p, NetworkableServer server);
    void onPlayerDisconnect(Player p, NetworkableServer server);
    void onPlayerDeath(Player p, NetworkableServer server);
    void onPlayerSpawn(Player p, NetworkableServer server);
}
