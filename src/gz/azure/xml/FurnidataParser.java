package gz.azure.xml;

import gz.azure.Log;
import gz.azure.xml.furnidata.Furnidata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Azure Camera Server - Furni Sprite Extractor
 * Written by Scott Stamp (scottstamp851, scott@hypermine.com)
 */
public class FurnidataParser {

    public static Furnidata getFurnidata() {
        try {
            return getFurnidata(new URL("https://www.habbo.com/gamedata/furnidata_xml/1"));
        } catch (MalformedURLException e) {
            // This is a static URL. It should never be "malformed"
            return null;
        }
    }

    public static Furnidata getFurnidata(URL sourceURL) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Furnidata.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StreamSource src = new StreamSource(sourceURL.openStream());
            return ((Furnidata) unmarshaller.unmarshal(src));
        } catch (JAXBException jaxbEx) {
            Log.error("Failed to create new JAXB instance", jaxbEx);
            return null;
        } catch (IOException ioEx) {
            Log.error("Failed to download furnidata XML for reading", ioEx);
            return null;
        }
    }
}
