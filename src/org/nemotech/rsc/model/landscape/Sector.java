package org.nemotech.rsc.model.landscape;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Sector {
    
    public static final int WIDTH = 48, HEIGHT = 48;
    
    private Tile[] tiles;
    
    public Sector() {
        tiles = new Tile[WIDTH * HEIGHT];
        for(int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile();
        }
    }
    
    public static Sector unpack(ByteBuffer in) throws IOException {
        int length = WIDTH * HEIGHT;
        if(in.remaining() < (10 * length)) {
            throw new IOException("Provided buffer too short");
        }
        Sector sector = new Sector();
        for (int i = 0; i < length; i++) {
            sector.setTile(Tile.unpack(in), i);
        }
        return sector;
    }
    
    public Tile getTile(int i) {
        return tiles[i];
    }
    
    public Tile getTile(int x, int y) {
        return getTile(x * Sector.WIDTH + y);
    }
    
    public void setTile(Tile t, int i) {
        tiles[i] = t;
    }
    
}