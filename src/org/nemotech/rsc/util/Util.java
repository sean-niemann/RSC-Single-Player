package org.nemotech.rsc.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.nemotech.rsc.client.BZLib;
import org.nemotech.rsc.model.Point;

public class Util {
    
    public static String formatNumber(int i) { // unused
        String s = String.valueOf(i);
        for (int j = s.length() - 3; j > 0; j -= 3) {
            s = s.substring(0, j) + "," + s.substring(j);
        }

        if (s.length() > 8) {
            s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
        } else if (s.length() > 4) {
            s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
        }
        return s;
    }
    
    public static void readFully(String s, byte abyte0[], int i) throws IOException {
        InputStream inputstream = openFile(s);
        DataInputStream datainputstream = new DataInputStream(inputstream);
        try {
            datainputstream.readFully(abyte0, 0, i);
        } catch (EOFException e) {
        }
        datainputstream.close();
    }

    private static final char[] SPECIAL_CHARS = "~`!@#$%^&*()_-+={}[]|\'\";:?><,./".toCharArray();
    
    public static long usernameToHash(String s) {
        s = s.toLowerCase();
        String s1 = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z')
                s1 = s1 + c;
            else if (c >= '0' && c <= '9')
                s1 = s1 + c;
            else
                s1 = s1 + ' ';
        }

