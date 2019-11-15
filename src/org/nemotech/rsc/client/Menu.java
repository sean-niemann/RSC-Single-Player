package org.nemotech.rsc.client;

public class Menu {

    public static boolean drawBackgroundArrow = true;
    public static int baseSpriteStart;
    public static int redMod = 114;
    public static int greenMod = 114;
    public static int blueMod = 176;
    public static int textListEntryHeightMod;
    public boolean controlShown[];
    public boolean controlListScrollbarHandleDragged[];
    public boolean controlMaskText[];
    public boolean controlClicked[];
    public int[] controlScrollAmount;
    public int controlListEntryCount[];
    public int controlListEntryMouseButtonDown[];
    public int controlListEntryMouseOver[];
    public boolean aBoolean219;
    protected Surface surface;
    private int controlCount;
    private boolean controlUseAlternativeColour[];
    private int controlX[];
    private int controlY[];
    private int controlType[];
    private int controlWidth[];
    private int controlHeight[];
    private int controlInputMaxLen[];
    private int controlTextSize[];
    private String controlText[];
    private String controlListEntries[][];
    private int mouseX;
    private int mouseY;
    private int mouseLastButtonDown;
    private int mouseButtonDown;
    private int focusControlIndex;
    private int mouseMetaButtonHeld;
    private int colourScrollbarTop;
    private int colourScrollbarBottom;
    private int colourScrollbarHandleLeft;
    private int colourScrollbarHandleMid;
    private int colourScrollbarHandleRight;
    private int colourRoundedBoxOut;
    private int colourRoundedBoxMid;
    private int colourRoundedBoxIn;
    private int colourBoxTopNBottom;
    private int colourBoxTopNBottom2;
    private int colourBoxLeftNRight2;
    private int colourBoxLeftNRight;

    public Menu(Surface surface, int max) {
        focusControlIndex = -1;
        aBoolean219 = true;
        this.surface = surface;
        controlShown = new boolean[max];
        controlListScrollbarHandleDragged = new boolean[max];
        controlMaskText = new boolean[max];
        controlClicked = new boolean[max];
        controlUseAlternativeColour = new boolean[max];
        controlScrollAmount = new int[max];// not so sure
        controlListEntryCount = new int[max];
        controlListEntryMouseButtonDown = new int[max];
        controlListEntryMouseOver = new int[max];
        controlX = new int[max];
        controlY = new int[max];
        controlType = new int[max];
        controlWidth = new int[max];
        controlHeight = new int[max];
        controlInputMaxLen = new int[max];
        controlTextSize = new int[max];
        controlText = new String[max];
        controlListEntries = new String[max][];
        colourScrollbarTop = rgb2longMod(114, 114, 176);
        colourScrollbarBottom = rgb2longMod(14, 14, 62);
        colourScrollbarHandleLeft = rgb2longMod(200, 208, 232);
        colourScrollbarHandleMid = rgb2longMod(96, 129, 184);
        colourScrollbarHandleRight = rgb2longMod(53, 95, 115);
        colourRoundedBoxOut = rgb2longMod(117, 142, 171);
        colourRoundedBoxMid = rgb2longMod(98, 122, 158);
        colourRoundedBoxIn = rgb2longMod(86, 100, 136);
        colourBoxTopNBottom = rgb2longMod(135, 146, 179);
        colourBoxTopNBottom2 = rgb2longMod(97, 112, 151);
        colourBoxLeftNRight2 = rgb2longMod(88, 102, 136);
        colourBoxLeftNRight = rgb2longMod(84, 93, 120);
    }

    private int rgb2longMod(int i, int j, int k) {
        return Surface.rgb2long((redMod * i) / 114, (greenMod * j) / 114, (blueMod * k) / 176);
    }

