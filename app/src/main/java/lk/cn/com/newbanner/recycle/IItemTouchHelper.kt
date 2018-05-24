package lk.cn.com.newbanner.recycle

/**
 * adapter实现的rv拖动接口
 * Created by lk on 2018/5/24.
 */
interface IItemTouchHelper {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}