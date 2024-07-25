package main;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends PApplet {

    public PGraphics pg;
    public static ArrayList<PImage> imgs;
    public static ArrayList<Integer> accessPool;
    public static PImage imageOne;
    public static PImage imageTwo;
    public static boolean[] updater;
    public static int max;
    public static final int imageNumber = 6;
    public static boolean switching = false;
    public static boolean isDone = true;
    public static boolean[] accessed;
    public static boolean[] reference;
    public static int access;
    public static int counter = 0;

    public void initImages() {
        imgs = new ArrayList<PImage>(imageNumber);
        for (int i = 0; i < imageNumber; i++) {
            PImage tempImg = loadImage("data/TEST " + (i + 1) + ".jpg");
            imgs.add(tempImg);
        }
        accessed = new boolean[imageNumber];
        for (int i = 0; i < imageNumber; i++) {
            accessed[i] = false;
        }
    }


    public void settings() {
        //Set size
        size(500, 500);
        pg = new PGraphics();
        this.g = pg;

        //Initializing images that will be switching
        imageOne = createImage(width, height, ARGB);
        imageTwo = createImage(width, height, ARGB);
        imageOne.loadPixels();
        imageTwo.loadPixels();

        //declaring the max variable. easier to type than pixels.length
        max = imageOne.pixels.length;

        //populate the ArrayList with all the PImages that will be accessed. These images won't be displayed though.
        initImages();

        access = (int) random(imageNumber);
        accessed[access] = true;

        imgs.get(access).loadPixels();

        for (int i = 0; i < max; i++) {
            imageOne.pixels[i] = imgs.get(access).pixels[i];
        }

        updater = new boolean[max];
        reference = new boolean[max];
        accessPool = new ArrayList<Integer>(max);
        for (int i = 0; i < max; i++) {
            reference[i] = true;
            updater[i] = false;
            accessPool.add(i);
        }
    }

    public void draw() {
        image(imageOne, 0, 0);

//        fill(0x80000000);
//        rect(0, 0, 250, 260);
        textSize(25);
//        fill(255);
        text(frameRate, 40, 40);
        text("Pool Size: " + Integer.toString(accessPool.size()), 40, 80);
//        text("Is Done? " + Boolean.toString(isDone), 40, 120);
//        text("Image Access: " + Integer.toString(access), 40, 160);
//        text("Counter: " + Integer.toString(counter), 40, 200);
//        textSize(15);
//        text("Image Pool: " + accessed[0] + ", " + accessed[1] + ", " + accessed[2] + ", " + accessed[3]
//        + ", " + accessed[4] + ", " + accessed[5], 40, 240);

        while (isDone) {
            accessPool.clear();
            for (int i = 0; i < max; i++) {
                accessPool.add(i);
                updater[i] = false;
            }

            access = (int) random(imageNumber);
            if (accessed[access]) {
                access = (int) random(imageNumber);
            } else accessed[access] = true;

            imgs.get(access).loadPixels();
            System.out.println("New Image Loaded");

            isDone = false;
            break;
        }

        if (!isDone) {

            if (mouseX - 50 > 0 && mouseX + 50 < width && mouseY - 50 > 0 && mouseY + 50 < height) {
                for (int i = mouseY - 50; i < mouseY + 50; i++) {
                    for (int j = mouseX - 50; j < mouseX + 50; j++) {
                        int loc = j + i * width;
                        imageOne.pixels[loc] = imgs.get(access).pixels[loc];
                        if (!updater[loc]) {
                            updater[loc] = true;
                            counter++;
                            accessPool.remove(accessPool.indexOf(loc));
                        }
                    }

                }
            }

            int accessor = accessPool.get((int) random(accessPool.size()));

            if (updater[accessor]) {
                while (updater[accessor]) {
                    if (accessor + 1 < accessPool.size()) {
                        accessor = accessPool.get(accessor + 1);
                    } else accessor = 0;
                }

            } else {
                imageOne.pixels[accessor] = imgs.get(access).pixels[accessor];
                updater[accessor] = true;
                counter++;
                accessPool.remove((Integer) accessor);
                if (accessor + 1 < max) {
                    imageOne.pixels[accessor + 1] = imgs.get(access).pixels[accessor + 1];
                    updater[accessor + 1] = true;
                    counter++;
                    if (accessPool.contains(accessor + 1)) {
                        accessPool.remove(accessPool.indexOf(accessor + 1));
                    }
                }
                if (accessor - 1 > 0) {
                    imageOne.pixels[accessor - 1] = imgs.get(access).pixels[accessor - 1];
                    updater[accessor - 1] = true;
                    counter++;
                    if (accessPool.contains(accessor - 1)) {
                        accessPool.remove(accessPool.indexOf(accessor - 1));
                    }
                }
                if (accessor - width > 0) {
                    imageOne.pixels[accessor - width] = imgs.get(access).pixels[accessor - width];
                    updater[accessor - width] = true;
                    counter++;
                    if (accessPool.contains(accessor - width)) {
                        accessPool.remove(accessPool.indexOf(accessor - width));
                    }
                }
                if (accessor + width < max) {
                    imageOne.pixels[accessor + width] = imgs.get(access).pixels[accessor + width];
                    updater[accessor + width] = true;
                    counter++;
                    if (accessPool.contains(accessor + width)) {
                        accessPool.remove(accessPool.indexOf(accessor + width));
                    }
                }
            }

            if (accessPool.isEmpty()) {
                System.out.println("Image Finished");
                isDone = true;
            }
        }
        imageOne.updatePixels();
    }

    public static void main(String[] args) {
        String[] processingAgs = {"Main"};
        Main main = new Main();
        PApplet.runSketch(processingAgs, main);
    }
}