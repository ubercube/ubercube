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

package fr.veridiangames.core.network;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public interface NetworkableClient
{
	public GameCore getCore();
	public void log(String msg);
	public void console(String msg);
	public void playSound(AudioSource audioSource);

	public void send(Packet packet, Protocol protocol);
	// public void tcpSend(Packet packet);
	// public void udpSend(Packet packet);

	public void setConnected(boolean connected);
	public boolean isConnected();

	public int getID();
}
