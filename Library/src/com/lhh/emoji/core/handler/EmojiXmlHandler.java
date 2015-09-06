package com.lhh.emoji.core.handler;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.loader.EmojiLoader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linhonghong on 2015/9/6.
 */
public class EmojiXmlHandler extends DefaultHandler {

    private List<EmojiObject> mEmojiList = null;

    private Map<String, String> mEmojiMap = null;

    private EmojiObject mEmoji = null;

    private StringBuilder mBuilder;

    private String mPath = null;

    public EmojiXmlHandler(String path) {
        this.mPath = path;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mEmojiList = new ArrayList<EmojiObject>();
        mBuilder = new StringBuilder();
        mEmojiMap = new HashMap<String, String>();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        mEmojiList.add(new EmojiObject(EmojiLoader.BACKSPACE, EmojiLoader.BACKSPACE));
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("emoji")) {
            mEmoji = new EmojiObject();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        mBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("name")) {
            mEmoji.setPath( mPath + File.separator + mBuilder.toString());
            mBuilder.delete(0, mBuilder.length());
        } else if (localName.equals("value")) {
            mEmoji.setPath(mBuilder.toString());
            mBuilder.delete(0, mBuilder.length());
        } else if (localName.equals("emoji")) {
            mEmojiList.add(mEmoji);
            mEmojiMap.put(mEmoji.getKey(), mEmoji.getPath());
        }
    }

    public List<EmojiObject> getEomjiList() {
        return this.mEmojiList;
    }

    public Map<String, String> getEmojiMap() {
        return this.mEmojiMap;
    }
}
