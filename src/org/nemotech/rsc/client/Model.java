package org.nemotech.rsc.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nemotech.rsc.util.Util;

public class Model {

    private static int sine9[];
    private static int sine11[];
    private static int base64Alphabet[];
    public int numVertices;
    public int projectVertexX[];
    public int projectVertexY[];
    public int projectVertexZ[];
    public int vertexViewX[];
    public int vertexViewY[];
    public int vertexIntensity[];
    public byte vertexAmbience[];
    public int numFaces;
    public int faceNumVertices[];
    public int faceVertices[][];
    public int faceFillFront[];
    public int faceFillBack[];
    public int normalMagnitude[];
    public int normalScale[];
    public int faceIntensity[];
    public int faceNormalX[];
    public int faceNormalY[];
    public int faceNormalZ[];
    public int depth;
    public int transformState;
    public boolean visible;
    public int x1;
    public int x2;
    public int y1;
    public int y2;
    public int z1;
    public int z2;
    public boolean textureTranslucent;
    public boolean transparent;
    public int key;
    public int faceTag[];
    public byte isLocalPlayer[];
    public boolean isolated;
    public boolean unlit;
    public boolean unpickable;
    public boolean projected;
    public int maxVerts;
    public int vertexX[];
    public int vertexY[];
    public int vertexZ[];
    public int vertexTransformedX[];
    public int vertexTransformedY[];
    public int vertexTransformedZ[];
    protected int lightDiffuse;
    protected int lightAmbience;
    private boolean autocommit;
    private int magic;
    private int maxFaces;
    private int faceTransStateThing[][];
    private int faceBoundLeft[];
    private int faceBoundRight[];
    private int faceBoundBottom[];
    private int faceBoundTop[];
    private int faceBoundNear[];
    private int faceBoundFar[];
    private int baseX;
    private int baseY;
    private int baseZ;
    private int orientationYaw;
    private int orientationPitch;
    private int orientationRoll;
    private int scaleFx;
    private int scaleFy;
    private int scaleFz;
    private int shearXy;
    private int shearXz;
    private int shearYx;
    private int shearYz;
    private int shearZx;
    private int shearZy;
    private int transformKind;
    private int diameter;
    private int lightDirectionX;
    private int lightDirectionY;
    private int lightDirectionZ;
    private int lightDirectionMagnitude;
    private int dataPtr;

    static {
        sine9 = new int[512];
        sine11 = new int[2048];

        base64Alphabet = new int[256];
        for (int i = 0; i < 256; i++) {
            sine9[i] = (int) (Math.sin((double) i * 0.02454369D) * 32768D);
            sine9[i + 256] = (int) (Math.cos((double) i * 0.02454369D) * 32768D);
        }

        for (int j = 0; j < 1024; j++) {
            sine11[j] = (int) (Math.sin((double) j * 0.00613592315D) * 32768D);
            sine11[j + 1024] = (int) (Math.cos((double) j * 0.00613592315D) * 32768D);
        }

        for (int j1 = 0; j1 < 10; j1++)
            base64Alphabet[48 + j1] = j1;

        for (int k1 = 0; k1 < 26; k1++)
            base64Alphabet[65 + k1] = k1 + 10;

        for (int l1 = 0; l1 < 26; l1++)
            base64Alphabet[97 + l1] = l1 + 36;

        base64Alphabet[163] = 62;
        base64Alphabet[36] = 63;
    }

    public Model(int numVertices, int numFaces) {
        transformState = 1;
        visible = true;
        textureTranslucent = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        unpickable = false;
        projected = false;
        magic = Terrain.COLOR_TRANSPARENT;
        diameter = Terrain.COLOR_TRANSPARENT;
        lightDirectionX = 180;
        lightDirectionY = 155;
        lightDirectionZ = 95;
        lightDirectionMagnitude = 256;
        lightDiffuse = 512;
        lightAmbience = 32;
        allocate(numVertices, numFaces);
        faceTransStateThing = new int[numFaces][1];
        for (int v = 0; v < numFaces; v++)
            faceTransStateThing[v][0] = v;

    }

