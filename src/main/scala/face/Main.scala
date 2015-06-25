package face

/**
 *  vm args: -Djava.library.path=F:/opencv/build/java/x64;F:/opencv/build/x64/vc12/bin -Xms100M -Xmx300M -XX:MaxPermSize=50M -XX:MaxDirectMemorySize=50M
 */
object Main extends App{
  val lowAge = "71"
  val uppAge = "75"
  val baseDir = "D:/work/photos-true/first"
  PhotoDropDu.main(Array(lowAge, uppAge))
  FaceDetect.main(Array(lowAge, uppAge))
  FaceDropDu.main(Array(lowAge, uppAge))
}
