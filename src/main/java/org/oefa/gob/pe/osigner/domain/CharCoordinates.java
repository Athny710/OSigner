package org.oefa.gob.pe.osigner.domain;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.List;

public class CharCoordinates  extends PDFTextStripper {
    String tag = "";

    public void setTag(String tag) {
        this.tag = tag;
    }
    public CharCoordinates() throws Exception{
        super();
    }

    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        TextPosition firstProsition = textPositions.get(0);
        String text2 = text.toUpperCase();
        if (text.toUpperCase().contains(this.tag.toUpperCase())) {
            Object[] t = textPositions.toArray();
            int ind = text2.indexOf(this.tag.toUpperCase());
            this.writeString(String.format("[%s , %s , %s]", Float.valueOf(((TextPosition)t[ind]).getX()), Float.valueOf(((TextPosition)t[ind]).getY()), text));
        }
    }
}
