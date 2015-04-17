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
  val session: SqlSession = sessionFactory.openSession()

  // save ren-ren user
  def saveUser(user: User): Unit = {
    if(isUserExist(user)) ()
    else {
      val statement = "ren-ren_crawler.mapper.saveUser"
      session.insert(statement, user)
      session.commit()
    }
  }

  private def isUserExist(user: User): Boolean = {
    val statement = "ren-ren_crawler.mapper.isUserExist"
    val exist = session.selectOne(statement, user).asInstanceOf[Int]
    session.commit()
    exist > 0
  }

  def allUsers() = {
    val statement = "ren-ren_crawler.mapper.allUsers"
    import scala.collection.JavaConversions.asScalaBuffer
    val users: mutable.Buffer[User] = session.selectList(statement).asInstanceOf[java.util.ArrayList[User]]
    session.commit()
    users
  }

  def savePhoto(photo: Photo): Unit = {
    if(isPhotoExist(photo)) ()
    else {
      val statement = "ren-ren_crawler.mapper.savePhoto"
      session.insert(statement, photo)
      session.commit()
    }
  }

  private def isPhotoExist(photo: Photo): Boolean = {
    val statement = "ren-ren_crawler.mapper.isPhotoExist"
    val exist = session.selectOne(statement, photo).asInstanceOf[Int]
    session.commit()
    exist > 0
  }

  def allPhotos() = {
    val statement = "ren-ren_crawler.mapper.allPhotos"
    import scala.collection.JavaConversions.asScalaBuffer
    val photos: mutable.Buffer[Photo] = session.selectList(statement).asInstanceOf[java.util.ArrayList[Photo]]
    session.commit()
    photos
  }

  def photosByAge(age: Int) = {
    val statement = "ren-ren_crawler.mapper.photosByAge"
    import scala.collection.JavaConversions.asScalaBuffer
    val photos: mutable.Buffer[Photo] = session.selectList(statement,age).asInstanceOf[java.util.ArrayList[Photo]]
    session.commit()
    photos
  }

  def main(args: Array[String]):Unit = {
    println(photosByAge(15))
  }
}
