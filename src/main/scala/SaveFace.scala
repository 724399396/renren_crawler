object SaveFace extends App {
  import scala.io.Source

  (41 to 75).foreach(saveFaceFromFile)

  def saveFaceFromFile(age: Int) = {
    val lines = Source.fromFile("D:/work/photos-true/message-generator/txt-complete/%d.txt".format(age), "utf-8").getLines().toList

    lines.map(raw => {
      val tmps = raw.split(" ")
      val id = tmps(0).takeWhile(_ != '-')
      val size = tmps(2)
      val width = size.takeWhile(_ != 'x')
      val height = size.dropWhile(_ != 'x').drop(1)
      Face(tmps(0), DBManager.photoForFace(id.toInt), tmps(1).toInt, tmps(6).toInt,
        width.toInt, height.toInt)
    }).filter(_.sex != 0).par.foreach(face => DBManager.saveFace(face))

    println(age + " finish!")
  }
}
