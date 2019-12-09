package com.sametyildirim.loginapp.helper

import android.app.Activity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.sametyildirim.loginapp.R

class ToastNotice {
    private var toast: Toast? = null
    private var activity: Activity

    enum class ToastType {
        TRUE, FALSE, WARNING
    }

    constructor(activity: Activity, message: String, type: ToastType) {

        this.activity = activity
        val inflater = activity.layoutInflater
        val notice = inflater.inflate(
            R.layout.toast_notice,
                activity.findViewById(R.id.layoutNotice) as? ViewGroup)
        val txtNotice = notice.findViewById(R.id.txtNotice) as TextView
        val imgNotice = notice.findViewById(R.id.imgNotice) as ImageView
        when (type) {
            ToastType.TRUE -> imgNotice.setImageResource(R.drawable.toast_image_true)
            ToastType.FALSE -> imgNotice.setImageResource(R.drawable.toast_image_false)
            ToastType.WARNING -> imgNotice.setImageResource(R.drawable.toast_image_warning)
        }

        txtNotice.text = message
        activity.runOnUiThread {
            toast = Toast(activity.applicationContext)
            toast!!.setGravity(Gravity.CENTER, 0, 50)
            toast!!.duration = Toast.LENGTH_SHORT
            toast!!.view = notice
            toast!!.show()
        }

    }

}