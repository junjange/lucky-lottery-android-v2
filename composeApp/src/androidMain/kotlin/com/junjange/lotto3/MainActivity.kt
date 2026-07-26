package com.junjange.lotto3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.zxing.integration.android.IntentIntegrator
import junjange.core.designsystem.theme.LottoTheme
import junjange.shared.navigation.LotteryNavHost
import junjange.core.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private val scanLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onScanResult(result.resultCode, result.data)
        }

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInterstitialAd()

        setContent {
            LottoTheme {
                LotteryNavHost(
                    onLaunchQrScanner = ::initiateScan,
                )
            }
        }
    }

    private fun initiateScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt(getString(R.string.qr_scanner_hint))
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.setOrientationLocked(false)
        scanLauncher.launch(integrator.createScanIntent())
    }

    private fun onScanResult(
        resultCode: Int,
        data: Intent?,
    ) {
        val result = IntentIntegrator.parseActivityResult(resultCode, data)

        if (result != null) {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this@MainActivity)
                mInterstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            result.contents?.let {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.contents))
                                startActivity(intent)
                            }
                            mInterstitialAd = null
                            setupInterstitialAd()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            mInterstitialAd = null
                        }
                    }
            }
        }
    }

    private fun setupInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        val adUnitId =
            if (BuildConfig.DEBUG) {
                "ca-app-pub-3940256099942544/1033173712"
            } else {
                BuildConfig.FULL_SCREEN_AD_UNIT_ID
            }

        InterstitialAd.load(
            this,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            },
        )
    }
}
