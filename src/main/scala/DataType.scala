import scala.beans.BeanProperty

/**
 * Created by li-wei on 2015/4/15.
 */
class User(@BeanProperty val nickName: String, @BeanProperty val birth: Int,
           @BeanProperty val home: String) {

  override def toString = s"User($nickName, $birth, $home)"
}
