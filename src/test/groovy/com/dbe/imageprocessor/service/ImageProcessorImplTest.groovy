package com.dbe.imageprocessor.service

import com.jhlabs.composite.MultiplyComposite
import com.jhlabs.image.CompositeFilter
import com.jhlabs.image.EdgeFilter
import com.jhlabs.image.GrayFilter
import com.jhlabs.image.GrayscaleFilter
import com.jhlabs.image.InvertFilter
import org.springframework.core.io.ClassPathResource
import spock.lang.Specification

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageProcessorImplTest extends Specification {

    def 'process image'() {
        setup:
        def outputDir = new File('output')
        outputDir.deleteDir()
        outputDir.mkdir()
        def imageprocessor = new ImageProcessorImpl()
        when:
        def newImage = imageprocessor.reduceColors(ImageIO.read(new ClassPathResource('images/diego.png').inputStream), 10)
        newImage = imageprocessor.drawLines(newImage)
        ImageIO.write(newImage, "png", new File(outputDir, 'diego-processed.png'))
        then: ''
    }

    def 'test with real file'() {
        when:
        def newImage = new ImageProcessorImpl().reduceColors(ImageIO.read(new File('/home/detlef/Bilder/2017/12/03/DSC06826.JPG')), 10)
        ImageIO.write(newImage, "jpg", new File('/home/detlef/Bilder/2017/12/03/DSC06826-p.JPG'))
        then: ''
    }

    def 'subimage'() {
        def src = ImageIO.read(new File('output/diego-processed.png'))
        def dst = new BufferedImage(src.width, src.height, src.type)
        EdgeFilter filter = new EdgeFilter()
        GrayscaleFilter grayFilter=new GrayscaleFilter()
        InvertFilter invertFilter =new InvertFilter()
        CompositeFilter compositeFilter=new CompositeFilter(new MultiplyComposite(1))

        filter
        when:
        filter.filter(src, dst)
        grayFilter.filter(dst,dst)
        invertFilter.filter(dst,dst)
        compositeFilter.filter(src,dst)
        ImageIO.write(dst, "jpg", new File('output/diego-edge.png'))
        then: ''
    }

    def 'edge filter'() {
        when:
        def src = ImageIO.read(new File('/home/detlef/Bilder/2017/12/03/DSC06826-p.JPG'))
        def dst = new BufferedImage(src.width, src.height, src.type)
        EdgeFilter filter = new EdgeFilter()
        filter.filter(src,dst)
        ImageIO.write(dst, "jpg", new File('output/diego-edge.png'))
        then:''

    }
}
