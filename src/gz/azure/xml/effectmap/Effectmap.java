package gz.azure.xml.effectmap;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Scott on 8/18/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "effect"
})
@XmlRootElement(name = "map")
public class Effectmap {
    protected List<Effect> effect;

    public List<Effect> getEffects() {
        return this.effect;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Effect {
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "lib")
        protected String lib;
        @XmlAttribute(name = "type")
        protected String type;
        @XmlAttribute(name = "revision")
        protected int revision;

        public String getId() {
            return this.id;
        }

        public String getLib() {
            return this.lib;
        }
    }
}
