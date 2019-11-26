package org.nemotech.rsc.model;

public class Point3D {
    
    private int x, y, z;
    
    private Point3D() {}
    
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Point3D) || o == null) {
            return false;
        }

        Point3D p = (Point3D) o;
        return x == p.x && y == p.y && z == p.z;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.x;
        hash = 41 * hash + this.y;
        hash = 41 * hash + this.z;
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
}