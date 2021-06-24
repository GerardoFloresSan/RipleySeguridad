package com.example.myfirstapp.ui.base

import android.Manifest
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.BlueToothDeviceData
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.adapter.DeviceAdapter
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.inflate
import com.example.myfirstapp.utils.setColorBackground
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_firma.*
import kotlin.collections.ArrayList

abstract class ScanBlueToothBaseActivity : PdfBaseActivity() {

    var blueAdapter: BluetoothAdapter? = null
    val mDeviceList = ArrayList<BlueToothDeviceData>()
    val pairedDevices: Set<BluetoothDevice>? = null
    val PERMISSION_LOCATION = 5678
    val REQUEST_CHECK_CODE = 8949
    val locationHelper by lazy {
        RipleyApplication.locationHelper
    }
    var builder: LocationSettingsRequest.Builder? = null

    fun initBlueToothScanPrint() {
        blueAdapter = BluetoothAdapter.getDefaultAdapter()
        blueAdapter?.startDiscovery()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
    }

    fun checkPermissionsLocation(): Boolean {
        val permissionState =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    fun checkAll(): Boolean {
        return checkPermissionsLocation() && checkPermissionsRead() && checkPermissionsCamera()
    }

    fun removeGPS() {
        try {
            locationHelper.stop()
        } catch (e: Exception) {
            Log.d("Error locationHelper", e.message.toString())
        }
    }

    fun gpsEnabled() {
        val request: LocationRequest = LocationRequest()
            .setFastestInterval(5000)
            .setInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder!!.build())

        result.addOnCompleteListener { task: Task<LocationSettingsResponse> ->
            try {
                task.getResult(ApiException::class.java)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this@ScanBlueToothBaseActivity,
                                REQUEST_CHECK_CODE
                            )
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                        } catch (ex: ClassCastException) {
                            ex
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                    else -> {
                        e
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocationPermission() {
        val permissionArrays = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (checkPermissionsLocation()) {
            toast("Permiso otorgado --- PERMISSION_LOCATION")
        } else {
            requestPermissions(permissionArrays, PERMISSION_LOCATION)
        }
    }

    fun enabledBluetooth() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        }
    }

    override fun onDestroy() {
        try {
            unregisterReceiver(mReceiver)
        } catch (e: Exception) {
            Log.d("error", "unregisterReceiver(mReceiver)")
        }
        super.onDestroy()
    }

    fun showBlueToothDevice(returnAction: (Boolean) -> Unit) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bluetooth)
        /*val linear: LinearLayoutCompat = dialog.findViewById<View>(R.id.linear_bluetooth) as LinearLayoutCompat*/
        val recyclerView: RecyclerView = dialog.findViewById<View>(R.id.recycler) as RecyclerView
        val lnlPrint: LinearLayoutCompat =
            dialog.findViewById<View>(R.id.lnl_print_empty) as LinearLayoutCompat
        var adapter: DeviceAdapter? = null

        val btnSave: AppCompatButton = dialog.findViewById<View>(R.id.btn_save) as AppCompatButton
        val btnTry: AppCompatButton = dialog.findViewById<View>(R.id.btn_try) as AppCompatButton
        val btnClose: AppCompatImageButton =
            dialog.findViewById<View>(R.id.btn_close) as AppCompatImageButton

        var macSelect = ""

        adapter = DeviceAdapter {
            PapersManager.macPrint2 = it.addressMac
            macSelect = it.addressMac
            adapter!!.notifyDataSetChanged()
            btnSave.setColorBackground(
                macSelect.isNotEmpty(),
                this,
                R.color.colorPrimary,
                R.color.colorPrimaryOpa
            )

        }
        recyclerView.removeAllViews()
        recyclerView.removeAllViewsInLayout()
        recyclerView.adapter = adapter
        adapter.data = mDeviceList

        lnlPrint.visibility = if (mDeviceList.isNotEmpty()) View.GONE else View.VISIBLE

