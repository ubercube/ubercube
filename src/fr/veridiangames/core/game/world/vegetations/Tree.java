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

package fr.veridiangames.core.game.world.vegetations;

import fr.veridiangames.core.game.world.Block;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marc on 21/06/2016.
 */
public class Tree
{
    public static void oakTree(World world, int x, int y, int z) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 9; k++) {
                    float ii = i - 4.5f;
                    float jj = j - 4.5f;
                    float kk = k - 4.5f;
                    float l = Mathf.sqrt(ii * ii + jj * jj + kk * kk);


                    if (l < 4.5f) {
                        if(Mathf.sqrt(ii * ii + (jj + 1) * (jj + 1) + kk * kk) >= 4.5f){
                            world.addBlock(x + (int)ii, y + (int)jj + 9, z + (int)kk, new Color4f(0.9f, 0.9f, 0.98f).add(world.getWorldGen().getRandom() * 0.02f).getARGB());
                        }else{
                            world.addBlock(x + (int)ii, y + (int)jj + 9, z + (int)kk, Block.LEAF.copy().add(Mathf.random() * 0.1f).getARGB());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            world.addBlock(x, y + i, z, Block.WOOD.copy().add(Mathf.random() * 0.05f).getARGB());
        }
    }

    public static void firTree(World world, int x, int y, int z) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 13; j++) {
                for (int k = 0; k < 9; k++) {
                    float ii = i - 4f;
                    float jj = j - 3f;
                    float kk = k - 4f;
                    float l = Mathf.sqrt(ii * ii + kk * kk);
                    float size = 1;

                    size -= (float)j / 13.0f;

                    if (l < 4.5f * size) {
                        //world.addBlock(x + (int)ii, y + (int)jj + 7, z + (int)kk, Block.LEAF.copy().add(Mathf.random() * 0.1f).getARGB());
                        if(Mathf.sqrt(ii * ii + (jj + 1) * (jj + 1) + kk * kk) >= 4.5f){
                            world.addBlock(x + (int)ii, y + (int)jj + 9, z + (int)kk, new Color4f(0.9f, 0.9f, 0.98f).add(world.getWorldGen().getRandom() * 0.02f).getARGB());
                        }else{
                            if(world.getWorldGen().getRandom() > 0.05f){
                                world.addBlock(x + (int)ii, y + (int)jj + 9, z + (int)kk, new Color4f(0.9f, 0.9f, 0.98f).add(world.getWorldGen().getRandom() * 0.02f).getARGB());
                            }else{
                                world.addBlock(x + (int)ii, y + (int)jj + 9, z + (int)kk, Block.LEAF.copy().add(Mathf.random() * 0.1f).getARGB());
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 13; i++) {
            world.addBlock(x, y + i, z, Block.WOOD.copy().add(Mathf.random() * 0.05f).getARGB());
        }
    }
}
