package com.dbe.imageprocessor.service

import java.awt.Color
import java.awt.image.BufferedImage

interface ImageProcessor {
    Map<Color, Long> groupColors(BufferedImage image)

    BufferedImage reduceColors(BufferedImage image, Integer colorCount)
}