    public void handleMouse(int mx, int my, int lastmb, int mbdown) {
        mouseX = mx;
        mouseY = my;
        mouseButtonDown = mbdown;
        if (lastmb != 0)
            mouseLastButtonDown = lastmb;
        if (lastmb == 1) {
            for (int i1 = 0; i1 < controlCount; i1++) {
                if (controlShown[i1] && controlType[i1] == 10 && mouseX >= controlX[i1] && mouseY >= controlY[i1] && mouseX <= controlX[i1] + controlWidth[i1] && mouseY <= controlY[i1] + controlHeight[i1])
                    controlClicked[i1] = true;
                if (controlShown[i1] && controlType[i1] == 14 && mouseX >= controlX[i1] && mouseY >= controlY[i1] && mouseX <= controlX[i1] + controlWidth[i1] && mouseY <= controlY[i1] + controlHeight[i1])
                    controlListEntryMouseButtonDown[i1] = 1 - controlListEntryMouseButtonDown[i1];
            }

        }
        if (mbdown == 1)
            mouseMetaButtonHeld++;
        else
            mouseMetaButtonHeld = 0;
        if (lastmb == 1 || mouseMetaButtonHeld > 20) {
            for (int j1 = 0; j1 < controlCount; j1++)
                if (controlShown[j1] && controlType[j1] == 15 && mouseX >= controlX[j1] && mouseY >= controlY[j1] && mouseX <= controlX[j1] + controlWidth[j1] && mouseY <= controlY[j1] + controlHeight[j1])
                    controlClicked[j1] = true;

            mouseMetaButtonHeld -= 5;
        }
    }

    public void handleScroll(int handle, int i) {
        int limit = controlListEntryCount[handle] - (controlHeight[handle] / surface.textHeight(controlTextSize[handle]));
        int diff = Math.abs(limit - controlScrollAmount[handle]);
        if (i > 0)
            if(diff < i)
                controlScrollAmount[handle] += diff;
            else
                controlScrollAmount[handle] += i;
        else if(i < 0 && controlScrollAmount[handle] > 0)
            if (controlScrollAmount[handle] < -i)
                controlScrollAmount[handle] -= controlScrollAmount[handle];
            else
                controlScrollAmount[handle] += i;
    }

    public boolean isClicked(int i) {
        if (controlShown[i] && controlClicked[i]) {
            controlClicked[i] = false;
            return true;
        } else {
            return false;
        }
    }

    public void keyPress(int key) {
        if (key == 0)
            return;
        if (focusControlIndex != -1 && controlText[focusControlIndex] != null && controlShown[focusControlIndex]) {
            int inputLen = controlText[focusControlIndex].length();
            if (key == 8 && inputLen > 0)
                controlText[focusControlIndex] = controlText[focusControlIndex].substring(0, inputLen - 1);
            if ((key == 10 || key == 13) && inputLen > 0)
                controlClicked[focusControlIndex] = true;
            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
            if (inputLen < controlInputMaxLen[focusControlIndex]) {
                for (int k = 0; k < s.length(); k++)
                    if (key == s.charAt(k))
                        controlText[focusControlIndex] += (char) key;

            }
            if (key == 9)
                do
                    focusControlIndex = (focusControlIndex + 1) % controlCount;
                while (controlType[focusControlIndex] != 5 && controlType[focusControlIndex] != 6);
        }
    }

    public void drawPanel() {
        for (int i = 0; i < controlCount; i++) {
            if (controlShown[i]) {
                if (controlType[i] == 0)// text
                    drawText(i, controlX[i], controlY[i], controlText[i], controlTextSize[i]);
                else if (controlType[i] == 1)// text (centered)
                    drawText(i, controlX[i] - surface.textWidth(controlText[i], controlTextSize[i]) / 2, controlY[i], controlText[i], controlTextSize[i]);
                else if (controlType[i] == 2)// component gradient bg
                    drawBox(controlX[i], controlY[i], controlWidth[i], controlHeight[i]);
                else if (controlType[i] == 3)// horiz line
                    drawLineHoriz(controlX[i], controlY[i], controlWidth[i]);
                else if (controlType[i] == 4)// text list (no interaction)
                    drawTextList(i, controlX[i], controlY[i], controlWidth[i], controlHeight[i], controlTextSize[i], controlListEntries[i], controlListEntryCount[i], controlScrollAmount[i]);
                else if (controlType[i] == 5 || controlType[i] == 6)// input text
                    drawTextInput(i, controlX[i], controlY[i], controlWidth[i], controlHeight[i], controlText[i], controlTextSize[i]);
                else if (controlType[i] == 7)// option list horiz
                    drawOptionListHoriz(i, controlX[i], controlY[i], controlTextSize[i], controlListEntries[i]);
                else if (controlType[i] == 8)// option list vert
                    drawOptionListVert(i, controlX[i], controlY[i], controlTextSize[i], controlListEntries[i]);
                else if (controlType[i] == 9)// text list (interaction)
                    drawTextListInteractive(i, controlX[i], controlY[i], controlWidth[i], controlHeight[i], controlTextSize[i], controlListEntries[i], controlListEntryCount[i], controlScrollAmount[i]);
                else if (controlType[i] == 11)// rounded box
                    drawRoundedBox(controlX[i], controlY[i], controlWidth[i], controlHeight[i]);
                else if (controlType[i] == 12)// image
                    drawPicture(controlX[i], controlY[i], controlTextSize[i]);
                else if (controlType[i] == 14)// checkbox
                    drawCheckbox(i, controlX[i], controlY[i], controlWidth[i], controlHeight[i]);
                //else if (controlType[i] == 15) // sprite list
            }
        }
        mouseLastButtonDown = 0;
    }

