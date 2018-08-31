package com.dbe.imageprocessor.data

import java.awt.*

class EquivalentColor extends Color {
    EquivalentColor(int rgb) {
        super(rgb)
    }

    @Override
    boolean equals(Object other) {
        def distance = Math.sqrt(Math.pow(other.red - red, 2) + Math.pow(other.green - green, 2) + Math.pow(other.blue - blue, 2))
        super.equals(other) || distance < 100
    }

    @Override
    int hashCode() {
        return super.hashCode()
    }
}