    public Model(int numVertices, int numFaces, boolean autocommit, boolean isolated, boolean unlit, boolean unpickable, boolean projected) {
        transformState = 1;
        visible = true;
        textureTranslucent = false;
        transparent = false;
        key = -1;
        magic = Terrain.COLOR_TRANSPARENT;
        diameter = Terrain.COLOR_TRANSPARENT;
        lightDirectionX = 180;
        lightDirectionY = 155;
        lightDirectionZ = 95;
        lightDirectionMagnitude = 256;
        lightDiffuse = 512;
        lightAmbience = 32;
        this.autocommit = autocommit;
        this.isolated = isolated;
        this.unlit = unlit;
        this.unpickable = unpickable;
        this.projected = projected;
        allocate(numVertices, numFaces);
    }

    public Model(byte data[], int offset, boolean unused) {
        transformState = 1;
        visible = true;
        textureTranslucent = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        unpickable = false;
        projected = false;
        magic = Terrain.COLOR_TRANSPARENT;
        diameter = Terrain.COLOR_TRANSPARENT;
        lightDirectionX = 180;
        lightDirectionY = 155;
        lightDirectionZ = 95;
        lightDirectionMagnitude = 256;
        lightDiffuse = 512;
        lightAmbience = 32;
        int j = Util.getUnsignedShort(data, offset);
        offset += 2;
        int k = Util.getUnsignedShort(data, offset);
        offset += 2;
        allocate(j, k);
        faceTransStateThing = new int[k][1];
        for (int l = 0; l < j; l++) {
            vertexX[l] = Util.getSignedShort(data, offset);
            offset += 2;
        }

        for (int i1 = 0; i1 < j; i1++) {
            vertexY[i1] = Util.getSignedShort(data, offset);
            offset += 2;
        }

        for (int j1 = 0; j1 < j; j1++) {
            vertexZ[j1] = Util.getSignedShort(data, offset);
            offset += 2;
        }

        numVertices = j;
        for (int k1 = 0; k1 < k; k1++)
            faceNumVertices[k1] = data[offset++] & 0xff;

        for (int l1 = 0; l1 < k; l1++) {
            faceFillFront[l1] = Util.getSignedShort(data, offset);
            offset += 2;
            if (faceFillFront[l1] == 32767)
                faceFillFront[l1] = magic;
        }

        for (int i2 = 0; i2 < k; i2++) {
            faceFillBack[i2] = Util.getSignedShort(data, offset);
            offset += 2;
            if (faceFillBack[i2] == 32767)
                faceFillBack[i2] = magic;
        }

        for (int j2 = 0; j2 < k; j2++) {
            int k2 = data[offset++] & 0xff;
            if (k2 == 0)
                faceIntensity[j2] = 0;
            else
                faceIntensity[j2] = magic;
        }

        for (int l2 = 0; l2 < k; l2++) {
            faceVertices[l2] = new int[faceNumVertices[l2]];
            for (int i3 = 0; i3 < faceNumVertices[l2]; i3++)
                if (j < 256) {
                    faceVertices[l2][i3] = data[offset++] & 0xff;
                } else {
                    faceVertices[l2][i3] = Util.getUnsignedShort(data, offset);
                    offset += 2;
                }

        }

        numFaces = k;
        transformState = 1;
    }

    public Model(String name) {
        transformState = 1;
        visible = true;
        textureTranslucent = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        unpickable = false;
        projected = false;
        magic = Terrain.COLOR_TRANSPARENT;
        diameter = Terrain.COLOR_TRANSPARENT;
        lightDirectionX = 180;
        lightDirectionY = 155;
        lightDirectionZ = 95;
        lightDirectionMagnitude = 256;
        lightDiffuse = 512;
        lightAmbience = 32;
        byte data[];
        try {
            InputStream inputstream = Util.openFile(name);
            DataInputStream datainputstream = new DataInputStream(inputstream);
            data = new byte[3];
            dataPtr = 0;
            for (int i = 0; i < 3; i += datainputstream.read(data, i, 3 - i)) ;
            int sz = readBase64(data);
            data = new byte[sz];
            dataPtr = 0;
            for (int j = 0; j < sz; j += datainputstream.read(data, j, sz - j)) ;
            datainputstream.close();
        } catch (IOException Ex) {
            numVertices = 0;
            numFaces = 0;
            return;
        }
        int nV = readBase64(data);
        int nF = readBase64(data);
        allocate(nV, nF);
        faceTransStateThing = new int[nF][];
        for (int j3 = 0; j3 < nV; j3++) {
            int x = readBase64(data);
            int y = readBase64(data);
            int z = readBase64(data);
            vertexAt(x, y, z);
        }

        for (int k3 = 0; k3 < nF; k3++) {
            int n = readBase64(data);
            int front = readBase64(data);
            int back = readBase64(data);
            int l2 = readBase64(data);
            lightDiffuse = readBase64(data);
            lightAmbience = readBase64(data);
            int gouraud = readBase64(data);
            int vs[] = new int[n];
            for (int i = 0; i < n; i++)
                vs[i] = readBase64(data);

            int ai1[] = new int[l2];
            for (int i4 = 0; i4 < l2; i4++)
                ai1[i4] = readBase64(data);

            int j4 = createFace(n, vs, front, back);
            faceTransStateThing[k3] = ai1;
            if (gouraud == 0)
                faceIntensity[j4] = 0;
            else
                faceIntensity[j4] = magic;
        }

        transformState = 1;
    }

