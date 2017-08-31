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

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Jimi Vacarians on 25/07/2016.
 */
public class Team {
    private String name = "";
    public void setName(String str){
        this.name = str;
    }
    public String getName(){
        return this.name;
    }

    private List<Player> players = new ArrayList<>();
    public List<Player> getPlayers(){
        return this.players;
    }

    private Color4f color = new Color4f(0);
    public Color4f getColor(){
        return this.color;
    }
    public void setColor(Color4f c){
        this.color = c;
    }

    private Vec3 spawn = new Vec3();
    public Vec3 getSpawn(){
        return this.spawn;
    }
    public void setSpawn(Vec3 pos){
        this.spawn.set(pos);
    }

    public Team(){

    }
}