    protected void drawCheckbox(int control, int x, int y, int width, int height) {
        surface.drawBox(x, y, width, height, 0xffffff);
        surface.drawLineHoriz(x, y, width, colourBoxTopNBottom);
        surface.drawLineVert(x, y, height, colourBoxTopNBottom);
        surface.drawLineHoriz(x, (y + height) - 1, width, colourBoxLeftNRight);
        surface.drawLineVert((x + width) - 1, y, height, colourBoxLeftNRight);
        if (controlListEntryMouseButtonDown[control] == 1) {
            for (int j1 = 0; j1 < height; j1++) {
                surface.drawLineHoriz(x + j1, y + j1, 1, 0);
                surface.drawLineHoriz((x + width) - 1 - j1, y + j1, 1, 0);
            }

        }
    }

    protected void drawText(int control, int x, int y, String text, int textSize) {
        int y2 = y + surface.textHeight(textSize) / 3;
        drawString(control, x, y2, text, textSize);
    }

    protected void drawString(int control, int x, int y, String text, int textSize) {
        int i1;
        if (controlUseAlternativeColour[control])
            i1 = 0xffffff;
        else
            i1 = 0;
        surface.drawString(text, x, y, textSize, i1);
    }

    protected void drawTextInput(int control, int x, int y, int width, int height, String text, int textSize) {
        if (controlMaskText[control]) {
            int len = text.length();
            text = "";
            for (int i2 = 0; i2 < len; i2++)
                text = text + "X";

        }
        if (controlType[control] == 5) {// "list input"
            if (mouseLastButtonDown == 1 && mouseX >= x && mouseY >= y - height / 2 && mouseX <= x + width && mouseY <= y + height / 2)
                focusControlIndex = control;
        } else if (controlType[control] == 6) {// "text input"
            if (mouseLastButtonDown == 1 && mouseX >= x - width / 2 && mouseY >= y - height / 2 && mouseX <= x + width / 2 && mouseY <= y + height / 2)
                focusControlIndex = control;
            x -= surface.textWidth(text, textSize) / 2;
        }
        if (focusControlIndex == control)
            text = text + "*";
        int y2 = y + surface.textHeight(textSize) / 3;
        drawString(control, x, y2, text, textSize);
    }

    public void drawBox(int x, int y, int width, int height) {
        surface.setBounds(x, y, x + width, y + height);
        surface.drawGradient(x, y, width, height, colourBoxLeftNRight, colourBoxTopNBottom);
        if (drawBackgroundArrow) {// set to false inside startGame, draw some kindof an arrow??
            for (int i1 = x - (y & 0x3f); i1 < x + width; i1 += 128) {
                for (int j1 = y - (y & 0x1f); j1 < y + height; j1 += 128)
                    surface.drawSpriteAlpha(i1, j1, 6 + baseSpriteStart, 128);

            }

        }
        surface.drawLineHoriz(x, y, width, colourBoxTopNBottom);
        surface.drawLineHoriz(x + 1, y + 1, width - 2, colourBoxTopNBottom);
        surface.drawLineHoriz(x + 2, y + 2, width - 4, colourBoxTopNBottom2);
        surface.drawLineVert(x, y, height, colourBoxTopNBottom);
        surface.drawLineVert(x + 1, y + 1, height - 2, colourBoxTopNBottom);
        surface.drawLineVert(x + 2, y + 2, height - 4, colourBoxTopNBottom2);
        surface.drawLineHoriz(x, (y + height) - 1, width, colourBoxLeftNRight);
        surface.drawLineHoriz(x + 1, (y + height) - 2, width - 2, colourBoxLeftNRight);
        surface.drawLineHoriz(x + 2, (y + height) - 3, width - 4, colourBoxLeftNRight2);
        surface.drawLineVert((x + width) - 1, y, height, colourBoxLeftNRight);
        surface.drawLineVert((x + width) - 2, y + 1, height - 2, colourBoxLeftNRight);
        surface.drawLineVert((x + width) - 3, y + 2, height - 4, colourBoxLeftNRight2);
        surface.resetBounds();
    }

