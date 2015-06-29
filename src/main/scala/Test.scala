import java.io.FileInputStream
import javax.imageio.ImageIO

/**
 * Created by li-wei on 2015/6/29.
 */
object Test extends App {
  val readers = ImageIO.getImageReadersByFormatName("jpg")
  val reader =  readers.next()
  val iis = ImageIO.createImageInputStream(new FileInputStream("D:\\work\\photos-true\\total\\15\\111\\62398-1.jpg"))
  reader.setInput(iis, true)
  System.out.println("width:" + reader.getWidth(0))
  System.out.println("height:" + reader.getHeight(0))
}