    public Model(Model pieces[], int count, boolean autocommit, boolean isolated, boolean unlit, boolean unpickable) {
        transformState = 1;
        visible = true;
        textureTranslucent = false;
        transparent = false;
        key = -1;
        projected = false;
        magic = Terrain.COLOR_TRANSPARENT;
        diameter = Terrain.COLOR_TRANSPARENT;
        lightDirectionX = 180;
        lightDirectionY = 155;
        lightDirectionZ = 95;
        lightDirectionMagnitude = 256;
        lightDiffuse = 512;
        lightAmbience = 32;
        this.autocommit = autocommit;
        this.isolated = isolated;
        this.unlit = unlit;
        this.unpickable = unpickable;
        merge(pieces, count, false);
    }

    public Model(Model pieces[], int count) {
        transformState = 1;
        visible = true;
        textureTranslucent = false;
        transparent = false;
        key = -1;
        autocommit = false;
        isolated = false;
        unlit = false;
        unpickable = false;
        projected = false;
        magic = Terrain.COLOR_TRANSPARENT;
        diameter = Terrain.COLOR_TRANSPARENT;
        lightDirectionX = 180;
        lightDirectionY = 155;
        lightDirectionZ = 95;
        lightDirectionMagnitude = 256;
        lightDiffuse = 512;
        lightAmbience = 32;
        merge(pieces, count, true);
    }

    private void allocate(int numV, int numF) {
        vertexX = new int[numV];
        vertexY = new int[numV];
        vertexZ = new int[numV];
        vertexIntensity = new int[numV];
        vertexAmbience = new byte[numV];
        faceNumVertices = new int[numF];
        faceVertices = new int[numF][];
        faceFillFront = new int[numF];
        faceFillBack = new int[numF];
        faceIntensity = new int[numF];
        normalScale = new int[numF];
        normalMagnitude = new int[numF];
        if (!projected) {
            projectVertexX = new int[numV];
            projectVertexY = new int[numV];
            projectVertexZ = new int[numV];
            vertexViewX = new int[numV];
            vertexViewY = new int[numV];
        }
        if (!unpickable) {
            isLocalPlayer = new byte[numF];
            faceTag = new int[numF];
        }
        if (autocommit) {
            vertexTransformedX = vertexX;
            vertexTransformedY = vertexY;
            vertexTransformedZ = vertexZ;
        } else {
            vertexTransformedX = new int[numV];
            vertexTransformedY = new int[numV];
            vertexTransformedZ = new int[numV];
        }
        if (!unlit || !isolated) {
            faceNormalX = new int[numF];
            faceNormalY = new int[numF];
            faceNormalZ = new int[numF];
        }
        if (!isolated) {
            faceBoundLeft = new int[numF];
            faceBoundRight = new int[numF];
            faceBoundBottom = new int[numF];
            faceBoundTop = new int[numF];
            faceBoundNear = new int[numF];
            faceBoundFar = new int[numF];
        }
        numFaces = 0;
        numVertices = 0;
        maxVerts = numV;
        maxFaces = numF;
        baseX = baseY = baseZ = 0;
        orientationYaw = orientationPitch = orientationRoll = 0;
        scaleFx = scaleFy = scaleFz = 256;
        shearXy = shearXz = shearYx = shearYz = shearZx = shearZy = 256;
        transformKind = 0;
    }

    public void projectionPrepare() {
        projectVertexX = new int[numVertices];
        projectVertexY = new int[numVertices];
        projectVertexZ = new int[numVertices];
        vertexViewX = new int[numVertices];
        vertexViewY = new int[numVertices];
    }

    public void clear() {
        numFaces = 0;
        numVertices = 0;
    }

    public void reduce(int df, int dz) {
        numFaces -= df;
        if (numFaces < 0)
            numFaces = 0;
        numVertices -= dz;
        if (numVertices < 0)
            numVertices = 0;
    }