    public void drawRoundedBox(int x, int y, int width, int height) {
        surface.drawBox(x, y, width, height, 0);
        surface.drawBoxEdge(x, y, width, height, colourRoundedBoxOut);
        surface.drawBoxEdge(x + 1, y + 1, width - 2, height - 2, colourRoundedBoxMid);
        surface.drawBoxEdge(x + 2, y + 2, width - 4, height - 4, colourRoundedBoxIn);
        surface.drawSprite(x, y, 2 + baseSpriteStart);
        surface.drawSprite((x + width) - 7, y, 3 + baseSpriteStart);
        surface.drawSprite(x, (y + height) - 7, 4 + baseSpriteStart);
        surface.drawSprite((x + width) - 7, (y + height) - 7, 5 + baseSpriteStart);
    }

    protected void drawPicture(int x, int y, int size) {
        surface.drawSprite(x, y, size);
    }

    protected void drawLineHoriz(int x, int y, int width) {
        surface.drawLineHoriz(x, y, width, 0xffffff);
    }

    protected void drawTextList(int control, int x, int y, int width, int height, int textSize,
                                String listEntries[], int listEntryCount, int l1) {
        int displayedEntryCount = height / surface.textHeight(textSize);
        if (l1 > listEntryCount - displayedEntryCount)
            l1 = listEntryCount - displayedEntryCount;
        if (l1 < 0)
            l1 = 0;
        controlScrollAmount[control] = l1;
        if (displayedEntryCount < listEntryCount) {
            int cornerTopRight = (x + width) - 12;
            int cornerBottomLeft = ((height - 27) * displayedEntryCount) / listEntryCount;
            if (cornerBottomLeft < 6)
                cornerBottomLeft = 6;
            int j3 = ((height - 27 - cornerBottomLeft) * l1) / (listEntryCount - displayedEntryCount);
            if (mouseButtonDown == 1 && mouseX >= cornerTopRight && mouseX <= cornerTopRight + 12) {
                if (mouseY > y && mouseY < y + 12 && l1 > 0)
                    l1--;
                if (mouseY > (y + height) - 12 && mouseY < y + height && l1 < listEntryCount - displayedEntryCount)
                    l1++;
                controlScrollAmount[control] = l1;
            }
            if (mouseButtonDown == 1 && (mouseX >= cornerTopRight && mouseX <= cornerTopRight + 12 || mouseX >= cornerTopRight - 12 && mouseX <= cornerTopRight + 24 && controlListScrollbarHandleDragged[control])) {
                if (mouseY > y + 12 && mouseY < (y + height) - 12) {
                    controlListScrollbarHandleDragged[control] = true;
                    int l3 = mouseY - y - 12 - cornerBottomLeft / 2;
                    l1 = (l3 * listEntryCount) / (height - 24);
                    if (l1 > listEntryCount - displayedEntryCount)
                        l1 = listEntryCount - displayedEntryCount;
                    if (l1 < 0)
                        l1 = 0;
                    controlScrollAmount[control] = l1;
                }
            } else {
                controlListScrollbarHandleDragged[control] = false;
            }
            j3 = ((height - 27 - cornerBottomLeft) * l1) / (listEntryCount - displayedEntryCount);
            drawListContainer(x, y, width, height, j3, cornerBottomLeft);
        }
        int entryListStartY = height - displayedEntryCount * surface.textHeight(textSize);
        int y2 = y + (surface.textHeight(textSize) * 5) / 6 + entryListStartY / 2;
        for (int entry = l1; entry < listEntryCount; entry++) {
            drawString(control, x + 2, y2, listEntries[entry], textSize);
            y2 += surface.textHeight(textSize) - textListEntryHeightMod;
            if (y2 >= y + height)
                return;
        }

    }

