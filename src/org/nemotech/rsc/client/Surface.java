package org.nemotech.rsc.client;

import java.awt.*;
import java.awt.image.*;

import org.nemotech.rsc.util.Util;

public class Surface implements ImageProducer, ImageObserver {

    public static int anInt346;
    public static int anInt347;
    public static int anInt348;
    static byte gameFonts[][] = new byte[50][];
    static int characterWidth[];
    public int width2;
    public int height2;
    public int pixels[];
    public Image image;
    public int surfacePixels[][];
    public byte spriteColoursUsed[][];
    public int spriteColourList[][];
    public int spriteWidth[];
    public int spriteHeight[];
    public int spriteTranslateX[];
    public int spriteTranslateY[];
    public int spriteWidthFull[];
    public int spriteHeightFull[];
    public boolean spriteTranslate[];
    public boolean interlace;
    public boolean loggedIn;
    private ColorModel colorModel;
    protected ImageConsumer imageConsumer;
    int landscapeColours[];
    int anIntArray340[];
    int anIntArray341[];
    int anIntArray342[];
    int anIntArray343[];
    int anIntArray344[];
    int anIntArray345[];
    private int boundsTopY;
    protected int boundsBottomY;
    private int boundsTopX;
    protected int boundsBottomX;
    
    public final int fontHeight(int font) {
        return font != 0 ? (font != 1 ? (font == 2 ? 14 : (font == 3 ? 15 : (font != 4 ? (font != 5 ? (font == 6 ? 24
            : (font != 7 ? this.c(font) : 29)) : 19) : 15))) : 14) : 12;
    }
    
    private int c(int var2) {
        if (var2 != 0) {
            return gameFonts[var2][8] - 1;
        } else {
            return gameFonts[var2][8] - 2;
        }
    }

    static {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
        characterWidth = new int[256];
        for (int i = 0; i < 256; i++) {
            int j = s.indexOf(i);
            if (j == -1)
                j = 74;
            characterWidth[i] = j * 9;
        }

    }
    
    public void drawPixels(int pixels[][], int drawx, int drawy, int width, int height) {
        for(int x = drawx; x < drawx + width; x++)
            for(int y = drawy; y < drawy + height; y++)
                this.pixels[x + y * width2] = pixels[x - drawx][y - drawy];
    }

    public Surface(int width, int height, int limit, Component component) {
        interlace = false;
        loggedIn = false;
        boundsBottomY = height;
        boundsBottomX = width;
        width2 = width;
        height2 = height;
        pixels = new int[width * height];
        surfacePixels = new int[limit][];
        spriteTranslate = new boolean[limit];
        spriteColoursUsed = new byte[limit][];
        spriteColourList = new int[limit][];
        spriteWidth = new int[limit];
        spriteHeight = new int[limit];
        spriteWidthFull = new int[limit];
        spriteHeightFull = new int[limit];
        spriteTranslateX = new int[limit];
        spriteTranslateY = new int[limit];
        if (width > 1 && height > 1 && component != null) {
            colorModel = new DirectColorModel(32, 0xff0000, 65280, 255);
            int l = width2 * height2;
            for (int i1 = 0; i1 < l; i1++)
                pixels[i1] = 0;

            image = component.createImage(this);
            setcomplete();
            component.prepareImage(image, component);
            setcomplete();
            component.prepareImage(image, component);
            setcomplete();
            component.prepareImage(image, component);
        }
    }

