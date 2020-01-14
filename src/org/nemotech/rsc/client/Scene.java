package org.nemotech.rsc.client;

public class Scene {

    public static int sin2048Cache[] = new int[2048];
    static int frustumMaxX;
    static int frustumMinX;
    static int furstumMaxY;
    static int furstumMinY;
    static int furstumFarZ;
    static int frustumNearZ;
    private static long textureCountLoaded;
    private static byte aByteArray434[];
    public int lastVisiblePolygonsCount;
    public int clipNear;
    public int clipFar3d;
    public int clipFar2d;
    public int fogZDensity;
    public int fogZDistance;
    public boolean wideBand;
    public double aDouble387;
    public int anInt388;
    public int modelCount;
    public int maxModelCount;
    public Model models[];
    public Model view;
    public int raster[];
    int rampCount;
    int gradientBase[];
    int gradientRamps[][];
    int anIntArray377[];
    int textureCount;
    byte textureColoursUsed[][];
    int textureColourList[][];
    int textureDimension[];
    long textureLoadedNumber[];
    int texturePixels[][];
    boolean textureBackTransparent[];
    int textureColours64[][];
    int textureColours128[][];
    Surface surface;
    Scanline scanlines[];
    int minY;
    int maxY;
    int planeX[];
    int planeY[];
    int vertexShade[];
    int vertexX[];
    int vertexY[];
    int vertexZ[];
    boolean interlace;
    int newStart;
    int newEnd;
    private boolean mousePickingActive;
    private int mouseX;
    private int mouseY;
    private int mousePickedCount;
    private int mousePickedMax;
    private Model mousePickedModels[];
    private int mousePickedFaces[];
    private int width;
    protected int clipX;
    protected int clipY;
    private int baseX;
    private int baseY;
    private int viewDistance;
    private int normalMagnitude;
    private int cameraX;
    private int cameraY;
    private int cameraZ;
    private int cameraYaw;
    private int cameraPitch;
    private int cameraRoll;
    private int visiblePolygonsCount;
    private Polygon visiblePolygons[];
    private int spriteCount;
    private int spriteId[];
    private int spriteX[];
    private int spriteZ[];
    private int spriteY[];
    private int spriteWidth[];
    private int spriteHeight[];
    private int spriteTranslateX[];

    public Scene(Surface surface, int i, int polygons, int k) {
        rampCount = 50;
        gradientBase = new int[rampCount];
        gradientRamps = new int[rampCount][256];
        clipNear = 5;
        clipFar3d = 1000;
        clipFar2d = 1000;
        fogZDensity = 20;
        fogZDistance = 10;
        wideBand = true;
        aDouble387 = 1.1000000000000001D;
        anInt388 = 1;
        mousePickingActive = false;
        mousePickedMax = 100;
        mousePickedModels = new Model[mousePickedMax];
        mousePickedFaces = new int[mousePickedMax];
        width = 512;
        clipX = 256;
        clipY = 192;
        baseX = 256;
        baseY = 256;
        viewDistance = 8;
        normalMagnitude = 4;
        planeX = new int[40];
        planeY = new int[40];
        vertexShade = new int[40];
        vertexX = new int[40];
        vertexY = new int[40];
        vertexZ = new int[40];
        interlace = false;
        this.surface = surface;
        clipX = surface.width2 / 2;
        clipY = surface.height2 / 2;
        raster = surface.imagePixelArray;
        modelCount = 0;
        maxModelCount = i;
        models = new Model[maxModelCount];
        visiblePolygonsCount = 0;
        visiblePolygons = new Polygon[polygons];
        for (int l = 0; l < polygons; l++) {
            visiblePolygons[l] = new Polygon();
        }
        spriteCount = 0;
        view = new Model(k * 2, k);
        spriteId = new int[k];
        spriteWidth = new int[k];
        spriteHeight = new int[k];
        spriteX = new int[k];
        spriteZ = new int[k];
        spriteY = new int[k];
        spriteTranslateX = new int[k];
        if (aByteArray434 == null)
            aByteArray434 = new byte[17691];
        cameraX = 0;
        cameraY = 0;
        cameraZ = 0;
        cameraYaw = 0;
        cameraPitch = 0;
        cameraRoll = 0;
        for (int j1 = 0; j1 < 1024; j1++) {
            sin2048Cache[j1] = (int) (Math.sin((double) j1 * 0.00613592315D) * 32768D); // 2^15 = 32768
            sin2048Cache[j1 + 1024] = (int) (Math.cos((double) j1 * 0.00613592315D) * 32768D); // 2^15 = 32768
        }
    }

    private static void textureScanline(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                        int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        int i4 = 0;
        if (i1 != 0) {
            i = k / i1 << 7;
            j = l / i1 << 7;
        }
        if (i < 0)
            i = 0;
        else if (i > 16256)
            i = 16256;
        k += j1;
        l += k1;
        i1 += l1;
        if (i1 != 0) {
            i3 = k / i1 << 7;
            j3 = l / i1 << 7;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 16256)
            i3 = 16256;
        int k3 = i3 - i >> 4;
        int l3 = j3 - j >> 4;
        for (int j4 = i2 >> 4; j4 > 0; j4--) {
            i += k2 & 0x600000;
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i = i3;
            j = j3;
            k += j1;
            l += k1;
            i1 += l1;
            if (i1 != 0) {
                i3 = k / i1 << 7;
                j3 = l / i1 << 7;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 16256)
                i3 = 16256;
            k3 = i3 - i >> 4;
            l3 = j3 - j >> 4;
        }

        for (int k4 = 0; k4 < (i2 & 0xf); k4++) {
            if ((k4 & 3) == 0) {
                i = (i & 0x3fff) + (k2 & 0x600000);
                i4 = k2 >> 23;
                k2 += l2;
            }
            ai[j2++] = ai1[(j & 0x3f80) + (i >> 7)] >>> i4;
            i += k3;
            j += l3;
        }

    }

    private static void textureTranslucentScanline(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                                   int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        int i4 = 0;
        if (i1 != 0) {
            i = k / i1 << 7;
            j = l / i1 << 7;
        }
        if (i < 0)
            i = 0;
        else if (i > 16256)
            i = 16256;
        k += j1;
        l += k1;
        i1 += l1;
        if (i1 != 0) {
            i3 = k / i1 << 7;
            j3 = l / i1 << 7;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 16256)
            i3 = 16256;
        int k3 = i3 - i >> 4;
        int l3 = j3 - j >> 4;
        for (int j4 = i2 >> 4; j4 > 0; j4--) {
            i += k2 & 0x600000;
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            i = (i & 0x3fff) + (k2 & 0x600000);
            i4 = k2 >> 23;
            k2 += l2;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i = i3;
            j = j3;
            k += j1;
            l += k1;
            i1 += l1;
            if (i1 != 0) {
                i3 = k / i1 << 7;
                j3 = l / i1 << 7;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 16256)
                i3 = 16256;
            k3 = i3 - i >> 4;
            l3 = j3 - j >> 4;
        }

        for (int k4 = 0; k4 < (i2 & 0xf); k4++) {
            if ((k4 & 3) == 0) {
                i = (i & 0x3fff) + (k2 & 0x600000);
                i4 = k2 >> 23;
                k2 += l2;
            }
            ai[j2++] = (ai1[(j & 0x3f80) + (i >> 7)] >>> i4) + (ai[j2] >> 1 & 0x7f7f7f);
            i += k3;
            j += l3;
        }

    }

