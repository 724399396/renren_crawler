package face

import org.opencv.core.Core

/**
 *  vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin -Xms100M -Xmx300M -XX:MaxPermSize=50M -XX:MaxDirectMemorySize=50M
 */
object Main extends App{
  System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
  val lowAge = "15"
  val uppAge = "17"
  val baseDir = "D:/work/photos-true/first"
  PhotoDropDu.main(Array(lowAge, uppAge))
  FaceDetect.main(Array(lowAge, uppAge))
  FaceDropDu.main(Array(lowAge, uppAge))
}
