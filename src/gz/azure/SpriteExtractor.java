package gz.azure;

import gz.azure.txt.ExternalVariables;
import gz.azure.utils.Log;
import gz.azure.xml.FiguremapParser;
import gz.azure.xml.FurnidataParser;
import gz.azure.xml.figuremap.Map;
import gz.azure.xml.furnidata.Furnidata;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Azure Camera Server - Furni Sprite Extractor
 * Written by Scott Stamp (scottstamp851, scott@hypermine.com)
 */
public class SpriteExtractor {
    public static ExternalVariables externalVariables;

    static {
        try {
            externalVariables = new ExternalVariables(new URL("https://www.habbo.com/gamedata/external_variables/1"));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        Log.println("             Azure Camera Server - Furni Sprite Extractor");
        Log.println("      Written by Scott Stamp (scottstamp851, scott@hypermine.com)");
        Log.println();
        Log.println("Note: Some files may fail to download/extract. Common with low revision ID numbers.");
        Log.println("      It means they're no longer hosted on Habbo's server. This is normal.");
        Log.println();

        // Download all pet sprites
        ExecutorService downloadPool = Executors.newFixedThreadPool(8);
        List<Callable<Object>> downloadTasks = externalVariables.getPets().stream()
                .map(pet -> Executors.callable(new DownloadMiscSprites("sprites", pet, false)))
                .collect(Collectors.toList());

        // Download all figure sprites
        Map figuremap = FiguremapParser.getFiguremap();
        if (figuremap != null) {
            downloadTasks.addAll(figuremap.getLib().stream()
                    .map(lib -> Executors.callable(new DownloadMiscSprites("sprites", lib.getId(), false)))
                    .collect(Collectors.toList()));
        }

        // Download all wall/floor/landscape masks
        downloadTasks.add(Executors.callable(new DownloadMiscSprites("masks", "HabboRoomContent", true)));

        // Download all furni sprites
        Furnidata furnidata = FurnidataParser.getFurnidata();
        if (furnidata != null) {
            List<Furnidata.Roomitemtypes.Furnitype> roomItemTypes
                    = furnidata.getRoomitemtypes().getFurnitype();
            List<Furnidata.Wallitemtypes.Furnitype> wallItemTypes
                    = furnidata.getWallitemtypes().getFurnitype();

            List<String> itemClassNames = new ArrayList<>();

            for (Furnidata.Roomitemtypes.Furnitype item : roomItemTypes) {
                String className = item.getClassname().split("\\*")[0];
                if (!itemClassNames.contains(className))
                    downloadTasks.add(Executors.callable(new DownloadFurniSprites(item.getRevision(), className)));

                itemClassNames.add(className);
            }

            for (Furnidata.Wallitemtypes.Furnitype item : wallItemTypes) {
                String className = item.getClassname().split("\\*")[0];
                if (!itemClassNames.contains(className))
                    downloadTasks.add(Executors.callable(new DownloadFurniSprites(item.getRevision(), className)));

                itemClassNames.add(className);
            }
        }

        downloadPool.invokeAll(downloadTasks);
        downloadPool.shutdown();
        downloadPool.awaitTermination(10, TimeUnit.SECONDS);
    }
}
