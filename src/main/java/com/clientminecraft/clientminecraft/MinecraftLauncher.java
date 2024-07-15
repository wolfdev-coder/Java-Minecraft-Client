package com.clientminecraft.clientminecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinecraftLauncher {
    public static void main(String[] args) {
        // Путь к папке с версией Minecraft 1.21
        String minecraftVersionPath = "C:/Users/Wolfitsu/AppData/Roaming/.minecraft/versions/1.21";

        try {
            // Получаем список всех необходимых файлов для запуска
            List<String> classpath = getClasspath(minecraftVersionPath);

            // Создаем массив аргументов для запуска Minecraft
            String[] launchArgs = createLaunchArgs(minecraftVersionPath, classpath);

            // Запускаем Minecraft
            launchMinecraft(launchArgs);
        } catch (IOException e) {
            System.err.println("Ошибка при запуске Minecraft: " + e.getMessage());
        }
    }

    private static List<String> getClasspath(String minecraftVersionPath) throws IOException {
        List<String> classpath = new ArrayList<>();

        // Добавляем все .jar файлы из папки версии в classpath
        File versionsDir = new File(minecraftVersionPath);
        if (versionsDir.exists() && versionsDir.isDirectory()) {
            for (File file : versionsDir.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    classpath.add(file.getAbsolutePath());
                }
            }
        } else {
            throw new IOException("Папка версии Minecraft не найдена: " + versionsDir.getAbsolutePath());
        }

        // Добавляем путь к библиотеке joptsimple
        classpath.add("lib/jopt-simple-5.0.4.jar");

        return classpath;
    }

    private static String[] createLaunchArgs(String minecraftVersionPath, List<String> classpath) {
        return new String[] {
                "java",
                "-Xmx2G",
                "-Xms2G",
                "-cp",
                String.join(File.pathSeparator, classpath),
                "net.minecraft.client.main.Main",
                "--version",
                "1.21",
                "--gameDir",
                "c:/users/user/AppData/Roaming/.minecraft",
                "--assetsDir",
                "c:/users/user/AppData/Roaming/.minecraft/assets"
        };
    }

    private static void launchMinecraft(String[] launchArgs) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(launchArgs);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (exitCode != 0) {
            throw new IOException("Minecraft failed to launch with exit code " + exitCode);
        }
    }
}