    public static int rgb2long(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    public static void createFont(byte bytes[], int id) {
        gameFonts[id] = bytes;
    }

    public synchronized void addConsumer(ImageConsumer imageconsumer) {
        this.imageConsumer = imageconsumer;
        imageconsumer.setDimensions(width2, height2);
        imageconsumer.setProperties(null);
        imageconsumer.setColorModel(colorModel);
        imageconsumer.setHints(14);
    }

    public synchronized boolean isConsumer(ImageConsumer imageconsumer) {
        return this.imageConsumer == imageconsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageconsumer) {
        if (this.imageConsumer == imageconsumer)
            this.imageConsumer = null;
    }

    public void startProduction(ImageConsumer imageconsumer) {
        addConsumer(imageconsumer);
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer) {
        System.out.println("TDLR");
    }

    public synchronized void setcomplete() {
        if (imageConsumer == null) {
            return;
        } else {
            imageConsumer.setPixels(0, 0, width2, height2, colorModel, pixels, 0, width2);
            imageConsumer.imageComplete(2);
            return;
        }
    }

    public void setBounds(int x1, int y1, int x2, int y2) {
        if (x1 < 0)
            x1 = 0;
        if (y1 < 0)
            y1 = 0;
        if (x2 > width2)
            x2 = width2;
        if (y2 > height2)
            y2 = height2;
        boundsTopX = x1;
        boundsTopY = y1;
        boundsBottomX = x2;
        boundsBottomY = y2;
    }

    public void resetBounds() {
        boundsTopX = 0;
        boundsTopY = 0;
        boundsBottomX = width2;
        boundsBottomY = height2;
    }

    public void draw(Graphics g, int x, int y) {
        setcomplete();
        g.drawImage(image, x, y, this);
    }

    public void blackScreen() {
        int area = width2 * height2;
        if (!interlace) {
            for (int j = 0; j < area; j++)
                pixels[j] = 0;

            return;
        }
        int k = 0;
        for (int l = -height2; l < 0; l += 2) {
            for (int i1 = -width2; i1 < 0; i1++)
                pixels[k++] = 0;

            k += width2;
        }

    }

    public void drawCircle(int x, int y, int radius, int colour, int alpha) {
        int bgAlpha = 256 - alpha;
        int red = (colour >> 16 & 0xff) * alpha;
        int green = (colour >> 8 & 0xff) * alpha;
        int blue = (colour & 0xff) * alpha;
        int top = y - radius;
        if (top < 0)
            top = 0;
        int bottom = y + radius;
        if (bottom >= height2)
            bottom = height2 - 1;
        byte vertInc = 1;
        if (interlace) {
            vertInc = 2;
            if ((top & 1) != 0)
                top++;
        }
        for (int yy = top; yy <= bottom; yy += vertInc) {
            int l3 = yy - y;
            int i4 = (int) Math.sqrt(radius * radius - l3 * l3);
            int j4 = x - i4;
            if (j4 < 0)
                j4 = 0;
            int k4 = x + i4;
            if (k4 >= width2)
                k4 = width2 - 1;
            int pixelIdx = j4 + yy * width2;
            for (int i5 = j4; i5 <= k4; i5++) {
                int bgRed = (pixels[pixelIdx] >> 16 & 0xff) * bgAlpha;
                int bgGreen = (pixels[pixelIdx] >> 8 & 0xff) * bgAlpha;
                int bgBlue = (pixels[pixelIdx] & 0xff) * bgAlpha;
                int newColour = ((red + bgRed >> 8) << 16) + ((green + bgGreen >> 8) << 8) + (blue + bgBlue >> 8);
                pixels[pixelIdx++] = newColour;
            }

        }

    }

    public void drawBoxAlpha(int x, int y, int width, int height, int colour, int alpha) {
        if (x < boundsTopX) {
            width -= boundsTopX - x;
            x = boundsTopX;
        }
        if (y < boundsTopY) {
            height -= boundsTopY - y;
            y = boundsTopY;
        }
        if (x + width > boundsBottomX)
            width = boundsBottomX - x;
        if (y + height > boundsBottomY)
            height = boundsBottomY - y;
        int bgAlpha = 256 - alpha;
        int red = (colour >> 16 & 0xff) * alpha;
        int green = (colour >> 8 & 0xff) * alpha;
        int blue = (colour & 0xff) * alpha;
        int j3 = width2 - width;// wat
        byte vertInc = 1;
        if (interlace) {
            vertInc = 2;
            j3 += width2;
            if ((y & 1) != 0) {
                y++;
                height--;
            }
        }
        int pixelIdx = x + y * width2;
        for (int l3 = 0; l3 < height; l3 += vertInc) {
            for (int i4 = -width; i4 < 0; i4++) {
                int bgRed = (pixels[pixelIdx] >> 16 & 0xff) * bgAlpha;
                int bgGreen = (pixels[pixelIdx] >> 8 & 0xff) * bgAlpha;
                int bgBlue = (pixels[pixelIdx] & 0xff) * bgAlpha;
                int newColour = ((red + bgRed >> 8) << 16) + ((green + bgGreen >> 8) << 8) + (blue + bgBlue >> 8);
                pixels[pixelIdx++] = newColour;
            }

            pixelIdx += j3;
        }

    }

    public void drawGradient(int x, int y, int width, int height, int colourTop, int colourBottom) {
        if (x < boundsTopX) {
            width -= boundsTopX - x;
            x = boundsTopX;
        }
        if (x + width > boundsBottomX)
            width = boundsBottomX - x;
        int btmRed = colourBottom >> 16 & 0xff;
        int btmGreen = colourBottom >> 8 & 0xff;
        int btmBlue = colourBottom & 0xff;
        int topRed = colourTop >> 16 & 0xff;
        int topGreen = colourTop >> 8 & 0xff;
        int topBlue = colourTop & 0xff;
        int i3 = width2 - width;// wat
        byte vertInc = 1;
        if (interlace) {
            vertInc = 2;
            i3 += width2;
            if ((y & 1) != 0) {
                y++;
                height--;
            }
        }
        int pixelIdx = x + y * width2;
        for (int k3 = 0; k3 < height; k3 += vertInc)
            if (k3 + y >= boundsTopY && k3 + y < boundsBottomY) {
                int newColour = ((btmRed * k3 + topRed * (height - k3)) / height << 16) + ((btmGreen * k3 + topGreen * (height - k3)) / height << 8) + (btmBlue * k3 + topBlue * (height - k3)) / height;
                for (int i4 = -width; i4 < 0; i4++)
                    pixels[pixelIdx++] = newColour;

                pixelIdx += i3;
            } else {
                pixelIdx += width2;
            }

    }

    public void drawBox(int x, int y, int w, int h, int colour) {
        if (x < boundsTopX) {
            w -= boundsTopX - x;
            x = boundsTopX;
        }
        if (y < boundsTopY) {
            h -= boundsTopY - y;
            y = boundsTopY;
        }
        if (x + w > boundsBottomX)
            w = boundsBottomX - x;
        if (y + h > boundsBottomY)
            h = boundsBottomY - y;
        int j1 = width2 - w;// wat
        byte vertInc = 1;
        if (interlace) {
            vertInc = 2;
            j1 += width2;
            if ((y & 1) != 0) {
                y++;
                h--;
            }
        }
        int pixelIdx = x + y * width2;
        for (int l1 = -h; l1 < 0; l1 += vertInc) {
            for (int i2 = -w; i2 < 0; i2++)
                pixels[pixelIdx++] = colour;

            pixelIdx += j1;
        }

    }

    public void drawBoxEdge(int x, int y, int width, int height, int colour) {
        drawLineHoriz(x, y, width, colour);
        drawLineHoriz(x, (y + height) - 1, width, colour);
        drawLineVert(x, y, height, colour);
        drawLineVert((x + width) - 1, y, height, colour);
    }

    public void drawLineHoriz(int x, int y, int width, int colour) {
        if (y < boundsTopY || y >= boundsBottomY)
            return;
        if (x < boundsTopX) {
            width -= boundsTopX - x;
            x = boundsTopX;
        }
        if (x + width > boundsBottomX)
            width = boundsBottomX - x;
        int i1 = x + y * width2;
        for (int j1 = 0; j1 < width; j1++)
            pixels[i1 + j1] = colour;

    }

    public void drawLineVert(int x, int y, int height, int colour) {
        if (x < boundsTopX || x >= boundsBottomX)
            return;
        if (y < boundsTopY) {
            height -= boundsTopY - y;
            y = boundsTopY;
        }
        if (y + height > boundsBottomX)
            height = boundsBottomY - y;
        int i1 = x + y * width2;
        for (int j1 = 0; j1 < height; j1++)
            pixels[i1 + j1 * width2] = colour;

    }

    public void setPixel(int x, int y, int colour) {
        if (x < boundsTopX || y < boundsTopY || x >= boundsBottomX || y >= boundsBottomY) {
            return;
        } else {
            pixels[x + y * width2] = colour;
            return;
        }
    }

    public void fade2black() {
        int k = width2 * height2;
        for (int j = 0; j < k; j++) {
            int i = pixels[j] & 0xffffff;
            pixels[j] = (i >>> 1 & 0x7f7f7f) + (i >>> 2 & 0x3f3f3f) + (i >>> 3 & 0x1f1f1f) + (i >>> 4 & 0xf0f0f);
        }
    }

    public void drawLineAlpha(int i, int j, int x, int y, int width, int height) {
        for (int xx = x; xx < x + width; xx++) {
            for (int yy = y; yy < y + height; yy++) {
                int i2 = 0;
                int j2 = 0;
                int k2 = 0;
                int l2 = 0;
                for (int i3 = xx - i; i3 <= xx + i; i3++)
                    if (i3 >= 0 && i3 < width2) {
                        for (int j3 = yy - j; j3 <= yy + j; j3++)
                            if (j3 >= 0 && j3 < height2) {
                                int k3 = pixels[i3 + width2 * j3];
                                i2 += k3 >> 16 & 0xff;
                                j2 += k3 >> 8 & 0xff;
                                k2 += k3 & 0xff;
                                l2++;
                            }

                    }

                pixels[xx + width2 * yy] = (i2 / l2 << 16) + (j2 / l2 << 8) + k2 / l2;
            }

        }

    }

    public void clear() {
        for (int i = 0; i < surfacePixels.length; i++) {
            surfacePixels[i] = null;
            spriteWidth[i] = 0;
            spriteHeight[i] = 0;
            spriteColoursUsed[i] = null;
            spriteColourList[i] = null;
        }

    }

    public void parseSprite(int spriteId, byte spriteData[], byte indexData[], int frameCount) {
        int indexOff = Util.getUnsignedShort(spriteData, 0);
        int fullWidth = Util.getUnsignedShort(indexData, indexOff);
        indexOff += 2;
        int fullHeight = Util.getUnsignedShort(indexData, indexOff);
        indexOff += 2;
        int colourCount = indexData[indexOff++] & 0xff;
        int colours[] = new int[colourCount];
        colours[0] = 0xff00ff;
        for (int i = 0; i < colourCount - 1; i++) {
            colours[i + 1] = ((indexData[indexOff] & 0xff) << 16) + ((indexData[indexOff + 1] & 0xff) << 8) + (indexData[indexOff + 2] & 0xff);
            indexOff += 3;
        }
        
        int spriteOff = 2;
        for (int id = spriteId; id < spriteId + frameCount; id++) {
            spriteTranslateX[id] = indexData[indexOff++] & 0xff;
            spriteTranslateY[id] = indexData[indexOff++] & 0xff;
            spriteWidth[id] = Util.getUnsignedShort(indexData, indexOff);
            indexOff += 2;
            spriteHeight[id] = Util.getUnsignedShort(indexData, indexOff);
            indexOff += 2;
            int unknown = indexData[indexOff++] & 0xff;
            int size = spriteWidth[id] * spriteHeight[id];
            spriteColoursUsed[id] = new byte[size];
            spriteColourList[id] = colours;
            spriteWidthFull[id] = fullWidth;
            spriteHeightFull[id] = fullHeight;
            surfacePixels[id] = null;
            spriteTranslate[id] = false;
            if (spriteTranslateX[id] != 0 || spriteTranslateY[id] != 0)
                spriteTranslate[id] = true;
            if (unknown == 0) {
                for (int pixel = 0; pixel < size; pixel++) {
                    spriteColoursUsed[id][pixel] = spriteData[spriteOff++];
                    if (spriteColoursUsed[id][pixel] == 0)
                        spriteTranslate[id] = true;
                }

            } else if (unknown == 1) {
                for (int x = 0; x < spriteWidth[id]; x++) {
                    for (int y = 0; y < spriteHeight[id]; y++) {
                        spriteColoursUsed[id][x + y * spriteWidth[id]] = spriteData[spriteOff++];
                        if (spriteColoursUsed[id][x + y * spriteWidth[id]] == 0)
                            spriteTranslate[id] = true;
                    }

                }

            }
        }
    }

    public void drawWorld(int spriteId) {
        int spriteSize = spriteWidth[spriteId] * spriteHeight[spriteId];
        int spritePixels[] = this.surfacePixels[spriteId];
        int ai1[] = new int[32768];
        for (int k = 0; k < spriteSize; k++) {
            int l = spritePixels[k];
            ai1[((l & 0xf80000) >> 9) + ((l & 0xf800) >> 6) + ((l & 0xf8) >> 3)]++;
        }

        int ai2[] = new int[256];
        ai2[0] = 0xff00ff;
        int ai3[] = new int[256];
        for (int i1 = 0; i1 < 32768; i1++) {
            int j1 = ai1[i1];
            if (j1 > ai3[255]) {
                for (int k1 = 1; k1 < 256; k1++) {
                    if (j1 <= ai3[k1])
                        continue;
                    for (int i2 = 255; i2 > k1; i2--) {
                        ai2[i2] = ai2[i2 - 1];
                        ai3[i2] = ai3[i2 - 1];
                    }

                    ai2[k1] = ((i1 & 0x7c00) << 9) + ((i1 & 0x3e0) << 6) + ((i1 & 0x1f) << 3) + 0x40404;
                    ai3[k1] = j1;
                    break;
                }

            }
            ai1[i1] = -1;
        }

        byte abyte0[] = new byte[spriteSize];
        for (int l1 = 0; l1 < spriteSize; l1++) {
            int j2 = spritePixels[l1];
            int k2 = ((j2 & 0xf80000) >> 9) + ((j2 & 0xf800) >> 6) + ((j2 & 0xf8) >> 3);
            int l2 = ai1[k2];
            if (l2 == -1) {
                int i3 = 0x3b9ac9ff;
                int j3 = j2 >> 16 & 0xff;
                int k3 = j2 >> 8 & 0xff;
                int l3 = j2 & 0xff;
                for (int i4 = 0; i4 < 256; i4++) {
                    int j4 = ai2[i4];
                    int k4 = j4 >> 16 & 0xff;
                    int l4 = j4 >> 8 & 0xff;
                    int i5 = j4 & 0xff;
                    int j5 = (j3 - k4) * (j3 - k4) + (k3 - l4) * (k3 - l4) + (l3 - i5) * (l3 - i5);
                    if (j5 < i3) {
                        i3 = j5;
                        l2 = i4;
                    }
                }

                ai1[k2] = l2;
            }
            abyte0[l1] = (byte) l2;
        }

        spriteColoursUsed[spriteId] = abyte0;
        spriteColourList[spriteId] = ai2;
        this.surfacePixels[spriteId] = null;
    }

    public void loadSprite(int spriteId) {
        if (spriteColoursUsed[spriteId] == null)
            return;
        int size = spriteWidth[spriteId] * spriteHeight[spriteId];
        byte idx[] = spriteColoursUsed[spriteId];
        int cols[] = spriteColourList[spriteId];
        int pixels[] = new int[size];
        for (int pixel = 0; pixel < size; pixel++) {
            int colour = cols[idx[pixel] & 0xff];
            if (colour == 0)
                colour = 1;
            else if (colour == 0xff00ff)
                colour = 0;
            pixels[pixel] = colour;
        }

        surfacePixels[spriteId] = pixels;
        spriteColoursUsed[spriteId] = null;
        spriteColourList[spriteId] = null;
    }

    public void drawSpriteMinimap(int sprite, int x, int y, int width, int height) {// used from World
        spriteWidth[sprite] = width;
        spriteHeight[sprite] = height;
        spriteTranslate[sprite] = false;
        spriteTranslateX[sprite] = 0;
        spriteTranslateY[sprite] = 0;
        spriteWidthFull[sprite] = width;
        spriteHeightFull[sprite] = height;
        int area = width * height;
        int pixel = 0;
        surfacePixels[sprite] = new int[area];
        for (int xx = x; xx < x + width; xx++) {
            for (int yy = y; yy < y + height; yy++) {
                surfacePixels[sprite][pixel++] = pixels[xx + yy * width2];
            }
        }
    }

    public void drawSpriteClipping(int sprite, int x, int y, int width, int height) {// used from mudclient
        spriteWidth[sprite] = width;
        spriteHeight[sprite] = height;
        spriteTranslate[sprite] = false;
        spriteTranslateX[sprite] = 0;
        spriteTranslateY[sprite] = 0;
        spriteWidthFull[sprite] = width;
        spriteHeightFull[sprite] = height;
        int area = width * height;
        int pixel = 0;
        surfacePixels[sprite] = new int[area];
        for (int yy = y; yy < y + height; yy++) {
            for (int xx = x; xx < x + width; xx++) {
                surfacePixels[sprite][pixel++] = pixels[xx + yy * width2];
            }
        }

    }

    public void drawSprite(int x, int y, int id) {
        if (spriteTranslate[id]) {
            x += spriteTranslateX[id];
            y += spriteTranslateY[id];
        }
        int rY = x + y * width2;
        int rX = 0;
        int height = spriteHeight[id];
        int width = spriteWidth[id];
        int w2 = width2 - width;
        int h2 = 0;
        if (y < boundsTopY) {
            int j2 = boundsTopY - y;
            height -= j2;
            y = boundsTopY;
            rX += j2 * width;
            rY += j2 * width2;
        }
        if (y + height >= boundsBottomY)
            height -= ((y + height) - boundsBottomY) + 1;
        if (x < boundsTopX) {
            int k2 = boundsTopX - x;
            width -= k2;
            x = boundsTopX;
            rX += k2;
            rY += k2;
            h2 += k2;
            w2 += k2;
        }
        if (x + width >= boundsBottomX) {
            int l2 = ((x + width) - boundsBottomX) + 1;
            width -= l2;
            h2 += l2;
            w2 += l2;
        }
        if (width <= 0 || height <= 0)
            return;
        byte inc = 1;
        if (interlace) {
            inc = 2;
            w2 += width2;
            h2 += spriteWidth[id];
            if ((y & 1) != 0) {
                rY += width2;
                height--;
            }
        }
        if (surfacePixels[id] == null) {
            drawSprite(pixels, spriteColoursUsed[id], spriteColourList[id], rX, rY, width, height, w2, h2, inc);
            return;
        } else {
            drawSprite(pixels, surfacePixels[id], 0, rX, rY, width, height, w2, h2, inc);
            return;
        }
    }

    public void spriteClipping(int x, int y, int width, int height, int spriteId) {
        try {
            int spriteWidth = this.spriteWidth[spriteId];
            int spriteHeight = this.spriteHeight[spriteId];
            int l1 = 0;
            int i2 = 0;
            int j2 = (spriteWidth << 16) / width;
            int k2 = (spriteHeight << 16) / height;
            if (spriteTranslate[spriteId]) {
                int l2 = spriteWidthFull[spriteId];
                int j3 = spriteHeightFull[spriteId];
                j2 = (l2 << 16) / width;
                k2 = (j3 << 16) / height;
                x += ((spriteTranslateX[spriteId] * width + l2) - 1) / l2;
                y += ((spriteTranslateY[spriteId] * height + j3) - 1) / j3;
                if ((spriteTranslateX[spriteId] * width) % l2 != 0)
                    l1 = (l2 - (spriteTranslateX[spriteId] * width) % l2 << 16) / width;
                if ((spriteTranslateY[spriteId] * height) % j3 != 0)
                    i2 = (j3 - (spriteTranslateY[spriteId] * height) % j3 << 16) / height;
                width = (width * (this.spriteWidth[spriteId] - (l1 >> 16))) / l2;
                height = (height * (this.spriteHeight[spriteId] - (i2 >> 16))) / j3;
            }
            int i3 = x + y * width2;
            int k3 = width2 - width;
            if (y < boundsTopY) {
                int l3 = boundsTopY - y;
                height -= l3;
                y = 0;
                i3 += l3 * width2;
                i2 += k2 * l3;
            }
            if (y + height >= boundsBottomY)
                height -= ((y + height) - boundsBottomY) + 1;
            if (x < boundsTopX) {
                int i4 = boundsTopX - x;
                width -= i4;
                x = 0;
                i3 += i4;
                l1 += j2 * i4;
                k3 += i4;
            }
            if (x + width >= boundsBottomX) {
                int j4 = ((x + width) - boundsBottomX) + 1;
                width -= j4;
                k3 += j4;
            }
            byte yInc = 1;
            if (interlace) {
                yInc = 2;
                k3 += width2;
                k2 += k2;
                if ((y & 1) != 0) {
                    i3 += width2;
                    height--;
                }
            }
            plotScale(pixels, surfacePixels[spriteId], 0, l1, i2, i3, k3, width, height, j2, k2, spriteWidth, yInc);
            return;
        } catch (Exception Ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    public void drawSpriteAlpha(int x, int y, int spriteId, int alpha) {
        if (spriteTranslate[spriteId]) {
            x += spriteTranslateX[spriteId];
            y += spriteTranslateY[spriteId];
        }
        int size = x + y * width2;
        int j1 = 0;
        int height = spriteHeight[spriteId];
        int width = spriteWidth[spriteId];
        int extraXSpace = width2 - width;
        int j2 = 0;
        if (y < boundsTopY) {
            int k2 = boundsTopY - y;
            height -= k2;
            y = boundsTopY;
            j1 += k2 * width;
            size += k2 * width2;
        }
        if (y + height >= boundsBottomY)
            height -= ((y + height) - boundsBottomY) + 1;
        if (x < boundsTopX) {
            int l2 = boundsTopX - x;
            width -= l2;
            x = boundsTopX;
            j1 += l2;
            size += l2;
            j2 += l2;
            extraXSpace += l2;
        }
        if (x + width >= boundsBottomX) {
            int i3 = ((x + width) - boundsBottomX) + 1;
            width -= i3;
            j2 += i3;
            extraXSpace += i3;
        }
        if (width <= 0 || height <= 0)
            return;
        byte yInc = 1;
        if (this.interlace) {
            yInc = 2;
            extraXSpace += width2;
            j2 += spriteWidth[spriteId];
            if ((y & 1) != 0) {
                size += width2;
                height--;
            }
        }
        if (surfacePixels[spriteId] == null) {
            drawSpriteAlpha(pixels, spriteColoursUsed[spriteId], spriteColourList[spriteId], j1, size, width, height, extraXSpace, j2, yInc, alpha);
            return;
        } else {
            drawSpriteAlpha(pixels, surfacePixels[spriteId], 0, j1, size, width, height, extraXSpace, j2, yInc, alpha);
            return;
        }
    }

    public void drawActionBubble(int x, int y, int scaleX, int scaleY, int sprite, int alpha) {
        try {
            int spriteWidth = this.spriteWidth[sprite];
            int spriteHeight = this.spriteHeight[sprite];
            int i2 = 0;
            int j2 = 0;
            int k2 = (spriteWidth << 16) / scaleX;
            int l2 = (spriteHeight << 16) / scaleY;
            if (spriteTranslate[sprite]) {
                int i3 = spriteWidthFull[sprite];
                int k3 = spriteHeightFull[sprite];
                k2 = (i3 << 16) / scaleX;
                l2 = (k3 << 16) / scaleY;
                x += ((spriteTranslateX[sprite] * scaleX + i3) - 1) / i3;
                y += ((spriteTranslateY[sprite] * scaleY + k3) - 1) / k3;
                if ((spriteTranslateX[sprite] * scaleX) % i3 != 0)
                    i2 = (i3 - (spriteTranslateX[sprite] * scaleX) % i3 << 16) / scaleX;
                if ((spriteTranslateY[sprite] * scaleY) % k3 != 0)
                    j2 = (k3 - (spriteTranslateY[sprite] * scaleY) % k3 << 16) / scaleY;
                scaleX = (scaleX * (this.spriteWidth[sprite] - (i2 >> 16))) / i3;
                scaleY = (scaleY * (this.spriteHeight[sprite] - (j2 >> 16))) / k3;
            }
            int j3 = x + y * width2;
            int l3 = width2 - scaleX;
            if (y < boundsTopY) {
                int i4 = boundsTopY - y;
                scaleY -= i4;
                y = 0;
                j3 += i4 * width2;
                j2 += l2 * i4;
            }
            if (y + scaleY >= boundsBottomY)
                scaleY -= ((y + scaleY) - boundsBottomY) + 1;
            if (x < boundsTopX) {
                int j4 = boundsTopX - x;
                scaleX -= j4;
                x = 0;
                j3 += j4;
                i2 += k2 * j4;
                l3 += j4;
            }
            if (x + scaleX >= boundsBottomX) {
                int k4 = ((x + scaleX) - boundsBottomX) + 1;
                scaleX -= k4;
                l3 += k4;
            }
            byte yInc = 1;
            if (interlace) {
                yInc = 2;
                l3 += width2;
                l2 += l2;
                if ((y & 1) != 0) {
                    j3 += width2;
                    scaleY--;
                }
            }
            transparentScale(pixels, surfacePixels[sprite], 0, i2, j2, j3, l3, scaleX, scaleY, k2, l2, spriteWidth, yInc, alpha);
            return;
        } catch (Exception ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    public void spriteClipping(int x, int y, int width, int height, int spriteId, int colour) {
        try {
            int k1 = spriteWidth[spriteId];
            int l1 = spriteHeight[spriteId];
            int i2 = 0;
            int j2 = 0;
            int k2 = (k1 << 16) / width;
            int l2 = (l1 << 16) / height;
            if (spriteTranslate[spriteId]) {
                int i3 = spriteWidthFull[spriteId];
                int k3 = spriteHeightFull[spriteId];
                k2 = (i3 << 16) / width;
                l2 = (k3 << 16) / height;
                x += ((spriteTranslateX[spriteId] * width + i3) - 1) / i3;
                y += ((spriteTranslateY[spriteId] * height + k3) - 1) / k3;
                if ((spriteTranslateX[spriteId] * width) % i3 != 0)
                    i2 = (i3 - (spriteTranslateX[spriteId] * width) % i3 << 16) / width;
                if ((spriteTranslateY[spriteId] * height) % k3 != 0)
                    j2 = (k3 - (spriteTranslateY[spriteId] * height) % k3 << 16) / height;
                width = (width * (spriteWidth[spriteId] - (i2 >> 16))) / i3;
                height = (height * (spriteHeight[spriteId] - (j2 >> 16))) / k3;
            }
            int j3 = x + y * width2;
            int l3 = width2 - width;
            if (y < boundsTopY) {
                int i4 = boundsTopY - y;
                height -= i4;
                y = 0;
                j3 += i4 * width2;
                j2 += l2 * i4;
            }
            if (y + height >= boundsBottomY)
                height -= ((y + height) - boundsBottomY) + 1;
            if (x < boundsTopX) {
                int j4 = boundsTopX - x;
                width -= j4;
                x = 0;
                j3 += j4;
                i2 += k2 * j4;
                l3 += j4;
            }
            if (x + width >= boundsBottomX) {
                int k4 = ((x + width) - boundsBottomX) + 1;
                width -= k4;
                l3 += k4;
            }
            byte yInc = 1;
            if (interlace) {
                yInc = 2;
                l3 += width2;
                l2 += l2;
                if ((y & 1) != 0) {
                    j3 += width2;
                    height--;
                }
            }
            plotScale(pixels, surfacePixels[spriteId], 0, i2, j2, j3, l3, width, height, k2, l2, k1, yInc, colour);
            return;
        } catch (Exception Ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    private void drawSprite(int dest[], int src[], int i, int srcPos, int destPos, int width, int height,
                            int j1, int k1, int yInc) {
        int i2 = -(width >> 2);
        width = -(width & 3);
        for (int j2 = -height; j2 < 0; j2 += yInc) {
            for (int k2 = i2; k2 < 0; k2++) {
                i = src[srcPos++];
                if (i != 0)
                    dest[destPos++] = i;
                else
                    destPos++;
                i = src[srcPos++];
                if (i != 0)
                    dest[destPos++] = i;
                else
                    destPos++;
                i = src[srcPos++];
                if (i != 0)
                    dest[destPos++] = i;
                else
                    destPos++;
                i = src[srcPos++];
                if (i != 0)
                    dest[destPos++] = i;
                else
                    destPos++;
            }

            for (int l2 = width; l2 < 0; l2++) {
                i = src[srcPos++];
                if (i != 0)
                    dest[destPos++] = i;
                else
                    destPos++;
            }

            destPos += j1;
            srcPos += k1;
        }

    }

    private void drawSprite(int target[], byte colourIdx[], int colours[], int srcPos, int destPos, int width, int height,
                            int w2, int h2, int rowInc) {
        int l1 = -(width >> 2);
        width = -(width & 3);
        for (int i2 = -height; i2 < 0; i2 += rowInc) {
            for (int j2 = l1; j2 < 0; j2++) {
                byte byte0 = colourIdx[srcPos++];
                if (byte0 != 0)
                    target[destPos++] = colours[byte0 & 0xff];
                else
                    destPos++;
                byte0 = colourIdx[srcPos++];
                if (byte0 != 0)
                    target[destPos++] = colours[byte0 & 0xff];
                else
                    destPos++;
                byte0 = colourIdx[srcPos++];
                if (byte0 != 0)
                    target[destPos++] = colours[byte0 & 0xff];
                else
                    destPos++;
                byte0 = colourIdx[srcPos++];
                if (byte0 != 0)
                    target[destPos++] = colours[byte0 & 0xff];
                else
                    destPos++;
            }

            for (int k2 = width; k2 < 0; k2++) {
                byte byte1 = colourIdx[srcPos++];
                if (byte1 != 0)
                    target[destPos++] = colours[byte1 & 0xff];
                else
                    destPos++;
            }

            destPos += w2;
            srcPos += h2;
        }

    }

    private void plotScale(int dest[], int src[], int i, int j, int k, int destPos, int i1,
                           int j1, int k1, int l1, int i2, int j2, int k2) {
        try {
            int l2 = j;
            for (int i3 = -k1; i3 < 0; i3 += k2) {
                int j3 = (k >> 16) * j2;
                for (int k3 = -j1; k3 < 0; k3++) {
                    i = src[(j >> 16) + j3];
                    if (i != 0)
                        dest[destPos++] = i;
                    else
                        destPos++;
                    j += l1;
                }

                k += i2;
                j = l2;
                destPos += i1;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in plotScale");
        }
    }

    private void drawSpriteAlpha(int dest[], int src[], int i, int srcPos, int size, int width, int height,
                                 int extraXSpace, int k1, int yInc, int alpha) {
        int j2 = 256 - alpha;
        for (int k2 = -height; k2 < 0; k2 += yInc) {
            for (int l2 = -width; l2 < 0; l2++) {
                i = src[srcPos++];
                if (i != 0) {
                    int i3 = dest[size];
                    dest[size++] = ((i & 0xff00ff) * alpha + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((i & 0xff00) * alpha + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
                } else {
                    size++;
                }
            }

            size += extraXSpace;
            srcPos += k1;
        }

    }

    private void drawSpriteAlpha(int dest[], byte coloursUsed[], int colourList[], int listPos, int size, int width, int height,
                                 int extraXSpace, int j1, int yInc, int alpha) {
        int i2 = 256 - alpha;
        for (int j2 = -height; j2 < 0; j2 += yInc) {
            for (int k2 = -width; k2 < 0; k2++) {
                int l2 = coloursUsed[listPos++];
                if (l2 != 0) {
                    l2 = colourList[l2 & 0xff];
                    int i3 = dest[size];
                    dest[size++] = ((l2 & 0xff00ff) * alpha + (i3 & 0xff00ff) * i2 & 0xff00ff00) + ((l2 & 0xff00) * alpha + (i3 & 0xff00) * i2 & 0xff0000) >> 8;
                } else {
                    size++;
                }
            }

            size += extraXSpace;
            listPos += j1;
        }

    }

    private void transparentScale(int dest[], int src[], int i, int j, int k, int destPos, int i1,
                                  int j1, int k1, int l1, int i2, int j2, int yInc, int alpha) {
        int i3 = 256 - alpha;
        try {
            int j3 = j;
            for (int k3 = -k1; k3 < 0; k3 += yInc) {
                int l3 = (k >> 16) * j2;
                for (int i4 = -j1; i4 < 0; i4++) {
                    i = src[(j >> 16) + l3];
                    if (i != 0) {
                        int j4 = dest[destPos];
                        dest[destPos++] = ((i & 0xff00ff) * alpha + (j4 & 0xff00ff) * i3 & 0xff00ff00) + ((i & 0xff00) * alpha + (j4 & 0xff00) * i3 & 0xff0000) >> 8;
                    } else {
                        destPos++;
                    }
                    j += l1;
                }

                k += i2;
                j = j3;
                destPos += i1;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in tranScale");
        }
    }

    private void plotScale(int target[], int pixels[], int i, int j, int k, int l, int i1,
                           int width, int height, int l1, int i2, int j2, int yInc, int colour) {
        int i3 = colour >> 16 & 0xff;
        int j3 = colour >> 8 & 0xff;
        int k3 = colour & 0xff;
        try {
            int l3 = j;
            for (int i4 = -height; i4 < 0; i4 += yInc) {
                int j4 = (k >> 16) * j2;
                for (int k4 = -width; k4 < 0; k4++) {
                    i = pixels[(j >> 16) + j4];
                    if (i != 0) {
                        int l4 = i >> 16 & 0xff;
                        int i5 = i >> 8 & 0xff;
                        int j5 = i & 0xff;
                        if (l4 == i5 && i5 == j5)
                            target[l++] = ((l4 * i3 >> 8) << 16) + ((i5 * j3 >> 8) << 8) + (j5 * k3 >> 8);
                        else
                            target[l++] = i;
                    } else {
                        l++;
                    }
                    j += l1;
                }

                k += i2;
                j = l3;
                l += i1;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in plotScale");
        }
    }

    public void drawMinimapSprite(int x, int y, int sprite, int rotation, int scale) {// "scale" is not actually scaling when it comes to the landscape
        int j1 = width2;
        int k1 = height2;
        if (landscapeColours == null) {
            landscapeColours = new int[512];
            for (int l1 = 0; l1 < 256; l1++) {
                landscapeColours[l1] = (int) (Math.sin((double) l1 * 0.02454369D) * 32768D);
                landscapeColours[l1 + 256] = (int) (Math.cos((double) l1 * 0.02454369D) * 32768D);
            }

        }
        int i2 = -spriteWidthFull[sprite] / 2;
        int j2 = -spriteHeightFull[sprite] / 2;
        if (spriteTranslate[sprite]) {
            i2 += spriteTranslateX[sprite];
            j2 += spriteTranslateY[sprite];
        }
        int k2 = i2 + spriteWidth[sprite];
        int l2 = j2 + spriteHeight[sprite];
        int i3 = k2;
        int j3 = j2;
        int k3 = i2;
        int l3 = l2;
        rotation &= 0xff;
        int i4 = landscapeColours[rotation] * scale;
        int j4 = landscapeColours[rotation + 256] * scale;
        int k4 = x + (j2 * i4 + i2 * j4 >> 22);
        int l4 = y + (j2 * j4 - i2 * i4 >> 22);
        int i5 = x + (j3 * i4 + i3 * j4 >> 22);
        int j5 = y + (j3 * j4 - i3 * i4 >> 22);
        int k5 = x + (l2 * i4 + k2 * j4 >> 22);
        int l5 = y + (l2 * j4 - k2 * i4 >> 22);
        int i6 = x + (l3 * i4 + k3 * j4 >> 22);
        int j6 = y + (l3 * j4 - k3 * i4 >> 22);
        if (scale == 192 && (rotation & 0x3f) == (anInt348 & 0x3f))
            anInt346++;
        else if (scale == 128)
            anInt348 = rotation;
        else
            anInt347++;
        int k6 = l4;
        int l6 = l4;
        if (j5 < k6)
            k6 = j5;
        else if (j5 > l6)
            l6 = j5;
        if (l5 < k6)
            k6 = l5;
        else if (l5 > l6)
            l6 = l5;
        if (j6 < k6)
            k6 = j6;
        else if (j6 > l6)
            l6 = j6;
        if (k6 < boundsTopY)
            k6 = boundsTopY;
        if (l6 > boundsBottomY)
            l6 = boundsBottomY;
        if (anIntArray340 == null || anIntArray340.length != k1 + 1) {
            anIntArray340 = new int[k1 + 1];
            anIntArray341 = new int[k1 + 1];
            anIntArray342 = new int[k1 + 1];
            anIntArray343 = new int[k1 + 1];
            anIntArray344 = new int[k1 + 1];
            anIntArray345 = new int[k1 + 1];
        }
        for (int i7 = k6; i7 <= l6; i7++) {
            anIntArray340[i7] = 99999999;
            anIntArray341[i7] = 0xfa0a1f01;
        }

        int i8 = 0;
        int k8 = 0;
        int i9 = 0;
        int j9 = spriteWidth[sprite];
        int k9 = spriteHeight[sprite];
        i2 = 0;
        j2 = 0;
        i3 = j9 - 1;
        j3 = 0;
        k2 = j9 - 1;
        l2 = k9 - 1;
        k3 = 0;
        l3 = k9 - 1;
        if (j6 != l4) {
            i8 = (i6 - k4 << 8) / (j6 - l4);
            i9 = (l3 - j2 << 8) / (j6 - l4);
        }
        int j7;
        int k7;
        int l7;
        int l8;
        if (l4 > j6) {
            l7 = i6 << 8;
            l8 = l3 << 8;
            j7 = j6;
            k7 = l4;
        } else {
            l7 = k4 << 8;
            l8 = j2 << 8;
            j7 = l4;
            k7 = j6;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            l8 -= i9 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int l9 = j7; l9 <= k7; l9++) {
            anIntArray340[l9] = anIntArray341[l9] = l7;
            l7 += i8;
            anIntArray342[l9] = anIntArray343[l9] = 0;
            anIntArray344[l9] = anIntArray345[l9] = l8;
            l8 += i9;
        }

        if (j5 != l4) {
            i8 = (i5 - k4 << 8) / (j5 - l4);
            k8 = (i3 - i2 << 8) / (j5 - l4);
        }
        int j8;
        if (l4 > j5) {
            l7 = i5 << 8;
            j8 = i3 << 8;
            j7 = j5;
            k7 = l4;
        } else {
            l7 = k4 << 8;
            j8 = i2 << 8;
            j7 = l4;
            k7 = j5;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            j8 -= k8 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int i10 = j7; i10 <= k7; i10++) {
            if (l7 < anIntArray340[i10]) {
                anIntArray340[i10] = l7;
                anIntArray342[i10] = j8;
                anIntArray344[i10] = 0;
            }
            if (l7 > anIntArray341[i10]) {
                anIntArray341[i10] = l7;
                anIntArray343[i10] = j8;
                anIntArray345[i10] = 0;
            }
            l7 += i8;
            j8 += k8;
        }

        if (l5 != j5) {
            i8 = (k5 - i5 << 8) / (l5 - j5);
            i9 = (l2 - j3 << 8) / (l5 - j5);
        }
        if (j5 > l5) {
            l7 = k5 << 8;
            j8 = k2 << 8;
            l8 = l2 << 8;
            j7 = l5;
            k7 = j5;
        } else {
            l7 = i5 << 8;
            j8 = i3 << 8;
            l8 = j3 << 8;
            j7 = j5;
            k7 = l5;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            l8 -= i9 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int j10 = j7; j10 <= k7; j10++) {
            if (l7 < anIntArray340[j10]) {
                anIntArray340[j10] = l7;
                anIntArray342[j10] = j8;
                anIntArray344[j10] = l8;
            }
            if (l7 > anIntArray341[j10]) {
                anIntArray341[j10] = l7;
                anIntArray343[j10] = j8;
                anIntArray345[j10] = l8;
            }
            l7 += i8;
            l8 += i9;
        }

        if (j6 != l5) {
            i8 = (i6 - k5 << 8) / (j6 - l5);
            k8 = (k3 - k2 << 8) / (j6 - l5);
        }
        if (l5 > j6) {
            l7 = i6 << 8;
            j8 = k3 << 8;
            l8 = l3 << 8;
            j7 = j6;
            k7 = l5;
        } else {
            l7 = k5 << 8;
            j8 = k2 << 8;
            l8 = l2 << 8;
            j7 = l5;
            k7 = j6;
        }
        if (j7 < 0) {
            l7 -= i8 * j7;
            j8 -= k8 * j7;
            j7 = 0;
        }
        if (k7 > k1 - 1)
            k7 = k1 - 1;
        for (int k10 = j7; k10 <= k7; k10++) {
            if (l7 < anIntArray340[k10]) {
                anIntArray340[k10] = l7;
                anIntArray342[k10] = j8;
                anIntArray344[k10] = l8;
            }
            if (l7 > anIntArray341[k10]) {
                anIntArray341[k10] = l7;
                anIntArray343[k10] = j8;
                anIntArray345[k10] = l8;
            }
            l7 += i8;
            j8 += k8;
        }

        int l10 = k6 * j1;
        int ai[] = surfacePixels[sprite];
        for (int i11 = k6; i11 < l6; i11++) {
            int j11 = anIntArray340[i11] >> 8;
            int k11 = anIntArray341[i11] >> 8;
            if (k11 - j11 <= 0) {
                l10 += j1;
            } else {
                int l11 = anIntArray342[i11] << 9;
                int i12 = ((anIntArray343[i11] << 9) - l11) / (k11 - j11);
                int j12 = anIntArray344[i11] << 9;
                int k12 = ((anIntArray345[i11] << 9) - j12) / (k11 - j11);
                if (j11 < boundsTopX) {
                    l11 += (boundsTopX - j11) * i12;
                    j12 += (boundsTopX - j11) * k12;
                    j11 = boundsTopX;
                }
                if (k11 > boundsBottomX)
                    k11 = boundsBottomX;
                if (!interlace || (i11 & 1) == 0)
                    if (!spriteTranslate[sprite])
                        drawMinimap(pixels, ai, 0, l10 + j11, l11, j12, i12, k12, j11 - k11, j9);
                    else
                        drawMinimapTranslate(pixels, ai, 0, l10 + j11, l11, j12, i12, k12, j11 - k11, j9);
                l10 += j1;
            }
        }

    }

    private void drawMinimap(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                             int j1, int k1, int l1) {
        for (i = k1; i < 0; i++) {
            pixels[j++] = ai1[(k >> 17) + (l >> 17) * l1];
            k += i1;
            l += j1;
        }

    }

    private void drawMinimapTranslate(int ai[], int ai1[], int i, int j, int k, int l, int i1,
                                      int j1, int k1, int l1) {
        for (int i2 = k1; i2 < 0; i2++) {
            i = ai1[(k >> 17) + (l >> 17) * l1];
            if (i != 0)
                pixels[j++] = i;
            else
                j++;
            k += i1;
            l += j1;
        }

    }

    public void spriteClipping(int x, int y, int w, int h, int id, int tx, int ty) {
        spriteClipping(x, y, w, h, id);
    }

    public void spriteClipping(int x, int y, int w, int h, int sprite, int colour1, int colour2,
                               int l1, boolean flag) {
        try {
            if (colour1 == 0)
                colour1 = 0xffffff;
            if (colour2 == 0)
                colour2 = 0xffffff;
            int width = spriteWidth[sprite];
            int height = spriteHeight[sprite];
            int k2 = 0;
            int l2 = 0;
            int i3 = l1 << 16;
            int j3 = (width << 16) / w;
            int k3 = (height << 16) / h;
            int l3 = -(l1 << 16) / h;
            if (spriteTranslate[sprite]) {
                int fullWidth = spriteWidthFull[sprite];
                int fullHeight = spriteHeightFull[sprite];
                j3 = (fullWidth << 16) / w;
                k3 = (fullHeight << 16) / h;
                int j5 = spriteTranslateX[sprite];
                int k5 = spriteTranslateY[sprite];
                if (flag)
                    j5 = fullWidth - spriteWidth[sprite] - j5;
                x += ((j5 * w + fullWidth) - 1) / fullWidth;
                int l5 = ((k5 * h + fullHeight) - 1) / fullHeight;
                y += l5;
                i3 += l5 * l3;
                if ((j5 * w) % fullWidth != 0)
                    k2 = (fullWidth - (j5 * w) % fullWidth << 16) / w;
                if ((k5 * h) % fullHeight != 0)
                    l2 = (fullHeight - (k5 * h) % fullHeight << 16) / h;
                w = ((((spriteWidth[sprite] << 16) - k2) + j3) - 1) / j3;
                h = ((((spriteHeight[sprite] << 16) - l2) + k3) - 1) / k3;
            }
            int j4 = y * width2;
            i3 += x << 16;
            if (y < boundsTopY) {
                int l4 = boundsTopY - y;
                h -= l4;
                y = boundsTopY;
                j4 += l4 * width2;
                l2 += k3 * l4;
                i3 += l3 * l4;
            }
            if (y + h >= boundsBottomY)
                h -= ((y + h) - boundsBottomY) + 1;
            int i5 = j4 / width2 & 1;
            if (!interlace)
                i5 = 2;
            if (colour2 == 0xffffff) {
                if (surfacePixels[sprite] != null)
                    if (!flag) {
                        transparentSpritePlot(pixels, surfacePixels[sprite], 0, k2, l2, j4, w, h, j3, k3, width, colour1, i3, l3, i5);
                        return;
                    } else {
                        transparentSpritePlot(pixels, surfacePixels[sprite], 0, (spriteWidth[sprite] << 16) - k2 - 1, l2, j4, w, h, -j3, k3, width, colour1, i3, l3, i5);
                        return;
                    }
                if (!flag) {
                    transparentSpritePlot(pixels, spriteColoursUsed[sprite], spriteColourList[sprite], 0, k2, l2, j4, w, h, j3, k3, width, colour1, i3, l3, i5);
                    return;
                } else {
                    transparentSpritePlot(pixels, spriteColoursUsed[sprite], spriteColourList[sprite], 0, (spriteWidth[sprite] << 16) - k2 - 1, l2, j4, w, h, -j3, k3, width, colour1, i3, l3, i5);
                    return;
                }
            }
            if (surfacePixels[sprite] != null)
                if (!flag) {
                    transparentSpritePlot(pixels, surfacePixels[sprite], 0, k2, l2, j4, w, h, j3, k3, width, colour1, colour2, i3, l3, i5);
                    return;
                } else {
                    transparentSpritePlot(pixels, surfacePixels[sprite], 0, (spriteWidth[sprite] << 16) - k2 - 1, l2, j4, w, h, -j3, k3, width, colour1, colour2, i3, l3, i5);
                    return;
                }
            if (!flag) {
                transparentSpritePlot(pixels, spriteColoursUsed[sprite], spriteColourList[sprite], 0, k2, l2, j4, w, h, j3, k3, width, colour1, colour2, i3, l3, i5);
                return;
            } else {
                transparentSpritePlot(pixels, spriteColoursUsed[sprite], spriteColourList[sprite], 0, (spriteWidth[sprite] << 16) - k2 - 1, l2, j4, w, h, -j3, k3, width, colour1, colour2, i3, l3, i5);
                return;
            }
        } catch (Exception Ex) {
            System.out.println("error in sprite clipping routine");
        }
    }

    private void transparentSpritePlot(int dest[], int src[], int i, int j, int k, int destPos, int i1,
                                       int j1, int k1, int l1, int i2, int j2, int k2, int l2,
                                       int i3) {
        int i4 = j2 >> 16 & 0xff;
        int j4 = j2 >> 8 & 0xff;
        int k4 = j2 & 0xff;
        try {
            int l4 = j;
            for (int i5 = -j1; i5 < 0; i5++) {
                int j5 = (k >> 16) * i2;
                int k5 = k2 >> 16;
                int l5 = i1;
                if (k5 < boundsTopX) {
                    int i6 = boundsTopX - k5;
                    l5 -= i6;
                    k5 = boundsTopX;
                    j += k1 * i6;
                }
                if (k5 + l5 >= boundsBottomX) {
                    int j6 = (k5 + l5) - boundsBottomX;
                    l5 -= j6;
                }
                i3 = 1 - i3;
                if (i3 != 0) {
                    for (int k6 = k5; k6 < k5 + l5; k6++) {
                        i = src[(j >> 16) + j5];
                        if (i != 0) {
                            int j3 = i >> 16 & 0xff;
                            int k3 = i >> 8 & 0xff;
                            int l3 = i & 0xff;
                            if (j3 == k3 && k3 == l3)
                                dest[k6 + destPos] = ((j3 * i4 >> 8) << 16) + ((k3 * j4 >> 8) << 8) + (l3 * k4 >> 8);
                            else
                                dest[k6 + destPos] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l4;
                destPos += width2;
                k2 += l2;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    private void transparentSpritePlot(int dest[], int src[], int i, int j, int k, int destPos, int i1,
                                       int j1, int k1, int l1, int i2, int j2, int k2, int l2,
                                       int i3, int j3) {
        int j4 = j2 >> 16 & 0xff;
        int k4 = j2 >> 8 & 0xff;
        int l4 = j2 & 0xff;
        int i5 = k2 >> 16 & 0xff;
        int j5 = k2 >> 8 & 0xff;
        int k5 = k2 & 0xff;
        try {
            int l5 = j;
            for (int i6 = -j1; i6 < 0; i6++) {
                int j6 = (k >> 16) * i2;
                int k6 = l2 >> 16;
                int l6 = i1;
                if (k6 < boundsTopX) {
                    int i7 = boundsTopX - k6;
                    l6 -= i7;
                    k6 = boundsTopX;
                    j += k1 * i7;
                }
                if (k6 + l6 >= boundsBottomX) {
                    int j7 = (k6 + l6) - boundsBottomX;
                    l6 -= j7;
                }
                j3 = 1 - j3;
                if (j3 != 0) {
                    for (int k7 = k6; k7 < k6 + l6; k7++) {
                        i = src[(j >> 16) + j6];
                        if (i != 0) {
                            int k3 = i >> 16 & 0xff;
                            int l3 = i >> 8 & 0xff;
                            int i4 = i & 0xff;
                            if (k3 == l3 && l3 == i4)
                                dest[k7 + destPos] = ((k3 * j4 >> 8) << 16) + ((l3 * k4 >> 8) << 8) + (i4 * l4 >> 8);
                            else if (k3 == 255 && l3 == i4)
                                dest[k7 + destPos] = ((k3 * i5 >> 8) << 16) + ((l3 * j5 >> 8) << 8) + (i4 * k5 >> 8);
                            else
                                dest[k7 + destPos] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l5;
                destPos += width2;
                l2 += i3;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    private void transparentSpritePlot(int dest[], byte colourIdx[], int colours[], int i, int j, int k, int l,
                                       int i1, int j1, int k1, int l1, int i2, int j2, int k2,
                                       int l2, int i3) {
        int i4 = j2 >> 16 & 0xff;
        int j4 = j2 >> 8 & 0xff;
        int k4 = j2 & 0xff;
        try {
            int l4 = j;
            for (int i5 = -j1; i5 < 0; i5++) {
                int j5 = (k >> 16) * i2;
                int k5 = k2 >> 16;
                int l5 = i1;
                if (k5 < boundsTopX) {
                    int i6 = boundsTopX - k5;
                    l5 -= i6;
                    k5 = boundsTopX;
                    j += k1 * i6;
                }
                if (k5 + l5 >= boundsBottomX) {
                    int j6 = (k5 + l5) - boundsBottomX;
                    l5 -= j6;
                }
                i3 = 1 - i3;
                if (i3 != 0) {
                    for (int k6 = k5; k6 < k5 + l5; k6++) {
                        i = colourIdx[(j >> 16) + j5] & 0xff;
                        if (i != 0) {
                            i = colours[i];
                            int j3 = i >> 16 & 0xff;
                            int k3 = i >> 8 & 0xff;
                            int l3 = i & 0xff;
                            if (j3 == k3 && k3 == l3)
                                dest[k6 + l] = ((j3 * i4 >> 8) << 16) + ((k3 * j4 >> 8) << 8) + (l3 * k4 >> 8);
                            else
                                dest[k6 + l] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l4;
                l += width2;
                k2 += l2;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    private void transparentSpritePlot(int dest[], byte coloursUsed[], int colourList[], int i, int j, int k, int l,
                                       int i1, int j1, int k1, int l1, int i2, int j2, int k2,
                                       int l2, int i3, int j3) {
        int j4 = j2 >> 16 & 0xff;
        int k4 = j2 >> 8 & 0xff;
        int l4 = j2 & 0xff;
        int i5 = k2 >> 16 & 0xff;
        int j5 = k2 >> 8 & 0xff;
        int k5 = k2 & 0xff;
        try {
            int l5 = j;
            for (int i6 = -j1; i6 < 0; i6++) {
                int j6 = (k >> 16) * i2;
                int k6 = l2 >> 16;
                int l6 = i1;
                if (k6 < boundsTopX) {
                    int i7 = boundsTopX - k6;
                    l6 -= i7;
                    k6 = boundsTopX;
                    j += k1 * i7;
                }
                if (k6 + l6 >= boundsBottomX) {
                    int j7 = (k6 + l6) - boundsBottomX;
                    l6 -= j7;
                }
                j3 = 1 - j3;
                if (j3 != 0) {
                    for (int k7 = k6; k7 < k6 + l6; k7++) {
                        i = coloursUsed[(j >> 16) + j6] & 0xff;
                        if (i != 0) {
                            i = colourList[i];
                            int k3 = i >> 16 & 0xff;
                            int l3 = i >> 8 & 0xff;
                            int i4 = i & 0xff;
                            if (k3 == l3 && l3 == i4)
                                dest[k7 + l] = ((k3 * j4 >> 8) << 16) + ((l3 * k4 >> 8) << 8) + (i4 * l4 >> 8);
                            else if (k3 == 255 && l3 == i4)
                                dest[k7 + l] = ((k3 * i5 >> 8) << 16) + ((l3 * j5 >> 8) << 8) + (i4 * k5 >> 8);
                            else
                                dest[k7 + l] = i;
                        }
                        j += k1;
                    }

                }
                k += l1;
                j = l5;
                l += width2;
                l2 += i3;
            }

            return;
        } catch (Exception Ex) {
            System.out.println("error in transparent sprite plot routine");
        }
    }

    public void drawstringRight(String text, int x, int y, int font, int colour) {
        drawString(text, x - textWidth(text, font), y, font, colour);
    }

    public void drawStringCenter(String text, int x, int y, int font, int colour) {
        drawString(text, x - textWidth(text, font) / 2, y, font, colour);
    }

    public void drawStringCentered(String text, int x, int y, int font, int colour, int max) {
        try {
            int width = 0;
            byte fontdata[] = gameFonts[font];
            int start = 0;
            int end = 0;
            for (int index = 0; index < text.length(); index++) {
                if (text.charAt(index) == '@' && index + 4 < text.length() && text.charAt(index + 4) == '@')
                    index += 4;
                else if (text.charAt(index) == '~' && index + 4 < text.length() && text.charAt(index + 4) == '~')
                    index += 4;
                else
                    width += fontdata[characterWidth[text.charAt(index)] + 7];
                if (text.charAt(index) == ' ')
                    end = index;
                if (text.charAt(index) == '%') {
                    end = index;
                    width = 1000;
                }
                if (width > max) {
                    if (end <= start)
                        end = index;
                    drawStringCenter(text.substring(start, end), x, y, font, colour);
                    width = 0;
                    start = index = end + 1;
                    y += textHeight(font);
                }
            }

            if (width > 0) {
                drawStringCenter(text.substring(start), x, y, font, colour);
                return;
            }
        } catch (Exception exception) {
            System.out.println("centrepara: " + exception);
            exception.printStackTrace();
        }
    }

    public void drawString(String text, int x, int y, int font, int colour) {
        try {
            byte fontData[] = gameFonts[font];
            for (int idx = 0; idx < text.length(); idx++) {
                if (text.charAt(idx) == '@' && idx + 4 < text.length() && text.charAt(idx + 4) == '@') {
                    if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("red"))
                        colour = 0xff0000;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("lre"))
                        colour = 0xff9040;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("yel"))
                        colour = 0xffff00;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("gre"))
                        colour = 65280;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("blu"))
                        colour = 255;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("cya"))
                        colour = 65535;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("mag"))
                        colour = 0xff00ff;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("whi"))
                        colour = 0xffffff;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("bla"))
                        colour = 0;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("dre"))
                        colour = 0xc00000;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("ora"))
                        colour = 0xff9040;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("ran"))
                        colour = (int) (Math.random() * 16777215D);
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("or1"))
                        colour = 0xffb000;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("or2"))
                        colour = 0xff7000;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("or3"))
                        colour = 0xff3000;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("gr1"))
                        colour = 0xc0ff00;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("gr2"))
                        colour = 0x80ff00;
                    else if (text.substring(idx + 1, idx + 4).equalsIgnoreCase("gr3"))
                        colour = 0x40ff00;
                    idx += 4;
                } else if (text.charAt(idx) == '~' && idx + 4 < text.length() && text.charAt(idx + 4) == '~') {
                    char c = text.charAt(idx + 1);
                    char c1 = text.charAt(idx + 2);
                    char c2 = text.charAt(idx + 3);
                    if (c >= '0' && c <= '9' && c1 >= '0' && c1 <= '9' && c2 >= '0' && c2 <= '9')
                        x = Integer.parseInt(text.substring(idx + 1, idx + 4));
                    idx += 4;
                } else {
                    int width = characterWidth[text.charAt(idx)];
                    if (loggedIn && colour != 0) {
                        drawCharacter(width, x + 1, y, 0, fontData);
                        drawCharacter(width, x, y + 1, 0, fontData);
                    }
                    drawCharacter(width, x, y, colour, fontData);
                    x += fontData[width + 7];
                }
            }
        } catch (Exception exception) {
            System.out.println("drawstring: " + exception);
            exception.printStackTrace();
        }
    }

    private void drawCharacter(int width, int x, int y, int colour, byte font[]) {
        int i1 = x + font[width + 5];
        int j1 = y - font[width + 6];
        int k1 = font[width + 3];
        int l1 = font[width + 4];
        int i2 = font[width] * 16384 + font[width + 1] * 128 + font[width + 2];
        int j2 = i1 + j1 * width2;
        int k2 = width2 - k1;
        int l2 = 0;
        if (j1 < boundsTopY) {
            int i3 = boundsTopY - j1;
            l1 -= i3;
            j1 = boundsTopY;
            i2 += i3 * k1;
            j2 += i3 * width2;
        }
        if (j1 + l1 >= boundsBottomY)
            l1 -= ((j1 + l1) - boundsBottomY) + 1;
        if (i1 < boundsTopX) {
            int j3 = boundsTopX - i1;
            k1 -= j3;
            i1 = boundsTopX;
            i2 += j3;
            j2 += j3;
            l2 += j3;
            k2 += j3;
        }
        if (i1 + k1 >= boundsBottomX) {
            int k3 = ((i1 + k1) - boundsBottomX) + 1;
            k1 -= k3;
            l2 += k3;
            k2 += k3;
        }
        if (k1 > 0 && l1 > 0) {
            plotLetter(pixels, font, colour, i2, j2, k1, l1, k2, l2);
        }
    }

    private void plotLetter(int ai[], byte abyte0[], int i, int j, int k, int l, int i1,
                            int j1, int k1) {
        try {
            int l1 = -(l >> 2);
            l = -(l & 3);
            for (int i2 = -i1; i2 < 0; i2++) {
                for (int j2 = l1; j2 < 0; j2++) {
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;
                }

                for (int k2 = l; k2 < 0; k2++)
                    if (abyte0[j++] != 0)
                        ai[k++] = i;
                    else
                        k++;

                k += j1;
                j += k1;
            }

            return;
        } catch (Exception exception) {
            System.out.println("plotletter: " + exception);
            exception.printStackTrace();
            return;
        }
    }

    public int textHeight(int fontId) {
        if (fontId == 0)
            return 12;
        if (fontId == 1)
            return 14;
        if (fontId == 2)
            return 14;
        if (fontId == 3)
            return 15;
        if (fontId == 4)
            return 15;
        if (fontId == 5)
            return 19;
        if (fontId == 6)
            return 24;
        if (fontId == 7)
            return 29;
        return textHeightFont(fontId);
    }

    public int textHeightFont(int fontId) {
        if (fontId == 0)
            return gameFonts[fontId][8] - 2;
        else
            return gameFonts[fontId][8] - 1;
    }

    public int textWidth(String text, int fontId) {
        int total = 0;
        byte font[] = gameFonts[fontId];
        for (int idx = 0; idx < text.length(); idx++)
            if (text.charAt(idx) == '@' && idx + 4 < text.length() && text.charAt(idx + 4) == '@')
                idx += 4;
            else if (text.charAt(idx) == '~' && idx + 4 < text.length() && text.charAt(idx + 4) == '~')
                idx += 4;
            else
                total += font[characterWidth[text.charAt(idx)] + 7];

        return total;
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
    
    public void loadSpriteRaw(byte abyte0[], int j, int k, boolean flag, boolean flag1) {
        loadSpriteRaw(abyte0, j, k, flag, 1, 1, flag1);
    }

    public void loadSpriteRaw(byte abyte0[], int j, int k, boolean flag, int l, boolean flag1) {
        loadSpriteRaw(abyte0, j, k, flag, l, 1, flag1);
    }

    public void loadSpriteRaw(byte abyte0[], int j, int k, boolean flag, int l, int i1, boolean flag1) {
        int j1 = (abyte0[13 + j] & 0xff) * 256 + (abyte0[12 + j] & 0xff);
        int k1 = (abyte0[15 + j] & 0xff) * 256 + (abyte0[14 + j] & 0xff);
        int l1 = -1;
        int ai[] = new int[256];
        for (int i2 = 0; i2 < 256; i2++) {
            ai[i2] = 0xff000000 + ((abyte0[j + 20 + i2 * 3] & 0xff) << 16) + ((abyte0[j + 19 + i2 * 3] & 0xff) << 8) + (abyte0[j + 18 + i2 * 3] & 0xff);
            if (ai[i2] == -65281)
                l1 = i2;
        }

        if (l1 == -1)
            flag = false;
        if (flag1 && flag)
            ai[l1] = ai[0];
        int j2 = j1 / l;
        int k2 = k1 / i1;
        int ai1[] = new int[j2 * k2];
        for (int l2 = 0; l2 < i1; l2++) {
            for (int i3 = 0; i3 < l; i3++) {
                int j3 = 0;
                for (int k3 = k2 * l2; k3 < k2 * (l2 + 1); k3++) {
                    for (int l3 = j2 * i3; l3 < j2 * (i3 + 1); l3++)
                        if (flag1)
                            ai1[j3++] = abyte0[j + 786 + l3 + (k1 - k3 - 1) * j1] & 0xff;
                        else
                            ai1[j3++] = ai[abyte0[j + 786 + l3 + (k1 - k3 - 1) * j1] & 0xff];

                }

                if (flag1)
                    af(ai1, j2, k2, k++, flag, ai, l1);
                else
                    af(ai1, j2, k2, k++, flag, null, -65281);
            }

        }

    }

    private void af(int ai[], int j, int k, int l, boolean flag, int ai1[], int i1) {
        int j1 = 0;
        int k1 = 0;
        int l1 = j;
        int i2 = k;
        if (flag) {
            label0:
            for (int j2 = 0; j2 < k; j2++) {
                for (int i3 = 0; i3 < j; i3++) {
                    int i4 = ai[i3 + j2 * j];
                    if (i4 == i1)
                        continue;
                    k1 = j2;
                    break label0;
                }

            }

            label1:
            for (int j3 = 0; j3 < j; j3++) {
                for (int j4 = 0; j4 < k; j4++) {
                    int j5 = ai[j3 + j4 * j];
                    if (j5 == i1)
                        continue;
                    j1 = j3;
                    break label1;
                }

            }

            label2:
            for (int k4 = k - 1; k4 >= 0; k4--) {
                for (int k5 = 0; k5 < j; k5++) {
                    int k6 = ai[k5 + k4 * j];
                    if (k6 == i1)
                        continue;
                    i2 = k4 + 1;
                    break label2;
                }

            }

            label3:
            for (int l5 = j - 1; l5 >= 0; l5--) {
                for (int l6 = 0; l6 < k; l6++) {
                    int i7 = ai[l5 + l6 * j];
                    if (i7 == i1)
                        continue;
                    l1 = l5 + 1;
                    break label3;
                }

            }

        }
        
        spriteWidth[l] = l1 - j1;
        spriteHeight[l] = i2 - k1;
        spriteTranslate[l] = flag;
        spriteTranslateX[l] = j1;
        spriteTranslateY[l] = k1;
        spriteWidthFull[l] = j;
        spriteHeightFull[l] = k;
        if (ai1 == null) {
            surfacePixels[l] = new int[(l1 - j1) * (i2 - k1)];
            int k2 = 0;
            for (int k3 = k1; k3 < i2; k3++) {
                for (int l4 = j1; l4 < l1; l4++) {
                    int i6 = ai[l4 + k3 * j];
                    if (flag) {
                        if (i6 == i1)
                            i6 = 0;
                        if (i6 == 0xff000000)
                            i6 = 0xff010101;
                    }
                    surfacePixels[l][k2++] = i6 & 0xffffff;
                }

            }

            return;
        }
        spriteColoursUsed[l] = new byte[(l1 - j1) * (i2 - k1)];
        spriteColourList[l] = ai1;
        int l2 = 0;
        for (int l3 = k1; l3 < i2; l3++) {
            for (int i5 = j1; i5 < l1; i5++) {
                int j6 = ai[i5 + l3 * j];
                if (flag)
                    if (j6 == i1)
                        j6 = 0;
                    else if (j6 == 0)
                        j6 = i1;
                spriteColoursUsed[l][l2++] = (byte) j6;
            }

        }

    }
    
}