        s1 = s1.trim();
        if (s1.length() > 12)
            s1 = s1.substring(0, 12);
        long l = 0L;
        for (int j = 0; j < s1.length(); j++) {
            char c1 = s1.charAt(j);
            l *= 37L;
            if (c1 >= 'a' && c1 <= 'z')
                l += (1 + c1) - 97;
            else if (c1 >= '0' && c1 <= '9')
                l += (27 + c1) - 48;
        }
        return l;
    }
    
    public static String hashToUsername(long l) {
        if (l < 0L)
            return "invalid_name";
        String s = "";
        while (l != 0L) {
            int i = (int) (l % 37L);
            l /= 37L;
            if (i == 0)
                s = " " + s;
            else if (i < 27) {
                if (l % 37L == 0L)
                    s = (char) ((i + 65) - 1) + s;
                else
                    s = (char) ((i + 97) - 1) + s;
            } else {
                s = (char) ((i + 48) - 27) + s;
            }
        }
        return s;
    }

    
    private static Random random = new Random();

    public static String formatString(String str, int length) {
        String s = "";
        for (int i = 0; i < length; i++)
            if (i >= str.length()) {
                s += " ";
            } else {
                char c = str.charAt(i);
                if (c >= 'a' && c <= 'z') {
                    s += c;
                } else {
                    if (c >= 'A' && c <= 'Z') {
                        s += c;
                    } else if (Arrays.binarySearch(SPECIAL_CHARS, c) != -1) {
                        s += c;
                    } else {
                        if (c >= '0' && c <= '9') {
                            s += c;
                        } else {
                            s += '_';
                        }
                    }
                }
            }
        return s;
    }
    
    public static int average(int[] values) {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        return (int) (total / values.length);
    }

    private static byte getMobCoordOffset(int coord1, int coord2) {
        byte offset = (byte) (coord1 - coord2);
        if (offset < 0) {
            offset += 32;
        }
        return offset;
    }

    public static byte[] getMobPositionOffsets(Point p1, Point p2) {
        byte[] rv = new byte[2];
        rv[0] = getMobCoordOffset(p1.getX(), p2.getX());
        rv[1] = getMobCoordOffset(p1.getY(), p2.getY());
        return rv;
    }
    
    public static Random getRandom() {
        return random;
    }
    
    public static int random(int range) {
        int number = (int) (Math.random() * (range + 1));
        return number < 0 ? 0 : number;
    }
    
    public static boolean inArray(int[] haystack, int needle) {
        for (int option : haystack) {
            if (needle == option) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean inPointArray(Point[] haystack, Point needle) {
        for (Point option : haystack) {
            if (needle.getX() == option.getX() && needle.getY() == option.getY()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean percentChance(int percent) {
        return random(1, 100) <= percent;
    }
    
    public static double random(double low, double high) {
        return high - (random.nextDouble() * low);
    }
    
    public static int random(int low, int high) {
        return low + random.nextInt(high - low + 1);
    }
    
    public static int randomWeighted(int low, int dip, int peak, int max) {
        int total = 0;
        int probability = 100;
        int[] probArray = new int[max + 1];
        for (int x = 0; x < probArray.length; x++) {
            total += probArray[x] = probability;
            if (x < dip || x > peak) {
                probability -= 3;
            } else {
                probability += 3;
            }
        }
        int hit = random(0, total);
        total = 0;
        for (int x = 0; x < probArray.length; x++) {
            if (hit >= total && hit < (total + probArray[x])) {
                return x;
            }
            total += probArray[x];
        }
        return 0;
    }

    public static double round(double value, int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return (bd.doubleValue());
    }

    public static int roundUp(double val) {
        return (int) Math.round(val + 0.5D);
    }
    
    public static final ByteBuffer streamToBuffer(BufferedInputStream in) throws IOException {
        byte[] buffer = new byte[in.available()];
        in.read(buffer, 0, buffer.length);
        return ByteBuffer.wrap(buffer);
    }
    
    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        return (ArrayList<E>) ((elements instanceof Collection) ? new ArrayList<>((Collection<E>) elements) : newArrayList((Iterable<? extends E>) elements.iterator()));
    }

    public static boolean chance(int num) {
        return random(0, num) == num;
    }
    
    public static boolean inArray(String[] haystack, String needle) {
        for (String option : haystack) {
            if (needle.equals(option)) {
                return true;
            }
        }
        return false;
    }

    public static String title(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
            }
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (i + 1 < s.length()) {
                    s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
                }
            }
        }
        return s;
    }
    
    public static List<Class<?>> loadClasses(String pckgname) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        ArrayList<File> directories = new ArrayList<>();
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
            while (resources.hasMoreElements()) {
                URL res = resources.nextElement();
                if (res.getProtocol().equalsIgnoreCase("jar")) {
                    JarURLConnection conn = (JarURLConnection) res.openConnection();
                    JarFile jar = conn.getJarFile();
                    for (JarEntry e : Collections.list(jar.entries())) {
                        if (e.getName().startsWith(pckgname.replace('.', '/')) && e.getName().endsWith(".class") && !e.getName().contains("$")) {
                            String className = e.getName().replace("/", ".").substring(0, e.getName().length() - 6);
                            classes.add(Class.forName(className));
                        }
                    }
                } else {
                    directories.add(new File(URLDecoder.decode(res.getPath(), "UTF-8")));
                }
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
        } catch (UnsupportedEncodingException encex) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)");
        } catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname);
        }

        for (File directory : directories) {
            if (directory.exists()) {
                String[] files = directory.list();
                for (String file : files) {
                    if (file.endsWith(".class")) {
                        classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
                    }
                }
            } else {
                throw new ClassNotFoundException(pckgname + " (" + directory.getPath() + ") does not appear to be a valid package");
            }
        }
        return classes;
    }
    
    public static List<Class<?>> loadClasses(String thePackage, Class<?> theInterface) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        for (Class<?> discovered : loadClasses(thePackage)) {
            if (Arrays.asList(discovered.getInterfaces()).contains(theInterface)) {
                classList.add(discovered);
            }
        }
        return classList;
    }

    public static List<Class<?>> loadInterfaces(String thePackage) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        for (Class<?> discovered : loadClasses(thePackage)) {
            if (discovered.isInterface()) {
                classList.add(discovered);
            }
        }
        return classList;
    }
    
    public static InputStream openFile(String s) throws IOException {
        return new BufferedInputStream(new FileInputStream(s));
    }

    public static int getUnsignedByte(byte data) {
        return data & 0xff;
    }

    public static int getUnsignedShort(byte[] data, int i) {
        return ((data[i] & 0xff) << 8) + (data[i + 1] & 0xff);
    }

    public static int getSignedShort(byte[] data, int i) {
        int ret = getUnsignedByte(data[i]) * 0x100 + getUnsignedByte(data[i + 1]);
        if (ret > 0x7fff) {
            ret -= 0x10000;
        }
        return ret;
    }

    public static String formatAuthString(String s, int maxlen) {
        String str = "";
        for (int i = 0; i < maxlen; i++) {
            if (i >= s.length()) {
                str = str + " ";
            } else {
                char c = s.charAt(i);
                if (c >= 'a' && c <= 'z')
                    str = str + c;
                else if (c >= 'A' && c <= 'Z')
                    str = str + c;
                else if (c >= '0' && c <= '9')
                    str = str + c;
                else
                    str = str + '_';
            }
        }
        return str;
    }

    public static int getDataFileOffset(String filename, byte data[]) {
        int numEntries = getUnsignedShort(data, 0);
        int wantedHash = 0;
        filename = filename.toUpperCase();
        for (int k = 0; k < filename.length(); k++)
            wantedHash = (wantedHash * 61 + filename.charAt(k)) - 32;

        int offset = 2 + numEntries * 10;
        for (int entry = 0; entry < numEntries; entry++) {
            int fileHash = (data[entry * 10 + 2] & 0xff) * 0x1000000 + (data[entry * 10 + 3] & 0xff) * 0x10000 + (data[entry * 10 + 4] & 0xff) * 256 + (data[entry * 10 + 5] & 0xff);
            int fileSize = (data[entry * 10 + 9] & 0xff) * 0x10000 + (data[entry * 10 + 10] & 0xff) * 256 + (data[entry * 10 + 11] & 0xff);
            if (fileHash == wantedHash)
                return offset;
            offset += fileSize;
        }

        return 0;
    }

    public static int getDataFileLength(String filename, byte data[]) {
        int numEntries = getUnsignedShort(data, 0);
        int wantedHash = 0;
        filename = filename.toUpperCase();
        for (int k = 0; k < filename.length(); k++)
            wantedHash = (wantedHash * 61 + filename.charAt(k)) - 32;

        int offset = 2 + numEntries * 10;
        for (int i1 = 0; i1 < numEntries; i1++) {
            int fileHash = (data[i1 * 10 + 2] & 0xff) * 0x1000000 + (data[i1 * 10 + 3] & 0xff) * 0x10000 + (data[i1 * 10 + 4] & 0xff) * 256 + (data[i1 * 10 + 5] & 0xff);
            int fileSize = (data[i1 * 10 + 6] & 0xff) * 0x10000 + (data[i1 * 10 + 7] & 0xff) * 256 + (data[i1 * 10 + 8] & 0xff);
            int fileSizeCompressed = (data[i1 * 10 + 9] & 0xff) * 0x10000 + (data[i1 * 10 + 10] & 0xff) * 256 + (data[i1 * 10 + 11] & 0xff);
            if (fileHash == wantedHash)
                return fileSize;
            offset += fileSizeCompressed;
        }

        return 0;
    }

    public static byte[] unpackData(String s, int i, byte[] data) {
        return Util.unpackData(s, i, data, null);
    }

    public static byte[] unpackData(String filename, int i, byte archiveData[], byte fileData[]) {
        int numEntries = (archiveData[0] & 0xff) * 256 + (archiveData[1] & 0xff);
        int wantedHash = 0;
        filename = filename.toUpperCase();
        for (int l = 0; l < filename.length(); l++) {
            wantedHash = (wantedHash * 61 + filename.charAt(l)) - 32;
        }
        int offset = 2 + numEntries * 10;
        for (int entry = 0; entry < numEntries; entry++) {
            int fileHash = (archiveData[entry * 10 + 2] & 0xff) * 0x1000000 + (archiveData[entry * 10 + 3] & 0xff) * 0x10000 + (archiveData[entry * 10 + 4] & 0xff) * 256 + (archiveData[entry * 10 + 5] & 0xff);
            int fileSize = (archiveData[entry * 10 + 6] & 0xff) * 0x10000 + (archiveData[entry * 10 + 7] & 0xff) * 256 + (archiveData[entry * 10 + 8] & 0xff);
            int fileSizeCompressed = (archiveData[entry * 10 + 9] & 0xff) * 0x10000 + (archiveData[entry * 10 + 10] & 0xff) * 256 + (archiveData[entry * 10 + 11] & 0xff);
            if (fileHash == wantedHash) {
                if (fileData == null)
                    fileData = new byte[fileSize + i];
                if (fileSize != fileSizeCompressed) {
                    BZLib.decompress(fileData, fileSize, archiveData, fileSizeCompressed, offset);
                } else {
                    for (int j = 0; j < fileSize; j++)
                        fileData[j] = archiveData[offset + j];

                }
                return fileData;
            }
            offset += fileSizeCompressed;
        }
        return null;
    }
    
}
