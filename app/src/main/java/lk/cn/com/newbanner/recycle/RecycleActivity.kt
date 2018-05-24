package lk.cn.com.newbanner.recycle

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_recycle.*
import lk.cn.com.newbanner.R
import lk.cn.com.newbanner.ViewUtil.toast
import java.util.*

class RecycleActivity : FragmentActivity(), BaseQuickAdapter.OnItemClickListener {
    private val random = Random()

    private val adapter by lazy {
        ItemAdapter(data)
    }

    private var num = 0

    private val colorSet by lazy {
        listOf(resources.getColor(R.color.c_blue), resources.getColor(R.color.c_green),
                resources.getColor(R.color.c_pink), resources.getColor(R.color.c_purple))
    }
    val data by lazy {
        getRanDomDataList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle)

// todo ptr 与 ItemTouchHelper滑动冲突，在拉到顶端，向下拖动时
        ptr.setEnableLoadmoreWhenContentNotFull(true)
        ptr.setOnRefreshListener { getInitData() }
        ptr.setOnLoadmoreListener { getMore() }

        rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        rv.itemAnimator = DefaultItemAnimator()

        // 添加ItemTouchHelper
        val callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rv)

        adapter.onItemClickListener = this
        adapter.openLoadAnimation()
        adapter.bindToRecyclerView(rv)
        adapter.setEmptyView(R.layout.view_empty)

        btn_add.setOnClickListener {
            val i = getRanDomData()
            if (data.size > 0) {
                val pos = random.nextInt(data.size)
                data.add(pos, i)
                adapter.notifyItemInserted(pos)
            } else {
                data.add(i)
                adapter.notifyDataSetChanged()
            }
            toast("add ${i.txt}")
        }

        btn_delete.setOnClickListener {
            if (data.size < 1) {
                return@setOnClickListener
            }
            val pos = random.nextInt(data.size)
            val i = data[pos]
            data.removeAt(pos)
            adapter.notifyItemRemoved(pos)
            toast("delete ${i.txt}")
        }

        btn_move.setOnClickListener {
            if (data.size < 2) {
                return@setOnClickListener
            }
            val pos1 = random.nextInt(data.size)
            var pos2 = random.nextInt(data.size)
            while (pos1 == pos2) {
                pos2 = random.nextInt(data.size)
            }
            val i2 = data[pos2]
            val i1 = data[pos1]

            if (pos1 < pos2) {
                //分别把中间所有的 item 的位置重新交换
                for (i in pos1 until pos2) {
                    Collections.swap(data, i, i + 1)
                }
            } else {
                for (i in pos1 downTo pos2 + 1) {
                    Collections.swap(data, i, i - 1)
                }
            }

            adapter.notifyItemMoved(pos1, pos2)
            toast("move ${i1.txt} to ${i2.txt} position")
        }
    }

    private fun getMore() {
        ptr.postDelayed({
            val newData = getRanDomDataList()
            data.addAll(newData)
            adapter.notifyItemRangeInserted(data.size, newData.size)
            ptr.finishLoadmore()

            if (num > 50) {
                ptr.isEnableLoadmore = false
            }
        }, 1000)
    }

    private fun getInitData() {
        ptr.postDelayed(
                {
                    num = 0
                    ptr.isEnableLoadmore = true
                    data.clear()
                    data.addAll(getRanDomDataList())
                    adapter.notifyDataSetChanged()
                    ptr.finishRefresh()
                }, 1000)
    }

    private fun getRanDomDataList(): ArrayList<Item> {
        val count = random.nextInt(20)
        val d = arrayListOf<Item>()
        for (i in 0..count) {
            d.add(getRanDomData())
        }
        return d
    }

    private fun getRanDomData() = Item("${num++}", colorSet[random.nextInt(colorSet.size)],
            60 * random.nextInt(8) + 100)

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val item = adapter?.data?.get(position) as Item
        val curColor = item.color
        val index = random.nextInt(colorSet.size)
        var newColor = colorSet[index]
        item.color = if (newColor != curColor) newColor else colorSet[((index + 1) % colorSet.size)]

        adapter.notifyItemChanged(position)
    }

}
