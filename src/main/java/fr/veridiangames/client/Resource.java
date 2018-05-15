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

package fr.veridiangames.client;

import fr.veridiangames.core.GameCore;

import java.io.File;

import static fr.veridiangames.core.utils.FileUtils.fileExist;

public class Resource
{
    public static String getResource(String path)
    {
        String fullPath = GameCore.RESOURCES_PATH + path;
        System.out.println(new File(fullPath).getAbsolutePath());
        if (!fileExist(fullPath))
            throw new RuntimeException("Unknown resource: " + fullPath);
        return (fullPath);
    }
}