    public void merge(Model pieces[], int count, boolean transState) {
        int numF = 0;
        int numV = 0;
        for (int i = 0; i < count; i++) {
            numF += pieces[i].numFaces;
            numV += pieces[i].numVertices;
        }

        allocate(numV, numF);
        if (transState)
            faceTransStateThing = new int[numF][];
        for (int i = 0; i < count; i++) {
            Model source = pieces[i];
            source.commit();
            lightAmbience = source.lightAmbience;
            lightDiffuse = source.lightDiffuse;
            lightDirectionX = source.lightDirectionX;
            lightDirectionY = source.lightDirectionY;
            lightDirectionZ = source.lightDirectionZ;
            lightDirectionMagnitude = source.lightDirectionMagnitude;
            for (int srcF = 0; srcF < source.numFaces; srcF++) {
                int dstVs[] = new int[source.faceNumVertices[srcF]];
                int srcVs[] = source.faceVertices[srcF];
                for (int v = 0; v < source.faceNumVertices[srcF]; v++)
                    dstVs[v] = vertexAt(source.vertexX[srcVs[v]], source.vertexY[srcVs[v]], source.vertexZ[srcVs[v]]);

                int dstF = createFace(source.faceNumVertices[srcF], dstVs, source.faceFillFront[srcF], source.faceFillBack[srcF]);
                faceIntensity[dstF] = source.faceIntensity[srcF];
                normalScale[dstF] = source.normalScale[srcF];
                normalMagnitude[dstF] = source.normalMagnitude[srcF];
                if (transState)
                    if (count > 1) {
                        faceTransStateThing[dstF] = new int[source.faceTransStateThing[srcF].length + 1];
                        faceTransStateThing[dstF][0] = i;
                        for (int i2 = 0; i2 < source.faceTransStateThing[srcF].length; i2++)
                            faceTransStateThing[dstF][i2 + 1] = source.faceTransStateThing[srcF][i2];

                    } else {
                        faceTransStateThing[dstF] = new int[source.faceTransStateThing[srcF].length];
                        for (int j2 = 0; j2 < source.faceTransStateThing[srcF].length; j2++)
                            faceTransStateThing[dstF][j2] = source.faceTransStateThing[srcF][j2];

                    }
            }

        }

        transformState = 1;
    }

    public int vertexAt(int x, int y, int z) {
        for (int l = 0; l < numVertices; l++)
            if (vertexX[l] == x && vertexY[l] == y && vertexZ[l] == z)
                return l;

        if (numVertices >= maxVerts) {
            return -1;
        } else {
            vertexX[numVertices] = x;
            vertexY[numVertices] = y;
            vertexZ[numVertices] = z;
            return numVertices++;
        }
    }

    public int createVertex(int i, int j, int k) {
        if (numVertices >= maxVerts) {
            return -1;
        } else {
            vertexX[numVertices] = i;
            vertexY[numVertices] = j;
            vertexZ[numVertices] = k;
            return numVertices++;
        }
    }

    public int createFace(int n, int vs[], int front, int back) {
        if (numFaces >= maxFaces) {
            return -1;
        } else {
            faceNumVertices[numFaces] = n;
            faceVertices[numFaces] = vs;
            faceFillFront[numFaces] = front;
            faceFillBack[numFaces] = back;
            transformState = 1;
            return numFaces++;
        }
    }

    public Model[] split(int unused1, int unused2, int pieceDx, int pieceDz, int rows, int count, int pieceMaxVertices, boolean pickable) {
        commit();
        int pieceNV[] = new int[count];
        int pieceNF[] = new int[count];
        for (int i = 0; i < count; i++) {
            pieceNV[i] = 0;
            pieceNF[i] = 0;
        }

        for (int f = 0; f < numFaces; f++) {
            int sumX = 0;
            int sumZ = 0;
            int n = faceNumVertices[f];
            int vs[] = faceVertices[f];
            for (int i = 0; i < n; i++) {
                sumX += vertexX[vs[i]];
                sumZ += vertexZ[vs[i]];
            }

            int p = sumX / (n * pieceDx) + (sumZ / (n * pieceDz)) * rows;
            pieceNV[p] += n;
            pieceNF[p]++;
        }

        Model pieces[] = new Model[count];
        for (int i = 0; i < count; i++) {
            if (pieceNV[i] > pieceMaxVertices)
                pieceNV[i] = pieceMaxVertices;
            pieces[i] = new Model(pieceNV[i], pieceNF[i], true, true, true, pickable, true);
            pieces[i].lightDiffuse = lightDiffuse;
            pieces[i].lightAmbience = lightAmbience;
        }

        for (int f = 0; f < numFaces; f++) {
            int sumX = 0;
            int sumZ = 0;
            int n = faceNumVertices[f];
            int vs[] = faceVertices[f];
            for (int i = 0; i < n; i++) {
                sumX += vertexX[vs[i]];
                sumZ += vertexZ[vs[i]];
            }

            int p = sumX / (n * pieceDx) + (sumZ / (n * pieceDz)) * rows;
            copyLighting(pieces[p], vs, n, f);
        }

        for (int p = 0; p < count; p++)
            pieces[p].projectionPrepare();

        return pieces;
    }

