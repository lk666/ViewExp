package lk.cn.com.newbanner.recycle

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import lk.cn.com.newbanner.R
import java.util.*




/**
 * 卡券Item
 */
public class ItemAdapter(data: ArrayList<Item>) : BaseQuickAdapter<Item, BaseViewHolder>(R.layout
        .item_recycle, data), IItemTouchHelper {

    override fun convert(helper: BaseViewHolder?, item: Item?) {
        var tv = helper?.getView<TextView>(R.id.tv)
        tv?.setBackgroundColor(item?.color ?: 0)
        tv?.text = item?.txt
        if (tv?.height != item?.height)
        {
            var lp = tv?.layoutParams
            lp?.height = item?.height
        }

//        helper?.addOnClickListener(R.id.tv)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition >= 0 && toPosition >= 0 && fromPosition != toPosition &&
                fromPosition < data.size && toPosition < data.size) {

            if (fromPosition < toPosition) {
                //分别把中间所有的 item 的位置重新交换
                for (i in fromPosition until toPosition) {
                    Collections.swap(data, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(data, i, i - 1)
                }
            }

            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onItemDismiss(position: Int) {
        if (position > -1 && position < data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}