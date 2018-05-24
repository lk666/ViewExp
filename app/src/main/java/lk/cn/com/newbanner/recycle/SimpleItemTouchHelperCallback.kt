package lk.cn.com.newbanner.recycle

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * rv的item touch的callback
 * Created by lk on 2018/5/24.
 */
class SimpleItemTouchHelperCallback(val adapter: IItemTouchHelper) : ItemTouchHelper.Callback() {

    /**
     * 返回可以滑动的方向
     */
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP.or(ItemTouchHelper.DOWN).or(ItemTouchHelper.LEFT)
                .or(ItemTouchHelper.RIGHT) //允许上下左右的拖动
        val swipeFlags = ItemTouchHelper.LEFT  //只允许从右向左侧滑
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * 当用户拖动一个Item进行上下移动从旧的位置到新的位置的时候会调用该方法，在该方法内，我们可以调用Adapter的notifyItemMoved方法来交换两个ViewHolder的位置，最后返回true，表示被拖动的ViewHolder已经移动到了目的位置
     */
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        adapter.onItemMove(viewHolder?.adapterPosition ?: -1, target?.adapterPosition ?: -1)
        return true
    }

    /**
     * 当用户左右滑动Item达到删除条件时，会调用该方法，一般手指触摸滑动的距离达到RecyclerView宽度的一半时，再松开手指，此时该Item会继续向原先滑动方向滑过去并且调用onSwiped方法进行删除，否则会反向滑回原来的位置。
     * 如果在onSwiped方法内我们没有进行任何操作，即不删除已经滑过去的Item，那么就会留下空白的地方，因为实际上该ItemView还占据着该位置，只是移出了我们的可视范围内罢了。
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        adapter.onItemDismiss(viewHolder?.adapterPosition?:-1)
    }

    /**
     * 该方法返回true时，表示支持长按拖动，即长按ItemView后才可以拖动，我们遇到的场景一般也是这样的。默认是返回true。
     */
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * 该方法返回true时，表示如果用户触摸并左右滑动了View，那么可以执行滑动删除操作，即可以调用到onSwiped()方法。默认是返回true。
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    /**
     * 从静止状态变为拖拽或者滑动的时候会回调该方法，参数actionState表示当前的状态。
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.itemView?.alpha = 0.5f
    }

    /**
     * 当用户操作完毕某个item并且其动画也结束后会调用该方法，一般我们在该方法内恢复ItemView的初始状态，防止由于复用而产生的显示错乱问题。
     */
    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)
        viewHolder?.itemView?.alpha = 1.0f
    }

    /**
     * 我们可以在这个方法内实现我们自定义的交互规则或者自定义的动画效果。 todo
     */
    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}