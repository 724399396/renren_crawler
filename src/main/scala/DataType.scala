import scala.beans.BeanProperty

/**
 * Created by li-wei on 2015/4/15.
 */
class User(@BeanProperty val id: Int, @BeanProperty val nickName: String, @BeanProperty val birth: Int,
           @BeanProperty val whereFrom: String, @BeanProperty val home: String, @BeanProperty val albumHome: String,
            @BeanProperty val avatarAlbum: String, @BeanProperty val uid: Long) {
  def this() {
    this(0,null,0,null,null,null,null,0)
  }

  override def toString = s"User($id, $nickName, $birth, $whereFrom, $home, $albumHome, $avatarAlbum, $uid)"
}

class Photo(@BeanProperty val id: Int, @BeanProperty val nickName: String, @BeanProperty val age: Int,
             @BeanProperty val whereFrom: String,@BeanProperty val avatarUrl: String,
              @BeanProperty val hashId: Int) {
  def this() {
    this(0,null,0,null,null,0)
  }

  override def toString = s"Photo($id, $nickName, $age, $whereFrom, $avatarUrl, $hashId)"
}
