package org.nemotech.rsc.client;

import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.client.sound.MusicPlayer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.DataInputStream;
import java.io.IOException;

public abstract class Shell extends Panel implements Runnable, MouseListener, MouseMotionListener, KeyListener {
    
    /* abstract methods */
    
    protected abstract void startGame();

    protected abstract void handleInputs();
    
    protected abstract void onClosing();

    protected abstract void draw();
    
    protected abstract void handleKeyPress(int key);

    protected abstract void handleMouseScroll(int rotation);
    
    /* unused key and mouse events */
    
    @Override public void keyTyped(KeyEvent e) {}

    @Override public void mouseClicked(MouseEvent e) {}

    @Override public void mouseEntered(MouseEvent e) {}

    @Override public void mouseExited(MouseEvent e) {}
    
    /* variables */
    
    private Font fontTimesRoman15, fontHelvetica13b, fontHelvetica12;
    private Image imageLogo;
    private Graphics graphics;
    private Thread gameThread;
    
    protected MusicPlayer musicPlayer;
    protected Application application;
    
    private final String CHAR_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    
    public String logoHeaderText, inputTextCurrent, inputTextFinal, loadingProgessText;
    public boolean keyLeft, keyRight, keyUp, keyDown, keySpace, interlace, closing;
    public int threadSleep, mouseX, mouseY, mouseButtonDown, lastMouseButtonDown, stopTimeout, interlaceTimer,
        loadingProgressPercent, panelWidth, panelHeight, targetFps, maxDrawTime, loadingStep;
    private long[] timings;
    
    /* resizable client code */
    
    protected final Dimension dimension = new Dimension();
    protected boolean resized = true;
    
    protected void doResize(int width, int height) {
        synchronized(dimension) {
            dimension.width = width;
            dimension.height = height;
            resized = false;
        }
    }
    
    /* constructor */

    public Shell() {
        panelWidth = Constants.APPLICATION_WIDTH;
        panelHeight = Constants.APPLICATION_HEIGHT;
        targetFps = 20;
        maxDrawTime = 1000;
        timings = new long[10];
        loadingStep = 1;
        loadingProgessText = "Loading";
        fontTimesRoman15 = new Font("TimesRoman", 0, 15);
        fontHelvetica13b = new Font("Helvetica", Font.BOLD, 13);
        fontHelvetica12 = new Font("Helvetica", 0, 12);
        threadSleep = 10;
        inputTextCurrent = "";
        inputTextFinal = "";
    }

    public void start() {
        System.out.println("\t[Classic Client] Loading process started...");
        panelWidth = Constants.APPLICATION_WIDTH;
        panelHeight = Constants.APPLICATION_HEIGHT;
        application = new Application(this);
        application.addMouseListener(this);
        application.addMouseMotionListener(this);
        application.addKeyListener(this);
        loadingStep = 1;
        gameThread = new Thread(this);
        gameThread.start();
        gameThread.setPriority(1);
    }

    protected void setTargetFps(int i) {
        targetFps = 1000 / i;
    }

    protected void resetTimings() {
        for(int i = 0; i < 10; i++) {
            timings[i] = 0L;
        }
    }
    
    public Application getApplication() {
        return application;
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        char chr = e.getKeyChar();
        int code = e.getKeyCode();
        handleKeyPress(chr);
        switch (code) {
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
            case KeyEvent.VK_UP:
                keyUp = true;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = true;
                break;
            case KeyEvent.VK_SPACE:
                keySpace = true;
                break;
            case KeyEvent.VK_F1:
                interlace = !interlace;
                break;
            default:
                break;
        }
        boolean foundText = false;
        for (int i = 0; i < CHAR_MAP.length(); i++) {
            if (CHAR_MAP.charAt(i) == chr) {
                foundText = true;
                break;
            }
        }
        if (foundText) {
            if (inputTextCurrent.length() < 20) {
                inputTextCurrent += chr;
            }
        }
        if (code == KeyEvent.VK_BACK_SPACE) {
            if (inputTextCurrent.length() > 0) {
                inputTextCurrent = inputTextCurrent.substring(0, inputTextCurrent.length() - 1);
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            inputTextFinal = inputTextCurrent;
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
            case KeyEvent.VK_UP:
                keyUp = false;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = false;
                break;
            case KeyEvent.VK_SPACE:
                keySpace = false;
                break;
            default:
                break;
        }
    }

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseButtonDown = 0;
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseButtonDown = 0;
    }

