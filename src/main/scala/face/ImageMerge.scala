package face

import java.awt.image.BufferedImage
import java.io.{FileOutputStream, File}
import javax.imageio.ImageIO

import com.sun.image.codec.jpeg.{JPEGCodec, JPEGImageEncoder}

/**
 * Created by li-wei on 2015/6/4.
 */
object ImageMerge extends App {

  merge("D:/work/photos-true/wall/19/nan", 5, 9, "D:/work/photos-true/wall/19/result/nan.jpg")

  def merge(imageDir: String, row: Int, col: Int, outFile: String):Unit = {
    val width = 1600 / col
    val height = 900 / row
    val images = Util.subFiles(new File(imageDir)).map(_.getAbsolutePath)
      .filter(_.endsWith(".jpg")).take(row*col).map(new File(_))
      .map(x => resize(x,width,height)).map(ImageIO.read).toList

    val resultImage = new BufferedImage(width * col, height * row, BufferedImage.TYPE_INT_RGB)
    for (cuRow <- 0 until row)
      for (cuCol <- 0 until col) {
        val image = images(cuRow * row + cuCol)
        var imageArray: Array[Int] = new Array[Int](width * height)
        imageArray = image.getRGB(0, 0, width, height, null, 0, width)
        resultImage.setRGB(cuCol * width, cuRow * height, width, height, imageArray, 0, width)
      }
    ImageIO.write(resultImage, "jpg", new File(outFile))
  }

  def resize(file: File, width: Int, height: Int): File = {
    val image = ImageIO.read(file)
    val tag: BufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB)
    tag.getGraphics().drawImage(image, 0, 0, width, height, null)
    val out = new File(file.getParentFile.getParent + "/" + file.getParentFile.getName + "-resize" + "/" + file.getName)
    if (!out.getParentFile.exists()) out.getParentFile.mkdirs()
    println(out)
    val outStream = new FileOutputStream(out)
    JPEGCodec.createJPEGEncoder(outStream).encode(tag)
    outStream.close()
    out
  }
}
