package lk.cn.com.newbanner.recycle

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import lk.cn.com.newbanner.R


/**
 * 卡券Item
 */
public class ItemAdapter(data: ArrayList<Item>) : BaseQuickAdapter<Item, BaseViewHolder>(R.layout
        .item_recycle, data) {

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
}