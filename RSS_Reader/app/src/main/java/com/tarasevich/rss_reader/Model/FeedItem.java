package com.tarasevich.rss_reader.Model;

import android.net.Uri;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "item", strict = false)
public  class FeedItem  {

    @Element(name = "title")
    public String title;

    @Element(name = "pubDate")
    public String pubDate;

    @Element(name = "description")
    public String content;

    @Element(name = "category")
    public String category;

    @Element(name = "thumbnail")
    public Image image;

    @Element (name = "link")
    public String link;

    @Root(name = "thumbnail", strict = false)
    @Namespace(prefix = "media", reference = "thumbnail")
    public static class Image {

        @Attribute(name = "url")
        public String url;
    }
}
