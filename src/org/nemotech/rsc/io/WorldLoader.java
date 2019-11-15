package org.nemotech.rsc.io;

import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.landscape.Sector;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.external.location.GameObjectLoc;
import org.nemotech.rsc.external.location.NPCLoc;
import org.nemotech.rsc.external.location.ItemLoc;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

public class WorldLoader {
    
    private ZipFile tileArchive;

    private void loadSection(int sectionX, int sectionY, int height, World world, int bigX, int bigY) {
        Sector s = null;
        try {
            String filename = "h" + height + "x" + sectionX + "y" + sectionY;
            ZipEntry e = tileArchive.getEntry(filename);
            if (e == null) {
                //throw new Exception("Missing tile: " + filename);
                return;
            }
            ByteBuffer data = Util.streamToBuffer(new BufferedInputStream(tileArchive.getInputStream(e)));
            s = Sector.unpack(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int y = 0; y < Sector.HEIGHT; y++) {
            for (int x = 0; x < Sector.WIDTH; x++) {
                int bx = bigX + x;
                int by = bigY + y;
                if (!world.withinWorld(bx, by) || s == null) {
                    continue;
                }
                world.getTileValue(bx, by).overlay = s.getTile(x, y).groundOverlay;
                world.getTileValue(bx, by).diagWallVal = s.getTile(x, y).diagonalWalls;
                world.getTileValue(bx, by).horizontalWallVal = s.getTile(x, y).horizontalWall;
                world.getTileValue(bx, by).verticalWallVal = s.getTile(x, y).verticalWall;
                world.getTileValue(bx, by).elevation = s.getTile(x, y).groundElevation;
                
                if ((s.getTile(x, y).groundOverlay & 0xff) == 250) {
                    s.getTile(x, y).groundOverlay = (byte) 2;
                }
                
                int groundOverlay = s.getTile(x, y).groundOverlay & 0xFF;
                if (groundOverlay > 0 && EntityManager.getTile(groundOverlay - 1).getObjectType() != 0) {
                    world.getTileValue(bx, by).mapValue |= 0x40; // 64
                }

                int verticalWall = s.getTile(x, y).verticalWall & 0xFF;
                if (verticalWall > 0 && EntityManager.getDoor(verticalWall - 1).getVisibility() == 0 && EntityManager.getDoor(verticalWall - 1).getType() != 0) {
                    world.getTileValue(bx, by).mapValue |= 1; // 1
                    world.getTileValue(bx, by - 1).mapValue |= 4; // 4
                }

                int horizontalWall = s.getTile(x, y).horizontalWall & 0xFF;
                if (horizontalWall > 0 && EntityManager.getDoor(horizontalWall - 1).getVisibility() == 0 && EntityManager.getDoor(horizontalWall - 1).getType() != 0) {
                    world.getTileValue(bx, by).mapValue |= 2; // 2
                    world.getTileValue(bx - 1, by).mapValue |= 8; // 8
                }

                int diagonalWalls = s.getTile(x, y).diagonalWalls;
                if (diagonalWalls > 0 && diagonalWalls < 12000 && EntityManager.getDoor(diagonalWalls - 1).getVisibility() == 0 && EntityManager.getDoor(diagonalWalls - 1).getType() != 0) {
                    world.getTileValue(bx, by).mapValue |= 0x20; // 32
                }
                if (diagonalWalls > 12000 && diagonalWalls < 24000 && EntityManager.getDoor(diagonalWalls - 12001).getVisibility() == 0 && EntityManager.getDoor(diagonalWalls - 12001).getType() != 0) {
                    world.getTileValue(bx, by).mapValue |= 0x10; // 16
                }
            }
        }
    }
    
    public void loadWorld(World world) {
        try {
            tileArchive = new ZipFile(new File(Constants.CACHE_DIRECTORY + "terrain-members.zip"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    
        for(int plane = 0; plane < 4; plane++) {
            int wildX = 2304;
            int wildY = 1776 - (plane * 944);
            for(int sectionX = 0; sectionX < 944; sectionX += 48) {
                for(int sectionY = 0; sectionY < 944; sectionY += 48) {
                    int x = (sectionX + wildX) / 48;
                    int y = (sectionY + (plane * 944) + wildY) / 48;
                    loadSection(x, y, plane, world, sectionX, sectionY + (944 * plane));
                }
            }
        }
        int x = 0;
        for(GameObjectLoc loc : EntityManager.getObjectLocs()) {
            if(loc == null) {
                System.out.println(x++);
            }
            world.registerGameObject(new GameObject(loc), true);
        }

        for(ItemLoc loc : EntityManager.getItemLocs()) {
            world.registerItem(new Item(loc));
        }

        for(NPCLoc loc : EntityManager.getNPCLocs()) {
            world.registerNpc(new NPC(loc));
        }
        
        System.gc();
    }
    
}