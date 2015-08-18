package gz.azure.xml.effectmap;

import gz.azure.utils.Log;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Scott on 8/18/2015.
 */
public class EffectmapParser {
    public static Effectmap getEffectmap() {
        try {
            return getEffectmap(new URL("https://habboo-a.akamaihd.net/gordon/PRODUCTION-201508111205-320867585/effectmap.xml"));
        } catch (MalformedURLException e) {
            // This is a static URL. It should never be "malformed"
            return null;
        }
    }

    public static Effectmap getEffectmap(URL sourceURL) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Effectmap.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StreamSource src = new StreamSource(sourceURL.openStream());
            return ((Effectmap) unmarshaller.unmarshal(src));
        } catch (JAXBException jaxbEx) {
            Log.error("Failed to create new JAXB instance", jaxbEx);
            return null;
        } catch (IOException ioEx) {
            Log.error("Failed to download furnidata XML for reading", ioEx);
            return null;
        }
    }
}