    public void copyLighting(Model model, int srcVs[], int nV, int inF) {
        int dstVs[] = new int[nV];
        for (int inV = 0; inV < nV; inV++) {
            int outV = dstVs[inV] = model.vertexAt(vertexX[srcVs[inV]], vertexY[srcVs[inV]], vertexZ[srcVs[inV]]);
            model.vertexIntensity[outV] = vertexIntensity[srcVs[inV]];
            model.vertexAmbience[outV] = vertexAmbience[srcVs[inV]];
        }

        int outF = model.createFace(nV, dstVs, faceFillFront[inF], faceFillBack[inF]);
        if (!model.unpickable && !unpickable)
            model.faceTag[outF] = faceTag[inF];
        model.faceIntensity[outF] = faceIntensity[inF];
        model.normalScale[outF] = normalScale[inF];
        model.normalMagnitude[outF] = normalMagnitude[inF];
    }

    public void setLight(boolean gouraud, int ambient, int diffuse, int x, int y, int z) {
        lightAmbience = 256 - ambient * 4;
        lightDiffuse = (64 - diffuse) * 16 + 128;
        if (unlit)
            return;
        for (int i = 0; i < numFaces; i++)
            if (gouraud)
                faceIntensity[i] = magic;
            else
                faceIntensity[i] = 0;

        lightDirectionX = x;
        lightDirectionY = y;
        lightDirectionZ = z;
        lightDirectionMagnitude = (int) Math.sqrt(x * x + y * y + z * z);
        light();
    }

    public void setLight(int ambience, int diffuse, int x, int y, int z) {
        lightAmbience = 256 - ambience * 4;
        lightDiffuse = (64 - diffuse) * 16 + 128;
        if (!unlit) {
            lightDirectionX = x;
            lightDirectionY = y;
            lightDirectionZ = z;
            lightDirectionMagnitude = (int) Math.sqrt(x * x + y * y + z * z);
            light();
        }
    }

    public void setLight(int x, int y, int z) {
        if (!unlit) {
            lightDirectionX = x;
            lightDirectionY = y;
            lightDirectionZ = z;
            lightDirectionMagnitude = (int) Math.sqrt(x * x + y * y + z * z);
            light();
        }
    }

    public void setVertexAmbience(int v, int ambience) {
        vertexAmbience[v] = (byte) ambience;
    }

    public void rotate(int yaw, int pitch, int roll) {
        orientationYaw = orientationYaw + yaw & 0xff;
        orientationPitch = orientationPitch + pitch & 0xff;
        orientationRoll = orientationRoll + roll & 0xff;
        determineTransformKind();
        transformState = 1;
    }

    public void orient(int yaw, int pitch, int roll) {
        orientationYaw = yaw & 0xff;
        orientationPitch = pitch & 0xff;
        orientationRoll = roll & 0xff;
        determineTransformKind();
        transformState = 1;
    }

    public void translate(int x, int y, int z) {
        baseX += x;
        baseY += y;
        baseZ += z;
        determineTransformKind();
        transformState = 1;
    }

    public void place(int x, int y, int z) {
        baseX = x;
        baseY = y;
        baseZ = z;
        determineTransformKind();
        transformState = 1;
    }

    private void determineTransformKind() {
        if (shearXy != 256 || shearXz != 256 || shearYx != 256 || shearYz != 256 || shearZx != 256 || shearZy != 256) {
            transformKind = 4;
        } else if (scaleFx != 256 || scaleFy != 256 || scaleFz != 256) {
            transformKind = 3;
        } else if (orientationYaw != 0 || orientationPitch != 0 || orientationRoll != 0) {
            transformKind = 2;
        } else if (baseX != 0 || baseY != 0 || baseZ != 0) {
            transformKind = 1;
        } else {
            transformKind = 0;
        }
    }