    protected void drawListContainer(int x, int y, int width, int height, int corner1, int corner2) {
        int x2 = (x + width) - 12;
        surface.drawBoxEdge(x2, y, 12, height, 0);
        surface.drawSprite(x2 + 1, y + 1, baseSpriteStart);// up arrow?
        surface.drawSprite(x2 + 1, (y + height) - 12, 1 + baseSpriteStart);// down arrow?
        surface.drawLineHoriz(x2, y + 13, 12, 0);
        surface.drawLineHoriz(x2, (y + height) - 13, 12, 0);
        surface.drawGradient(x2 + 1, y + 14, 11, height - 27, colourScrollbarTop, colourScrollbarBottom);
        surface.drawBox(x2 + 3, corner1 + y + 14, 7, corner2, colourScrollbarHandleMid);
        surface.drawLineVert(x2 + 2, corner1 + y + 14, corner2, colourScrollbarHandleLeft);
        surface.drawLineVert(x2 + 2 + 8, corner1 + y + 14, corner2, colourScrollbarHandleRight);
    }

    protected void drawOptionListHoriz(int control, int x, int y, int textSize, String listEntries[]) {
        int listTotalTextWidth = 0;
        int listEntryCount = listEntries.length;
        for (int idx = 0; idx < listEntryCount; idx++) {
            listTotalTextWidth += surface.textWidth(listEntries[idx], textSize);
            if (idx < listEntryCount - 1)
                listTotalTextWidth += surface.textWidth("  ", textSize);
        }

        int left = x - listTotalTextWidth / 2;
        int bottom = y + surface.textHeight(textSize) / 3;
        for (int idx = 0; idx < listEntryCount; idx++) {
            int colour;
            if (controlUseAlternativeColour[control])
                colour = 0xffffff;
            else
                colour = 0;
            if (mouseX >= left && mouseX <= left + surface.textWidth(listEntries[idx], textSize) && mouseY <= bottom && mouseY > bottom - surface.textHeight(textSize)) {
                if (controlUseAlternativeColour[control])
                    colour = 0x808080;
                else
                    colour = 0xffffff;
                if (mouseLastButtonDown == 1) {
                    controlListEntryMouseButtonDown[control] = idx;
                    controlClicked[control] = true;
                }
            }
            if (controlListEntryMouseButtonDown[control] == idx)
                if (controlUseAlternativeColour[control])
                    colour = 0xff0000;
                else
                    colour = 0xc00000;
            surface.drawString(listEntries[idx], left, bottom, textSize, colour);
            left += surface.textWidth(listEntries[idx] + "  ", textSize);
        }

    }

    protected void drawOptionListVert(int control, int x, int y, int textSize, String listEntries[]) {
        int listEntryCount = listEntries.length;
        int listTotalTextHeightMid = y - (surface.textHeight(textSize) * (listEntryCount - 1)) / 2;
        for (int idx = 0; idx < listEntryCount; idx++) {
            int colour;
            if (controlUseAlternativeColour[control])
                colour = 0xffffff;
            else
                colour = 0;
            int entryTextWidth = surface.textWidth(listEntries[idx], textSize);
            if (mouseX >= x - entryTextWidth / 2 && mouseX <= x + entryTextWidth / 2 && mouseY - 2 <= listTotalTextHeightMid && mouseY - 2 > listTotalTextHeightMid - surface.textHeight(textSize)) {
                if (controlUseAlternativeColour[control])
                    colour = 0x808080;
                else
                    colour = 0xffffff;
                if (mouseLastButtonDown == 1) {
                    controlListEntryMouseButtonDown[control] = idx;
                    controlClicked[control] = true;
                }
            }
            if (controlListEntryMouseButtonDown[control] == idx)
                if (controlUseAlternativeColour[control])
                    colour = 0xff0000;
                else
                    colour = 0xc00000;
            surface.drawString(listEntries[idx], x - entryTextWidth / 2, listTotalTextHeightMid, textSize, colour);
            listTotalTextHeightMid += surface.textHeight(textSize);
        }

    }

