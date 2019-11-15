package org.nemotech.rsc.model.landscape;

public class Path {
    /**
     * The starting coordinates for the path
     */
    private int startX, startY;
    /**
     * Offsets for the coordinates to follow
     */
    private byte[] waypointXoffsets, waypointYoffsets;
    
    /**
     * Constructs a new path with the given coords and offsets
     */
    public Path(int startX, int startY, byte[] waypointXoffsets, byte[] waypointYoffsets) {
        this.startX = startX;
        this.startY = startY;
        this.waypointXoffsets = waypointXoffsets;
        this.waypointYoffsets = waypointYoffsets;
    }
    
    public Path(int x, int y, int endX, int endY) {
        startX = endX;
        startY = endY;
        waypointXoffsets = new byte[0];
        waypointYoffsets = new byte[0];
    }
    
    /**
     * Starting X coord of our path
     */
    public int getStartX() {
        return startX;
    }
    
    /**
     * Starting Y coord of our path
     */
    public int getStartY() {
        return startY;
    }
    
    /**
     * The length of our path
     */
    public int length() {
        if(waypointXoffsets == null) {
            return 0;
        }
        return waypointXoffsets.length;
    }
    
    /**
     * Gets the X coord of the given waypoint
     */
    public int getWaypointX(int wayPoint) {
        return startX + getWaypointXoffset(wayPoint);
    }
    
    /**
     * Gets the Y coord of the given waypoint
     */
    public int getWaypointY(int wayPoint) {
        return startY + getWaypointYoffset(wayPoint);
    }
    
    /**
     * Gets the X offset of the given waypoint
     */
    public byte getWaypointXoffset(int wayPoint) {
        if(wayPoint >= length()) {
            return (byte)0;
        }
        return waypointXoffsets[wayPoint];
    }
    
    /**
     * Gets the Y offset of the given waypoint
     */
    public byte getWaypointYoffset(int wayPoint) {
        if(wayPoint >= length()) {
            return (byte)0;
        }
        return waypointYoffsets[wayPoint];
    }
}