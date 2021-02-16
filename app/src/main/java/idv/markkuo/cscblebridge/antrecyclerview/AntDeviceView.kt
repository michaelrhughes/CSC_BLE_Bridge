package idv.markkuo.cscblebridge.antrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import idv.markkuo.cscblebridge.R
import idv.markkuo.cscblebridge.service.ant.AntDevice

class AntDeviceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var nameView: TextView

    init {
        inflate(context, R.layout.ant_list_item, this)
        layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    fun doInflation(viewGroup: ViewGroup): AntDeviceView {
        nameView = findViewById(R.id.ant_device_name)
        return this
    }

    fun bind(antDevice: AntDevice) {
        nameView.text = antDevice.deviceName
    }
}