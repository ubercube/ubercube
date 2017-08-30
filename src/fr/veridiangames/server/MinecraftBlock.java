package fr.veridiangames.server;

public class MinecraftBlock 
{
	public static int getColor(int id)
	{
		if (id < 0)
			id += 256;
		switch (id)
		{
			case 0: // air
				return 0x00000000;
			case 1: // stone
				return 0xFF4E4E52;
			case 2: // grass
				return 0xFF599532;
			case 8: // water
				return 0xFF1E398D;
			case 9: // water
				return 0xFF1E398D;
			case 10: // lava
				return 0xFFB12402;
			case 11: // lava
				return 0xFFB12402;
			case 12: // sand
				return 0xFFACA383;
			case 17: // wood
				return 0xFF4C3C24;
			case 18: // leaves
				return 0xFF337F11;
			case 24: // sand
				return 0xFFACA383;
			case 31: // tall grass
				return 0;
			case 32: // dead tree
				return 0;
			case 81: // cactus
				return 0xFF599532;
			case 161: // leaves
				return 0xFF337F11;
			case 162: // wood
				return 0xFF4C3C24;
			default:
				return 0xFFFFFFFF;
		}
	}
}
