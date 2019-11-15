package org.nemotech.rsc.client;

public class BZLib {

    public static int decompress(byte out[], int outSize, byte in[], int inSize, int offset) {
        Context block = new Context();
        block.input = in;
        block.nextIn = offset;
        block.output = out;
        block.availOut = 0;
        block.availIn = inSize;
        block.decompressedSize = outSize;
        block.bsLive = 0;
        block.bsBuff = 0;
        block.totalInLo32 = 0;
        block.totalInHi32 = 0;
        block.totalOutLo32 = 0;
        block.totalOutHi32 = 0;
        block.blockNo = 0;
        decompress(block);
        outSize -= block.decompressedSize;
        return outSize;
    }

    private static void nextHeader(Context state) {
        byte cStateOutCh = state.stateOutCh;
        int cStateOutLen = state.stateOutLen;
        int cNblockUsed = state.nblockUsed;
        int cK0 = state.k0;
        int cTt[] = state.tt;
        int cTpos = state.tpos;
        byte output[] = state.output;
        int csNextOut = state.availOut;
        int csAvailOut = state.decompressedSize;
        int asdasdasd = csAvailOut;
        int sSaveNblockPP = state.saveNblock + 1;
        returnNotr:
        do {
            if (cStateOutLen > 0) {
                do {
                    if (csAvailOut == 0)
                        break returnNotr;
                    if (cStateOutLen == 1)
                        break;
                    output[csNextOut] = cStateOutCh;
                    cStateOutLen--;
                    csNextOut++;
                    csAvailOut--;
                } while (true);
                if (csAvailOut == 0) {
                    cStateOutLen = 1;
                    break;
                }
                output[csNextOut] = cStateOutCh;
                csNextOut++;
                csAvailOut--;
            }
            boolean flag = true;
            while (flag) {
                flag = false;
                if (cNblockUsed == sSaveNblockPP) {
                    cStateOutLen = 0;
                    break returnNotr;
                }
                cStateOutCh = (byte) cK0;
                cTpos = cTt[cTpos];
                byte k1 = (byte) (cTpos & 0xff);
                cTpos >>= 8;
                cNblockUsed++;
                if (k1 != cK0) {
                    cK0 = k1;
                    if (csAvailOut == 0) {
                        cStateOutLen = 1;
                    } else {
                        output[csNextOut] = cStateOutCh;
                        csNextOut++;
                        csAvailOut--;
                        flag = true;
                        continue;
                    }
                    break returnNotr;
                }
                if (cNblockUsed != sSaveNblockPP)
                    continue;
                if (csAvailOut == 0) {
                    cStateOutLen = 1;
                    break returnNotr;
                }
                output[csNextOut] = cStateOutCh;
                csNextOut++;
                csAvailOut--;
                flag = true;
            }
            cStateOutLen = 2;
            cTpos = cTt[cTpos];
            byte k2 = (byte) (cTpos & 0xff);
            cTpos >>= 8;
            if (++cNblockUsed != sSaveNblockPP)
                if (k2 != cK0) {
                    cK0 = k2;
                } else {
                    cStateOutLen = 3;
                    cTpos = cTt[cTpos];
                    byte k3 = (byte) (cTpos & 0xff);
                    cTpos >>= 8;
                    if (++cNblockUsed != sSaveNblockPP)
                        if (k3 != cK0) {
                            cK0 = k3;
                        } else {
                            cTpos = cTt[cTpos];
                            byte byte3 = (byte) (cTpos & 0xff);
                            cTpos >>= 8;
                            cNblockUsed++;
                            cStateOutLen = (byte3 & 0xff) + 4;
                            cTpos = cTt[cTpos];
                            cK0 = (byte) (cTpos & 0xff);
                            cTpos >>= 8;
                            cNblockUsed++;
                        }
                }
        } while(true);
        int i2 = state.totalOutLo32;
        state.totalOutLo32 += asdasdasd - csAvailOut;
        if (state.totalOutLo32 < i2)
            state.totalOutHi32++;
        state.stateOutCh = cStateOutCh;
        state.stateOutLen = cStateOutLen;
        state.nblockUsed = cNblockUsed;
        state.k0 = cK0;
        state.tt = cTt;
        state.tpos = cTpos;
        state.output = output;
        state.availOut = csNextOut;
        state.decompressedSize = csAvailOut;
    }

