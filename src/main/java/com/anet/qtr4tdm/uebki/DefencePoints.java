package com.anet.qtr4tdm.uebki;

import java.util.HashMap;

import com.anet.qtr4tdm.common.supers.IDefenceSystem;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;
import com.anet.qtr4tdm.common.tiles.MiniSiloTile;
import com.anet.qtr4tdm.common.tiles.Mrk1Tile;
import com.anet.qtr4tdm.common.tiles.ThermalBaseTile;

public class DefencePoints {

    private static final HashMap<Class<? extends IDefenceSystem>, Integer> points = new HashMap<Class<? extends IDefenceSystem>, Integer>();
    static {
        points.put(Kaz1Tile.class, 50);
        points.put(Mrk1Tile.class, 25);
        points.put(ThermalBaseTile.class, 30);
        points.put(MiniSiloTile.class, 35);
    }

    public static int GetPointsForClass (Class<? extends IDefenceSystem> class1) {
        if (points.containsKey(class1)) {
            return points.get(class1);
        }
        else return 0;
    }
    
}
