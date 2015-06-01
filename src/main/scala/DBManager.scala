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
    val session: SqlSession = sessionFactory.openSession()
    try {
      if(!isUserExist(user, session)) {
        val statement = "ren-ren_crawler.mapper.saveUser"
        session.insert(statement, user)
        session.commit()
      }
    } finally {
      session.close()
    }
  }

  // check is user exist?
  private def isUserExist(user: User, session: SqlSession): Boolean = {
    val statement = "ren-ren_crawler.mapper.isUserExist"
    val exist = session.selectOne[Int](statement, user)
    session.commit()
    exist > 0
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

  def main(args: Array[String]):Unit = {
    fixUserEntryYear {
      val old = allNotHaveEntryYearUsers()(2)
      println(old)
      val enrty = DoubleCheck.getUserEntryYear(old, 2015)
      new User(old.id, enrty)
    }
  }
}
