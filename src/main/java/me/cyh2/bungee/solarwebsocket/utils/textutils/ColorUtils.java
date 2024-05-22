package me.cyh2.bungee.solarwebsocket.utils.textutils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ColorUtils {
    public static String ReColor (@Nullable String toColor) {
        if (toColor != null) return toColor.replace("&", "§").replace("§§", "&");
        else return "NULL";
    }
    public static List<String> ReColorList (List<String> toColorList) {
        List<String> reColor = new ArrayList<>();
        for (String str : toColorList) {
            reColor.add(ReColor(str));
        }
        return reColor;
    }
}
