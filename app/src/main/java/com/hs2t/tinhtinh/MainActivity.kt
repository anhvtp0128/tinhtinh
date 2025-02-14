package com.hs2t.tinhtinh

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import nl.dionsegijn.konfetti.xml.KonfettiView

class MainActivity : AppCompatActivity() {

    private lateinit var textamount: TextView
    private lateinit var textdatetime: TextView
    private lateinit var textmemo: TextView

    private lateinit var viewKonfetti: KonfettiView

    // Receiver để nhận thông báo
    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == "com.hs2t.tinhtinh.NOTIFICATION_POSTED") {
                textamount.setText(intent.getStringExtra("amount"))
                textdatetime.setText(intent.getStringExtra("datetime"))
                textmemo.setText(intent.getStringExtra("memo"))

                viewKonfetti.start(Party(
                    speed = 0f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    position = Position.Relative(0.5, 0.3),
                    emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
                ))
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        textamount = findViewById(R.id.textamount)
        textdatetime = findViewById(R.id.textdatetime)
        textmemo = findViewById(R.id.textmemo)
        viewKonfetti = findViewById(R.id.konfettiView)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Đăng ký BroadcastReceiver
        val filter = IntentFilter("com.hs2t.tinhtinh.NOTIFICATION_POSTED")
        registerReceiver(notificationReceiver, filter)


        // Giữ màn hình luôn sáng
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký Receiver
        unregisterReceiver(notificationReceiver)

        // Bỏ cờ giữ màn hình sáng khi Activity bị hủy
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}