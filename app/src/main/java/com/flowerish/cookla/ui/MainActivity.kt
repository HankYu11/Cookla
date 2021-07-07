package com.flowerish.cookla.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flowerish.cookla.R
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
            .addOnSuccessListener {
                if (it != null) {
                    Timber.d("got dynamic link $it")
                }
            }
            .addOnFailureListener{
                Timber.e("dynamic link error $it")
            }

    }
}