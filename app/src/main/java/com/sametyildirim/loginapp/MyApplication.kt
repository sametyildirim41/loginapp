package com.sametyildirim.loginapp

import android.app.Application
import com.sametyildirim.loginapp.helper.RealmManager
import io.realm.Realm





 class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

//         Initialize Realm. Should only be done once when the application starts.
        Realm.init(this)
        RealmManager.initializeRealmConfig()

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            val channelId = "default_notification_channel_id"
//            val channelName = "General announcements"
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager!!.createNotificationChannel(NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW))
//        }



        // new code ***

        // Kullanıcı şifresini tutacağımız için şifreleme gereği duymuştuk. Ancak yaptığımız araştırmalar neticesinde güvenlik olarak içimiz rahatlamadı.
        // Bu sebeple kullanıcı şifresini tutmayacağız. Diğer veriler önemsiz olduğundan şifreleme mantıklarına da ihtiyacımız kalmadı.

//        val realmEncryptionHelper = RealmEncryptionHelper.initHelper(this, getString(R.string.app_name))
//
//// Config Realm
//        Realm.init(this)
//        val config = RealmConfiguration.Builder()
//                .name("realm_encrypt.realm")
//                .encryptionKey(realmEncryptionHelper.encryptKey) // Call realmEncryptionHelper to get encrypt key
//                .build()
//        Realm.setDefaultConfiguration(config)

    }

}