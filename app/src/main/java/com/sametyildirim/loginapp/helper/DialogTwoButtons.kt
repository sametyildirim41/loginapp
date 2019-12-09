package com.sametyildirim.loginapp.helper

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import com.sametyildirim.loginapp.R
import kotlinx.android.synthetic.main.dialog_two_buttons.*

class DialogTwoButtons(activity: Activity)
{
   private val dialogSametYildirimDesign = Dialog(activity, R.style.CustomDialog)

    interface OnClickDialogButtonListener{
        fun onClickLeftButton(dialogCode : String)
        fun onClickRightButton(dialogCode : String)
    }

    /* cancelButton ile leftButton aynı butondur.
     * Birlikte kullanılamaz.
     * cancelButton dolu geldiğinde leftButton'u daima ezer.
     * */
    fun createDialog(dialogCode : String, title: String, message: String, leftButton: String, rightButton: String, cancelButton: String, listener: OnClickDialogButtonListener){

        dialogSametYildirimDesign.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSametYildirimDesign.setContentView(R.layout.dialog_two_buttons)
        dialogSametYildirimDesign.setCancelable(true)
        dialogSametYildirimDesign.setOnShowListener {

            if(title.isEmpty()) dialogSametYildirimDesign.txtTitle.visibility = View.INVISIBLE else dialogSametYildirimDesign.txtTitle.text = title
            if(message.isEmpty()) dialogSametYildirimDesign.txtMessage.visibility = View.INVISIBLE else dialogSametYildirimDesign.txtMessage.text = message
            if(leftButton.isEmpty()) dialogSametYildirimDesign.btnLeft.visibility = View.INVISIBLE else dialogSametYildirimDesign.btnLeft.text = leftButton
            if(rightButton.isEmpty()) dialogSametYildirimDesign.btnRight.visibility = View.INVISIBLE else dialogSametYildirimDesign.btnRight.text = rightButton
            if(!cancelButton.isEmpty()) { dialogSametYildirimDesign.btnLeft.visibility = View.VISIBLE
                dialogSametYildirimDesign.btnLeft.text = cancelButton }


            dialogSametYildirimDesign.btnRight.setOnClickListener { listener.onClickRightButton(dialogCode) }
            if(!cancelButton.isEmpty()) dialogSametYildirimDesign.btnLeft.setOnClickListener { closeDialog() }
            else dialogSametYildirimDesign.btnLeft.setOnClickListener { listener.onClickLeftButton(dialogCode) }

        }
        dialogSametYildirimDesign.show()
 }

    private fun closeDialog()
    {
        dialogSametYildirimDesign.dismiss()
    }

}

