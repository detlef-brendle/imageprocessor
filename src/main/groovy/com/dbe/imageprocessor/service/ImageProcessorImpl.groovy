package com.dbe.imageprocessor.service

import java.awt.*
import java.awt.image.BufferedImage

import static java.lang.Math.pow
import static java.lang.Math.sqrt

class ImageProcessorImpl implements ImageProcessor {
    @Override
    Map<Color, Long> groupColors(BufferedImage image, Integer colorCount) {
        Map<Color, Long> colorsMap = [:]
        def distance = 51
        def checkedDistance = []
        while (colorsMap.size() != colorCount) {
            println "check with distance $distance"
            if (checkedDistance.contains(distance)) {
                break
            }
            colorsMap.clear()
            Map<Color, Color> equivalentColors = [:]
            for (int w = 0; w < image.width; w++) {
                for (int h = 0; h < image.height; h++) {
                    Color c = searchColor(image, w, h, colorsMap.keySet(), equivalentColors, distance)
                    colorsMap.putIfAbsent(c, 1)
                    colorsMap.put(c, colorsMap.get(c) + 1)
                }
            }
            checkedDistance << distance
            if (colorsMap.size() > colorCount) {
                distance = distance + colorsMap.size() - colorCount
            } else if (colorsMap.size() < colorCount) {
                distance = distance - colorCount - colorsMap.size()
            }
        }
        println "final distance was $distance"
        return colorsMap.sort { v -> v.value * -1 }
    }

    Color searchColor(image, w, h, Set colors, Map equivalentColors, d) {
        Color sourceColor = new Color(image.getRGB(w, h))
        Color equivalentColor = null
        if (equivalentColors.containsKey(sourceColor)) {
            equivalentColor = equivalentColors.get(sourceColor)
        }
        def iter = colors.iterator()
        while (equivalentColor == null && iter.hasNext()) {
            Color c = iter.next()
            def distance = sqrt(pow(c.red - sourceColor.red, 2) + pow(c.green - sourceColor.green, 2) + pow(c.blue - sourceColor.blue, 2))
            if (distance < d) {
                equivalentColor = c
                equivalentColors.put(sourceColor, c)
            }
        }
        equivalentColor != null ? equivalentColor : sourceColor
    }

    @Override
    BufferedImage reduceColors(BufferedImage image, Integer colorCount) {
        BufferedImage img = new BufferedImage(image.width, image.height, image.type)
        def colors = groupColors(image, colorCount)
        def targetColors = colors.take(colorCount)
        Map<Integer, Integer> colorsCache = [:]

        for (int w = 0; w < image.width; w++) {
            for (int h = 0; h < image.height; h++) {
                def sourceRGB = image.getRGB(w, h)
                def newColor
                if (!colorsCache.containsKey(sourceRGB)) {
                    Color sourceColor = new Color(sourceRGB)
                    targetColors = targetColors.sort({ c -> compareColors(c.key, sourceColor) })
                    newColor = targetColors.keySet()[0].getRGB()
                } else {
                    newColor = colorsCache.get(sourceRGB)
                }
                colorsCache.put(sourceRGB, newColor)
                img.setRGB(w, h, newColor)
            }
        }
        return img
    }

    int compareColors(Color sourceColor, Color c) {
        sqrt(pow(c.red - sourceColor.red, 2) + pow(c.green - sourceColor.green, 2) + pow(c.blue - sourceColor.blue, 2))
    }

    @Override
    BufferedImage generateMinimalBlocks(BufferedImage bufferedImage, int x, int y) {
        BufferedImage result = new BufferedImage(image.width, image.height, image.type)
        bufferedImage.width.times { w ->
            bufferedImage.height.times { h ->
                bufferedImage.getRGB(w, h)
            }
        }
    }

    BufferedImage drawLines(BufferedImage image) {
        def subimage = image.getSubimage(0, 0, 10, 10)
        println subimage
    }
}