    private void applyTranslate(int dx, int dy, int dz) {
        for (int v = 0; v < numVertices; v++) {
            vertexTransformedX[v] += dx;
            vertexTransformedY[v] += dy;
            vertexTransformedZ[v] += dz;
        }

    }

    private void applyRotation(int yaw, int roll, int pitch) {
        for (int v = 0; v < numVertices; v++) {
            if (pitch != 0) {
                int sin = sine9[pitch];
                int cos = sine9[pitch + 256];
                int x = vertexTransformedY[v] * sin + vertexTransformedX[v] * cos >> 15;
                vertexTransformedY[v] = vertexTransformedY[v] * cos - vertexTransformedX[v] * sin >> 15;
                vertexTransformedX[v] = x;
            }
            if (yaw != 0) {
                int sin = sine9[yaw];
                int cos = sine9[yaw + 256];
                int y = vertexTransformedY[v] * cos - vertexTransformedZ[v] * sin >> 15;
                vertexTransformedZ[v] = vertexTransformedY[v] * sin + vertexTransformedZ[v] * cos >> 15;
                vertexTransformedY[v] = y;
            }
            if (roll != 0) {
                int sin = sine9[roll];
                int cos = sine9[roll + 256];
                int x = vertexTransformedZ[v] * sin + vertexTransformedX[v] * cos >> 15;
                vertexTransformedZ[v] = vertexTransformedZ[v] * cos - vertexTransformedX[v] * sin >> 15;
                vertexTransformedX[v] = x;
            }
        }

    }

    private void applyShear(int xy, int xz, int yx, int yz, int zx, int zy) {
        for (int idx = 0; idx < numVertices; idx++) {
            if (xy != 0)
                vertexTransformedX[idx] += vertexTransformedY[idx] * xy >> 8;
            if (xz != 0)
                vertexTransformedZ[idx] += vertexTransformedY[idx] * xz >> 8;
            if (yx != 0)
                vertexTransformedX[idx] += vertexTransformedZ[idx] * yx >> 8;
            if (yz != 0)
                vertexTransformedY[idx] += vertexTransformedZ[idx] * yz >> 8;
            if (zx != 0)
                vertexTransformedZ[idx] += vertexTransformedX[idx] * zx >> 8;
            if (zy != 0)
                vertexTransformedY[idx] += vertexTransformedX[idx] * zy >> 8;
        }

    }

    private void applyScale(int fx, int fy, int fz) {
        for (int v = 0; v < numVertices; v++) {
            vertexTransformedX[v] = vertexTransformedX[v] * fx >> 8;
            vertexTransformedY[v] = vertexTransformedY[v] * fy >> 8;
            vertexTransformedZ[v] = vertexTransformedZ[v] * fz >> 8;
        }

    }

    private void computeBounds() {
        x1 = y1 = z1 = 0xf423f;
        diameter = x2 = y2 = z2 = 0xfff0bdc1;
        for (int face = 0; face < numFaces; face++) {
            int vs[] = faceVertices[face];
            int v = vs[0];
            int n = faceNumVertices[face];
            int x1;
            int x2 = x1 = vertexTransformedX[v];
            int y1;
            int y2 = y1 = vertexTransformedY[v];
            int z1;
            int z2 = z1 = vertexTransformedZ[v];
            for (int i = 0; i < n; i++) {
                v = vs[i];
                if (vertexTransformedX[v] < x1)
                    x1 = vertexTransformedX[v];
                else if (vertexTransformedX[v] > x2)
                    x2 = vertexTransformedX[v];
                if (vertexTransformedY[v] < y1)
                    y1 = vertexTransformedY[v];
                else if (vertexTransformedY[v] > y2)
                    y2 = vertexTransformedY[v];
                if (vertexTransformedZ[v] < z1)
                    z1 = vertexTransformedZ[v];
                else if (vertexTransformedZ[v] > z2)
                    z2 = vertexTransformedZ[v];
            }

            if (!isolated) {
                faceBoundLeft[face] = x1;
                faceBoundRight[face] = x2;
                faceBoundBottom[face] = y1;
                faceBoundTop[face] = y2;
                faceBoundNear[face] = z1;
                faceBoundFar[face] = z2;
            }
            if (x2 - x1 > diameter)
                diameter = x2 - x1;
            if (y2 - y1 > diameter)
                diameter = y2 - y1;
            if (z2 - z1 > diameter)
                diameter = z2 - z1;
            if (x1 < this.x1)
                this.x1 = x1;
            if (x2 > this.x2)
                this.x2 = x2;
            if (y1 < this.y1)
                this.y1 = y1;
            if (y2 > this.y2)
                this.y2 = y2;
            if (z1 < this.z1)
                this.z1 = z1;
            if (z2 > this.z2)
                this.z2 = z2;
        }

    }

