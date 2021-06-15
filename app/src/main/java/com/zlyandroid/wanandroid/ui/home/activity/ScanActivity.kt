package com.zlyandroid.wanandroid.ui.home.activity

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.zlyandroid.wanandroid.R
import com.zlyandroid.wanandroid.base.BaseMvpActivity
import com.zlyandroid.wanandroid.ui.home.presenter.ScanPresenter
import com.zlyandroid.wanandroid.ui.home.view.ScanView
import com.zlyandroid.wanandroid.util.LogUtil
import com.zlyandroid.wanandroid.util.PictureSelector
import com.zlyandroid.wanandroid.util.bitmap.BitmapUtils
import com.zlylib.mypermissionlib.MyPermission
import com.zlylib.mypermissionlib.RequestListener
import com.zlylib.mypermissionlib.RuntimeRequester
import kotlinx.android.synthetic.main.activity_scan.*



/**
 * @author zhangliyang
 * @date 2020/2/26
 */
class ScanActivity : BaseMvpActivity<ScanPresenter>(), ScanView {

    companion object {
        private const val REQ_CODE_PERMISSION_CAMERA = 1
        private const val REQ_CODE_PERMISSION_ALBUM = 2
        private const val REQ_CODE_SELECT_PIC = 3

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, ScanActivity::class.java)
            context.startActivity(intent)
        }
    }

    enum class Mode {
        SCAN, ALBUM
    }

    private var mRuntimeRequester: RuntimeRequester? = null
    private var tvTipAnim: Animator? = null
    private var mode = Mode.SCAN



    override fun createPresenter(): ScanPresenter {
       return  ScanPresenter()
    }


    override fun getLayoutID(): Int {
       return R.layout.activity_scan
    }


    override fun initView() {
        llTip.visibility = View.INVISIBLE
        ivTorch.visibility = View.INVISIBLE
        ivAlbum.setOnClickListener {
            startModeAlbum()
        }
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        if (mode == Mode.SCAN) {
            startScan()
        }
    }

    override fun onPause() {
        super.onPause()
        stopScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTipAnim()
    }

    private fun startModeScan() {
        mode = Mode.SCAN
        startScan()
    }

    private fun startScan() {
        mRuntimeRequester = MyPermission.with(context)
                .runtime(REQ_CODE_PERMISSION_CAMERA)
                .permissions(Manifest.permission.CAMERA)
                .onBeforeRequest { _, executor ->
                    showTip("扫码二维码/条码需要相机权限", "点击申请", View.OnClickListener {
                        hideTip()
                        executor.execute()
                    })
                }
                .onBeenDenied { _, executor ->
                    showTip("拒绝相机权限将无法扫码", "重新申请", View.OnClickListener {
                        hideTip()
                        executor.execute()
                    })
                }
                .onGoSetting { _, executor ->
                    showTip("相机权限已被拒绝", "去设置", View.OnClickListener {
                        hideTip()
                        executor.execute()
                    })
                }
                .request(object : RequestListener {
                    override fun onSuccess() {
                    }

                    override fun onFailed() {
                        showTip("无法获取相机权限", "点击获取", View.OnClickListener {
                            hideTip()
                            startModeScan()
                        })
                    }
                })
    }

    private fun stopScan() {
    }

    private fun startModeAlbum() {
        mode = Mode.ALBUM
        stopScan()
        startAlbum()
    }

    private fun startAlbum() {
        mRuntimeRequester = MyPermission.with(context)
                .runtime(REQ_CODE_PERMISSION_ALBUM)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onBeforeRequest { _, executor ->
                    showTip("选择图片需要存储权限", "点击申请", View.OnClickListener {
                        hideTip()
                        executor.execute()
                    }, "返回扫码", View.OnClickListener {
                        hideTip()
                        executor.cancel()
                    })
                }
                .onBeenDenied { _, executor ->
                    showTip("拒绝存储权限将无法选择图片", "重新申请", View.OnClickListener {
                        hideTip()
                        executor.execute()
                    }, "返回扫码", View.OnClickListener {
                        hideTip()
                        executor.cancel()
                    })
                }
                .onGoSetting { _, executor ->
                    showTip("存储权限已被拒绝", "去设置", View.OnClickListener {
                        hideTip()
                        executor.execute()
                    }, "返回扫码", View.OnClickListener {
                        hideTip()
                        executor.cancel()
                    })
                }
                .request(object : RequestListener {
                    override fun onSuccess() {
                        PictureSelector.select(this@ScanActivity, REQ_CODE_SELECT_PIC)
                    }

                    override fun onFailed() {
                        startModeScan()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_PERMISSION_CAMERA -> {
                mRuntimeRequester?.onActivityResult(requestCode)
            }
            REQ_CODE_PERMISSION_ALBUM -> {
                mRuntimeRequester?.onActivityResult(requestCode)
            }
            REQ_CODE_SELECT_PIC -> {
                PictureSelector.result(resultCode, data)?.let {
                    BitmapUtils.getBitmapFromUri(context, it)?.let { bitmap ->

                    } ?: run {
                        showTip("打开图片失败", "返回扫码", View.OnClickListener {
                            hideTip()
                            startModeScan()
                        })
                    }
                } ?: run {
                    startModeScan()
                }
            }
        }
    }



    private fun onAlbumQRCodeSuccess(result: String?) {
        LogUtil.d("ScanActivity"+"result=$result")
        vibrate()
       // UrlOpenUtils.with(result).open(context)
        finish()
    }



    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    private fun cancelTipAnim() {
        tvTipAnim?.cancel()
        tvTipAnim = null
    }

    private fun showTip(text: String, btnSure: String, onSure: View.OnClickListener, btnCancel: String? = null, onCancel: View.OnClickListener? = null) {
        llTip ?: return
        tvTipText.text = text
        tvTipBtnSure.text = btnSure
        if (btnCancel.isNullOrEmpty()) {
            tvTipBtnCancel.visibility = View.GONE
        } else {
            tvTipBtnCancel.visibility = View.VISIBLE
            tvTipBtnCancel.text = btnCancel
            tvTipBtnCancel.setOnClickListener(onCancel)
        }
        llTip.setOnClickListener(onSure)
        cancelTipAnim()
        tvTipAnim = ObjectAnimator.ofFloat(llTip, "alpha", 0F, 1F).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    llTip ?: return
                    llTip.visibility = View.VISIBLE
                }
            })
            start()
        }
    }

    private fun hideTip() {
        llTip ?: return
        cancelTipAnim()
        if (llTip.visibility != View.VISIBLE) {
            return
        }
        tvTipText.text = ""
        tvTipBtnSure.text = ""
        tvTipBtnCancel.text = ""
        tvTipBtnCancel.visibility = View.GONE
        tvTipBtnCancel.setOnClickListener(null)
        llTip.setOnClickListener(null)
        tvTipAnim = ObjectAnimator.ofFloat(llTip, "alpha", 1F, 0F).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    llTip ?: return
                    llTip.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    llTip ?: return
                    llTip.visibility = View.VISIBLE
                }
            })
            start()
        }
    }


}