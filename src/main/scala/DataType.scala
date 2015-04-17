import scala.beans.BeanProperty

/**
 * Created by li-wei on 2015/4/15.
 */
class User(@BeanProperty val nickName: String, @BeanProperty val birth: Int,
           @BeanProperty val home: String, @BeanProperty val albumHome: String,
            @BeanProperty val avatarAlbum: String) {
  def this() {
    this(null,0,null,null,null)
  }
  override def toString = s"User($nickName, $birth, $home, $albumHome, $avatarAlbum)"
}

class Photo(@BeanProperty val nickName: String, @BeanProperty val age: Int,
             @BeanProperty val avatarUrl: String) {
  def this() {
    this(null,0,null)
  }

  override def toString = s"Photo($nickName, $age, $avatarUrl)"
}
