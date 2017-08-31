package fr.veridiangames.client.rendering.guis;

import static javax.swing.JOptionPane.*;

import javax.swing.JOptionPane;

public class GuiUtils
{
    public static int warning(String msg, Object[] o)
    {
        return JOptionPane.showOptionDialog(null, msg,"Warning", PLAIN_MESSAGE, QUESTION_MESSAGE, null, o, o[0]);
    }
}
