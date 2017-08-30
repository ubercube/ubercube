package fr.veridiangames.server;

public class MinecraftBlock 
{
	public static int getColor(int id, int metaID)
	{
		if (id < 0) id += 256;
		if (metaID < 0) metaID += 16;
		if (metaID >= blocks[id].length)
			return blocks[id][0];
		return blocks[id][metaID];
	}
	private static int[][] blocks = new int[][]{
			{0x0},
			{0xFF5B5B5B,0xFF674D43,0xFF6B4D42,0xFF79797A,0xFF7B7B7D,0xFF585858,0xFF59595A},
			{0xFC4F582C},
			{0xFF614630,0xFF4E3827,0xFF48331E},
			{0xFE595959},
			{0xFF725D39,0xFF44331E,0xFF817651,0xFF654832,0xFF713D22,0xFF281A0C},
			{0,0,0,0,0,0}, // sapling 6
			{0xFF3D3D3D},
			{0x881A3CA4},
			{0x881A3CA4},
			{0xFC8B4911},
			{0xFC8B4911},
			{0xFFA09A74,0xFF713A16},
			{0xFF625B5B},
			{0xFF68655B},
			{0xFF625F5C},
			{0xFF555555},
			{0xFF5B492D,0xFF42321C,0xFF887F6E,0xFF4E3E20},
			{0xFF1E4C0A,0xFF1E4C0A,0xFF1E4C0A,0xFF1E4B09},
			{0xFF828238,0xFF6A6A2A},
			{0xFF99ACAF},
			{0xFF4A5262},
			{0xFF192D5C},
			{0xFF4C4C4C},
			{0xFF9F9973,0xFF8F8A67,0xFF918B6A},
			{0xFF4A3125},
			{0xFFAF7474},
			{0xFF9A6846},
			{0xFF786559},
			{0xFF565645},
			{0},// cobweb 30
			{0,0,0}, // tall grass 31
			{0}, // dead tree 32
			{0xFF595142},
			{0xFF998159},
			{0xFFA2A2A2,0xFFAB5D28,0xFF8B3793,0xFF4C659B,0xFF8E8414,0xFF2B8A23,0xFF9F6071,0xFF303030,0xFF737979,0xFF1C556D,0xFF5E278F,0xFF1C2570,0xFF3E2514,0xFF283811,0xFF78201D,0xFF131010},
			{},
			{0},//dandelion 37
			{0,0,0,0,0,0,0,0,0}, //plants 38
			{0xFF8A6953},
			{0xFFC33538},
			{0xFFB7AD39},
			{0xFFA9A9A9},
			{0xFF787878,0xFF9F9973,0xFF725D39,0xFE595959,0xFF6A483F,0xFF595959,0xFF261316,0xFF9E9C97},
			{0xFF808080,0xFFACA67D,0xFF7B643E,0xFF626262,0xFF6C4A40,0xFF595959,0xFF201013,0xFFAFADA8},
			{0xFF6A483F},
			{0xFF703D2F},
			{0xFF5D4B30},
			{0xFF586758},
			{0xFF0E0D15},
			{0}, // torch 50
			{0}, // fire 51
			{0xFF162129},
			{0xFF75603B},
			{0xFF5C431C},
			{0}, // redstone wire 55
			{0xFF5E6567},
			{0xFF4AA19D},
			{0xFF513D25},
			{0}, // wheat crops 59
			{0xFF5D402A},
			{0xFF454545},
			{0xFC46413D},
			{0}, // sign 63
			{0}, // door 64
			{0}, // ladder 65
			{0}, // rail 66
			{0xFF5C5C5C},
			{0}, // sign mounted 68
			{0}, // level 69
			{0}, // stone pressure plate 70
			{0}, // iron door 71
			{0}, // wood pressure plate 72
			{0xFF604F4F},
			{0xFF604F4F},
			{0}, // redstone torch off 75
			{0}, // redstone torch on 76
			{0}, // button 77
			{0xFFAFB8B8},
			{0xFF5B7EBB},
			{0xFFAFB8B8},
			{0xFF094711},
			{0xFF747881},
			{0xFF94C065},
			{0xFF4C3326},
			{0xFF6A5635},
			{0xFF824D0E},
			{0xFF502625},
			{0xFF3E2F25},
			{0xFF63512E},
			{0xFF40088C},
			{0xFF8D5C12},
			{0xFFE4CDCE},
			{0xFF979393},
			{0xFFA19393},
			{0x34A4A4A4,0x5E994702,0x55710D96,0x4C184F93,0x629E9E07,0x6B4E8A00,0x4BA5244E,0x4D000000,0x284A4A4A,0x4D003B5B,0x55420078,0x5C001877,0x5C2F1800,0x5C2F4700,0x5C5F0000,0x6B000000},
			{0}, // trap 96
			{0xFF5B5B5B,0xFE595959,0xFF595959,0xFF4B4E46,0xFF4F4F4F,0xFF4E4E4E},
			{0xFF595959,0xF74D5048,0xF7505050,0xFF4E4E4E},
			{0xFC5B4535},
			{0xFC751716},
			{0xFF6D6C6A},
			{0xFFDAF0F4},
			{0xFF7C7F1E},
			{0}, // pumpkin 104
			{0}, // pumpkin 105
			{0xFF1F4F0A},
			{0xFF856C42},
			{0xFF7C554A},
			{0xFF686868},
			{0xFF5E4D47},
			{0}, // lily pad 111
			{0xFF261316},
			{0xFF251316},
			{0xFF261316},
			{0xFF6F1311},
			{0xF84E4742},
			{0xEF46423B},
			{0xFA262626},
			{0xFF672CB4},
			{0xF94F5C49},
			{0xFC8E906A},
			{0xFF08050A},
			{0xFC2B1A10},
			{0xFC4A3D28},
			{0xFF725D39,0xFF44331E,0xFF817651,0xFF654832,0xFF713D22,0xFF281A0C},
			{0xFF7B643E,0xFF4C3922,0xFF8F835A,0xFF715038,0xFF7E4425,0xFF2D1D0D},
			{0xFA5C3213},
			{0xFB908B68},
			{0xFC46534A},
			{0xFB161F1F},
			{0xF563543D},
			{0}, // wire 132
			{0xFC338B4A},
			{0xFB45331F},
			{0xFB827751},
			{0xFB664933},
			{0xFC735847},
			{0xFF5E8686},
			{0xFF4D4D4D,0xFF414D41},
			{0xFF512D23},
			{0}, // carrots 141
			{0}, // potatoes 142
			{0}, // wooden button 143
			{0xFF6E6E6E},
			{0xFF2D2C2C},
			{0xFF5C431C},
			{0}, // gold pressure plate 147
			{0}, // iron pressure plate 148
			{0}, // comparator inactive 149
			{0}, // comparator active 150
			{0}, // daylight sensor 151
			{0xFF721206},
			{0xFF533936},
			{0xFF4C4C4C},
			{0xFF9E9C97,0xFF9B9994,0xFF9C9994},
			{0xFFA4A29D},
			{0}, // cart sensor 157
			{0xFF474747},
			{0xFF8C776C,0xFF6C3819,0xFF643A48,0xFF4B485C,0xFF7C5917,0xFF454E23,0xFF6C3434,0xFF261C17,0xFF5A4741,0xFF3A3C3C,0xFF4F2F39,0xFF31273D,0xFF332217,0xFF32371C,0xFF5F281F,0xFF180F0A},
			{0x7BFFFFFF,0x7BD87F33,0x7BB24CD8,0x7B6699D8,0x7BE5E533,0x7B7FCC19,0x7BF27FA5,0x7B4C4C4C,0x7B999999,0x7B4C7F99,0x7B7F3FB2,0x7B334CB2,0x7B664C33,0x7B667F33,0x7B993333,0x7B191919},
			{0xFF1D480A,0xFF1D480A},
			{0xFF554034,0xFF2B2215},
			{0xFF753F23},
			{0xFF2A1B0C},
			{0xFF608756},
			{0}, // barrier 166
			{0}, // iron trap 167
			{0xFF477265,0xFF426B5F,0xFF283B32},
			{0xFF73867F},
			{0xFF6C540B},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // carpet 171
			{0xFF643E2C},
			{0xFF0C0C0C},
			{0xFF6E82A4},
			{0,0,0,0,0,0}, // plants 175
			{0}, // banner 176
			{0}, // banner 177
			{0}, // inversed sensor 178
			{0xFF6F3813,0xFF6E3813,0xFF703914},
			{0xFF733A14},
			{0xFF6F3813},
			{0xFF7B3F16},
			{0xFF392B19},
			{0xFF6E6445},
			{0xFF553D2A},
			{0xFF22160A},
			{0xFF5F331C},
			{0xFF3E2F1B},
			{0xFF756B49},
			{0xFF5C422E},
			{0xFF24180B},
			{0xFF65361E},
			{0}, // spruce door 193
			{0}, // birch door 194
			{0}, // jungle door 195
			{0}, // acacia door 196
			{0}, // dark wood door 197
			{0}, // end rop 198
			{0xFF3E263E},
			{0xFF635163},
			{0xFF6F516F},
			{0xFF725572},
			{0xFF735473},
			{0xFF6F516F},
			{0xFF7C5B7C},
			{0xFF989B73},
			{0xFFA9895B},
			{0xFF624D2F},
			{0xF212121D},
			{0xFF524770},
			{0xFF566860},
			{0xFF5272A9},
			{0xFF592B11},
			{0xFF51020A},
			{0xFF32020A},
			{0xFF8D907D},
			{0}, // structure void 217
			{0xFF3A3A3A},
			{0xFF817F7F},
			{0xFF7D4118},
			{0xFF703675},
			{0xFF35517F},
			{0xFF756E19},
			{0xFF226D1A},
			{0xFF7D4958},
			{0xFF313030},
			{0xFF615F5F},
			{0xFF245164},
			{0xFF5D415D},
			{0xFF3A427D},
			{0xFF554135},
			{0xFF414D2F},
			{0xFF78312F},
			{0xFF1D1B1B},
			{0xFF7D8E88},
			{0xFF54694D},
			{0xFF8A437F},
			{0xFF3F6F8C},
			{0xFF9C803A},
			{0xFF6E8425},
			{0xFF9D6879},
			{0xFF373C3E},
			{0xFF607071},
			{0xFF214F53},
			{0xFF492066},
			{0xFF1F2B5D},
			{0xFF514739},
			{0xFF4E5F2D},
			{0xFF792723},
			{0xFF2D1415},
			{0xFF8A8E8F,0xFF964100,0xFF71206A,0xFF175B85,0xFFA1750E,0xFF3F7110,0xFF8F435F,0xFF242629,0xFF53534D,0xFF0E4F5B,0xFF431568,0xFF1D1F60,0xFF402715,0xFF313D18,0xFF5F1515,0xFF05060A},
			{0xFF979898,0xFF985815,0xFF81387B,0xFF31788F,0xFF9C8524,0xFF547F1C,0xFF996679,0xFF333638,0xFF676763,0xFF186369,0xFF582577,0xFF2F316F,0xFF543824,0xFF414F1D,0xFF702422,0xFF101115},
			{},
			{},
			{0xFB2D2426},
			};
}
