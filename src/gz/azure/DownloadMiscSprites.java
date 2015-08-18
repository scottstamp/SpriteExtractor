package gz.azure;

import com.jpexs.decompiler.flash.tags.base.ImageTag;
import gz.azure.utils.Log;
import gz.azure.utils.OSCheck;
import gz.azure.utils.SWFData;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Scott on 8/18/2015.
 */
public class DownloadMiscSprites implements Runnable {
    private String imageType;
    private String className;
    private Boolean stripClass;

    public DownloadMiscSprites(String imageType, String className, Boolean stripClass) {
        this.imageType = imageType;
        this.className = className;
        this.stripClass = stripClass;
    }

    @Override
    public void run() {
        try {
            URL swfURL = new URL("https:" + SpriteExtractor.externalVariables.getFlashClientURL() + className + ".swf");
            String path;
            if (OSCheck.getOperatingSystemType() == OSCheck.OSType.Windows) path = imageType + "\\";
            else path = imageType + "/";

            if (!Files.isDirectory(Paths.get(path))) Files.createDirectory(Paths.get(path));

            Log.info("Extracting: " + swfURL.toString());

            SWFData swfData = new SWFData(swfURL);

            for (ImageTag tag : swfData.getImageTags()) {
                String spriteName;
                if (stripClass) {
                    spriteName = tag.getClassName().replace(className + "_", "");
                } else {
                    spriteName = Arrays.stream(tag.getClassName().split(className + "_"))
                            .distinct().collect(Collectors.joining(className + "_"));
                }
                if (spriteName.contains("_32_")) continue;
                ImageIO.write(tag.getImage().getBufferedImage(), "png", new File(path + spriteName + ".png"));
            }
        } catch (MalformedURLException ex) {
            Log.error("Malformed URL!", ex);
        } catch (Throwable ex) {
            Log.error("Failed to load/extract SWF!", ex);
        }
    }
}