    public void light() {
        if (unlit)
            return;
        int divisor = lightDiffuse * lightDirectionMagnitude >> 8;
        for (int face = 0; face < numFaces; face++)
            if (faceIntensity[face] != magic)
                faceIntensity[face] = (faceNormalX[face] * lightDirectionX + faceNormalY[face] * lightDirectionY + faceNormalZ[face] * lightDirectionZ) / divisor;

        int normalX[] = new int[numVertices];
        int normalY[] = new int[numVertices];
        int normalZ[] = new int[numVertices];
        int normalMagnitude[] = new int[numVertices];
        for (int k = 0; k < numVertices; k++) {
            normalX[k] = 0;
            normalY[k] = 0;
            normalZ[k] = 0;
            normalMagnitude[k] = 0;
        }

        for (int face = 0; face < numFaces; face++)
            if (faceIntensity[face] == magic) {
                for (int v = 0; v < faceNumVertices[face]; v++) {
                    int k1 = faceVertices[face][v];
                    normalX[k1] += faceNormalX[face];
                    normalY[k1] += faceNormalY[face];
                    normalZ[k1] += faceNormalZ[face];
                    normalMagnitude[k1]++;
                }

            }

        for (int v = 0; v < numVertices; v++)
            if (normalMagnitude[v] > 0)
                vertexIntensity[v] = (normalX[v] * lightDirectionX + normalY[v] * lightDirectionY + normalZ[v] * lightDirectionZ) / (divisor * normalMagnitude[v]);

    }

    public void relight() {
        if (unlit && isolated)
            return;
        for (int face = 0; face < numFaces; face++) {
            int verts[] = faceVertices[face];
            int aX = vertexTransformedX[verts[0]];
            int aY = vertexTransformedY[verts[0]];
            int aZ = vertexTransformedZ[verts[0]];
            int bX = vertexTransformedX[verts[1]] - aX;
            int bY = vertexTransformedY[verts[1]] - aY;
            int bZ = vertexTransformedZ[verts[1]] - aZ;
            int cX = vertexTransformedX[verts[2]] - aX;
            int cY = vertexTransformedY[verts[2]] - aY;
            int cZ = vertexTransformedZ[verts[2]] - aZ;
            int normX = bY * cZ - cY * bZ;
            int normY = bZ * cX - cZ * bX;
            int normZ;
            for (normZ = bX * cY - cX * bY; normX > 8192 || normY > 8192 || normZ > 8192 || normX < -8192 || normY < -8192 || normZ < -8192; normZ >>= 1) {
                normX >>= 1;
                normY >>= 1;
            }

            int normMag = (int) (256D * Math.sqrt(normX * normX + normY * normY + normZ * normZ));
            if (normMag <= 0)
                normMag = 1;
            faceNormalX[face] = (normX * 0x10000) / normMag;
            faceNormalY[face] = (normY * 0x10000) / normMag;
            faceNormalZ[face] = (normZ * 65535) / normMag;
            normalScale[face] = -1;
        }

        light();
    }

    public void apply() {
        if (transformState == 2) {
            transformState = 0;
            for (int v = 0; v < numVertices; v++) {
                vertexTransformedX[v] = vertexX[v];
                vertexTransformedY[v] = vertexY[v];
                vertexTransformedZ[v] = vertexZ[v];
            }

            x1 = y1 = z1 = 0xff676981;
            diameter = x2 = y2 = z2 = 0x98967f;
            return;
        }
        if (transformState == 1) {
            transformState = 0;
            for (int v = 0; v < numVertices; v++) {
                vertexTransformedX[v] = vertexX[v];
                vertexTransformedY[v] = vertexY[v];
                vertexTransformedZ[v] = vertexZ[v];
            }

            if (transformKind >= 2)
                applyRotation(orientationYaw, orientationPitch, orientationRoll);
            if (transformKind >= 3)
                applyScale(scaleFx, scaleFy, scaleFz);
            if (transformKind >= 4)
                applyShear(shearXy, shearXz, shearYx, shearYz, shearZx, shearZy);
            if (transformKind >= 1)
                applyTranslate(baseX, baseY, baseZ);
            computeBounds();
            relight();
        }
    }

