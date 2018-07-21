package net.comsoria.engine.loaders.xhtml;

import org.json.JSONObject;
import org.json.XML;

public class XHTMLLoader {
    private JSONObject xml;

    public XHTMLLoader(String xhtml) {
        xml = XML.toJSONObject(xhtml);

        System.out.println(xml.toString(4));
    }
}
