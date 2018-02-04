package com.tarasevich.rss_reader.Model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "rss", strict = false)
public class OnlinerRss {

    @ElementList(inline = true, name = "item")
    @Path("channel")
    public ArrayList<FeedItem> feeds;
}
