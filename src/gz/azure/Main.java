package gz.azure;

import com.jpexs.decompiler.flash.tags.base.ImageTag;
import gz.azure.xml.FurnidataParser;
import gz.azure.xml.furnidata.Furnidata;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Azure Camera Server - Furni Sprite Extractor
 * Written by Scott Stamp (scottstamp851, scott@hypermine.com)
 */
class Main {
    private static final List<String> downloadedFurnis = new ArrayList<>();

    public static void main(String[] args) throws Throwable {
        Log.println("             Azure Camera Server - Furni Sprite Extractor");
        Log.println("      Written by Scott Stamp (scottstamp851, scott@hypermine.com)");
        Log.println();
        Log.println("Note: Some files may fail to download/extract. Common with low revision ID numbers.");
        Log.println("      It means they're no longer hosted on Habbo's server. This is normal.");
        Log.println();

        Files.createDirectory(Paths.get("images"));

        List<Furnidata.Roomitemtypes.Furnitype> roomItemTypes
                = FurnidataParser.getFurnidata().getRoomitemtypes().getFurnitype();
        List<Furnidata.Wallitemtypes.Furnitype> wallItemTypes
                = FurnidataParser.getFurnidata().getWallitemtypes().getFurnitype();

        for (Furnidata.Roomitemtypes.Furnitype roomItem : roomItemTypes)
            DownloadImages(roomItem.getRevision(), roomItem.getClassname().split("\\*")[0]);

        for (Furnidata.Wallitemtypes.Furnitype wallItem : wallItemTypes)
            DownloadImages(wallItem.getRevision(), wallItem.getClassname().split("\\*")[0]);
    }

    private static void DownloadImages(int revision, String className) throws MalformedURLException {
        URL swfURL = new URL("https://habboo-a.akamaihd.net/dcr/hof_furni/" + revision + "/" + className + ".swf");

        try {
            if (downloadedFurnis.contains(className)) return;
            else Files.createDirectory(Paths.get("images\\" + className));

            Log.info("Extracting: " + swfURL.toString());

            SWFData swfData = new SWFData(swfURL);

            for (ImageTag tag : swfData.getImageTags()) {
                String spriteName = Arrays.stream(tag.getClassName().split(className + "_"))
                        .distinct().collect(Collectors.joining(className + "_"));
                ImageIO.write(tag.getImage().getBufferedImage(), "png", new File("images\\" + spriteName + ".png"));
            }

            downloadedFurnis.add(className);
        } catch (Throwable ex) {
            Log.error("Failed to load/extract: " + swfURL);
        }
    }
}
