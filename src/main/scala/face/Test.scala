package face

import java.io.{FileOutputStream, FileInputStream}
import java.util

/**
 * Created by liwei on 2015/7/17.
 */
object Test extends App {
  val in = new FileInputStream("D:/DSC_0072_1.jpg")
  val out = new FileOutputStream("D:/1.jpg")
  val byte = new Array[Byte](1024)
  while (in.read(byte) != -1)
    out.write(byte)
  (1 to 200000).foreach(_ => out.write(0))
  out.close()
}
