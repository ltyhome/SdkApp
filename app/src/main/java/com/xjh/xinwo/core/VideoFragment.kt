package com.xjh.xinwo.core

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.WorkerThread
import com.xjh.xinwo.R

abstract class VideoFragment:VMFragment(R.layout.fragment_svideo) {
    private val manager by lazy { mActivity.getSystemService(CameraManager::class.java) }
    private var cameraDevice:CameraDevice? = null
    private var frontCameraId:String? = null
    private var backCameraId:String? =null
    override fun onLazyBeforeView() {
        manager.cameraIdList.forEach {cameraId ->
            val cameraCharacteristics = manager.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_FRONT) {
                frontCameraId = cameraId
            } else if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                backCameraId = cameraId
            }
        }
    }

    override fun onLazyAfterView() {
        checkCameraPermission("应用程序未授权,无法使用摄像头!"){
            val cameraId = backCameraId ?: frontCameraId
            if (cameraId != null) {
               manager.openCamera(cameraId,object : CameraDevice.StateCallback() {
                   @WorkerThread
                   override fun onOpened(camera: CameraDevice) {
                       cameraDevice = camera
                   }
                   @WorkerThread
                   override fun onDisconnected(camera: CameraDevice) {
                       camera.close()
                   }
                   @WorkerThread
                   override fun onClosed(camera: CameraDevice) {
                       super.onClosed(camera)
                       camera.close()
                   }
                   @WorkerThread
                   override fun onError(camera: CameraDevice, error: Int) {
                       camera.close()
                       cameraDevice = null
                   }
               }, object :Handler(Looper.getMainLooper()){
                   override fun handleMessage(msg: Message) {

                   }
               })
            } else {
                throw RuntimeException("Camera id must not be null.")
            }
        }
    }

}