    private static void decompress(Context state) {
        int gMinLen = 0;
        int gLimit[] = null;
        int gBase[] = null;
        int gPerm[] = null;
        state.blocksize100k = 1;
        if (state.tt == null) {
            state.tt = new int[state.blocksize100k * 100000];
        }
        boolean goingandshit = true;
        while (goingandshit) {
            byte uc = getUchar(state);
            if (uc == 23)
                return;
            for(int i = 0; i < 5; i++) {
                uc = getUchar(state);
            }
            state.blockNo++;
            for(int i = 0; i < 4; i++) {
                uc = getUchar(state);
            }
            uc = getBit(state);
            state.blockRandomised = uc != 0;
            if (state.blockRandomised)
                System.out.println("PANIC! RANDOMISED BLOCK!");
            state.origPtr = 0;
            uc = getUchar(state);
            state.origPtr = state.origPtr << 8 | uc & 0xff;
            uc = getUchar(state);
            state.origPtr = state.origPtr << 8 | uc & 0xff;
            uc = getUchar(state);
            state.origPtr = state.origPtr << 8 | uc & 0xff;
            for (int i = 0; i < 16; i++) {
                uc = getBit(state);
                state.inUse_16[i] = uc == 1;
            }

            for (int i = 0; i < 256; i++)
                state.inUse[i] = false;

            for (int i = 0; i < 16; i++)
                if (state.inUse_16[i]) {
                    for (int j = 0; j < 16; j++) {
                        uc = getBit(state);
                        if (uc == 1)
                            state.inUse[i * 16 + j] = true;
                    }

                }

            makeMaps(state);
            int alphaSize = state.nInUse + 2;
            int nGroups = getBits(3, state);
            int nSelectors = getBits(15, state);
            for (int i = 0; i < nSelectors; i++) {
                int j = 0;
                do {
                    uc = getBit(state);
                    if (uc == 0)
                        break;
                    j++;
                } while (true);
                state.selectorMtf[i] = (byte) j;
            }

            byte pos[] = new byte[6];
            for (byte v = 0; v < nGroups; v++)
                pos[v] = v;

            for (int i = 0; i < nSelectors; i++) {
                byte v = state.selectorMtf[i];
                byte tmp = pos[v];
                for (; v > 0; v--)
                    pos[v] = pos[v - 1];

                pos[0] = tmp;
                state.selector[i] = tmp;
            }

            for (int t = 0; t < nGroups; t++) {
                int curr = getBits(5, state);
                for (int i = 0; i < alphaSize; i++) {
                    do {
                        uc = getBit(state);
                        if (uc == 0)
                            break;
                        uc = getBit(state);
                        if (uc == 0)
                            curr++;
                        else
                            curr--;
                    } while (true);
                    state.len[t][i] = (byte) curr;
                }

            }

            for (int t = 0; t < nGroups; t++) {
                byte minLen = 32;
                int maxLen = 0;
                for (int l1 = 0; l1 < alphaSize; l1++) {
                    if (state.len[t][l1] > maxLen)
                        maxLen = state.len[t][l1];
                    if (state.len[t][l1] < minLen)
                        minLen = state.len[t][l1];
                }

                createDecodeTables(state.limit[t], state.base[t], state.perm[t], state.len[t], minLen, maxLen, alphaSize);
                state.minLens[t] = minLen;
            }

            int eob = state.nInUse + 1;
            int nblockMax = 100000 * state.blocksize100k;
            int groupNo = -1;
            int groupPos = 0;
            for (int i = 0; i <= 255; i++)
                state.unzftab[i] = 0;

            int kk = 4095; // MTFASIZE-1;
            for (int ii = 15; ii >= 0; ii--) {
                for (int jj = 15; jj >= 0; jj--) {
                    state.mtfa[kk] = (byte) (ii * 16 + jj);
                    kk--;
                }

                state.mtfbase[ii] = kk + 1;
            }

            int nblock = 0;
            // GETMTFVAL
            if (groupPos == 0) {
                groupNo++;
                groupPos = 50; // BZGSIZE
                byte gSel = state.selector[groupNo];
                gMinLen = state.minLens[gSel];
                gLimit = state.limit[gSel];
                gPerm = state.perm[gSel];
                gBase = state.base[gSel];
            }
            groupPos--;
            int zn = gMinLen;
            int zvec;
            byte zj;
            for (zvec = getBits(zn, state); zvec > gLimit[zn]; zvec = zvec << 1 | zj) {
                zn++;
                zj = getBit(state);
            }

            for (int nextSym = gPerm[zvec - gBase[zn]]; nextSym != eob; )
                if (nextSym == 0 || nextSym == 1) { // BZRUNA, BZRUNB
                    int es = -1;
                    int N = 1;
                    do {
                        if (nextSym == 0)
                            es += N;
                        else if (nextSym == 1)
                            es += 2 * N;
                        N *= 2;
                        // GETMTFVAL, y da fuk did they not subroutine this
                        if (groupPos == 0) {
                            groupNo++;
                            groupPos = 50;
                            byte gSel = state.selector[groupNo];
                            gMinLen = state.minLens[gSel];
                            gLimit = state.limit[gSel];
                            gPerm = state.perm[gSel];
                            gBase = state.base[gSel];
                        }
                        groupPos--;
                        int zn_2 = gMinLen;
                        int zvec_2;
                        byte zj_2;
                        for (zvec_2 = getBits(zn_2, state); zvec_2 > gLimit[zn_2]; zvec_2 = zvec_2 << 1 | zj_2) {
                            zn_2++;
                            zj_2 = getBit(state);
                        }

                        nextSym = gPerm[zvec_2 - gBase[zn_2]];
                    } while (nextSym == 0 || nextSym == 1);
                    es++;
                    uc = state.setToUnseq[state.mtfa[state.mtfbase[0]] & 0xff];
                    state.unzftab[uc & 0xff] += es;
                    for (; es > 0; es--) {
                        state.tt[nblock] = uc & 0xff;
                        nblock++;
                    }

                } else {
                    int nn = nextSym - 1;
                    if (nn < 16) { // MTFLSIZE
                        int pp = state.mtfbase[0];
                        uc = state.mtfa[pp + nn];
                        for (; nn > 3; nn -= 4) {
                            int z = pp + nn;
                            state.mtfa[z] = state.mtfa[z - 1];
                            state.mtfa[z - 1] = state.mtfa[z - 2];
                            state.mtfa[z - 2] = state.mtfa[z - 3];
                            state.mtfa[z - 3] = state.mtfa[z - 4];
                        }

                        for (; nn > 0; nn--)
                            state.mtfa[pp + nn] = state.mtfa[(pp + nn) - 1];

                        state.mtfa[pp] = uc;
                    } else {
                        int lno = nn / 16;
                        int off = nn % 16;
                        int pp = state.mtfbase[lno] + off;
                        uc = state.mtfa[pp];
                        for (; pp > state.mtfbase[lno]; pp--)
                            state.mtfa[pp] = state.mtfa[pp - 1];

                        state.mtfbase[lno]++;
                        for (; lno > 0; lno--) {
                            state.mtfbase[lno]--;
                            state.mtfa[state.mtfbase[lno]] = state.mtfa[(state.mtfbase[lno - 1] + 16) - 1];
                        }

                        state.mtfbase[0]--;
                        state.mtfa[state.mtfbase[0]] = uc;
                        if (state.mtfbase[0] == 0) {
                            kk = 4095; // MTFASIZE - 1
                            for (int ii = 15; ii >= 0; ii--) {
                                for (int jj = 15; jj >= 0; jj--) {
                                    state.mtfa[kk] = state.mtfa[state.mtfbase[ii] + jj];
                                    kk--;
                                }

                                state.mtfbase[ii] = kk + 1;
                            }

                        }
                    }
                    state.unzftab[state.setToUnseq[uc & 0xff] & 0xff]++;
                    state.tt[nblock] = state.setToUnseq[uc & 0xff] & 0xff;
                    nblock++;
                    // GETMTFVAL here we go AGAIN
                    if (groupPos == 0) {
                        groupNo++;
                        groupPos = 50;
                        byte gSel = state.selector[groupNo];
                        gMinLen = state.minLens[gSel];
                        gLimit = state.limit[gSel];
                        gPerm = state.perm[gSel];
                        gBase = state.base[gSel];
                    }
                    groupPos--;
                    int zn_2 = gMinLen;
                    int zvec_2;
                    byte zj_2;
                    for (zvec_2 = getBits(zn_2, state); zvec_2 > gLimit[zn_2]; zvec_2 = zvec_2 << 1 | zj_2) {
                        zn_2++;
                        zj_2 = getBit(state);
                    }

                    nextSym = gPerm[zvec_2 - gBase[zn_2]];
                }

            state.stateOutLen = 0;
            state.stateOutCh = 0;
            state.cftab[0] = 0;
            for (int i = 1; i <= 256; i++)
                state.cftab[i] = state.unzftab[i - 1];

            for (int i = 1; i <= 256; i++)
                state.cftab[i] += state.cftab[i - 1];

            for (int i = 0; i < nblock; i++) {
                uc = (byte) (state.tt[i] & 0xff);
                state.tt[state.cftab[uc & 0xff]] |= i << 8;
                state.cftab[uc & 0xff]++;
            }

            state.tpos = state.tt[state.origPtr] >> 8;
            state.nblockUsed = 0;
            state.tpos = state.tt[state.tpos];
            state.k0 = (byte) (state.tpos & 0xff);
            state.tpos >>= 8;
            state.nblockUsed++;
            state.saveNblock = nblock;
            nextHeader(state);
            goingandshit = state.nblockUsed == state.saveNblock + 1 && state.stateOutLen == 0;
        }
    }

