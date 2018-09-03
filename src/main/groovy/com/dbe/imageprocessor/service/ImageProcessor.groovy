package com.dbe.imageprocessor.service

import java.awt.Color
import java.awt.image.BufferedImage

interface ImageProcessor {
    Map<Color, Long> groupColors(BufferedImage image,Integer colorCount)

    BufferedImage reduceColors(BufferedImage image, Integer colorCount)

    BufferedImage generateMinimalBlocks(BufferedImage bufferedImage, int x, int y)
}