    @Override
    public synchronized void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mouseX = x;
        mouseY = y;
        if (e.isMetaDown()) {
            mouseButtonDown = 2;
        } else {
            mouseButtonDown = 1;
        }
        lastMouseButtonDown = mouseButtonDown;
        handleMouseDown(mouseButtonDown, x, y);
    }

    protected void handleMouseDown(int i, int j, int k) {
    }

    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (e.isMetaDown()) {
            mouseButtonDown = 2;
        } else {
            mouseButtonDown = 1;
        }
    }
    
    protected void drawTextBox(String s, String s1) {
        Graphics g = getGraphics();
        Font font = new Font("Sans Serif", 1, 15); // Helvetica
        char c = '\u0200';
        char c1 = '\u0158';
        g.setColor(Color.black);
        g.fillRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
        g.setColor(Color.white);
        g.drawRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
        drawString(g, s, font, c / 2, c1 / 2 - 10);
        drawString(g, s1, font, c / 2, c1 / 2 + 10);
    }

    public void closeProgram() {
        closing = true;
        stopTimeout = -2;
        System.out.println("\nSaving player data and closing application...");
        if(musicPlayer.isRunning()) {
            musicPlayer.stop();
        }
        if(application != null) {
            application.dispose();
        }
        onClosing();
        System.exit(0);
    }

    @Override
    public void run() {
        if (loadingStep == 1) {
            loadingStep = 2;
            graphics = getGraphics();
            loadJagex();
            drawLoadingScreen(0, "Loading...");
            startGame();
            loadingStep = 0;
        }
        int i = 0;
        int j = 256;
        int sleep = 1;
        int i1 = 0;
        for (int j1 = 0; j1 < 10; j1++)
            timings[j1] = System.currentTimeMillis();

        //long l = System.currentTimeMillis();
        while (stopTimeout >= 0) {
            if (stopTimeout > 0) {
                stopTimeout--;
                if (stopTimeout == 0) {
                    closeProgram();
                    gameThread = null;
                    return;
                }
            }
            int k1 = j;
            int lastSleep = sleep;
            j = 300;
            sleep = 1;
            long time = System.currentTimeMillis();
            if (timings[i] == 0L) {
                j = k1;
                sleep = lastSleep;
            } else if (time > timings[i])
                j = (int) ((long) (2560 * targetFps) / (time - timings[i]));
            if (j < 25)
                j = 25;
            if (j > 256) {
                j = 256;
                sleep = (int) ((long) targetFps - (time - timings[i]) / 10L);
                if (sleep < threadSleep)
                    sleep = threadSleep;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                /* ignore */
            }
            timings[i] = time;
            i = (i + 1) % 10;
            if (sleep > 1) {
                for (int j2 = 0; j2 < 10; j2++)
                    if (timings[j2] != 0L)
                        timings[j2] += sleep;

            }
            int k2 = 0;
            while (i1 < 256) {
                handleInputs();
                i1 += j;
                if (++k2 > maxDrawTime) {
                    i1 = 0;
                    interlaceTimer += 6;
                    if (interlaceTimer > 25) {
                        interlaceTimer = 0;
                        interlace = true;
                    }
                    break;
                }
            }
            interlaceTimer--;
            i1 &= 0xff;
            if(!closing) {
                draw();
            }
        }
        if(stopTimeout == -1) {
            closeProgram();
        }
        gameThread = null;
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if(loadingStep == 2 && imageLogo != null) {
            drawLoadingScreen(loadingProgressPercent, loadingProgessText);
        }
        /*if(loadingStep == 0) {
            drawBlueBar();
        }*/
    }

    private void loadJagex() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, panelWidth, panelHeight);
        byte buff[] = readDataFile("jagex.jag", "Jagex library", 0);
        if (buff != null) {
            byte logo[] = Util.unpackData("logo.tga", 0, buff);
            imageLogo = createImage(logo);
        }
        buff = readDataFile("fonts.jag", "Game fonts", 5);
        if (buff != null) {
            Surface.createFont(Util.unpackData("h11p.jf", 0, buff), 0);
            Surface.createFont(Util.unpackData("h12b.jf", 0, buff), 1);
            Surface.createFont(Util.unpackData("h12p.jf", 0, buff), 2);
            Surface.createFont(Util.unpackData("h13b.jf", 0, buff), 3);
            Surface.createFont(Util.unpackData("h14b.jf", 0, buff), 4);
            Surface.createFont(Util.unpackData("h16b.jf", 0, buff), 5);
            Surface.createFont(Util.unpackData("h20b.jf", 0, buff), 6);
            Surface.createFont(Util.unpackData("h24b.jf", 0, buff), 7);
        }
    }

    private void drawLoadingScreen(int percent, String text) {
        int midx = (panelWidth - 281) / 2;
        int midy = (panelHeight - 148) / 2;
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, panelWidth, panelHeight + 10);
        graphics.drawImage(imageLogo, midx, midy, this);
        midx += 2;
        midy += 90;
        loadingProgressPercent = percent;
        loadingProgessText = text;
        graphics.setColor(new Color(132, 132, 132));
        graphics.drawRect(midx - 2, midy - 2, 280, 23);
        graphics.fillRect(midx, midy, (277 * percent) / 100, 20);
        graphics.setColor(new Color(198, 198, 198));
        drawString(graphics, text, fontTimesRoman15, midx + 138, midy + 10);
        drawString(graphics, "Created by JAGeX - visit www.jagex.com", fontHelvetica13b, midx + 138, midy + 30);
        drawString(graphics, "\2512001-2003 Andrew Gower and Jagex Ltd", fontHelvetica13b, midx + 138, midy + 44);
        graphics.setColor(new Color(132, 132, 152));
        drawString(graphics, "Serverless Modification by Zoso [sean@nemotech.org]", fontHelvetica12, midx + 138, panelHeight - 20);
        if (logoHeaderText != null) {
            graphics.setColor(Color.WHITE);
            drawString(graphics, logoHeaderText, fontHelvetica13b, midx + 138, midy - 120);
        }
    }

    protected void showLoadingProgress(int i, String s) {
        int j = (panelWidth - 281) / 2;
        int k = (panelHeight - 148) / 2;
        j += 2;
        k += 90;
        loadingProgressPercent = i;
        loadingProgessText = s;
        int l = (277 * i) / 100;
        graphics.setColor(new Color(132, 132, 132));
        graphics.fillRect(j, k, l, 20);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(j + l, k, 277 - l, 20);
        graphics.setColor(new Color(198, 198, 198));
        drawString(graphics, s, fontTimesRoman15, j + 138, k + 10);
    }

    protected void drawString(Graphics g, String s, Font font, int i, int j) {
        FontMetrics fontmetrics = application.getFontMetrics(font);
        fontmetrics.stringWidth(s);
        g.setFont(font);
        g.drawString(s, i - fontmetrics.stringWidth(s) / 2, j + fontmetrics.getHeight() / 4);
        //g.drawString(s, i, j);
    }

    public Image createImage(byte buff[]) {
        int i = buff[13] * 256 + buff[12];
        int j = buff[15] * 256 + buff[14];
        byte abyte1[] = new byte[256];
        byte abyte2[] = new byte[256];
        byte abyte3[] = new byte[256];
        for (int k = 0; k < 256; k++) {
            abyte1[k] = buff[20 + k * 3];
            abyte2[k] = buff[19 + k * 3];
            abyte3[k] = buff[18 + k * 3];
        }

        IndexColorModel indexcolormodel = new IndexColorModel(8, 256, abyte1, abyte2, abyte3);
        byte abyte4[] = new byte[i * j];
        int l = 0;
        for (int i1 = j - 1; i1 >= 0; i1--) {
            for (int j1 = 0; j1 < i; j1++)
                abyte4[l++] = buff[786 + j1 + i1 * i];

        }

        MemoryImageSource memoryimagesource = new MemoryImageSource(i, j, indexcolormodel, abyte4, 0, i);
        return createImage(memoryimagesource);
    }

    protected byte[] readDataFile(String file, String description, int percent) {
        file = Constants.CACHE_DIRECTORY + "jags/" + file;
        int archiveSize = 0;
        int archiveSizeCompressed = 0;
        byte archiveData[] = null;
        try {
            showLoadingProgress(percent, "Loading " + description + " - 0%");
            java.io.InputStream inputstream = Util.openFile(file);
            DataInputStream datainputstream = new DataInputStream(inputstream);
            byte header[] = new byte[6];
            datainputstream.readFully(header, 0, 6);
            archiveSize = ((header[0] & 0xff) << 16) + ((header[1] & 0xff) << 8) + (header[2] & 0xff);
            archiveSizeCompressed = ((header[3] & 0xff) << 16) + ((header[4] & 0xff) << 8) + (header[5] & 0xff);
            showLoadingProgress(percent, "Loading " + description + " - 5%");
            int read = 0;
            archiveData = new byte[archiveSizeCompressed];
            while (read < archiveSizeCompressed) {
                int length = archiveSizeCompressed - read;
                if (length > 1000)
                    length = 1000;
                datainputstream.readFully(archiveData, read, length);
                read += length;
                showLoadingProgress(percent, "Loading " + description + " - " + (5 + (read * 95) / archiveSizeCompressed) + "%");
            }
            datainputstream.close();
        } catch (IOException e) {
            e.printStackTrace(); // was ignored
        }
        showLoadingProgress(percent, "Unpacking " + description);
        if (archiveSizeCompressed != archiveSize) {
            byte decompressed[] = new byte[archiveSize];
            BZLib.decompress(decompressed, archiveSize, archiveData, archiveSizeCompressed, 0);
            return decompressed;
        } else {
            return archiveData;
        }
    }

    @Override
    public Graphics getGraphics() {
        return application.getGraphics();
    }

    @Override
    public Image createImage(int x, int y) {
        return application.createImage(x, y);
    }

}