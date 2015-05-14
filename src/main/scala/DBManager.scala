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


  // save ren-ren user
  def saveUser(user: User): Unit = {
    if(isUserExist(user)) ()
    else {
      val session: SqlSession = sessionFactory.openSession()
      try {
        val statement = "ren-ren_crawler.mapper.saveUser"
        session.insert(statement, user)
        session.commit()
      } finally {
        session.close()
      }
    }
  }

  // check is user exist?
  private def isUserExist(user: User): Boolean = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.isUserExist"
      val exist = session.selectOne(statement, user).asInstanceOf[Int]
      session.commit()
      exist > 0
    } finally {
      session.close()
    }
  }

  // return all users
  def allUsers() = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.allUsers"
      import scala.collection.JavaConversions.asScalaBuffer
      val users: mutable.Buffer[User] = session.selectList(statement).asInstanceOf[java.util.ArrayList[User]]
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

  // save photo info to database
  def savePhoto(photo: Photo): Unit = {
    if(isPhotoExist(photo)) ()
    else {
      val session: SqlSession = sessionFactory.openSession()
      try {
        val statement = "ren-ren_crawler.mapper.savePhoto"
        session.insert(statement, photo)
        session.commit()
      } finally {
        session.close()
      }
    }
  }

  // check is photo exist
  private def isPhotoExist(photo: Photo): Boolean = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.isPhotoExist"
      val exist = session.selectOne(statement, photo).asInstanceOf[Int]
      session.commit()
      exist > 0
    } finally {
      session.close()
    }
  }

  // return all photo info
  def allNotSavePhotos() = {
    val session: SqlSession = sessionFactory.openSession()
    try {
      val statement = "ren-ren_crawler.mapper.allNotSavePhotos"
      import scala.collection.JavaConversions.asScalaBuffer
      val photos: mutable.Buffer[Photo] = session.selectList(statement).asInstanceOf[java.util.ArrayList[Photo]]
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
      val photos: mutable.Buffer[Photo] = session.selectList(statement,age).asInstanceOf[java.util.ArrayList[Photo]]
      session.commit()
      photos
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

  def main(args: Array[String]):Unit = {
    println(getPhotoById(1))
  }
}
