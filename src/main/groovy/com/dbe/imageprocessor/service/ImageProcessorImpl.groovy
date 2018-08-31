package com.dbe.imageprocessor.service


import java.awt.*
import java.awt.image.BufferedImage

class ImageProcessorImpl implements ImageProcessor {
    @Override
    Map<Color, Long> groupColors(BufferedImage image) {
        Map<Color, Long> colorsMap = [:]
        Map<Color, Color> equivalentColors = [:]
        image.width.times { w ->
            image.height.times { h ->
                Color sourceColor = new Color(image.getRGB(w, h))
                Color c = searchColor(sourceColor, colorsMap.keySet(), equivalentColors)
                colorsMap.putIfAbsent(c, 1)
                colorsMap.put(c, colorsMap.get(c) + 1)
            }
        }
        return colorsMap.sort { v -> v.value * -1 }
    }

    Color searchColor(Color sourceColor, Set colors, Map equivalentColors) {
        Color equivalentColor = null
        if (equivalentColors.containsKey(sourceColor)) {
            equivalentColor = equivalentColors.get(sourceColor)
        }
        def iter = colors.iterator()
        while (equivalentColor == null && iter.hasNext()) {
            Color c = iter.next()
            def distance = Math.sqrt(Math.pow(c.red - sourceColor.red, 2) + Math.pow(c.green - sourceColor.green, 2) + Math.pow(c.blue - sourceColor.blue, 2))
            if (distance < 40) {
                equivalentColor = c
                equivalentColors.put(sourceColor,c)
            }
        }
        equivalentColor != null ? equivalentColor : sourceColor
    }

    @Override
    BufferedImage reduceColors(BufferedImage image, Integer colorCount) {
        BufferedImage img = new BufferedImage(image.width, image.height, image.type)
        def colors = groupColors(image)
        def targetColors = colors.take(colorCount)
        Map<Integer, Integer> colorsCache = [:]
        image.width.times { w ->
            image.height.times { h ->
                def sourceRGB = image.getRGB(w, h)
                def newColor
                if (!colorsCache.containsKey(sourceRGB)) {
                    newColor = findNearestNewColor(targetColors.keySet(), sourceRGB)
                } else {
                    newColor = colorsCache.get(sourceRGB)
                }
                colorsCache.put(sourceRGB, newColor)
                img.setRGB(w, h, newColor)
            }
        }
        return img
    }

    private findNearestNewColor(Set<Color> targetColors, int color) {
        Color nearestTargetColor = null;
        Integer nearestDistance = new Integer(Integer.MAX_VALUE)
        Color sourceColor = new Color(color)
        targetColors.each { Color c ->
            def distance = Math.sqrt(Math.pow(c.red - sourceColor.red, 2) + Math.pow(c.green - sourceColor.green, 2) + Math.pow(c.blue - sourceColor.blue, 2))

            if (nearestDistance > distance) {
                nearestTargetColor = c
                nearestDistance = distance
            }
        }

        return nearestTargetColor.RGB
    }

}
