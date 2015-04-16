import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.{SqlSession, SqlSessionFactoryBuilder, SqlSessionFactory}

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
    val statement = "ren-ren_crawler.mapper.saveUser"
    session.insert(statement, user)
    session.commit()
  }
}
