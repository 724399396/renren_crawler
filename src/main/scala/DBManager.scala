import java.io.File
import java.util.Scanner

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.{SqlSession, SqlSessionFactoryBuilder, SqlSessionFactory}

import scala.collection.mutable

/**
 * Created by li-wei on 2015/4/15.
 */
object DBManager {
  // mybatis init
  val resource = "dbconf.xml"
  val reader = Resources.getResourceAsReader(resource)
  val sessionFactory: SqlSessionFactory =
    new SqlSessionFactoryBuilder().build(reader)

  val uidSet = allUidListCache()

  // save ren-ren user
  def saveUser(user: User): Unit = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      if(!isUserExistFromMem(user)) {
        val statement = "ren-ren_crawler.mapper.saveUser"
        session.insert(statement, user)
        session.commit()
      }
    } finally {
      session.close()
    }
  }

  // check is user exist?
  private def isUserExistFromDB(user: User, session: SqlSession): Boolean = {
    val statement = "ren-ren_crawler.mapper.isUserExist"
    val exist = session.selectOne[Int](statement, user)
    session.commit()
    exist > 0
  }

  // check is user exist from memory
  private def isUserExistFromMem(user: User): Boolean = {
    if(uidSet contains user.uid) true
    else {
      uidSet add user.uid
      false
    }
  }

  def notFetchUsersByBirth(birth: Int) = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.notFetchUsersByBirth"
      import scala.collection.JavaConversions.asScalaBuffer
      val users: mutable.Buffer[User] = session.selectList[User](statement, birth)
      session.commit()
      users
    } finally {
      session.close()
    }
  }

  // return all noy fetch users
  def allNotFetchUsers() = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.allNotFetchUsers"
      import scala.collection.JavaConversions.asScalaBuffer
      val users: mutable.Buffer[User] = session.selectList[User](statement)
      session.commit()
      users
    } finally {
      session.close()
    }
  }

  // if has fetch, then set isFetch true
  def changeUserIsFetch(user: User) = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.changeUserIsFetch"
      session.update(statement, user)
      session.commit()
    } finally {
      session.close()
    }
  }

  def allNotHaveEntryYearUsers() = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.allNotHaveEntryYearUsers"
      import scala.collection.JavaConversions.asScalaBuffer
      val users: mutable.Buffer[User] = session.selectList[User](statement)
      session.commit()
      users
    } finally {
      session.close()
    }
  }

  def fixUserEntryYear(user: User): Unit = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.fixUserEntryYear"
      session.update(statement, user)
      session.commit()
    } finally {
      session.close()
    }
  }

  // save photo info to database
  def savePhoto(photo: Photo): Unit = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      if(!isPhotoExist(photo,session)) {
        val statement = "ren-ren_crawler.mapper.savePhoto"
        session.insert(statement, photo)
        session.commit()
      }
    } finally {
      session.close()
    }
  }

  // check is photo exist
  private def isPhotoExist(photo: Photo, session: SqlSession): Boolean = {
    val statement = "ren-ren_crawler.mapper.isPhotoExist"
    import scala.collection.JavaConversions.asScalaBuffer
    val exist: mutable.Buffer[String] = session.selectList[String](statement, photo)
    session.commit()
    if (exist.size == 0) false
    else exist.exists(_ == photo.avatarUrl)
  }

  // return all photo info
  def allNotSavePhotos() = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.allNotSavePhotos"
      import scala.collection.JavaConversions.asScalaBuffer
      val photos: mutable.Buffer[Photo] = session.selectList[Photo](statement)
      session.commit()
      photos
    } finally {
      session.close()
    }
  }

  def notSavePhotosByAge(age: Int) = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.notSavePhotosByAge"
      import scala.collection.JavaConversions.asScalaBuffer
      val photos: mutable.Buffer[Photo] = session.selectList[Photo](statement,age)
      session.commit()
      photos
    } finally {
      session.close()
    }
  }

  def hasSavedPhotoNumByAge(age: Int): Int = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.hasSavedPhotoNumByAge"
      val num = session.selectOne[Int](statement,age)
      session.commit()
      num
    } finally {
      session.close()
    }
  }

  def getPhotoById(id: Int): Photo = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.getPhotoById"
      val photo: Photo = session.selectOne[Photo](statement, id)
      session.commit()
      photo
    } finally {
      session.close
    }
  }
  
  // if has fetch, then set isFetch true
  def changePhotoIsFetch(photo: Photo) = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.changePhotoIsFetch"
      session.update(statement, photo)
      session.commit()
    } finally {
      session.close()
    }
  }

  def allUidListCache(): mutable.Set[Long] = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.allUidListCache"
      import scala.collection.JavaConversions.{asScalaBuffer, asJavaCollection, asScalaSet}
      val uids: mutable.Buffer[Long] = session.selectList[Long](statement)
      session.commit()
      import java.util.concurrent.ConcurrentSkipListSet
      new ConcurrentSkipListSet[Long](uids.toList)
    } finally {
      session.close()
    }
  }

  def saveFace(face: Face): Unit = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      if(!isFaceExistFromDB(face,session)) {
        val statement = "ren-ren_crawler.mapper.saveFace"
        session.insert(statement, face)
        session.commit()
      }
    } finally {
      session.close()
    }
  }

  private def isFaceExistFromDB(face: Face, session: SqlSession): Boolean = {
    val statement = "ren-ren_crawler.mapper.isFaceExist"
    val exist = session.selectOne[String](statement, face)
    session.commit()
    exist != null
  }

  def photoForFace(id: Int): String = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.photoForFace"
      val name = session.selectOne[String](statement, id)
      session.commit()
      name
    } finally {
      session.close()
    }
  }

  def fix(): List[Int] = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.fix"
      import scala.collection.JavaConversions.asScalaBuffer
      val names:mutable.Buffer[Int] = session.selectList[Int](statement)
      session.commit()
      names.toList
    } finally {
      session.close()
    }
  }

  def delete(id: Int) = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.delete"
      session.delete(statement, id)
      session.commit()
    } finally {
      session.close()
    }
  }

  def main(args: Array[String]):Unit = {
    val localFile = face.Util.subFiles(new File("D:/work/photos-true/first/photos")).toList ++
      face.Util.subFiles(new File("D:/work/photos-true/first/photos-repeat")).toList
    val faceFile = face.Util.subFiles(new File("D:/work/photos-true/first/faces")).toList ++
      face.Util.subFiles(new File("D:/work/photos-true/first/faces-repeat")).toList
    val totalFile = face.Util.subFiles(new File("D:/work/photos-true/total")).toList
    val downs =
      localFile.map(_.getName).filter(_.endsWith(".jpg")).map(_.takeWhile(_ != '.').toInt).toSet ++
      totalFile.map(_.getName).filter(_.endsWith(".jpg")).map(_.takeWhile(_ != '-').toInt).toSet ++
      faceFile.map(_.getName).filter(_.endsWith(".jpg")).map(_.takeWhile(_ != '-').toInt).toSet
    val different = fix().toSet diff downs
    println(different)

    val wilDelP = localFile.filter(_.getName.endsWith(".jpg")).filter(fileName => different.contains(fileName.getName.takeWhile(_ != '.').toInt))
    val wilDelF = faceFile.filter(_.getName.endsWith(".jpg")).filter(fileName => different.contains(fileName.getName.takeWhile(_ != '-').toInt))
    val wilDelT = totalFile.filter(_.getName.endsWith(".jpg")).filter(fileName => different.contains(fileName.getName.takeWhile(_ != '-').toInt))
    println(wilDelP)
    println(wilDelF)
    println(wilDelT)

    val sc: Scanner = new Scanner(System.in)
    if (sc.hasNext) {
      different.foreach(delete)
      wilDelP.foreach(_.delete)
      wilDelF.foreach(_.delete)
      wilDelT.foreach(_.delete)
    }
  }
}
