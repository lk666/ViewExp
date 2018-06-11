package lk.cn.com.newbanner.views

import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.commit451.nativestackblur.NativeStackBlur
import kotlinx.android.synthetic.main.activity_views.*
import lk.cn.com.newbanner.R


class ViewsActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_views)

        Thread({
            val res = resources
            val bmp = BitmapFactory.decodeResource(res, R.mipmap.hh)
            val bm = NativeStackBlur.process(bmp, 20)
            bmp.recycle()
            val d = BitmapDrawable(res, bm)
            tv72.post {
                tv72.background = d
            }

        }).start()

        tv81.setOnClickListener {
            tv81.isEnabled = false
            tv82.isEnabled = true
        }
        tv82.setOnClickListener {
            tv82.isEnabled = false
            tv81.isEnabled = true
        }
    }


}
