import java.io.*;
import java.util.Arrays;
import java.util.List;
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
    static File tempDir = new File(gameDirectory.getPath() + "/temp");
    static File log = new File(tempDir.getPath() + "/log.txt");

    public static void main(String[] args) {
        installGame();

        List<GameProgress> gameProgresses = Arrays.asList(new GameProgress(59, "AK", 10, 33.6),
                new GameProgress(80, "AKv1", 15, 70.6),
                new GameProgress(10, "AKv3", 44, 100));

        for (int i = 0; i < gameProgresses.size(); i++) {
            saveGame(gameProgresses.get(i), saveGamesDir.getPath() + "/saveGame" + i + ".txt");
        }
        List<String> saveGames = Arrays.asList(saveGamesDir.list());

        saveFileInZIP(saveGamesDir.getPath() + "/saveGame.zip",saveGamesDir.getPath(), saveGames);

    }

    public static void saveFileInZIP(String zipFile, String folderWithSaves, List<String> filesForArchiving) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

        ) {
            for (String file : filesForArchiving) {
                String pathAndFile = folderWithSaves +"/"+ file;
                FileInputStream fileInputStream = new FileInputStream(pathAndFile);
                ZipEntry zipEntry = new ZipEntry(pathAndFile);
                byte[] buffer = new byte[fileInputStream.available()];

                zipOutputStream.putNextEntry(zipEntry);
                fileInputStream.read(buffer);
                zipOutputStream.write(buffer);
                zipOutputStream.closeEntry();
                fileInputStream.close();
                deleteFile(pathAndFile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public static void saveGame(GameProgress gameProgress, String saveFile) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile, true);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
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

    static void deleteFile(String fileToDelete) {
        File file = new File(fileToDelete);
        if (!file.isDirectory()) {
            file.delete();
        } else {
            System.out.println("Для удаления была выбрана пака а не файл");
        }
    }


}