    private static byte getUchar(Context state) {
        return (byte) getBits(8, state);
    }

    private static byte getBit(Context state) {
        return (byte) getBits(1, state);
    }

    private static int getBits(int i, Context state) {
        int ret;
        do {
            if (state.bsLive >= i) {
                int v = state.bsBuff >> state.bsLive - i & (1 << i) - 1;
                state.bsLive -= i;
                ret = v;
                break;
            }
            state.bsBuff = state.bsBuff << 8 | state.input[state.nextIn] & 0xff;
            state.bsLive += 8;
            state.nextIn++;
            state.availIn--;
            state.totalInLo32++;
            if (state.totalInLo32 == 0)
                state.totalInHi32++;
        } while (true);
        return ret;
    }

    private static void makeMaps(Context state) {
        state.nInUse = 0;
        for (int i = 0; i < 256; i++) {
            if (state.inUse[i]) {
                state.setToUnseq[state.nInUse] = (byte) i;
                state.nInUse++;
            }
        }
    }

    private static void createDecodeTables(int limit[], int base[], int perm[], byte length[], int minLen, int maxLen, int alphaSize) {
        int pp = 0;
        for (int i = minLen; i <= maxLen; i++) {
            for (int j = 0; j < alphaSize; j++)
                if (length[j] == i) {
                    perm[pp] = j;
                    pp++;
                }

        }

        for (int i = 0; i < 23; i++)
            base[i] = 0;

        for (int i = 0; i < alphaSize; i++)
            base[length[i] + 1]++;

        for (int i = 1; i < 23; i++)
            base[i] += base[i - 1];

        for (int i = 0; i < 23; i++)
            limit[i] = 0;

        int vec = 0;
        for (int i = minLen; i <= maxLen; i++) {
            vec += base[i + 1] - base[i];
            limit[i] = vec - 1;
            vec <<= 1;
        }

        for (int i = minLen + 1; i <= maxLen; i++)
            base[i] = (limit[i - 1] + 1 << 1) - base[i];

    }
    
    private static class Context {
        
        private int nextIn, availIn, totalInLo32, totalInHi32, availOut, decompressedSize, totalOutLo32, totalOutHi32,
            tt[], stateOutLen, bsBuff, bsLive, blocksize100k, blockNo, origPtr, tpos, k0, unzftab[], nblockUsed,
            cftab[], nInUse, mtfbase[], limit[][], base[][], perm[][], minLens[], saveNblock;
        private byte input[], output[], stateOutCh, setToUnseq[], mtfa[], selector[], selectorMtf[], len[][];
        private boolean blockRandomised, inUse[], inUse_16[];
        
        public Context() {
            unzftab = new int[256];
            cftab = new int[257];
            inUse = new boolean[256];
            inUse_16 = new boolean[16];
            setToUnseq = new byte[256];
            mtfa = new byte[4096];
            mtfbase = new int[16];
            selector = new byte[18002];
            selectorMtf = new byte[18002];
            len = new byte[6][258];
            limit = new int[6][258];
            base = new int[6][258];
            perm = new int[6][258];
            minLens = new int[6];
        }
    }
    
}