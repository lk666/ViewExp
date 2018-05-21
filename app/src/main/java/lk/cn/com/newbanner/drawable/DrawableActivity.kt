package lk.cn.com.newbanner.drawable

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_drawable.*
import lk.cn.com.newbanner.R


class DrawableActivity : FragmentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)
        v_transition.setOnClickListener({
            (it.background as TransitionDrawable)?.startTransition(3000)
        })

        var map = HashMap<String, String>()
        map.get("sad")
        map.put("asdsa","asdsad")
    }
}