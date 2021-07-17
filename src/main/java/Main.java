import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    static StringBuilder logs = new StringBuilder();

    static File gameDirectory = new File("temp/Game/myGame");

    static File srcDir = new File(gameDirectory.getPath() + "/src");
    static File mainDir = new File(srcDir.getPath() + "/main");
    static File mainDoc = new File(mainDir.getPath() + "/Main.java");
    static File utilsDoc = new File(mainDir.getPath() + "/Utils.java");

    static File testDir = new File(srcDir.getPath() + "/test");

    static File resDir = new File(gameDirectory.getPath() + "/rec");
    static File drawablesDir = new File(resDir.getPath() + "/drawables");
    static File vectorsDir = new File(resDir.getPath() + "/vectors");
    static File iconsDir = new File(resDir.getPath() + "/icons");

    static File saveGamesDir = new File(gameDirectory.getPath() + "/saveGames");
    static File saveGamesDoc = new File(saveGamesDir.getPath() + "/saveGames.txt");
    static File tempDir = new File(gameDirectory.getPath() + "/temp");
    static File log = new File(tempDir.getPath() + "/log.txt");

    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(59, "AK", 10, 33.6);
        GameProgress gameProgress2 = new GameProgress(80, "AKv1", 15, 70.6);
        GameProgress gameProgress3 = new GameProgress(10, "AKv3", 44, 100);

        installGame();

        saveGame(gameProgress1, saveGamesDoc.getPath());
        saveGame(gameProgress2, saveGamesDoc.getPath());
        saveGame(gameProgress3, saveGamesDoc.getPath());

        saveFileInZIP(saveGamesDoc);
    }

    public static void saveFileInZIP(File file) {
        File file1 = new File(saveGamesDoc.getPath() + ".zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file1));
             FileInputStream fileInputStream = new FileInputStream(file)
        ) {
            ZipEntry zipEntry = new ZipEntry(file.getPath());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            zipOutputStream.write(buffer);
            zipOutputStream.closeEntry();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        file.delete();
    }

    public static void saveGame(GameProgress gameProgress, String saveFile) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile, true);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            if (!saveGamesDoc.exists()) {
                saveGamesDoc.createNewFile();
            }
            objectOutputStream.writeObject(gameProgress);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void installGame() {
        System.out.println("Установка игры myGame");

        createDir(gameDirectory);
        createDir(srcDir);
        createDir(mainDir);
        createFile(mainDoc);
        createFile(utilsDoc);
        createDir(testDir);
        createDir(resDir);
        createDir(drawablesDir);
        createDir(vectorsDir);
        createDir(iconsDir);
        createDir(saveGamesDir);
        createDir(tempDir);
        createFile(log);

        try (FileWriter fileWriter = new FileWriter(log.getPath())) {
            fileWriter.write(logs.toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    static void createDir(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                String s = String.format("Создали папку - %s по пути - %s\n", file.getName(), file.getAbsolutePath());
                System.out.println(s);
                logs.append(s);
            }

        }
    }

    static void createFile(File file) {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    String s = String.format("Создали файл - %s по пути - %s\n", file.getName(), file.getAbsolutePath());
                    System.out.println(s);
                    logs.append(s);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


}