    protected void drawTextListInteractive(int control, int x, int y, int width, int height, int textSize,
                                           String listEntries[], int listEntryCount, int l1) {
        int displayedEntryCount = height / surface.textHeight(textSize);
        if (displayedEntryCount < listEntryCount) {
            int right = (x + width) - 12;
            int l2 = ((height - 27) * displayedEntryCount) / listEntryCount;
            if (l2 < 6)
                l2 = 6;
            int j3 = ((height - 27 - l2) * l1) / (listEntryCount - displayedEntryCount);
            if (mouseButtonDown == 1 && mouseX >= right && mouseX <= right + 12) {
                if (mouseY > y && mouseY < y + 12 && l1 > 0)
                    l1--;
                if (mouseY > (y + height) - 12 && mouseY < y + height && l1 < listEntryCount - displayedEntryCount)
                    l1++;
                controlScrollAmount[control] = l1;
            }
            if (mouseButtonDown == 1 && (mouseX >= right && mouseX <= right + 12 || mouseX >= right - 12 && mouseX <= right + 24 && controlListScrollbarHandleDragged[control])) {
                if (mouseY > y + 12 && mouseY < (y + height) - 12) {
                    controlListScrollbarHandleDragged[control] = true;
                    int l3 = mouseY - y - 12 - l2 / 2;
                    l1 = (l3 * listEntryCount) / (height - 24);
                    if (l1 < 0)
                        l1 = 0;
                    if (l1 > listEntryCount - displayedEntryCount)
                        l1 = listEntryCount - displayedEntryCount;
                    controlScrollAmount[control] = l1;
                }
            } else {
                controlListScrollbarHandleDragged[control] = false;
            }
            j3 = ((height - 27 - l2) * l1) / (listEntryCount - displayedEntryCount);
            drawListContainer(x, y, width, height, j3, l2);
        } else {
            l1 = 0;
            controlScrollAmount[control] = 0;
        }
        controlListEntryMouseOver[control] = -1;
        int k2 = height - displayedEntryCount * surface.textHeight(textSize);
        int i3 = y + (surface.textHeight(textSize) * 5) / 6 + k2 / 2;
        for (int k3 = l1; k3 < listEntryCount; k3++) {
            int i4;
            if (controlUseAlternativeColour[control])
                i4 = 0xffffff;
            else
                i4 = 0;
            if (mouseX >= x + 2 && mouseX <= x + 2 + surface.textWidth(listEntries[k3], textSize) && mouseY - 2 <= i3 && mouseY - 2 > i3 - surface.textHeight(textSize)) {
                if (controlUseAlternativeColour[control])
                    i4 = 0x808080;
                else
                    i4 = 0xffffff;
                controlListEntryMouseOver[control] = k3;
                if (mouseLastButtonDown == 1) {
                    controlListEntryMouseButtonDown[control] = k3;
                    controlClicked[control] = true;
                }
            }
            if (controlListEntryMouseButtonDown[control] == k3 && aBoolean219)
                i4 = 0xff0000;
            surface.drawString(listEntries[k3], x + 2, i3, textSize, i4);
            i3 += surface.textHeight(textSize);
            if (i3 >= y + height)
                return;
        }

    }

    public int addText(int x, int y, String text, int size, boolean flag) {
        controlType[controlCount] = 1;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlTextSize[controlCount] = size;
        controlUseAlternativeColour[controlCount] = flag;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlText[controlCount] = text;
        return controlCount++;
    }

    public int addButtonBackground(int x, int y, int width, int height) {
        controlType[controlCount] = 2;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x - width / 2;
        controlY[controlCount] = y - height / 2;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        return controlCount++;
    }

    public int addBoxRounded(int x, int y, int width, int height) {
        controlType[controlCount] = 11;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x - width / 2;
        controlY[controlCount] = y - height / 2;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        return controlCount++;
    }

