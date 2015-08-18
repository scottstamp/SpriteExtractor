package gz.azure;

import com.jpexs.decompiler.flash.tags.base.ImageTag;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DownloadFurniSprites implements Runnable {
    private int revision;
    private String className;

    public DownloadFurniSprites(int revision, String className) {
        this.revision = revision;
        this.className = className;
    }

    @Override
    public void run() {
        try {
            URL swfURL = new URL("https:" + Main.externalVariables.getFlashDynamicDownloadURL() + revision + "/" + className + ".swf");
            String path;
            if (OSCheck.getOperatingSystemType() == OSCheck.OSType.Windows) path = "sprites\\";
            else path = "sprites/";

            if (!Files.isDirectory(Paths.get(path))) Files.createDirectory(Paths.get(path));

            Log.info("Extracting: " + swfURL.toString());

            SWFData swfData = new SWFData(swfURL);

            for (ImageTag tag : swfData.getImageTags()) {
                String spriteName = Arrays.stream(tag.getClassName().split(className + "_"))
                        .distinct().collect(Collectors.joining(className + "_"));
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
