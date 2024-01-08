package com.jin;

/**
 * @author 大力pig
 * @date 2024/1/7
 **/

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LaunchConEmu {

    public static void main(String[] args) {
        try {

            // 指定要运行的程序和参数
            String command = "D:\\software\\ConEmu\\ConEmu64.exe";
            String[] commandArray = {command};
            // 使用ProcessBuilder启动程序
            ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
            Process process = processBuilder.start();
            OutputStream outputStream = process.getOutputStream();

            // 将用户输入写入ConEmu的标准输入流.
            String userInput = "java -version\n";
            outputStream.write(userInput.getBytes());
            outputStream.flush();
            ocrCapture();

            // 如果你想等待程序退出，可以使用waitFor方法
            int exitCode = process.waitFor();
            System.out.println("ConEmu已退出，退出代码：" + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void ocrCapture() {
        // 设置 Tesseract 的安装路径
        String tessPath = "D:\\software\\ocr\\tessdata\\";
        System.setProperty("TESSDATA_PREFIX", tessPath);


        // 创建 Tesseract 实例
        ITesseract tess = new Tesseract();

        try {
            // 截取整个屏幕
//            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//            BufferedImage screenshot = new Robot().createScreenCapture(screenRect);

            File imageFile = new File("src/main/resources/hello.PNG"); // 替换为你的图像文件路径


            // 进行 OCR 识别
            String result = tess.doOCR(imageFile);

            // 输出识别结果
            System.out.println("OCR Result:");
            System.out.println(result);


            // 预期的文本
            String expectedText = "java version \"17.0.9\" 2023-10-17 LTS\n" +
                    "Java(TM) SE Runtime Environment (build 17.0.9+11-LTS-201)\n" +
                    "Java HotSpot(TM) 64-Bit Server VM (build 17.0.9+11-LTS-201, mixed mode, sharing)";

            // 输出识别结果
            System.out.println("OCR Result:");
            System.out.println(result);

            // 比较实际识别结果和预期文本
            if (result.trim().equals(expectedText.trim())) {
                System.out.println("识别结果与预期一致！");
            } else {
                System.out.println("识别结果与预期不一致。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 激活ConEmu窗口
    private static void activateConEmuWindow() throws AWTException {
        Robot robot = new Robot();
        activateWindow(robot, "ConEmu");
    }

    // 激活应用程序窗口
    private static void activateWindow(Robot robot, String windowTitle) {
        java.util.List<java.awt.Window> windows = java.util.Arrays.asList(Window.getWindows());
        for (Window window : windows) {
            if (window instanceof Frame && window.isShowing() && ((Frame) window).getTitle().contains(windowTitle)) {
                Frame frame = (Frame) window;
                frame.toFront();
                frame.requestFocus();
                break;
            }
        }
    }

    // 模拟键盘输入文本
    private static void typeText(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }
    }


}

