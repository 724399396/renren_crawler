import java.io.FileInputStream
import javax.imageio.ImageIO

/**
 * Created by liwei on 2015/7/5.
 */
object Test extends App {
  val readers = ImageIO.getImageReadersByFormatName("jpg")
  val reader =  readers.next()
  val iis = ImageIO.createImageInputStream(new FileInputStream("D:\\work\\photos-true\\total\\15\\111\\62398-1.jpg"))
  reader.setInput(iis, true)
  System.out.println("width:" + reader.getWidth(0))
  System.out.println("height:" + reader.getHeight(0))
}