    public int addSprite(int x, int y, int spriteId) {
        int imgWidth = surface.spriteWidth[spriteId];
        int imgHeight = surface.spriteHeight[spriteId];
        controlType[controlCount] = 12;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x - imgWidth / 2;
        controlY[controlCount] = y - imgHeight / 2;
        controlWidth[controlCount] = imgWidth;
        controlHeight[controlCount] = imgHeight;
        controlTextSize[controlCount] = spriteId;
        return controlCount++;
    }

    public int addTextListScrollable(int x, int y, int width, int height, int size, int maxLength, boolean flag) {
        controlType[controlCount] = 4;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        controlUseAlternativeColour[controlCount] = flag;
        controlTextSize[controlCount] = size;
        controlInputMaxLen[controlCount] = maxLength;
        controlListEntryCount[controlCount] = 0;
        controlScrollAmount[controlCount] = 0;
        controlListEntries[controlCount] = new String[maxLength];
        return controlCount++;
    }

    public int addTextListInput(int x, int y, int width, int height, int size, int maxLength, boolean flag,
                                boolean flag1) {
        controlType[controlCount] = 5;
        controlShown[controlCount] = true;
        controlMaskText[controlCount] = flag;
        controlClicked[controlCount] = false;
        controlTextSize[controlCount] = size;
        controlUseAlternativeColour[controlCount] = flag1;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        controlInputMaxLen[controlCount] = maxLength;
        controlText[controlCount] = "";
        return controlCount++;
    }

    public int addTextInput(int x, int y, int width, int height, int size, int maxLength, boolean flag,
                            boolean flag1) {
        controlType[controlCount] = 6;
        controlShown[controlCount] = true;
        controlMaskText[controlCount] = flag;
        controlClicked[controlCount] = false;
        controlTextSize[controlCount] = size;
        controlUseAlternativeColour[controlCount] = flag1;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        controlInputMaxLen[controlCount] = maxLength;
        controlText[controlCount] = "";
        return controlCount++;
    }

    public int addTextListInteractive(int x, int y, int width, int height, int textSize, int maxLength, boolean flag) {
        controlType[controlCount] = 9;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlTextSize[controlCount] = textSize;
        controlUseAlternativeColour[controlCount] = flag;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        controlInputMaxLen[controlCount] = maxLength;
        controlListEntries[controlCount] = new String[maxLength];
        controlListEntryCount[controlCount] = 0;
        controlScrollAmount[controlCount] = 0;
        controlListEntryMouseButtonDown[controlCount] = -1;
        controlListEntryMouseOver[controlCount] = -1;
        return controlCount++;
    }

    public int addButton(int x, int y, int width, int height) {
        controlType[controlCount] = 10;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x - width / 2;
        controlY[controlCount] = y - height / 2;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        return controlCount++;
    }

    public int addLineHoriz(int x, int y, int width) {
        controlType[controlCount] = 3;
        controlShown[controlCount] = true;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        return controlCount++;
    }

    public int addOptionListHoriz(int x, int y, int textSize, int maxListCount,
                                  boolean useAltColour) {
        controlType[controlCount] = 7;
        controlShown[controlCount] = true;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlTextSize[controlCount] = textSize;
        controlListEntries[controlCount] = new String[maxListCount];
        controlListEntryCount[controlCount] = 0;
        controlUseAlternativeColour[controlCount] = useAltColour;
        controlClicked[controlCount] = false;
        return controlCount++;
    }

    public int addOptionListVert(int x, int y, int textSize, int maxListCount,
                                 boolean useAltColour) {
        controlType[controlCount] = 8;
        controlShown[controlCount] = true;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlTextSize[controlCount] = textSize;
        controlListEntries[controlCount] = new String[maxListCount];
        controlListEntryCount[controlCount] = 0;
        controlUseAlternativeColour[controlCount] = useAltColour;
        controlClicked[controlCount] = false;
        return controlCount++;
    }

    public int addCheckbox(int x, int y, int width, int height) {
        controlType[controlCount] = 14;
        controlShown[controlCount] = true;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        controlListEntryMouseButtonDown[controlCount] = 0;
        return controlCount++;
    }

