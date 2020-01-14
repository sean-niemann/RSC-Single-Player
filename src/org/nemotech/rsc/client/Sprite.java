package org.nemotech.rsc.client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Sprite {
    
    private static final int TRANSPARENT = Color.BLACK.getRGB();

    public int[] pixels;
    public int width;
    public int height;

    public String packageName = "unknown";
    public int id = -1;
    
    public int[] transparentPixels;
    public byte[] spriteColorsUsed;

    public boolean requiresShift;
    public int xShift = 0;
    public int yShift = 0;

    public int assumedWidth = 0;
    public int assumedHeight = 0;

    public Sprite() {
        pixels = new int[0];
        width = 0;
        height = 0;
    }

    public Sprite(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public void setAssumedDimensions(int assumedWidth, int assumedHeight) {
        this.assumedWidth = assumedWidth;
        this.assumedHeight = assumedHeight;
    }

    public int getAssumedWidth() {
        return assumedWidth;
    }

    public int getAssumedHeight() {
        return assumedHeight;
    }

    public void setName(int id, String packageName) {
        this.id = id;
        this.packageName = packageName;
    }

    public int getID() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setShift(int xShift, int yShift) {
        this.xShift = xShift;
        this.yShift = yShift;
    }

    public void setRequiresShift(boolean requiresShift) {
        this.requiresShift = requiresShift;
    }

    public boolean requiresShift() {
        return requiresShift;
    }

    public int getXShift() {
        return xShift;
    }

    public int getYShift() {
        return yShift;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixel(int i) {
        return pixels[i];
    }

    public void setPixel(int i, int val) {
        pixels[i] = val;
    }
    
    public void allocatePixels(int len) {
        pixels = new int[len];
    }
    
    public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "id = " + id + "; package = " + packageName;
    }

    public static Sprite fromImage(BufferedImage img) {
        int[] pixels = new int[img.getWidth() * img.getHeight()];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                if (rgb == TRANSPARENT) {
                    rgb = 0;
                }
                pixels[x + y * img.getWidth()] = rgb;
            }
        }
        return new Sprite(pixels, img.getWidth(), img.getHeight());
    }
	
	public BufferedImage toImage() {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				img.setRGB(x, y, pixels[x + y * width]);
			}
		}
		return img;
	}

    /**
     * Create a new sprite from raw data packed into the given ByteBuffer
     */
    public static Sprite unpack(ByteBuffer in) throws IOException {
        if (in.remaining() < 25) {
            throw new IOException("Provided buffer too short - Headers missing");
        }
        int width = in.getInt();
        int height = in.getInt();

        boolean requiresShift = in.get() == 1;
        int xShift = in.getInt();
        int yShift = in.getInt();

        int something1 = in.getInt();
        int something2 = in.getInt();

        int[] pixels = new int[width * height];
        if (in.remaining() < (pixels.length * 4)) {
            throw new IOException("Provided buffer too short - Pixels missing");
        }
        for (int c = 0; c < pixels.length; c++) {
            pixels[c] = in.getInt();
        }

        Sprite sprite = new Sprite(pixels, width, height);
        sprite.setRequiresShift(requiresShift);
        sprite.setShift(xShift, yShift);
        sprite.setAssumedDimensions(something1, something2);

        return sprite;
	}
    
    public int[] getTransparentPixels() {
		return transparentPixels;
	}

	public void setTransparentPixels(int[] transparentPixels) {
		this.transparentPixels = transparentPixels;
	}
    
    public byte[] getSpriteColorsUsed() {
		return spriteColorsUsed;
	}
    
    public void allocateSpriteColorsUsed(int len) {
        spriteColorsUsed = new byte[len];
    }

	public void setSpriteColorsUsed(byte[] spriteColorsUsed) {
		this.spriteColorsUsed = spriteColorsUsed;
	}
    
}