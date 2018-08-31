package com.dbe.imageprocessor.service

import spock.lang.Specification

import javax.imageio.ImageIO
import java.util.function.Consumer

class ImageProcessorImplTest extends Specification {
    def 'process image'() {
        when:
        (4..14).parallelStream().forEach(new Consumer() {
            @Override
            void accept(def it) {
                def newImage = new ImageProcessorImpl().reduceColors(ImageIO.read(new File('/home/detlef/Bilder/2018/06/10/DSC06883.JPG')), it)
                ImageIO.write(newImage, "png", new File("/home/detlef/temp/newimage${it}.png"))
                println "write file $it"
            }
        })

        then: ''
    }

}