    private static void textureBackTranslucentScanline(int ai[], int i, int j, int k, int ai1[], int l, int i1, int j1,
                                                       int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
        if (j2 <= 0)
            return;
        int j3 = 0;
        int k3 = 0;
        i3 <<= 2;
        if (j1 != 0) {
            j3 = l / j1 << 7;
            k3 = i1 / j1 << 7;
        }
        if (j3 < 0)
            j3 = 0;
        else if (j3 > 16256)
            j3 = 16256;
        for (int j4 = j2; j4 > 0; j4 -= 16) {
            l += k1;
            i1 += l1;
            j1 += i2;
            j = j3;
            k = k3;
            if (j1 != 0) {
                j3 = l / j1 << 7;
                k3 = i1 / j1 << 7;
            }
            if (j3 < 0)
                j3 = 0;
            else if (j3 > 16256)
                j3 = 16256;
            int l3 = j3 - j >> 4;
            int i4 = k3 - k >> 4;
            int k4 = l2 >> 23;
            j += l2 & 0x600000;
            l2 += i3;
            if (j4 < 16) {
                for (int l4 = 0; l4 < j4; l4++) {
                    if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                        ai[k2] = i;
                    k2++;
                    j += l3;
                    k += i4;
                    if ((l4 & 3) == 3) {
                        j = (j & 0x3fff) + (l2 & 0x600000);
                        k4 = l2 >> 23;
                        l2 += i3;
                    }
                }

            } else {
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0x3fff) + (l2 & 0x600000);
                k4 = l2 >> 23;
                l2 += i3;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0x3fff) + (l2 & 0x600000);
                k4 = l2 >> 23;
                l2 += i3;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0x3fff) + (l2 & 0x600000);
                k4 = l2 >> 23;
                l2 += i3;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0x3f80) + (j >> 7)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
            }
        }

    }

    private static void textureScanline2(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                         int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        l2 <<= 2;
        if (i1 != 0) {
            i3 = k / i1 << 6;
            j3 = l / i1 << 6;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 4032)
            i3 = 4032;
        for (int i4 = i2; i4 > 0; i4 -= 16) {
            k += j1;
            l += k1;
            i1 += l1;
            i = i3;
            j = j3;
            if (i1 != 0) {
                i3 = k / i1 << 6;
                j3 = l / i1 << 6;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 4032)
                i3 = 4032;
            int k3 = i3 - i >> 4;
            int l3 = j3 - j >> 4;
            int j4 = k2 >> 20;
            i += k2 & 0xc0000;
            k2 += l2;
            if (i4 < 16) {
                for (int k4 = 0; k4 < i4; k4++) {
                    ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                    i += k3;
                    j += l3;
                    if ((k4 & 3) == 3) {
                        i = (i & 0xfff) + (k2 & 0xc0000);
                        j4 = k2 >> 20;
                        k2 += l2;
                    }
                }

            } else {
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
                i += k3;
                j += l3;
                ai[j2++] = ai1[(j & 0xfc0) + (i >> 6)] >>> j4;
            }
        }

    }

    private static void textureTranslucentScanline2(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1,
                                                    int k1, int l1, int i2, int j2, int k2, int l2) {
        if (i2 <= 0)
            return;
        int i3 = 0;
        int j3 = 0;
        l2 <<= 2;
        if (i1 != 0) {
            i3 = k / i1 << 6;
            j3 = l / i1 << 6;
        }
        if (i3 < 0)
            i3 = 0;
        else if (i3 > 4032)
            i3 = 4032;
        for (int i4 = i2; i4 > 0; i4 -= 16) {
            k += j1;
            l += k1;
            i1 += l1;
            i = i3;
            j = j3;
            if (i1 != 0) {
                i3 = k / i1 << 6;
                j3 = l / i1 << 6;
            }
            if (i3 < 0)
                i3 = 0;
            else if (i3 > 4032)
                i3 = 4032;
            int k3 = i3 - i >> 4;
            int l3 = j3 - j >> 4;
            int j4 = k2 >> 20;
            i += k2 & 0xc0000;
            k2 += l2;
            if (i4 < 16) {
                for (int k4 = 0; k4 < i4; k4++) {
                    ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                    i += k3;
                    j += l3;
                    if ((k4 & 3) == 3) {
                        i = (i & 0xfff) + (k2 & 0xc0000);
                        j4 = k2 >> 20;
                        k2 += l2;
                    }
                }

            } else {
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                i = (i & 0xfff) + (k2 & 0xc0000);
                j4 = k2 >> 20;
                k2 += l2;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
                i += k3;
                j += l3;
                ai[j2++] = (ai1[(j & 0xfc0) + (i >> 6)] >>> j4) + (ai[j2] >> 1 & 0x7f7f7f);
            }
        }

    }

    private static void textureBackTranslucentScanline2(int ai[], int i, int j, int k, int ai1[], int l, int i1, int j1,
                                                        int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
        if (j2 <= 0)
            return;
        int j3 = 0;
        int k3 = 0;
        i3 <<= 2;
        if (j1 != 0) {
            j3 = l / j1 << 6;
            k3 = i1 / j1 << 6;
        }
        if (j3 < 0)
            j3 = 0;
        else if (j3 > 4032)
            j3 = 4032;
        for (int j4 = j2; j4 > 0; j4 -= 16) {
            l += k1;
            i1 += l1;
            j1 += i2;
            j = j3;
            k = k3;
            if (j1 != 0) {
                j3 = l / j1 << 6;
                k3 = i1 / j1 << 6;
            }
            if (j3 < 0)
                j3 = 0;
            else if (j3 > 4032)
                j3 = 4032;
            int l3 = j3 - j >> 4;
            int i4 = k3 - k >> 4;
            int k4 = l2 >> 20;
            j += l2 & 0xc0000;
            l2 += i3;
            if (j4 < 16) {
                for (int l4 = 0; l4 < j4; l4++) {
                    if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                        ai[k2] = i;
                    k2++;
                    j += l3;
                    k += i4;
                    if ((l4 & 3) == 3) {
                        j = (j & 0xfff) + (l2 & 0xc0000);
                        k4 = l2 >> 20;
                        l2 += i3;
                    }
                }

            } else {
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0xfff) + (l2 & 0xc0000);
                k4 = l2 >> 20;
                l2 += i3;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0xfff) + (l2 & 0xc0000);
                k4 = l2 >> 20;
                l2 += i3;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                j = (j & 0xfff) + (l2 & 0xc0000);
                k4 = l2 >> 20;
                l2 += i3;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
                j += l3;
                k += i4;
                if ((i = ai1[(k & 0xfc0) + (j >> 6)] >>> k4) != 0)
                    ai[k2] = i;
                k2++;
            }
        }

    }

    private static void gradientScanline(int ai[], int i, int j, int k, int ai1[], int l, int i1) {
        if (i >= 0)
            return;
        i1 <<= 1;
        k = ai1[l >> 8 & 0xff];
        l += i1;
        int j1 = i / 8;
        for (int k1 = j1; k1 < 0; k1++) {
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
        }

        j1 = -(i % 8);
        for (int l1 = 0; l1 < j1; l1++) {
            ai[j++] = k;
            if ((l1 & 1) == 1) {
                k = ai1[l >> 8 & 0xff];
                l += i1;
            }
        }

    }

    private static void textureGradientScanline(int ai[], int i, int j, int k, int ai1[], int l, int i1) {
        if (i >= 0)
            return;
        i1 <<= 2;
        k = ai1[l >> 8 & 0xff];
        l += i1;
        int j1 = i / 16;
        for (int k1 = j1; k1 < 0; k1++) {
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            k = ai1[l >> 8 & 0xff];
            l += i1;
        }

        j1 = -(i % 16);
        for (int l1 = 0; l1 < j1; l1++) {
            ai[j++] = k + (ai[j] >> 1 & 0x7f7f7f);
            if ((l1 & 3) == 3) {
                k = ai1[l >> 8 & 0xff];
                l += i1;
                l += i1;
            }
        }

    }

    private static void gradientScanline2(int ai[], int i, int j, int k, int ai1[], int l, int i1) {
        if (i >= 0)
            return;
        i1 <<= 2;
        k = ai1[l >> 8 & 0xff];
        l += i1;
        int j1 = i / 16;
        for (int k1 = j1; k1 < 0; k1++) {
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            ai[j++] = k;
            k = ai1[l >> 8 & 0xff];
            l += i1;
        }

        j1 = -(i % 16);
        for (int l1 = 0; l1 < j1; l1++) {
            ai[j++] = k;
            if ((l1 & 3) == 3) {
                k = ai1[l >> 8 & 0xff];
                l += i1;
            }
        }

    }

    public static int rgb(int i, int j, int k) {
        return -1 - (i / 8) * 1024 - (j / 8) * 32 - k / 8;
    }

    public void addModel(Model model) {
        if (model == null)
            System.out.println("Warning tried to add null object!");
        if (modelCount < maxModelCount) {
            //modelState[modelCount] = 0; // only set, not used
            models[modelCount++] = model;
        }
    }

    public void removeModel(Model gameModel) {
        for (int i = 0; i < modelCount; i++)
            if (models[i] == gameModel) {
                modelCount--;
                for (int j = i; j < modelCount; j++) {
                    models[j] = models[j + 1];
                    //modelState[j] = modelState[j + 1]; // only set, not used
                }

            }

    }

    public void dispose() {
        clear();
        for (int i = 0; i < modelCount; i++)
            models[i] = null;

        modelCount = 0;
    }

    public void clear() {
        spriteCount = 0;
        view.clear();
    }

    public void reduceSprites(int i) {
        spriteCount -= i;
        view.reduce(i, i * 2);
        if (spriteCount < 0)
            spriteCount = 0;
    }

    public int addSprite(int n, int x, int z, int y, int w, int h, int tag) {
        spriteId[spriteCount] = n;
        spriteX[spriteCount] = x;
        spriteZ[spriteCount] = z;
        spriteY[spriteCount] = y;
        spriteWidth[spriteCount] = w;
        spriteHeight[spriteCount] = h;
        spriteTranslateX[spriteCount] = 0;
        int l1 = view.createVertex(x, z, y);
        int i2 = view.createVertex(x, z - h, y);
        int vs[] = {
                l1, i2
        };
        view.createFace(2, vs, 0, 0);
        view.faceTag[spriteCount] = tag;
        view.isLocalPlayer[spriteCount++] = 0;
        return spriteCount - 1;
    }

    public void setLocalPlayer(int i) {
        view.isLocalPlayer[i] = 1;
    }

    public void setSpriteTranslateX(int i, int n) {
        spriteTranslateX[i] = n;
    }

    public void setMouseLoc(int x, int y) {
        mouseX = x - baseX;
        mouseY = y;
        mousePickedCount = 0;
        mousePickingActive = true;
    }

    public int getMousePickedCount() {
        return mousePickedCount;
    }

    public int[] getMousePickedFaces() {
        return mousePickedFaces;
    }

    public Model[] getMousePickedModels() {
        return mousePickedModels;
    }

    public void setBounds(int baseX, int baseY, int clipX, int clipY, int width, int viewDistance) {
        this.clipX = clipX;
        this.clipY = clipY;
        this.baseX = baseX;
        this.baseY = baseY;
        this.width = width;
        this.viewDistance = viewDistance;
        scanlines = new Scanline[clipY + baseY];
        for (int k1 = 0; k1 < clipY + baseY; k1++) {
            scanlines[k1] = new Scanline();
        }
    }

    private void polygonsQSort(Polygon polygons[], int low, int high) {
        if (low < high) {
            int min = low - 1;
            int max = high + 1;
            int mid = (low + high) / 2;
            Polygon polygon = polygons[mid];
            polygons[mid] = polygons[low];
            polygons[low] = polygon;
            int j1 = polygon.depth;
            while (min < max) {
                do
                    max--;
                while (polygons[max].depth < j1);
                do
                    min++;
                while (polygons[min].depth > j1);
                if (min < max) {
                    Polygon polygon_1 = polygons[min];
                    polygons[min] = polygons[max];
                    polygons[max] = polygon_1;
                }
            }
            polygonsQSort(polygons, low, max);
            polygonsQSort(polygons, max + 1, high);
        }
    }

    private void polygonsIntersectSort(int step, Polygon polygons[], int count) {
        for (int i = 0; i <= count; i++) {
            polygons[i].skipSomething = false;
            polygons[i].index = i;
            polygons[i].index2 = -1;
        }

        int l = 0;
        do {
            while (polygons[l].skipSomething)
                l++;
            if (l == count)
                return;
            Polygon polygon = polygons[l];
            polygon.skipSomething = true;
            int i1 = l;
            int j1 = l + step;
            if (j1 >= count)
                j1 = count - 1;
            for (int k1 = j1; k1 >= i1 + 1; k1--) {
                Polygon other = polygons[k1];
                if (polygon.minPlaneX < other.maxPlaneX && other.minPlaneX < polygon.maxPlaneX && polygon.minPlaneY < other.maxPlaneY && other.minPlaneY < polygon.maxPlaneY && polygon.index != other.index2 && !separatePolygon(polygon, other) && heuristicPolygon(other, polygon)) {
                    polygonsOrder(polygons, i1, k1);
                    if (polygons[k1] != other)
                        k1++;
                    i1 = newStart;
                    other.index2 = polygon.index;
                }
            }

        } while (true);
    }

    public boolean polygonsOrder(Polygon polygons[], int start, int end) {
        do {
            Polygon polygon = polygons[start];
            for (int k = start + 1; k <= end; k++) {
                Polygon polygon_1 = polygons[k];
                if (!separatePolygon(polygon_1, polygon))
                    break;
                polygons[start] = polygon_1;
                polygons[k] = polygon;
                start = k;
                if (start == end) {
                    newStart = start;
                    newEnd = start - 1;
                    return true;
                }
            }

            Polygon polygon_2 = polygons[end];
            for (int l = end - 1; l >= start; l--) {
                Polygon polygon_3 = polygons[l];
                if (!separatePolygon(polygon_2, polygon_3))
                    break;
                polygons[end] = polygon_3;
                polygons[l] = polygon_2;
                end = l;
                if (start == end) {
                    newStart = end + 1;
                    newEnd = end;
                    return true;
                }
            }

            if (start + 1 >= end) {
                newStart = start;
                newEnd = end;
                return false;
            }
            if (!polygonsOrder(polygons, start + 1, end)) {
                newStart = start;
                return false;
            }
            end = newEnd;
        } while (true);
    }

    public void setFrustum(int i, int j, int k) {
        int l = -cameraYaw + 1024 & 0x3ff;
        int i1 = -cameraPitch + 1024 & 0x3ff;
        int j1 = -cameraRoll + 1024 & 0x3ff;
        if (j1 != 0) {
            int k1 = sin2048Cache[j1];
            int j2 = sin2048Cache[j1 + 1024];
            int i3 = j * k1 + i * j2 >> 15;
            j = j * j2 - i * k1 >> 15;
            i = i3;
        }
        if (l != 0) {
            int l1 = sin2048Cache[l];
            int k2 = sin2048Cache[l + 1024];
            int j3 = j * k2 - k * l1 >> 15;
            k = j * l1 + k * k2 >> 15;
            j = j3;
        }
        if (i1 != 0) {
            int i2 = sin2048Cache[i1];
            int l2 = sin2048Cache[i1 + 1024];
            int k3 = k * i2 + i * l2 >> 15;
            k = k * l2 - i * i2 >> 15;
            i = k3;
        }
        if (i < frustumMaxX)
            frustumMaxX = i;
        if (i > frustumMinX)
            frustumMinX = i;
        if (j < furstumMaxY)
            furstumMaxY = j;
        if (j > furstumMinY)
            furstumMinY = j;
        if (k < furstumFarZ)
            furstumFarZ = k;
        if (k > frustumNearZ)
            frustumNearZ = k;
    }

    public void render() {
        interlace = surface.interlace;
        int i3 = clipX * clipFar3d >> viewDistance;
        int j3 = clipY * clipFar3d >> viewDistance;
        frustumMaxX = 0;
        frustumMinX = 0;
        furstumMaxY = 0;
        furstumMinY = 0;
        furstumFarZ = 0;
        frustumNearZ = 0;
        setFrustum(-i3, -j3, clipFar3d);
        setFrustum(-i3, j3, clipFar3d);
        setFrustum(i3, -j3, clipFar3d);
        setFrustum(i3, j3, clipFar3d);
        setFrustum(-clipX, -clipY, 0);
        setFrustum(-clipX, clipY, 0);
        setFrustum(clipX, -clipY, 0);
        setFrustum(clipX, clipY, 0);
        frustumMaxX += cameraX;
        frustumMinX += cameraX;
        furstumMaxY += cameraY;
        furstumMinY += cameraY;
        furstumFarZ += cameraZ;
        frustumNearZ += cameraZ;
        models[modelCount] = view;
        view.transformState = 2;
        for (int i = 0; i < modelCount; i++)
            models[i].project(cameraX, cameraY, cameraZ, cameraYaw, cameraPitch, cameraRoll, viewDistance, clipNear);

        models[modelCount].project(cameraX, cameraY, cameraZ, cameraYaw, cameraPitch, cameraRoll, viewDistance, clipNear);
        visiblePolygonsCount = 0;
        for (int count = 0; count < modelCount; count++) {
            Model gameModel = models[count];
            if (gameModel.visible) {
                for (int face = 0; face < gameModel.numFaces; face++) {
                    int num_vertices = gameModel.faceNumVertices[face];
                    int vertices[] = gameModel.faceVertices[face];
                    boolean visible = false;
                    for (int vertex = 0; vertex < num_vertices; vertex++) {
                        int z = gameModel.projectVertexZ[vertices[vertex]];
                        if (z <= clipNear || z >= clipFar3d)
                            continue;
                        visible = true;
                        break;
                    }

                    if (visible) {
                        int viewxcount = 0;
                        for (int vertex = 0; vertex < num_vertices; vertex++) {
                            int x = gameModel.vertexViewX[vertices[vertex]];
                            if (x > -clipX)
                                viewxcount |= 1;
                            if (x < clipX)
                                viewxcount |= 2;
                            if (viewxcount == 3)
                                break;
                        }

                        if (viewxcount == 3) {
                            int viewycount = 0;
                            for (int vertex = 0; vertex < num_vertices; vertex++) {
                                int k1 = gameModel.vertexViewY[vertices[vertex]];
                                if (k1 > -clipY)
                                    viewycount |= 1;
                                if (k1 < clipY)
                                    viewycount |= 2;
                                if (viewycount == 3)
                                    break;
                            }

                            if (viewycount == 3) {
                                Polygon polygon_1 = visiblePolygons[visiblePolygonsCount];
                                polygon_1.model = gameModel;
                                polygon_1.face = face;
                                initialisePolygon3d(visiblePolygonsCount);
                                int facefill;
                                if (polygon_1.visibility < 0)
                                    facefill = gameModel.faceFillFront[face];
                                else
                                    facefill = gameModel.faceFillBack[face];
                                if (facefill != Terrain.COLOR_TRANSPARENT) { // 12345678 = invisible ?
                                    int h = 0;
                                    for (int vertex = 0; vertex < num_vertices; vertex++)
                                        h += gameModel.projectVertexZ[vertices[vertex]];

                                    polygon_1.depth = h / num_vertices + gameModel.depth;
                                    polygon_1.facefill = facefill;
                                    visiblePolygonsCount++;
                                }
                            }
                        }
                    }
                }

            }
        }

        Model model_2d = view;
        if (model_2d.visible) {
            for (int face = 0; face < model_2d.numFaces; face++) {
                int faceVertices[] = model_2d.faceVertices[face];
                int vertex0 = faceVertices[0];
                int vx = model_2d.vertexViewX[vertex0];
                int vy = model_2d.vertexViewY[vertex0];
                int vz = model_2d.projectVertexZ[vertex0];
                if (vz > clipNear && vz < clipFar2d) {
                    int vw = (spriteWidth[face] << viewDistance) / vz;
                    int vh = (spriteHeight[face] << viewDistance) / vz;
                    if (vx - vw / 2 <= clipX && vx + vw / 2 >= -clipX && vy - vh <= clipY && vy >= -clipY) {
                        Polygon polygon_2 = visiblePolygons[visiblePolygonsCount];
                        polygon_2.model = model_2d;
                        polygon_2.face = face;
                        initialisePolygon2d(visiblePolygonsCount);
                        polygon_2.depth = (vz + model_2d.projectVertexZ[faceVertices[1]]) / 2;
                        visiblePolygonsCount++;
                    }
                }
            }

        }
        if (visiblePolygonsCount == 0)
            return;
        lastVisiblePolygonsCount = visiblePolygonsCount;
        polygonsQSort(visiblePolygons, 0, visiblePolygonsCount - 1);
        polygonsIntersectSort(100, visiblePolygons, visiblePolygonsCount);
        for (int model = 0; model < visiblePolygonsCount; model++) {
            Polygon polygon = visiblePolygons[model];
            Model gameModel_2 = polygon.model;
            int l = polygon.face;
            if (gameModel_2 == view) {
                int faceverts[] = gameModel_2.faceVertices[l];
                int face_0 = faceverts[0];
                int vx = gameModel_2.vertexViewX[face_0];
                int vy = gameModel_2.vertexViewY[face_0];
                int vz = gameModel_2.projectVertexZ[face_0];
                int w = (spriteWidth[l] << viewDistance) / vz;
                int h = (spriteHeight[l] << viewDistance) / vz;
                //int i11 = vy - gameModel_2.vertexViewY[faceverts[1]]; // not used
                //int tx = ((gameModel_2.vertexViewX[faceverts[1]] - vx) * i11) / h; // redundant
                int tx = gameModel_2.vertexViewX[faceverts[1]] - vx;
                int x = vx - w / 2;
                int y = (baseY + vy) - h;
                surface.spriteClipping(x + baseX, y, w, h, spriteId[l], tx, (256 << viewDistance) / vz);
                if (mousePickingActive && mousePickedCount < mousePickedMax) {
                    x += (spriteTranslateX[l] << viewDistance) / vz;
                    if (mouseY >= y && mouseY <= y + h && mouseX >= x && mouseX <= x + w && !gameModel_2.unpickable && gameModel_2.isLocalPlayer[l] == 0) {
                        mousePickedModels[mousePickedCount] = gameModel_2;
                        mousePickedFaces[mousePickedCount] = l;
                        mousePickedCount++;
                    }
                }
            } else {
                int k8 = 0;
                int j10 = 0;
                int l10 = gameModel_2.faceNumVertices[l];
                int ai3[] = gameModel_2.faceVertices[l];
                if (gameModel_2.faceIntensity[l] != Terrain.COLOR_TRANSPARENT)
                    if (polygon.visibility < 0)
                        j10 = gameModel_2.lightAmbience - gameModel_2.faceIntensity[l];
                    else
                        j10 = gameModel_2.lightAmbience + gameModel_2.faceIntensity[l];
                for (int k11 = 0; k11 < l10; k11++) {
                    int k2 = ai3[k11];
                    vertexX[k11] = gameModel_2.projectVertexX[k2];
                    vertexY[k11] = gameModel_2.projectVertexY[k2];
                    vertexZ[k11] = gameModel_2.projectVertexZ[k2];
                    if (gameModel_2.faceIntensity[l] == Terrain.COLOR_TRANSPARENT)
                        if (polygon.visibility < 0)
                            j10 = (gameModel_2.lightAmbience - gameModel_2.vertexIntensity[k2]) + gameModel_2.vertexAmbience[k2];
                        else
                            j10 = gameModel_2.lightAmbience + gameModel_2.vertexIntensity[k2] + gameModel_2.vertexAmbience[k2];
                    if (gameModel_2.projectVertexZ[k2] >= clipNear) {
                        planeX[k8] = gameModel_2.vertexViewX[k2];
                        planeY[k8] = gameModel_2.vertexViewY[k2];
                        vertexShade[k8] = j10;
                        if (gameModel_2.projectVertexZ[k2] > fogZDistance)
                            vertexShade[k8] += (gameModel_2.projectVertexZ[k2] - fogZDistance) / fogZDensity;
                        k8++;
                    } else {
                        int k9;
                        if (k11 == 0)
                            k9 = ai3[l10 - 1];
                        else
                            k9 = ai3[k11 - 1];
                        if (gameModel_2.projectVertexZ[k9] >= clipNear) {
                            int k7 = gameModel_2.projectVertexZ[k2] - gameModel_2.projectVertexZ[k9];
                            int i5 = gameModel_2.projectVertexX[k2] - ((gameModel_2.projectVertexX[k2] - gameModel_2.projectVertexX[k9]) * (gameModel_2.projectVertexZ[k2] - clipNear)) / k7;
                            int j6 = gameModel_2.projectVertexY[k2] - ((gameModel_2.projectVertexY[k2] - gameModel_2.projectVertexY[k9]) * (gameModel_2.projectVertexZ[k2] - clipNear)) / k7;
                            planeX[k8] = (i5 << viewDistance) / clipNear;
                            planeY[k8] = (j6 << viewDistance) / clipNear;
                            vertexShade[k8] = j10;
                            k8++;
                        }
                        if (k11 == l10 - 1)
                            k9 = ai3[0];
                        else
                            k9 = ai3[k11 + 1];
                        if (gameModel_2.projectVertexZ[k9] >= clipNear) {
                            int l7 = gameModel_2.projectVertexZ[k2] - gameModel_2.projectVertexZ[k9];
                            int j5 = gameModel_2.projectVertexX[k2] - ((gameModel_2.projectVertexX[k2] - gameModel_2.projectVertexX[k9]) * (gameModel_2.projectVertexZ[k2] - clipNear)) / l7;
                            int k6 = gameModel_2.projectVertexY[k2] - ((gameModel_2.projectVertexY[k2] - gameModel_2.projectVertexY[k9]) * (gameModel_2.projectVertexZ[k2] - clipNear)) / l7;
                            planeX[k8] = (j5 << viewDistance) / clipNear;
                            planeY[k8] = (k6 << viewDistance) / clipNear;
                            vertexShade[k8] = j10;
                            k8++;
                        }
                    }
                }

                for (int i12 = 0; i12 < l10; i12++) {
                    if (vertexShade[i12] < 0)
                        vertexShade[i12] = 0;
                    else if (vertexShade[i12] > 255)
                        vertexShade[i12] = 255;
                    if (polygon.facefill >= 0)
                        if (textureDimension[polygon.facefill] == 1)
                            vertexShade[i12] <<= 9;
                        else
                            vertexShade[i12] <<= 6;
                }

                generateScanlines(0, 0, 0, 0, k8, planeX, planeY, vertexShade, gameModel_2, l);
                if (maxY > minY)
                    rasterize(0, 0, l10, vertexX, vertexY, vertexZ, polygon.facefill, gameModel_2);
            }
        }

        mousePickingActive = false;
    }

    private void generateScanlines(int i, int j, int k, int l, int i1, int ai[], int ai1[],
                                   int ai2[], Model gameModel, int pid) {
        if (i1 == 3) {
            int k1 = ai1[0] + baseY;
            int k2 = ai1[1] + baseY;
            int k3 = ai1[2] + baseY;
            int k4 = ai[0];
            int l5 = ai[1];
            int j7 = ai[2];
            int l8 = ai2[0];
            int j10 = ai2[1];
            int j11 = ai2[2];
            int j12 = (baseY + clipY) - 1;
            int l12 = 0;
            int j13 = 0;
            int l13 = 0;
            int j14 = 0;
            int l14 = Terrain.COLOR_TRANSPARENT;
            int j15 = 0xff439eb2;
            if (k3 != k1) {
                j13 = (j7 - k4 << 8) / (k3 - k1);
                j14 = (j11 - l8 << 8) / (k3 - k1);
                if (k1 < k3) {
                    l12 = k4 << 8;
                    l13 = l8 << 8;
                    l14 = k1;
                    j15 = k3;
                } else {
                    l12 = j7 << 8;
                    l13 = j11 << 8;
                    l14 = k3;
                    j15 = k1;
                }
                if (l14 < 0) {
                    l12 -= j13 * l14;
                    l13 -= j14 * l14;
                    l14 = 0;
                }
                if (j15 > j12)
                    j15 = j12;
            }
            int l15 = 0;
            int j16 = 0;
            int l16 = 0;
            int j17 = 0;
            int l17 = Terrain.COLOR_TRANSPARENT;
            int j18 = 0xff439eb2;
            if (k2 != k1) {
                j16 = (l5 - k4 << 8) / (k2 - k1);
                j17 = (j10 - l8 << 8) / (k2 - k1);
                if (k1 < k2) {
                    l15 = k4 << 8;
                    l16 = l8 << 8;
                    l17 = k1;
                    j18 = k2;
                } else {
                    l15 = l5 << 8;
                    l16 = j10 << 8;
                    l17 = k2;
                    j18 = k1;
                }
                if (l17 < 0) {
                    l15 -= j16 * l17;
                    l16 -= j17 * l17;
                    l17 = 0;
                }
                if (j18 > j12)
                    j18 = j12;
            }
            int l18 = 0;
            int j19 = 0;
            int l19 = 0;
            int j20 = 0;
            int l20 = Terrain.COLOR_TRANSPARENT;
            int j21 = 0xff439eb2;
            if (k3 != k2) {
                j19 = (j7 - l5 << 8) / (k3 - k2);
                j20 = (j11 - j10 << 8) / (k3 - k2);
                if (k2 < k3) {
                    l18 = l5 << 8;
                    l19 = j10 << 8;
                    l20 = k2;
                    j21 = k3;
                } else {
                    l18 = j7 << 8;
                    l19 = j11 << 8;
                    l20 = k3;
                    j21 = k2;
                }
                if (l20 < 0) {
                    l18 -= j19 * l20;
                    l19 -= j20 * l20;
                    l20 = 0;
                }
                if (j21 > j12)
                    j21 = j12;
            }
            minY = l14;
            if (l17 < minY)
                minY = l17;
            if (l20 < minY)
                minY = l20;
            maxY = j15;
            if (j18 > maxY)
                maxY = j18;
            if (j21 > maxY)
                maxY = j21;
            int l21 = 0;
            for (k = minY; k < maxY; k++) {
                if (k >= l14 && k < j15) {
                    i = j = l12;
                    l = l21 = l13;
                    l12 += j13;
                    l13 += j14;
                } else {
                    i = 0xa0000;
                    j = 0xfff60000;
                }
                if (k >= l17 && k < j18) {
                    if (l15 < i) {
                        i = l15;
                        l = l16;
                    }
                    if (l15 > j) {
                        j = l15;
                        l21 = l16;
                    }
                    l15 += j16;
                    l16 += j17;
                }
                if (k >= l20 && k < j21) {
                    if (l18 < i) {
                        i = l18;
                        l = l19;
                    }
                    if (l18 > j) {
                        j = l18;
                        l21 = l19;
                    }
                    l18 += j19;
                    l19 += j20;
                }
                Scanline scanline_6 = scanlines[k];
                scanline_6.startX = i;
                scanline_6.endX = j;
                scanline_6.startS = l;
                scanline_6.endS = l21;
            }

            if (minY < baseY - clipY)
                minY = baseY - clipY;
        } else if (i1 == 4) {
            int l1 = ai1[0] + baseY;
            int l2 = ai1[1] + baseY;
            int l3 = ai1[2] + baseY;
            int l4 = ai1[3] + baseY;
            int i6 = ai[0];
            int k7 = ai[1];
            int i9 = ai[2];
            int k10 = ai[3];
            int k11 = ai2[0];
            int k12 = ai2[1];
            int i13 = ai2[2];
            int k13 = ai2[3];
            int i14 = (baseY + clipY) - 1;
            int k14 = 0;
            int i15 = 0;
            int k15 = 0;
            int i16 = 0;
            int k16 = Terrain.COLOR_TRANSPARENT;
            int i17 = 0xff439eb2;
            if (l4 != l1) {
                i15 = (k10 - i6 << 8) / (l4 - l1);
                i16 = (k13 - k11 << 8) / (l4 - l1);
                if (l1 < l4) {
                    k14 = i6 << 8;
                    k15 = k11 << 8;
                    k16 = l1;
                    i17 = l4;
                } else {
                    k14 = k10 << 8;
                    k15 = k13 << 8;
                    k16 = l4;
                    i17 = l1;
                }
                if (k16 < 0) {
                    k14 -= i15 * k16;
                    k15 -= i16 * k16;
                    k16 = 0;
                }
                if (i17 > i14)
                    i17 = i14;
            }
            int k17 = 0;
            int i18 = 0;
            int k18 = 0;
            int i19 = 0;
            int k19 = Terrain.COLOR_TRANSPARENT;
            int i20 = 0xff439eb2;
            if (l2 != l1) {
                i18 = (k7 - i6 << 8) / (l2 - l1);
                i19 = (k12 - k11 << 8) / (l2 - l1);
                if (l1 < l2) {
                    k17 = i6 << 8;
                    k18 = k11 << 8;
                    k19 = l1;
                    i20 = l2;
                } else {
                    k17 = k7 << 8;
                    k18 = k12 << 8;
                    k19 = l2;
                    i20 = l1;
                }
                if (k19 < 0) {
                    k17 -= i18 * k19;
                    k18 -= i19 * k19;
                    k19 = 0;
                }
                if (i20 > i14)
                    i20 = i14;
            }
            int k20 = 0;
            int i21 = 0;
            int k21 = 0;
            int i22 = 0;
            int j22 = Terrain.COLOR_TRANSPARENT;
            int k22 = 0xff439eb2;
            if (l3 != l2) {
                i21 = (i9 - k7 << 8) / (l3 - l2);
                i22 = (i13 - k12 << 8) / (l3 - l2);
                if (l2 < l3) {
                    k20 = k7 << 8;
                    k21 = k12 << 8;
                    j22 = l2;
                    k22 = l3;
                } else {
                    k20 = i9 << 8;
                    k21 = i13 << 8;
                    j22 = l3;
                    k22 = l2;
                }
                if (j22 < 0) {
                    k20 -= i21 * j22;
                    k21 -= i22 * j22;
                    j22 = 0;
                }
                if (k22 > i14)
                    k22 = i14;
            }
            int l22 = 0;
            int i23 = 0;
            int j23 = 0;
            int k23 = 0;
            int l23 = Terrain.COLOR_TRANSPARENT;
            int i24 = 0xff439eb2;
            if (l4 != l3) {
                i23 = (k10 - i9 << 8) / (l4 - l3);
                k23 = (k13 - i13 << 8) / (l4 - l3);
                if (l3 < l4) {
                    l22 = i9 << 8;
                    j23 = i13 << 8;
                    l23 = l3;
                    i24 = l4;
                } else {
                    l22 = k10 << 8;
                    j23 = k13 << 8;
                    l23 = l4;
                    i24 = l3;
                }
                if (l23 < 0) {
                    l22 -= i23 * l23;
                    j23 -= k23 * l23;
                    l23 = 0;
                }
                if (i24 > i14)
                    i24 = i14;
            }
            minY = k16;
            if (k19 < minY)
                minY = k19;
            if (j22 < minY)
                minY = j22;
            if (l23 < minY)
                minY = l23;
            maxY = i17;
            if (i20 > maxY)
                maxY = i20;
            if (k22 > maxY)
                maxY = k22;
            if (i24 > maxY)
                maxY = i24;
            int j24 = 0;
            for (k = minY; k < maxY; k++) {
                if (k >= k16 && k < i17) {
                    i = j = k14;
                    l = j24 = k15;
                    k14 += i15;
                    k15 += i16;
                } else {
                    i = 0xa0000;
                    j = 0xfff60000;
                }
                if (k >= k19 && k < i20) {
                    if (k17 < i) {
                        i = k17;
                        l = k18;
                    }
                    if (k17 > j) {
                        j = k17;
                        j24 = k18;
                    }
                    k17 += i18;
                    k18 += i19;
                }
                if (k >= j22 && k < k22) {
                    if (k20 < i) {
                        i = k20;
                        l = k21;
                    }
                    if (k20 > j) {
                        j = k20;
                        j24 = k21;
                    }
                    k20 += i21;
                    k21 += i22;
                }
                if (k >= l23 && k < i24) {
                    if (l22 < i) {
                        i = l22;
                        l = j23;
                    }
                    if (l22 > j) {
                        j = l22;
                        j24 = j23;
                    }
                    l22 += i23;
                    j23 += k23;
                }
                Scanline scanline_7 = scanlines[k];
                scanline_7.startX = i;
                scanline_7.endX = j;
                scanline_7.startS = l;
                scanline_7.endS = j24;
            }

            if (minY < baseY - clipY)
                minY = baseY - clipY;
        } else {
            maxY = minY = ai1[0] += baseY;
            for (k = 1; k < i1; k++) {
                int i2;
                if ((i2 = ai1[k] += baseY) < minY)
                    minY = i2;
                else if (i2 > maxY)
                    maxY = i2;
            }

            if (minY < baseY - clipY)
                minY = baseY - clipY;
            if (maxY >= baseY + clipY)
                maxY = (baseY + clipY) - 1;
            if (minY >= maxY)
                return;
            for (k = minY; k < maxY; k++) {
                Scanline scanline = scanlines[k];
                scanline.startX = 0xa0000;
                scanline.endX = 0xfff60000;
            }

            int j2 = i1 - 1;
            int i3 = ai1[0];
            int i4 = ai1[j2];
            if (i3 < i4) {
                int i5 = ai[0] << 8;
                int j6 = (ai[j2] - ai[0] << 8) / (i4 - i3);
                int l7 = ai2[0] << 8;
                int j9 = (ai2[j2] - ai2[0] << 8) / (i4 - i3);
                if (i3 < 0) {
                    i5 -= j6 * i3;
                    l7 -= j9 * i3;
                    i3 = 0;
                }
                if (i4 > maxY)
                    i4 = maxY;
                for (k = i3; k <= i4; k++) {
                    Scanline scanline_2 = scanlines[k];
                    scanline_2.startX = scanline_2.endX = i5;
                    scanline_2.startS = scanline_2.endS = l7;
                    i5 += j6;
                    l7 += j9;
                }

            } else if (i3 > i4) {
                int j5 = ai[j2] << 8;
                int k6 = (ai[0] - ai[j2] << 8) / (i3 - i4);
                int i8 = ai2[j2] << 8;
                int k9 = (ai2[0] - ai2[j2] << 8) / (i3 - i4);
                if (i4 < 0) {
                    j5 -= k6 * i4;
                    i8 -= k9 * i4;
                    i4 = 0;
                }
                if (i3 > maxY)
                    i3 = maxY;
                for (k = i4; k <= i3; k++) {
                    Scanline scanline_3 = scanlines[k];
                    scanline_3.startX = scanline_3.endX = j5;
                    scanline_3.startS = scanline_3.endS = i8;
                    j5 += k6;
                    i8 += k9;
                }

            }
            for (k = 0; k < j2; k++) {
                int k5 = k + 1;
                int j3 = ai1[k];
                int j4 = ai1[k5];
                if (j3 < j4) {
                    int l6 = ai[k] << 8;
                    int j8 = (ai[k5] - ai[k] << 8) / (j4 - j3);
                    int l9 = ai2[k] << 8;
                    int l10 = (ai2[k5] - ai2[k] << 8) / (j4 - j3);
                    if (j3 < 0) {
                        l6 -= j8 * j3;
                        l9 -= l10 * j3;
                        j3 = 0;
                    }
                    if (j4 > maxY)
                        j4 = maxY;
                    for (int l11 = j3; l11 <= j4; l11++) {
                        Scanline scanline_4 = scanlines[l11];
                        if (l6 < scanline_4.startX) {
                            scanline_4.startX = l6;
                            scanline_4.startS = l9;
                        }
                        if (l6 > scanline_4.endX) {
                            scanline_4.endX = l6;
                            scanline_4.endS = l9;
                        }
                        l6 += j8;
                        l9 += l10;
                    }

                } else if (j3 > j4) {
                    int i7 = ai[k5] << 8;
                    int k8 = (ai[k] - ai[k5] << 8) / (j3 - j4);
                    int i10 = ai2[k5] << 8;
                    int i11 = (ai2[k] - ai2[k5] << 8) / (j3 - j4);
                    if (j4 < 0) {
                        i7 -= k8 * j4;
                        i10 -= i11 * j4;
                        j4 = 0;
                    }
                    if (j3 > maxY)
                        j3 = maxY;
                    for (int i12 = j4; i12 <= j3; i12++) {
                        Scanline scanline_5 = scanlines[i12];
                        if (i7 < scanline_5.startX) {
                            scanline_5.startX = i7;
                            scanline_5.startS = i10;
                        }
                        if (i7 > scanline_5.endX) {
                            scanline_5.endX = i7;
                            scanline_5.endS = i10;
                        }
                        i7 += k8;
                        i10 += i11;
                    }

                }
            }

            if (minY < baseY - clipY)
                minY = baseY - clipY;
        }
        if (mousePickingActive && mousePickedCount < mousePickedMax && mouseY >= minY && mouseY < maxY) {
            Scanline scanline_1 = scanlines[mouseY];
            if (mouseX >= scanline_1.startX >> 8 && mouseX <= scanline_1.endX >> 8 && scanline_1.startX <= scanline_1.endX && !gameModel.unpickable && gameModel.isLocalPlayer[pid] == 0) {
                mousePickedModels[mousePickedCount] = gameModel;
                mousePickedFaces[mousePickedCount] = pid;
                mousePickedCount++;
            }
        }
    }

    private void rasterize(int i, int j, int k, int ai[], int ai1[], int ai2[], int l,
                           Model gameModel) {
        if (l == -2)
            return;
        if (l >= 0) {
            if (l >= textureCount)
                l = 0;
            prepareTexture(l);
            int i1 = ai[0];
            int k1 = ai1[0];
            int j2 = ai2[0];
            int i3 = i1 - ai[1];
            int k3 = k1 - ai1[1];
            int i4 = j2 - ai2[1];
            k--;
            int i6 = ai[k] - i1;
            int j7 = ai1[k] - k1;
            int k8 = ai2[k] - j2;
            if (textureDimension[l] == 1) {
                int l9 = i6 * k1 - j7 * i1 << 12;
                int k10 = j7 * j2 - k8 * k1 << (5 - viewDistance) + 7 + 4;
                int i11 = k8 * i1 - i6 * j2 << (5 - viewDistance) + 7;
                int k11 = i3 * k1 - k3 * i1 << 12;
                int i12 = k3 * j2 - i4 * k1 << (5 - viewDistance) + 7 + 4;
                int k12 = i4 * i1 - i3 * j2 << (5 - viewDistance) + 7;
                int i13 = k3 * i6 - i3 * j7 << 5;
                int k13 = i4 * j7 - k3 * k8 << (5 - viewDistance) + 4;
                int i14 = i3 * k8 - i4 * i6 >> viewDistance - 5;
                int k14 = k10 >> 4;
                int i15 = i12 >> 4;
                int k15 = k13 >> 4;
                int i16 = minY - baseY;
                int k16 = width;
                int i17 = baseX + minY * k16;
                byte byte1 = 1;
                l9 += i11 * i16;
                k11 += k12 * i16;
                i13 += i14 * i16;
                if (interlace) {
                    if ((minY & 1) == 1) {
                        minY++;
                        l9 += i11;
                        k11 += k12;
                        i13 += i14;
                        i17 += k16;
                    }
                    i11 <<= 1;
                    k12 <<= 1;
                    i14 <<= 1;
                    k16 <<= 1;
                    byte1 = 2;
                }
                if (gameModel.textureTranslucent) {
                    for (i = minY; i < maxY; i += byte1) {
                        Scanline scanline_3 = scanlines[i];
                        j = scanline_3.startX >> 8;
                        int k17 = scanline_3.endX >> 8;
                        int k20 = k17 - j;
                        if (k20 <= 0) {
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        } else {
                            int i22 = scanline_3.startS;
                            int k23 = (scanline_3.endS - i22) / k20;
                            if (j < -clipX) {
                                i22 += (-clipX - j) * k23;
                                j = -clipX;
                                k20 = k17 - j;
                            }
                            if (k17 > clipX) {
                                int l17 = clipX;
                                k20 = l17 - j;
                            }
                            textureTranslucentScanline(raster, texturePixels[l], 0, 0, l9 + k14 * j, k11 + i15 * j, i13 + k15 * j, k10, i12, k13, k20, i17 + j, i22, k23 << 2);
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        }
                    }

                    return;
                }
                if (!textureBackTransparent[l]) {
                    for (i = minY; i < maxY; i += byte1) {
                        Scanline scanline_4 = scanlines[i];
                        j = scanline_4.startX >> 8;
                        int i18 = scanline_4.endX >> 8;
                        int l20 = i18 - j;
                        if (l20 <= 0) {
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        } else {
                            int j22 = scanline_4.startS;
                            int l23 = (scanline_4.endS - j22) / l20;
                            if (j < -clipX) {
                                j22 += (-clipX - j) * l23;
                                j = -clipX;
                                l20 = i18 - j;
                            }
                            if (i18 > clipX) {
                                int j18 = clipX;
                                l20 = j18 - j;
                            }
                            textureScanline(raster, texturePixels[l], 0, 0, l9 + k14 * j, k11 + i15 * j, i13 + k15 * j, k10, i12, k13, l20, i17 + j, j22, l23 << 2);
                            l9 += i11;
                            k11 += k12;
                            i13 += i14;
                            i17 += k16;
                        }
                    }

                    return;
                }
                for (i = minY; i < maxY; i += byte1) {
                    Scanline scanline_5 = scanlines[i];
                    j = scanline_5.startX >> 8;
                    int k18 = scanline_5.endX >> 8;
                    int i21 = k18 - j;
                    if (i21 <= 0) {
                        l9 += i11;
                        k11 += k12;
                        i13 += i14;
                        i17 += k16;
                    } else {
                        int k22 = scanline_5.startS;
                        int i24 = (scanline_5.endS - k22) / i21;
                        if (j < -clipX) {
                            k22 += (-clipX - j) * i24;
                            j = -clipX;
                            i21 = k18 - j;
                        }
                        if (k18 > clipX) {
                            int l18 = clipX;
                            i21 = l18 - j;
                        }
                        textureBackTranslucentScanline(raster, 0, 0, 0, texturePixels[l], l9 + k14 * j, k11 + i15 * j, i13 + k15 * j, k10, i12, k13, i21, i17 + j, k22, i24);
                        l9 += i11;
                        k11 += k12;
                        i13 += i14;
                        i17 += k16;
                    }
                }

                return;
            }
            int i10 = i6 * k1 - j7 * i1 << 11;
            int l10 = j7 * j2 - k8 * k1 << (5 - viewDistance) + 6 + 4;
            int j11 = k8 * i1 - i6 * j2 << (5 - viewDistance) + 6;
            int l11 = i3 * k1 - k3 * i1 << 11;
            int j12 = k3 * j2 - i4 * k1 << (5 - viewDistance) + 6 + 4;
            int l12 = i4 * i1 - i3 * j2 << (5 - viewDistance) + 6;
            int j13 = k3 * i6 - i3 * j7 << 5;
            int l13 = i4 * j7 - k3 * k8 << (5 - viewDistance) + 4;
            int j14 = i3 * k8 - i4 * i6 >> viewDistance - 5;
            int l14 = l10 >> 4;
            int j15 = j12 >> 4;
            int l15 = l13 >> 4;
            int j16 = minY - baseY;
            int l16 = width;
            int j17 = baseX + minY * l16;
            byte byte2 = 1;
            i10 += j11 * j16;
            l11 += l12 * j16;
            j13 += j14 * j16;
            if (interlace) {
                if ((minY & 1) == 1) {
                    minY++;
                    i10 += j11;
                    l11 += l12;
                    j13 += j14;
                    j17 += l16;
                }
                j11 <<= 1;
                l12 <<= 1;
                j14 <<= 1;
                l16 <<= 1;
                byte2 = 2;
            }
            if (gameModel.textureTranslucent) {
                for (i = minY; i < maxY; i += byte2) {
                    Scanline scanline_6 = scanlines[i];
                    j = scanline_6.startX >> 8;
                    int i19 = scanline_6.endX >> 8;
                    int j21 = i19 - j;
                    if (j21 <= 0) {
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    } else {
                        int l22 = scanline_6.startS;
                        int j24 = (scanline_6.endS - l22) / j21;
                        if (j < -clipX) {
                            l22 += (-clipX - j) * j24;
                            j = -clipX;
                            j21 = i19 - j;
                        }
                        if (i19 > clipX) {
                            int j19 = clipX;
                            j21 = j19 - j;
                        }
                        textureTranslucentScanline2(raster, texturePixels[l], 0, 0, i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10, j12, l13, j21, j17 + j, l22, j24);
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    }
                }

                return;
            }
            if (!textureBackTransparent[l]) {
                for (i = minY; i < maxY; i += byte2) {
                    Scanline scanline_7 = scanlines[i];
                    j = scanline_7.startX >> 8;
                    int k19 = scanline_7.endX >> 8;
                    int k21 = k19 - j;
                    if (k21 <= 0) {
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    } else {
                        int i23 = scanline_7.startS;
                        int k24 = (scanline_7.endS - i23) / k21;
                        if (j < -clipX) {
                            i23 += (-clipX - j) * k24;
                            j = -clipX;
                            k21 = k19 - j;
                        }
                        if (k19 > clipX) {
                            int l19 = clipX;
                            k21 = l19 - j;
                        }
                        textureScanline2(raster, texturePixels[l], 0, 0, i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10, j12, l13, k21, j17 + j, i23, k24);
                        i10 += j11;
                        l11 += l12;
                        j13 += j14;
                        j17 += l16;
                    }
                }

                return;
            }
            for (i = minY; i < maxY; i += byte2) {
                Scanline scanline = scanlines[i];
                j = scanline.startX >> 8;
                int i20 = scanline.endX >> 8;
                int l21 = i20 - j;
                if (l21 <= 0) {
                    i10 += j11;
                    l11 += l12;
                    j13 += j14;
                    j17 += l16;
                } else {
                    int j23 = scanline.startS;
                    int l24 = (scanline.endS - j23) / l21;
                    if (j < -clipX) {
                        j23 += (-clipX - j) * l24;
                        j = -clipX;
                        l21 = i20 - j;
                    }
                    if (i20 > clipX) {
                        int j20 = clipX;
                        l21 = j20 - j;
                    }
                    textureBackTranslucentScanline2(raster, 0, 0, 0, texturePixels[l], i10 + l14 * j, l11 + j15 * j, j13 + l15 * j, l10, j12, l13, l21, j17 + j, j23, l24);
                    i10 += j11;
                    l11 += l12;
                    j13 += j14;
                    j17 += l16;
                }
            }

            return;
        }
        for (int j1 = 0; j1 < rampCount; j1++) {
            if (gradientBase[j1] == l) {
                anIntArray377 = gradientRamps[j1];
                break;
            }
            if (j1 == rampCount - 1) {
                int l1 = (int) (Math.random() * (double) rampCount);
                gradientBase[l1] = l;
                l = -1 - l;
                int k2 = (l >> 10 & 0x1f) * 8;
                int j3 = (l >> 5 & 0x1f) * 8;
                int l3 = (l & 0x1f) * 8;
                for (int j4 = 0; j4 < 256; j4++) {
                    int j6 = j4 * j4;
                    int k7 = (k2 * j6) / 0x10000;
                    int l8 = (j3 * j6) / 0x10000;
                    int j10 = (l3 * j6) / 0x10000;
                    gradientRamps[l1][255 - j4] = (k7 << 16) + (l8 << 8) + j10;
                }

                anIntArray377 = gradientRamps[l1];
            }
        }

        int i2 = width;
        int l2 = baseX + minY * i2;
        byte byte0 = 1;
        if (interlace) {
            if ((minY & 1) == 1) {
                minY++;
                l2 += i2;
            }
            i2 <<= 1;
            byte0 = 2;
        }
        if (gameModel.transparent) {
            for (i = minY; i < maxY; i += byte0) {
                Scanline scanline = scanlines[i];
                j = scanline.startX >> 8;
                int k4 = scanline.endX >> 8;
                int k6 = k4 - j;
                if (k6 <= 0) {
                    l2 += i2;
                } else {
                    int l7 = scanline.startS;
                    int i9 = (scanline.endS - l7) / k6;
                    if (j < -clipX) {
                        l7 += (-clipX - j) * i9;
                        j = -clipX;
                        k6 = k4 - j;
                    }
                    if (k4 > clipX) {
                        int l4 = clipX;
                        k6 = l4 - j;
                    }
                    textureGradientScanline(raster, -k6, l2 + j, 0, anIntArray377, l7, i9);
                    l2 += i2;
                }
            }

            return;
        }
        if (wideBand) {
            for (i = minY; i < maxY; i += byte0) {
                Scanline scanline_1 = scanlines[i];
                j = scanline_1.startX >> 8;
                int i5 = scanline_1.endX >> 8;
                int l6 = i5 - j;
                if (l6 <= 0) {
                    l2 += i2;
                } else {
                    int i8 = scanline_1.startS;
                    int j9 = (scanline_1.endS - i8) / l6;
                    if (j < -clipX) {
                        i8 += (-clipX - j) * j9;
                        j = -clipX;
                        l6 = i5 - j;
                    }
                    if (i5 > clipX) {
                        int j5 = clipX;
                        l6 = j5 - j;
                    }
                    gradientScanline(raster, -l6, l2 + j, 0, anIntArray377, i8, j9);
                    l2 += i2;
                }
            }

            return;
        }
        for (i = minY; i < maxY; i += byte0) {
            Scanline scanline_2 = scanlines[i];
            j = scanline_2.startX >> 8;
            int k5 = scanline_2.endX >> 8;
            int i7 = k5 - j;
            if (i7 <= 0) {
                l2 += i2;
            } else {
                int j8 = scanline_2.startS;
                int k9 = (scanline_2.endS - j8) / i7;
                if (j < -clipX) {
                    j8 += (-clipX - j) * k9;
                    j = -clipX;
                    i7 = k5 - j;
                }
                if (k5 > clipX) {
                    int l5 = clipX;
                    i7 = l5 - j;
                }
                gradientScanline2(raster, -i7, l2 + j, 0, anIntArray377, j8, k9);
                l2 += i2;
            }
        }

    }

    public void setCamera(int x, int z, int y, int pitch, int yaw, int roll, int distance) {
        pitch &= 0x3ff;
        yaw &= 0x3ff;
        roll &= 0x3ff;
        cameraYaw = 1024 - pitch & 0x3ff;
        cameraPitch = 1024 - yaw & 0x3ff;
        cameraRoll = 1024 - roll & 0x3ff;
        int l1 = 0;
        int i2 = 0;
        int j2 = distance;
        if (pitch != 0) {
            int k2 = sin2048Cache[pitch];
            int j3 = sin2048Cache[pitch + 1024];
            int i4 = i2 * j3 - j2 * k2 >> 15;
            j2 = i2 * k2 + j2 * j3 >> 15;
            i2 = i4;
        }
        if (yaw != 0) {
            int l2 = sin2048Cache[yaw];
            int k3 = sin2048Cache[yaw + 1024];
            int j4 = j2 * l2 + l1 * k3 >> 15;
            j2 = j2 * k3 - l1 * l2 >> 15;
            l1 = j4;
        }
        if (roll != 0) {
            int i3 = sin2048Cache[roll];
            int l3 = sin2048Cache[roll + 1024];
            int k4 = i2 * i3 + l1 * l3 >> 15;
            i2 = i2 * l3 - l1 * i3 >> 15;
            l1 = k4;
        }
        cameraX = x - l1;
        cameraY = z - i2;
        cameraZ = y - j2;
    }

    private void initialisePolygon3d(int i) {
        Polygon polygon = visiblePolygons[i];
        Model gameModel = polygon.model;
        int face = polygon.face;
        int faceVertices[] = gameModel.faceVertices[face];
        int faceNumVertices = gameModel.faceNumVertices[face];
        int faceCameraNormalScale = gameModel.normalScale[face];
        int vcx = gameModel.projectVertexX[faceVertices[0]];
        int vcy = gameModel.projectVertexY[faceVertices[0]];
        int vcz = gameModel.projectVertexZ[faceVertices[0]];
        int vcx1 = gameModel.projectVertexX[faceVertices[1]] - vcx;
        int vcy1 = gameModel.projectVertexY[faceVertices[1]] - vcy;
        int vcz1 = gameModel.projectVertexZ[faceVertices[1]] - vcz;
        int vcx2 = gameModel.projectVertexX[faceVertices[2]] - vcx;
        int vcy2 = gameModel.projectVertexY[faceVertices[2]] - vcy;
        int vcz2 = gameModel.projectVertexZ[faceVertices[2]] - vcz;
        int t1 = vcy1 * vcz2 - vcy2 * vcz1;
        int t2 = vcz1 * vcx2 - vcz2 * vcx1;
        int t3 = vcx1 * vcy2 - vcx2 * vcy1;
        if (faceCameraNormalScale == -1) {
            faceCameraNormalScale = 0;
            /* andrew, what the fuck is this bro */
            for (; t1 > 25000 || t2 > 25000 || t3 > 25000 || t1 < -25000 || t2 < -25000 || t3 < -25000; t3 >>= 1) {
                faceCameraNormalScale++;
                t1 >>= 1;
                t2 >>= 1;
            }

            gameModel.normalScale[face] = faceCameraNormalScale;
            gameModel.normalMagnitude[face] = (int) ((double) normalMagnitude * Math.sqrt(t1 * t1 + t2 * t2 + t3 * t3));
        } else {
            t1 >>= faceCameraNormalScale;
            t2 >>= faceCameraNormalScale;
            t3 >>= faceCameraNormalScale;
        }
        polygon.visibility = vcx * t1 + vcy * t2 + vcz * t3;
        polygon.normalX = t1;
        polygon.normalY = t2;
        polygon.normalZ = t3;
        int j4 = gameModel.projectVertexZ[faceVertices[0]];
        int k4 = j4;
        int l4 = gameModel.vertexViewX[faceVertices[0]];
        int i5 = l4;
        int j5 = gameModel.vertexViewY[faceVertices[0]];
        int k5 = j5;
        for (int l5 = 1; l5 < faceNumVertices; l5++) {
            int i1 = gameModel.projectVertexZ[faceVertices[l5]];
            if (i1 > k4)
                k4 = i1;
            else if (i1 < j4)
                j4 = i1;
            i1 = gameModel.vertexViewX[faceVertices[l5]];
            if (i1 > i5)
                i5 = i1;
            else if (i1 < l4)
                l4 = i1;
            i1 = gameModel.vertexViewY[faceVertices[l5]];
            if (i1 > k5)
                k5 = i1;
            else if (i1 < j5)
                j5 = i1;
        }

        polygon.minZ = j4;
        polygon.maxZ = k4;
        polygon.minPlaneX = l4;
        polygon.maxPlaneX = i5;
        polygon.minPlaneY = j5;
        polygon.maxPlaneY = k5;
    }

    private void initialisePolygon2d(int i) {
        Polygon polygon = visiblePolygons[i];
        Model gameModel = polygon.model;
        int j = polygon.face;
        int ai[] = gameModel.faceVertices[j];
        int l = 0;
        int i1 = 0;
        int j1 = 1;
        int k1 = gameModel.projectVertexX[ai[0]];
        int l1 = gameModel.projectVertexY[ai[0]];
        int i2 = gameModel.projectVertexZ[ai[0]];
        gameModel.normalMagnitude[j] = 1;
        gameModel.normalScale[j] = 0;
        polygon.visibility = k1 * l + l1 * i1 + i2 * j1;
        polygon.normalX = l;
        polygon.normalY = i1;
        polygon.normalZ = j1;
        int j2 = gameModel.projectVertexZ[ai[0]];
        int k2 = j2;
        int l2 = gameModel.vertexViewX[ai[0]];
        int i3 = l2;
        if (gameModel.vertexViewX[ai[1]] < l2)
            l2 = gameModel.vertexViewX[ai[1]];
        else
            i3 = gameModel.vertexViewX[ai[1]];
        int j3 = gameModel.vertexViewY[ai[1]];
        int k3 = gameModel.vertexViewY[ai[0]];
        int k = gameModel.projectVertexZ[ai[1]];
        if (k > k2)
            k2 = k;
        else if (k < j2)
            j2 = k;
        k = gameModel.vertexViewX[ai[1]];
        if (k > i3)
            i3 = k;
        else if (k < l2)
            l2 = k;
        k = gameModel.vertexViewY[ai[1]];
        if (k > k3)
            k3 = k;
        else if (k < j3)
            j3 = k;
        polygon.minZ = j2;
        polygon.maxZ = k2;
        polygon.minPlaneX = l2 - 20;
        polygon.maxPlaneX = i3 + 20;
        polygon.minPlaneY = j3;
        polygon.maxPlaneY = k3;
    }

    private boolean separatePolygon(Polygon polyA, Polygon polyB) {
        if (polyA.minPlaneX >= polyB.maxPlaneX)
            return true;
        if (polyB.minPlaneX >= polyA.maxPlaneX)
            return true;
        if (polyA.minPlaneY >= polyB.maxPlaneY)
            return true;
        if (polyB.minPlaneY >= polyA.maxPlaneY)
            return true;
        if (polyA.minZ >= polyB.maxZ)
            return true;
        if (polyB.minZ > polyA.maxZ)
            return false;
        Model modelA = polyA.model;
        Model modelB = polyB.model;
        int i = polyA.face;
        int j = polyB.face;
        int ai[] = modelA.faceVertices[i];
        int ai1[] = modelB.faceVertices[j];
        int k = modelA.faceNumVertices[i];
        int l = modelB.faceNumVertices[j];
        int k2 = modelB.projectVertexX[ai1[0]];
        int l2 = modelB.projectVertexY[ai1[0]];
        int i3 = modelB.projectVertexZ[ai1[0]];
        int j3 = polyB.normalX;
        int k3 = polyB.normalY;
        int l3 = polyB.normalZ;
        int i4 = modelB.normalMagnitude[j];
        int j4 = polyB.visibility;
        boolean flag = false;
        for (int k4 = 0; k4 < k; k4++) {
            int i1 = ai[k4];
            int i2 = (k2 - modelA.projectVertexX[i1]) * j3 + (l2 - modelA.projectVertexY[i1]) * k3 + (i3 - modelA.projectVertexZ[i1]) * l3;
            if ((i2 >= -i4 || j4 >= 0) && (i2 <= i4 || j4 <= 0))
                continue;
            flag = true;
            break;
        }

        if (!flag)
            return true;
        k2 = modelA.projectVertexX[ai[0]];
        l2 = modelA.projectVertexY[ai[0]];
        i3 = modelA.projectVertexZ[ai[0]];
        j3 = polyA.normalX;
        k3 = polyA.normalY;
        l3 = polyA.normalZ;
        i4 = modelA.normalMagnitude[i];
        j4 = polyA.visibility;
        flag = false;
        for (int l4 = 0; l4 < l; l4++) {
            int j1 = ai1[l4];
            int j2 = (k2 - modelB.projectVertexX[j1]) * j3 + (l2 - modelB.projectVertexY[j1]) * k3 + (i3 - modelB.projectVertexZ[j1]) * l3;
            if ((j2 >= -i4 || j4 <= 0) && (j2 <= i4 || j4 >= 0))
                continue;
            flag = true;
            break;
        }

        if (!flag)
            return true;
        int ai2[];
        int ai3[];
        if (k == 2) {
            ai2 = new int[4];
            ai3 = new int[4];
            int i5 = ai[0];
            int k1 = ai[1];
            ai2[0] = modelA.vertexViewX[i5] - 20;
            ai2[1] = modelA.vertexViewX[k1] - 20;
            ai2[2] = modelA.vertexViewX[k1] + 20;
            ai2[3] = modelA.vertexViewX[i5] + 20;
            ai3[0] = ai3[3] = modelA.vertexViewY[i5];
            ai3[1] = ai3[2] = modelA.vertexViewY[k1];
        } else {
            ai2 = new int[k];
            ai3 = new int[k];
            for (int j5 = 0; j5 < k; j5++) {
                int i6 = ai[j5];
                ai2[j5] = modelA.vertexViewX[i6];
                ai3[j5] = modelA.vertexViewY[i6];
            }

        }
        int ai4[];
        int ai5[];
        if (l == 2) {
            ai4 = new int[4];
            ai5 = new int[4];
            int k5 = ai1[0];
            int l1 = ai1[1];
            ai4[0] = modelB.vertexViewX[k5] - 20;
            ai4[1] = modelB.vertexViewX[l1] - 20;
            ai4[2] = modelB.vertexViewX[l1] + 20;
            ai4[3] = modelB.vertexViewX[k5] + 20;
            ai5[0] = ai5[3] = modelB.vertexViewY[k5];
            ai5[1] = ai5[2] = modelB.vertexViewY[l1];
        } else {
            ai4 = new int[l];
            ai5 = new int[l];
            for (int l5 = 0; l5 < l; l5++) {
                int j6 = ai1[l5];
                ai4[l5] = modelB.vertexViewX[j6];
                ai5[l5] = modelB.vertexViewY[j6];
            }

        }
        return !intersect(ai2, ai3, ai4, ai5);
    }

    private boolean heuristicPolygon(Polygon polygon, Polygon polygon_1) {
        Model gameModel = polygon.model;
        Model gameModel_1 = polygon_1.model;
        int i = polygon.face;
        int j = polygon_1.face;
        int ai[] = gameModel.faceVertices[i];
        int ai1[] = gameModel_1.faceVertices[j];
        int k = gameModel.faceNumVertices[i];
        int l = gameModel_1.faceNumVertices[j];
        int i2 = gameModel_1.projectVertexX[ai1[0]];
        int j2 = gameModel_1.projectVertexY[ai1[0]];
        int k2 = gameModel_1.projectVertexZ[ai1[0]];
        int l2 = polygon_1.normalX;
        int i3 = polygon_1.normalY;
        int j3 = polygon_1.normalZ;
        int k3 = gameModel_1.normalMagnitude[j];
        int l3 = polygon_1.visibility;
        boolean flag = false;
        for (int i4 = 0; i4 < k; i4++) {
            int i1 = ai[i4];
            int k1 = (i2 - gameModel.projectVertexX[i1]) * l2 + (j2 - gameModel.projectVertexY[i1]) * i3 + (k2 - gameModel.projectVertexZ[i1]) * j3;
            if ((k1 >= -k3 || l3 >= 0) && (k1 <= k3 || l3 <= 0))
                continue;
            flag = true;
            break;
        }

        if (!flag)
            return true;
        i2 = gameModel.projectVertexX[ai[0]];
        j2 = gameModel.projectVertexY[ai[0]];
        k2 = gameModel.projectVertexZ[ai[0]];
        l2 = polygon.normalX;
        i3 = polygon.normalY;
        j3 = polygon.normalZ;
        k3 = gameModel.normalMagnitude[i];
        l3 = polygon.visibility;
        flag = false;
        for (int j4 = 0; j4 < l; j4++) {
            int j1 = ai1[j4];
            int l1 = (i2 - gameModel_1.projectVertexX[j1]) * l2 + (j2 - gameModel_1.projectVertexY[j1]) * i3 + (k2 - gameModel_1.projectVertexZ[j1]) * j3;
            if ((l1 >= -k3 || l3 <= 0) && (l1 <= k3 || l3 >= 0))
                continue;
            flag = true;
            break;
        }

        return !flag;
    }

    public void allocateTextures(int count, int something7, int something11) {
        textureCount = count;
        textureColoursUsed = new byte[count][];
        textureColourList = new int[count][];
        textureDimension = new int[count];
        textureLoadedNumber = new long[count];
        textureBackTransparent = new boolean[count];
        texturePixels = new int[count][];
        textureCountLoaded = 0L;
        textureColours64 = new int[something7][];// 64x64 rgba
        textureColours128 = new int[something11][];// 128x128 rgba
    }

    public void defineTexture(int id, byte usedColours[], int colours[], int wide128) {
        textureColoursUsed[id] = usedColours;
        textureColourList[id] = colours;
        textureDimension[id] = wide128;// is 1 if the texture is 128+ pixels wide, 0 if <128
        textureLoadedNumber[id] = 0L;// as in the current loaded texture count when its loaded
        textureBackTransparent[id] = false;
        texturePixels[id] = null;
        prepareTexture(id);
    }

    public void prepareTexture(int id) {
        if (id < 0)
            return;
        textureLoadedNumber[id] = textureCountLoaded++;
        if (texturePixels[id] != null)
            return;
        if (textureDimension[id] == 0) {// is 64 pixels wide
            for (int j = 0; j < textureColours64.length; j++)
                if (textureColours64[j] == null) {
                    textureColours64[j] = new int[16384];
                    texturePixels[id] = textureColours64[j];
                    setTexturePixels(id);
                    return;
                }

            long GIGALONG = 1L << 30;// almost as large as exemplar's nas storage
            int wut = 0;
            for (int k1 = 0; k1 < textureCount; k1++)
                if (k1 != id && textureDimension[k1] == 0 && texturePixels[k1] != null && textureLoadedNumber[k1] < GIGALONG) {
                    GIGALONG = textureLoadedNumber[k1];
                    wut = k1;
                }

            texturePixels[id] = texturePixels[wut];
            texturePixels[wut] = null;
            setTexturePixels(id);
            return;
        }
        // is 128 wide
        for (int k = 0; k < textureColours128.length; k++)
            if (textureColours128[k] == null) {
                textureColours128[k] = new int[0x10000];
                texturePixels[id] = textureColours128[k];
                setTexturePixels(id);
                return;
            }

        long GIGALONG = 1L << 30;// 1G 2G 3G... 4G?
        int wat = 0;
        for (int i2 = 0; i2 < textureCount; i2++)
            if (i2 != id && textureDimension[i2] == 1 && texturePixels[i2] != null && textureLoadedNumber[i2] < GIGALONG) {
                GIGALONG = textureLoadedNumber[i2];
                wat = i2;
            }

        texturePixels[id] = texturePixels[wat];
        texturePixels[wat] = null;
        setTexturePixels(id);
    }

    private void setTexturePixels(int id) {
        char textureWidth;
        if (textureDimension[id] == 0)
            textureWidth = 64;
        else
            textureWidth = 128;
        int colours[] = texturePixels[id];
        int colourCount = 0;
        for (int x = 0; x < textureWidth; x++) {
            for (int y = 0; y < textureWidth; y++) {
                int colour = textureColourList[id][textureColoursUsed[id][y + x * textureWidth] & 0xff];
                colour &= 0xf8f8ff;
                if (colour == 0)
                    colour = 1;
                else if (colour == 0xf800ff) {
                    colour = 0;
                    textureBackTransparent[id] = true;
                }
                colours[colourCount++] = colour;
            }

        }

        for (int i1 = 0; i1 < colourCount; i1++) {
            int colour = colours[i1];// ??
            colours[colourCount + i1] = colour - (colour >>> 3) & 0xf8f8ff;
            colours[colourCount * 2 + i1] = colour - (colour >>> 2) & 0xf8f8ff;
            colours[colourCount * 3 + i1] = colour - (colour >>> 2) - (colour >>> 3) & 0xf8f8ff;
        }
    }

    public void animateTexture(int id) {
        if (texturePixels[id] == null)
            return;
        int colours[] = texturePixels[id];
        for (int i = 0; i < 64; i++) {
            int k = i + 4032;
            int l = colours[k];
            for (int j1 = 0; j1 < 63; j1++) {
                colours[k] = colours[k - 64];
                k -= 64;
            }

            texturePixels[id][k] = l;
        }

        char c = 4096;
        for (int i1 = 0; i1 < c; i1++) {
            int k1 = colours[i1];
            colours[c + i1] = k1 - (k1 >>> 3) & 0xf8f8ff;
            colours[c * 2 + i1] = k1 - (k1 >>> 2) & 0xf8f8ff;
            colours[c * 3 + i1] = k1 - (k1 >>> 2) - (k1 >>> 3) & 0xf8f8ff;
        }

    }

    public int method302(int i) {
        if (i == Terrain.COLOR_TRANSPARENT)
            return 0;
        prepareTexture(i);
        if (i >= 0)
            return texturePixels[i][0];
        if (i < 0) {
            i = -(i + 1);
            int j = i >> 10 & 0x1f;
            int k = i >> 5 & 0x1f;
            int l = i & 0x1f;
            return (j << 19) + (k << 11) + (l << 3);
        } else {
            return 0;
        }
    }

    public void setLight(int i, int j, int k) {
        if (i == 0 && j == 0 && k == 0)
            i = 32;
        for (int l = 0; l < modelCount; l++)
            models[l].setLight(i, j, k);

    }

    public void setLight(int i, int j, int k, int l, int i1) {
        if (k == 0 && l == 0 && i1 == 0)
            k = 32;
        for (int j1 = 0; j1 < modelCount; j1++)
            models[j1].setLight(i, j, k, l, i1);

    }

    public int method306(int i, int j, int k, int l, int i1) {
        if (l == j)
            return i;
        else
            return i + ((k - i) * (i1 - j)) / (l - j);
    }

    public boolean method307(int i, int j, int k, int l, boolean flag) {
        if (flag && i <= k || i < k) {
            if (i > l)
                return true;
            if (j > k)
                return true;
            if (j > l)
                return true;
            return !flag;
        }
        if (i < l)
            return true;
        if (j < k)
            return true;
        if (j < l)
            return true;
        else
            return flag;
    }

    public boolean method308(int i, int j, int k, boolean flag) {
        if (flag && i <= k || i < k) {
            if (j > k)
                return true;
            return !flag;
        }
        if (j < k)
            return true;
        else
            return flag;
    }

    public boolean intersect(int ai[], int ai1[], int ai2[], int ai3[]) {
        int i = ai.length;
        int j = ai2.length;
        byte byte0 = 0;
        int i20;
        int k20 = i20 = ai1[0];
        int k = 0;
        int j20;
        int l20 = j20 = ai3[0];
        int i1 = 0;
        for (int i21 = 1; i21 < i; i21++)
            if (ai1[i21] < i20) {
                i20 = ai1[i21];
                k = i21;
            } else if (ai1[i21] > k20)
                k20 = ai1[i21];

        for (int j21 = 1; j21 < j; j21++)
            if (ai3[j21] < j20) {
                j20 = ai3[j21];
                i1 = j21;
            } else if (ai3[j21] > l20)
                l20 = ai3[j21];

        if (j20 >= k20)
            return false;
        if (i20 >= l20)
            return false;
        int l;
        int j1;
        boolean flag;
        if (ai1[k] < ai3[i1]) {
            for (l = k; ai1[l] < ai3[i1]; l = (l + 1) % i) ;
            for (; ai1[k] < ai3[i1]; k = ((k - 1) + i) % i) ;
            int k1 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
            int k6 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
            int l10 = ai2[i1];
            flag = (k1 < l10) | (k6 < l10);
            if (method308(k1, k6, l10, flag))
                return true;
            j1 = (i1 + 1) % j;
            i1 = ((i1 - 1) + j) % j;
            if (k == l)
                byte0 = 1;
        } else {
            for (j1 = i1; ai3[j1] < ai1[k]; j1 = (j1 + 1) % j) ;
            for (; ai3[i1] < ai1[k]; i1 = ((i1 - 1) + j) % j) ;
            int l1 = ai[k];
            int i11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
            int l15 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
            flag = (l1 < i11) | (l1 < l15);
            if (method308(i11, l15, l1, !flag))
                return true;
            l = (k + 1) % i;
            k = ((k - 1) + i) % i;
            if (i1 == j1)
                byte0 = 2;
        }
        while (byte0 == 0)
            if (ai1[k] < ai1[l]) {
                if (ai1[k] < ai3[i1]) {
                    if (ai1[k] < ai3[j1]) {
                        int i2 = ai[k];
                        int l6 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai1[k]);
                        int j11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
                        int i16 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
                        if (method307(i2, l6, j11, i16, flag))
                            return true;
                        k = ((k - 1) + i) % i;
                        if (k == l)
                            byte0 = 1;
                    } else {
                        int j2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                        int i7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                        int k11 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                        int j16 = ai2[j1];
                        if (method307(j2, i7, k11, j16, flag))
                            return true;
                        j1 = (j1 + 1) % j;
                        if (i1 == j1)
                            byte0 = 2;
                    }
                } else if (ai3[i1] < ai3[j1]) {
                    int k2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                    int j7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                    int l11 = ai2[i1];
                    int k16 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
                    if (method307(k2, j7, l11, k16, flag))
                        return true;
                    i1 = ((i1 - 1) + j) % j;
                    if (i1 == j1)
                        byte0 = 2;
                } else {
                    int l2 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                    int k7 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                    int i12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                    int l16 = ai2[j1];
                    if (method307(l2, k7, i12, l16, flag))
                        return true;
                    j1 = (j1 + 1) % j;
                    if (i1 == j1)
                        byte0 = 2;
                }
            } else if (ai1[l] < ai3[i1]) {
                if (ai1[l] < ai3[j1]) {
                    int i3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai1[l]);
                    int l7 = ai[l];
                    int j12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[l]);
                    int i17 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
                    if (method307(i3, l7, j12, i17, flag))
                        return true;
                    l = (l + 1) % i;
                    if (k == l)
                        byte0 = 1;
                } else {
                    int j3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                    int i8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                    int k12 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                    int j17 = ai2[j1];
                    if (method307(j3, i8, k12, j17, flag))
                        return true;
                    j1 = (j1 + 1) % j;
                    if (i1 == j1)
                        byte0 = 2;
                }
            } else if (ai3[i1] < ai3[j1]) {
                int k3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                int j8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                int l12 = ai2[i1];
                int k17 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
                if (method307(k3, j8, l12, k17, flag))
                    return true;
                i1 = ((i1 - 1) + j) % j;
                if (i1 == j1)
                    byte0 = 2;
            } else {
                int l3 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                int k8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                int i13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                int l17 = ai2[j1];
                if (method307(l3, k8, i13, l17, flag))
                    return true;
                j1 = (j1 + 1) % j;
                if (i1 == j1)
                    byte0 = 2;
            }
        while (byte0 == 1)
            if (ai1[k] < ai3[i1]) {
                if (ai1[k] < ai3[j1]) {
                    int i4 = ai[k];
                    int j13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
                    int i18 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
                    return method308(j13, i18, i4, !flag);
                }
                int j4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                int l8 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                int k13 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                int j18 = ai2[j1];
                if (method307(j4, l8, k13, j18, flag))
                    return true;
                j1 = (j1 + 1) % j;
                if (i1 == j1)
                    byte0 = 0;
            } else if (ai3[i1] < ai3[j1]) {
                int k4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                int i9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                int l13 = ai2[i1];
                int k18 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai3[i1]);
                if (method307(k4, i9, l13, k18, flag))
                    return true;
                i1 = ((i1 - 1) + j) % j;
                if (i1 == j1)
                    byte0 = 0;
            } else {
                int l4 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[j1]);
                int j9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[j1]);
                int i14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai3[j1]);
                int l18 = ai2[j1];
                if (method307(l4, j9, i14, l18, flag))
                    return true;
                j1 = (j1 + 1) % j;
                if (i1 == j1)
                    byte0 = 0;
            }
        while (byte0 == 2)
            if (ai3[i1] < ai1[k]) {
                if (ai3[i1] < ai1[l]) {
                    int i5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
                    int k9 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
                    int j14 = ai2[i1];
                    return method308(i5, k9, j14, flag);
                }
                int j5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai1[l]);
                int l9 = ai[l];
                int k14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[l]);
                int i19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
                if (method307(j5, l9, k14, i19, flag))
                    return true;
                l = (l + 1) % i;
                if (k == l)
                    byte0 = 0;
            } else if (ai1[k] < ai1[l]) {
                int k5 = ai[k];
                int i10 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai1[k]);
                int l14 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
                int j19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
                if (method307(k5, i10, l14, j19, flag))
                    return true;
                k = ((k - 1) + i) % i;
                if (k == l)
                    byte0 = 0;
            } else {
                int l5 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai1[l]);
                int j10 = ai[l];
                int i15 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[l]);
                int k19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[l]);
                if (method307(l5, j10, i15, k19, flag))
                    return true;
                l = (l + 1) % i;
                if (k == l)
                    byte0 = 0;
            }
        if (ai1[k] < ai3[i1]) {
            int i6 = ai[k];
            int j15 = method306(ai2[(i1 + 1) % j], ai3[(i1 + 1) % j], ai2[i1], ai3[i1], ai1[k]);
            int l19 = method306(ai2[((j1 - 1) + j) % j], ai3[((j1 - 1) + j) % j], ai2[j1], ai3[j1], ai1[k]);
            return method308(j15, l19, i6, !flag);
        }
        int j6 = method306(ai[(k + 1) % i], ai1[(k + 1) % i], ai[k], ai1[k], ai3[i1]);
        int k10 = method306(ai[((l - 1) + i) % i], ai1[((l - 1) + i) % i], ai[l], ai1[l], ai3[i1]);
        int k15 = ai2[i1];
        return method308(j6, k10, k15, flag);
    }
    
    private class Scanline {

        int startX, endX, startS, endS;
        
    }
    
    private class Polygon {

        protected int minPlaneX, minPlaneY, maxPlaneX, maxPlaneY, minZ, maxZ, face,
            depth, normalX, normalY, normalZ,  visibility, facefill, index, index2;
        protected Model model;
        protected boolean skipSomething;

        public Polygon() {
            skipSomething = false;
            index2 = -1;
        }
        
    }

}