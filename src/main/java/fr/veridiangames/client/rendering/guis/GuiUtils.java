package fr.veridiangames.client.rendering.guis;

import javax.swing.*;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class GuiUtils
{
    public static int warning(String msg, Object[] o)
    {
        return JOptionPane.showOptionDialog(null, msg,"Warning", PLAIN_MESSAGE, QUESTION_MESSAGE, null, o, o[0]);
    }
}
