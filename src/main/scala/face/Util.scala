package face

import java.io.{FileOutputStream, File, FileInputStream}

/**
 * Created by li-wei on 2015/5/1.
 */
object Util {
  def copyFile(source: String, desc: String): Unit = {
    val in = new FileInputStream(source)
    val outDirectory = new File(desc)
    outDirectory.getParent match {
      case null =>
      case _ =>
        if (! outDirectory.getParentFile.exists () ) {
          outDirectory.getParentFile.mkdirs ()
        }
    }
    val out = new FileOutputStream(desc)
    val buffer = new Array[Byte](1024)
    var len = 0
    while ( {
      len = in.read(buffer)
      len != -1
    }) {
      out.write(buffer, 0, len)
    }
    in.close()
    out.close()
  }

  def subFiles(dir: File) : Iterator[File] = {
    val all = dir.listFiles()
    val files = all.filter(_.isFile)
    val dirs = all.filter(_.isDirectory)
    files.toIterator ++ dirs.toIterator.flatMap(subFiles _)
  }
}