    public void project(int cameraX, int cameraY, int cameraZ, int cameraPitch, int cameraRoll, int cameraYaw, int viewDist, int clipNear) {
        apply();
        if (z1 > Scene.frustumNearZ || z2 < Scene.furstumFarZ || x1 > Scene.frustumMinX || x2 < Scene.frustumMaxX || y1 > Scene.furstumMinY || y2 < Scene.furstumMaxY) {
            visible = false;
            return;
        }
        visible = true;
        int yawSin = 0;
        int yawCos = 0;
        int pitchSin = 0;
        int pitchCos = 0;
        int rollSin = 0;
        int rollCos = 0;
        if (cameraYaw != 0) {
            yawSin = sine11[cameraYaw];
            yawCos = sine11[cameraYaw + 1024];
        }
        if (cameraRoll != 0) {
            rollSin = sine11[cameraRoll];
            rollCos = sine11[cameraRoll + 1024];
        }
        if (cameraPitch != 0) {
            pitchSin = sine11[cameraPitch];
            pitchCos = sine11[cameraPitch + 1024];
        }
        for (int v = 0; v < numVertices; v++) {
            int x = vertexTransformedX[v] - cameraX;
            int y = vertexTransformedY[v] - cameraY;
            int z = vertexTransformedZ[v] - cameraZ;
            if (cameraYaw != 0) {
                int X = y * yawSin + x * yawCos >> 15;
                y = y * yawCos - x * yawSin >> 15;
                x = X;
            }
            if (cameraRoll != 0) {
                int X = z * rollSin + x * rollCos >> 15;
                z = z * rollCos - x * rollSin >> 15;
                x = X;
            }
            if (cameraPitch != 0) {
                int Y = y * pitchCos - z * pitchSin >> 15;
                z = y * pitchSin + z * pitchCos >> 15;
                y = Y;
            }
            if (z >= clipNear)
                vertexViewX[v] = (x << viewDist) / z;
            else
                vertexViewX[v] = x << viewDist;
            if (z >= clipNear)
                vertexViewY[v] = (y << viewDist) / z;
            else
                vertexViewY[v] = y << viewDist;
            projectVertexX[v] = x;
            projectVertexY[v] = y;
            projectVertexZ[v] = z;
        }

    }

    public void commit() {
        apply();
        for (int i = 0; i < numVertices; i++) {
            vertexX[i] = vertexTransformedX[i];
            vertexY[i] = vertexTransformedY[i];
            vertexZ[i] = vertexTransformedZ[i];
        }

        baseX = baseY = baseZ = 0;
        orientationYaw = orientationPitch = orientationRoll = 0;
        scaleFx = scaleFy = scaleFz = 256;
        shearXy = shearXz = shearYx = shearYz = shearZx = shearZy = 256;
        transformKind = 0;
    }

    public Model copy() {
        Model pieces[] = new Model[1];
        pieces[0] = this;
        Model gameModel = new Model(pieces, 1);
        gameModel.depth = depth;
        gameModel.transparent = transparent;
        return gameModel;
    }

    public Model copy(boolean autocommit, boolean isolated, boolean unlit, boolean pickable) {
        Model pieces[] = new Model[1];
        pieces[0] = this;
        Model gameModel = new Model(pieces, 1, autocommit, isolated, unlit, pickable);
        gameModel.depth = depth;
        return gameModel;
    }

    public void copyPosition(Model model) {
        orientationYaw = model.orientationYaw;
        orientationPitch = model.orientationPitch;
        orientationRoll = model.orientationRoll;
        baseX = model.baseX;
        baseY = model.baseY;
        baseZ = model.baseZ;
        determineTransformKind();
        transformState = 1;
    }

    public int readBase64(byte buff[]) {
        for (; buff[dataPtr] == 10 || buff[dataPtr] == 13; dataPtr++) ;
        int hi = base64Alphabet[buff[dataPtr++] & 0xff];
        int mid = base64Alphabet[buff[dataPtr++] & 0xff];
        int lo = base64Alphabet[buff[dataPtr++] & 0xff];
        int val = (hi * 4096 + mid * 64 + lo) - 0x20000;
        if (val == 0x1e240)
            val = magic;
        return val;
    }
}