        //TODO --> Acá poner la validación

        if (mDeviceList.isEmpty()) {
            btnTry.isEnabled = false
            btnTry.isClickable = false
            btnTry.isFocusable = false
            btnTry.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorBloc
                )
            )
        } else {
            btnTry.isEnabled = true
            btnTry.isClickable = true
            btnTry.isFocusable = true
            btnTry.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                )
            )
        }

        btnSave.setColorBackground(
            macSelect.isNotEmpty(),
            this,
            R.color.colorPrimary,
            R.color.colorPrimaryOpa
        )

        btnSave.setOnClickListener {
            if (macSelect.isNotEmpty()) {
                PapersManager.macPrint = macSelect
                dialog.dismiss()
                returnAction(true)
            }
        }

        btnTry.setOnClickListener {
            if (macSelect.isNotEmpty()) {
                testPrint(macSelect, true)
            }
        }

        btnClose.setOnClickListener {
            PapersManager.macPrint = ""
            dialog.dismiss()
            returnAction(false)
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.setCancelable(false)
        dialog.show()
    }


    private fun testPrint(mac: String, test: Boolean) {
        val jsonAdapter by lazy {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            moshi.adapter<FeaturesJsonEntity>(FeaturesJsonEntity::class.java)
        }
        val stringBuilder: StringBuilder by lazy {
            this.assets.open("json/test.json").use {
                val size = it.available()
                val buffer = ByteArray(size)
                it.read(buffer)
                it.close()
                val json = String(buffer, Charsets.UTF_8)

                StringBuilder(json)
            }
        }

        val featuresJsonEntity = jsonAdapter.fromJson(stringBuilder.toString())

                                                                /* {"clientVoucher": [{"text": "Impresora instalada con éxito","align": "CENTER","tipo": "TEXT"},{"tipo": "CUT"}]}**/

        initPrint(
            mac,
            (featuresJsonEntity?.features as MutableList<CloseCartResponse.ClientVoucher>) as ArrayList,
            test
        ) {

        }
    }


    private fun itemInflateView(
        mac: String,
        data: BlueToothDeviceData,
        linear: LinearLayoutCompat,
        returnAction: (String) -> Unit
    ): View {
        val itemBT = linear.inflate(R.layout.item_bluetooth)

        val lblName = itemBT.findViewById<AppCompatTextView>(R.id.name_bluetooth)
        val lblMac = itemBT.findViewById<AppCompatTextView>(R.id.mac_bluetooth)
        if (mac == data.addressMac) {
            itemBT.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreen))
        }

        lblName.text = data.name
        lblMac.text = data.addressMac
        itemBT.setOnClickListener {
            returnAction(data.addressMac)
        }
        return itemBT
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if ((if (device!!.name == null) "SIN NOMBRE" else device!!.name).toUpperCase()
                        .contains("BP60A")
                ) {
                    if (mDeviceList.isEmpty()) {
                        mDeviceList.add(
                            BlueToothDeviceData().apply {
                                this.name =
                                    if (device!!.name == null) "SIN NOMBRE" else device!!.name
                                this.addressMac = device.address
                            }
                        )
                    } else {
                        var contains = false
                        for (e in mDeviceList) {
                            if (e.name == (if (device!!.name == null) "SIN NOMBRE" else device!!.name)) {
                                contains = true
                                break
                            }
                        }
                        if (!contains) {
                            mDeviceList.add(
                                BlueToothDeviceData().apply {
                                    this.name =
                                        if (device!!.name == null) "SIN NOMBRE" else device!!.name
                                    this.addressMac = device.address
                                }
                            )
                        }
                    }
                }
                Log.i("BlueToothDeviceData", "${device!!.name} ${device.address}".trimIndent())
            }
        }
    }

    data class FeaturesJsonEntity(val features: List<CloseCartResponse.ClientVoucher>)
}