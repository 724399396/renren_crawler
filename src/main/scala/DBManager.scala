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

  // check is user exist?
  private def isUserExist(user: User): Boolean = {
    val statement = "ren-ren_crawler.mapper.isUserExist"
    val exist = session.selectOne(statement, user).asInstanceOf[Int]
    session.commit()
    exist > 0
  }

  // return all users
  def allUsers() = {
    val statement = "ren-ren_crawler.mapper.allUsers"
    import scala.collection.JavaConversions.asScalaBuffer
    val users: mutable.Buffer[User] = session.selectList(statement).asInstanceOf[java.util.ArrayList[User]]
    session.commit()
    users
  }

  // if has fetch, then set isFetch true
  def changeUserIsFetch(user: User) = {
    val statement = "ren-ren_crawler.mapper.changeUserIsFetch"
    session.update(statement, user)
    session.commit()
  }

  // save photo info to database
  def savePhoto(photo: Photo): Unit = {
    if(isPhotoExist(photo)) ()
    else {
      val statement = "ren-ren_crawler.mapper.savePhoto"
      session.insert(statement, photo)
      session.commit()
    }
  }

  // check is photo exist
  private def isPhotoExist(photo: Photo): Boolean = {
    val statement = "ren-ren_crawler.mapper.isPhotoExist"
    val exist = session.selectOne(statement, photo).asInstanceOf[Int]
    session.commit()
    exist > 0
  }

  // return all photo info
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

  // if has fetch, then set isFetch true
  def changePhotoIsFetch(photo: Photo) = {
    val statement = "ren-ren_crawler.mapper.changePhotoIsFetch"
    session.update(statement, photo)
    session.commit()
  }

  def fixWhere(user: User): Unit = {
    val statement = "ren-ren_crawler.mapper.fixWhere"
    session.update(statement, user)
    session.commit()
  }

  def fixPhotoWhere(user: User) = {
    val statement = "ren-ren_crawler.mapper.fixPhotoWhere"
    session.update(statement, user)
    session.commit()
  }

  def main(args: Array[String]):Unit = {
    changeUserIsFetch(new User("程文化", 2001, "南京大学", null, null, "http://photo.renren.com/photo/407349737/album-500143900/v7"))
  }
}