    public int addSpriteList(int spriteId, int x, int y, int maxLength) {
        int imgWidth = surface.spriteWidth[spriteId];
        int imgHeight = surface.spriteHeight[spriteId];
        controlType[controlCount] = 15;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x - imgWidth / 2;
        controlY[controlCount] = y - imgHeight / 2;
        controlWidth[controlCount] = imgWidth;
        controlHeight[controlCount] = imgHeight;
        controlUseAlternativeColour[controlCount] = false;
        controlTextSize[controlCount] = spriteId;
        controlInputMaxLen[controlCount] = maxLength;
        controlListEntryCount[controlCount] = 0;
        controlScrollAmount[controlCount] = 0;
        controlListEntries[controlCount] = new String[maxLength];
        return controlCount++;
    }

    public void clearList(int control) {
        controlListEntryCount[control] = 0;
    }

    public void resetListProps(int control) {
        controlScrollAmount[control] = 0;
        controlListEntryMouseOver[control] = -1;
    }
    
    /*public int addSprite(int x, int y, int spriteId) {
        int imgWidth = surface.spriteWidth[spriteId];
        int imgHeight = surface.spriteHeight[spriteId];
        controlType[controlCount] = 12;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x - imgWidth / 2;
        controlY[controlCount] = y - imgHeight / 2;
        controlWidth[controlCount] = imgWidth;
        controlHeight[controlCount] = imgHeight;
        controlTextSize[controlCount] = spriteId;
        return controlCount++;
    }

    public int addTextList(int x, int y, int width, int height, int size, int maxLength, boolean flag) {
        controlType[controlCount] = 4;
        controlShown[controlCount] = true;
        controlClicked[controlCount] = false;
        controlX[controlCount] = x;
        controlY[controlCount] = y;
        controlWidth[controlCount] = width;
        controlHeight[controlCount] = height;
        controlUseAlternativeColour[controlCount] = flag;
        controlTextSize[controlCount] = size;
        controlInputMaxLen[controlCount] = maxLength;
        controlListEntryCount[controlCount] = 0;
        controlFlashText[controlCount] = 0;
        controlListEntries[controlCount] = new String[maxLength];
        return controlCount++;
    }*/
    
    public void addListSprite(int sprite, int control) {
        int j = controlListEntryCount[control]++;
        if (j >= controlInputMaxLen[control]) {
            j--;
            controlListEntryCount[control]--;
            for (int k = 0; k < j; k++) {
                controlListEntries[control][k] = controlListEntries[control][k + 1];
            }
        }
        controlTextSize[control] = sprite;
    }
    
    public void addListEntry(String text, int control) {
        int j = controlListEntryCount[control]++;
        if (j >= controlInputMaxLen[control]) {
            j--;
            controlListEntryCount[control]--;
            for (int k = 0; k < j; k++) {
                controlListEntries[control][k] = controlListEntries[control][k + 1];
            }
        }
        controlListEntries[control][j] = text;
    }
    
    // custom above

    public void addListEntry(int control, int index, String text) {
        controlListEntries[control][index] = text;
        if (index + 1 > controlListEntryCount[control])
            controlListEntryCount[control] = index + 1;
    }

    public void removeListEntry(int control, String text, boolean flag) {
        int j = controlListEntryCount[control]++;
        if (j >= controlInputMaxLen[control]) {
            j--;
            controlListEntryCount[control]--;
            for (int k = 0; k < j; k++)
                controlListEntries[control][k] = controlListEntries[control][k + 1];

        }
        controlListEntries[control][j] = text;
        if (flag)
            controlScrollAmount[control] = 999999;
    }

    public void updateText(int control, String s) {
        controlText[control] = s;
    }

    public String getText(int control) {
        if (controlText[control] == null)
            return "null";
        else
            return controlText[control];
    }

    public void show(int control) {
        controlShown[control] = true;
    }

    public void hide(int control) {
        controlShown[control] = false;
    }

    public void setFocus(int control) {
        focusControlIndex = control;
    }

    public int getListEntryIndex(int control) {
        return controlListEntryMouseOver[control];
    }
    
    public void reposition(int id, int x, int y, int w, int h) {
        this.controlX[id] = x;
        this.controlY[id] = y;
        this.controlWidth[id] = w;
        this.controlHeight[id] = h;
    }
    
    public int getScrollPosition(int index) {
        return controlScrollAmount[index];
    }

    public void resetScrollIndex(int auctionScrollHandle) {
        controlScrollAmount[auctionScrollHandle] = 0;
    }

}
