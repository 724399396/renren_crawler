import scala.beans.BeanProperty

/**
 * Created by li-wei on 2015/4/15.
 */
class User(@BeanProperty val id: Int, @BeanProperty val nickName: String, @BeanProperty val birth: Int,
           @BeanProperty val whereFrom: String, @BeanProperty val home: String, @BeanProperty val albumHome: String,
            @BeanProperty val avatarAlbum: String) {
  def this() {
    this(0,null,0,null,null,null,null)
  }

  override def toString = s"User($id, $nickName, $birth, $whereFrom, $home, $albumHome, $avatarAlbum)"
}

class Photo(@BeanProperty val id: Int, @BeanProperty val nickName: String, @BeanProperty val age: Int,
             @BeanProperty val whereFrom: String,@BeanProperty val avatarUrl: String) {
  def this() {
    this(0,null,0,null,null)
  }

  override def toString = s"Photo($id, $nickName, $age, $whereFrom, $avatarUrl)"
}
