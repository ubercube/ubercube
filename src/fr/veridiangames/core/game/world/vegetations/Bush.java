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

/**
 * Created by Jimi Vacarians on 05/08/2016.
 */
public class Bush {
    public static void bush(World world, int x, int y, int z) {
        float random = world.getWorldGen().getRandom() * 0.5f + 0.5f;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    float ii = i - 4.5f;
                    float jj = j - 4.5f;
                    float kk = k - 4.5f;
                    float l = Mathf.sqrt(ii * ii + jj * jj + kk * kk);

                    if (l < random * 4f) {
                        world.addBlock(x + (int)ii, y + (int)jj, z + (int)kk, Block.BUSH.copy().add(Mathf.random() * 0.1f).getARGB());
                    }
                }
            }
        }
    }
}
