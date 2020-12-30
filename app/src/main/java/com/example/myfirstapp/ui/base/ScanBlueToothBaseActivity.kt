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

        btnSave.setColorBackground(
            macSelect.isNotEmpty(),
            this,
            R.color.colorPrimary,
            R.color.colorPrimaryOpa
        )

        /*listView(linear, macSelect) {
            macSelect = it
            btnSave.setColorBackground(
                macSelect.isNotEmpty(),
                this,
                R.color.colorPrimary,
                R.color.colorPrimaryOpa)
        }
*/
        btnSave.setOnClickListener {
            PapersManager.macPrint = macSelect
            dialog.dismiss()
            returnAction(true)
        }

        btnTry.setOnClickListener {
            if (macSelect.isNotEmpty()) {
                testPrint(macSelect)
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


    private fun testPrint(mac: String) {
        val jsonAdapter by lazy {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            moshi.adapter<FeaturesJsonEntity>(FeaturesJsonEntity::class.java)
        }

        val featuresJsonEntity = jsonAdapter.fromJson(
            "{\"features\": [     {       \"text\": \"RIPLEY\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"TIENDAS POR DEPARTAMENTO RIPLEY S.A.\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CALLE LAS BEGONIAS 545-577\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"SAN ISIDRO - LIMA\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"RUC 20337564373\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CONSTANCIA DE EMISIÓN DEL\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"COMPROBANTE ELECTRÓNICO\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"20024/899 08/11/20 13:07\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"74354 VENDEDOR SUCUR: 20024\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text1\": \"Doc Cliente :\",       \"text2\": \" DNI 77177123\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"Nombre Cliente :\",       \"text2\": \" Gerardo Flores\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"eMail :\",       \"text2\": \" gerardo.gabriel.flores@gmail.com\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"textLeft\": \"2015250306817\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"POL MC DEGLOBO INX C\",       \"textRight\": \"179.85\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"3 X 59.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2030237401712\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"FLORERO PEQUEO OCRE \",       \"textRight\": \"99.95\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"1 X 99.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2044234672075\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"BOLSA REGULAR.      \",       \"textRight\": \"1.25\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"5 X 0.25\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"SUBTOTAL S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"text\": \"NRO DE UNIDADES: 9\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"TOTAL A PAGAR S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"MASTERCARD\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"525435****7677\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"ORDEN DE COMPRA: 14770282\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"value\": \"ee9818b027bcd29f0cdf880ee11729ed416723ab3894d446434d144d3ce6837e451df3a74059c989016dc34cb9ffc78e148bfbde64e270cee16c6ad4cdd5d376\",       \"tipo\": \"CODE_TEXT_QR\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"Tu boleta electronica sera enviada a\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"gerardo.gabriel.flores@gmail.com\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"CUT\"     }   ]}"
        )
        initPrint(
            mac,
            (featuresJsonEntity?.features as MutableList<CloseCartResponse.ClientVoucher>) as ArrayList
        )
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
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if((if(device!!.name == null)  "SIN NOMBRE" else device!!.name).toUpperCase().contains("BP60A")) {
                    if(mDeviceList.isEmpty()){
                        mDeviceList.add(
                            BlueToothDeviceData().apply {
                                this.name = if(device!!.name == null)  "SIN NOMBRE" else device!!.name
                                this.addressMac = device.address
                            }
                        )
                    } else {
                        var contains = false
                        for(e in mDeviceList) {
                            if(e.name == (if(device!!.name == null)  "SIN NOMBRE" else device!!.name)) {
                                contains = true
                                break
                            }
                        }
                        if(!contains) {
                            mDeviceList.add(
                                BlueToothDeviceData().apply {
                                    this.name = if(device!!.name == null)  "SIN NOMBRE" else device!!.name
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