package gz.azure.utils;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.base.ImageTag;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Azure Camera Server - Furni Sprite Extractor
 * Written by Scott Stamp (scottstamp851, scott@hypermine.com)
 */

public class SWFData {
    private SWF swf;

    public SWFData(String fileLoc) throws IOException, InterruptedException {
        swf = new SWF(new FileInputStream(fileLoc), false, false);
    }

    public SWFData(URL fileLoc) throws IOException, InterruptedException {
        swf = new SWF(fileLoc.openStream(), false, false);
    }

    public List<Tag> getTags() {
        return swf.tags;
    }

    public List<ImageTag> getImageTags() {
        return swf.tags.stream()
                .filter((tag) -> tag.getTagName().equals("DefineBitsLossless2"))
                .map((tag) -> swf.getImage(Integer.parseInt(tag.toString().split("\\(")[1].split(":")[0])))
                .collect(Collectors.toList());
    }
}
