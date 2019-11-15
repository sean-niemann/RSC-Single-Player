package org.nemotech.rsc.model.landscape;

import java.nio.ByteBuffer;

public class Tile {
    
    public static Tile unpack(ByteBuffer in) {
        Tile tile = new Tile();
        tile.groundElevation = in.get();
        tile.groundTexture = in.get();
        tile.groundOverlay = in.get();
        tile.roofTexture = in.get();
        tile.horizontalWall = in.get();
        tile.verticalWall = in.get();
        tile.diagonalWalls = in.getInt();
        return tile;
    }
    
    public int diagonalWalls = 0;
    
    public byte groundElevation = 0;
    
    public byte groundOverlay = 0;
    
    public byte groundTexture = 0;
    
    public byte horizontalWall = 0;
    
    public byte roofTexture = 0;
    
    public byte verticalWall = 0;
    
}