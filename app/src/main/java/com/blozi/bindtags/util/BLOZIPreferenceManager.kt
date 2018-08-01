package com.blozi.bindtags.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.blozi.bindtags.security.RSAUtil

import java.util.HashSet

/**
 * Created by 骆长涛
 */

class BLOZIPreferenceManager private constructor(context: Context) {

//    var isShowProducts: String
//        get() = sPreferences!!.getString(WebServiceUrl, DEFAULT_WEB_SERVICE)
//        set(webServiceUrl) {
//            val editor = sPreferences!!.cancelEdit()
//            editor.putString(PROJECT_PATH, DEFAULT_PROJECT_PATH_STR)
//            editor.putString(WebServiceUrl, webServiceUrl)
//            editor.apply()
//        }
    /**
     *
     *自动绑定
     * */
    var autoBind:Boolean
        get() = sPreferences!!.getBoolean(AUTOBIND,true)
        set(value) {
            val editor = sPreferences!!.edit()
            editor.putBoolean(AUTOBIND,value)
            editor.apply()
        }
    /**
     * 自动上传
     * */
    var autoImport:Boolean
        get() = sPreferences!!.getBoolean(AUTOIMPORT,true)
        set(value) {
            val editor = sPreferences!!.edit()
            editor.putBoolean(AUTOIMPORT,value)
            editor.apply()
        }
    /**
     * 自动上传
     * */
    var importAction:Int
        get() = sPreferences!!.getInt(ImportTagActionSelection,0)
        set(value) {
            val editor = sPreferences!!.edit()
            editor.putInt(ImportTagActionSelection,value)
            editor.apply()
        }

    var importForceTag:Boolean
        get() = sPreferences!!.getBoolean(ImportTagForceScanTags,false)
        set(value) {
            val editor = sPreferences!!.edit()
            editor.putBoolean(ImportTagForceScanTags,value)
            editor.apply()
        }
    /**
     * 设置Web的URL地址
     *
     * @description 设置项目的URL地址
     */
    var webServiceUrl: String
        get() = sPreferences!!.getString(WebServiceUrl, DEFAULT_WEB_SERVICE)
        set(webServiceUrl) {
            val editor = sPreferences!!.edit()
            editor.putString(PROJECT_PATH, DEFAULT_PROJECT_PATH_STR)
            editor.putString(WebServiceUrl, webServiceUrl)
            editor.apply()
        }

    /**
     * 获取完整项目路径
     */
    val compliteURL: String
        get() {
            val burl = sPreferences!!.getString(WebServiceUrl, DEFAULT_WEB_SERVICE)
            val aftUrl = sPreferences!!.getString(PROJECT_PATH, DEFAULT_PROJECT_PATH_STR)
            return burl!! + aftUrl!!
        }

    /**
     * 管理员以上权限
     */
    val isAdminPlus: Boolean
        get() {
            var yes = sPreferences!!.getBoolean(IS_ADMIN, false)
            if (!yes) yes = sPreferences!!.getBoolean(IS_Super_ADMIN, false)
            return yes
        }

    /**
     * 根据存储信息确认用户角色是不是管理员
     */
    val isAdmin: Boolean
        get() = sPreferences!!.getBoolean(IS_ADMIN, false)

    /**
     * 根据存储信息确认用户角色是不是超级管理员
     */
    val isSuperAdmin: Boolean
        get() = sPreferences!!.getBoolean(IS_Super_ADMIN, false)

    /**
     * 获取用户名
     */
    val userName: String?
        get() = sPreferences!!.getString(USER_NAME, null)

    /**
     * 获取密码
     */
    /**
     * 设置密码
     */
    var password: String?
        get() = sPreferences!!.getString(PASSWORD, "")
        set(password) {
            val editor = sPreferences!!.edit()
            editor.putString(PASSWORD, password)
            editor.apply()
        }

    /**
     * 获取登录名
     */
    /**
     * 设置登录名
     */
    var loginid: String?
        get() = sPreferences!!.getString(LOGINID, "")
        set(loginId) {
            val editor = sPreferences!!.edit()
            editor.putString(LOGINID, loginId)
            editor.apply()
        }
    /**
     * 获取用户ID
     */
    /**
     * 设置用户ID
     */
    var userid: String?
        get() = sPreferences!!.getString(USERID, null)
        set(userId) {
            val editor = sPreferences!!.edit()
            editor.putString(USERID, userId)
            editor.apply()
        }

    val newApkUrl: String
        get() = webServiceUrl + APK_Path + sPreferences!!.getString(key_app_APKFileName, "")

    val isLocal: Boolean
        get() = sPreferences!!.getBoolean(Local_isLocal, false)

    var localIPPort: String?
        get() = sPreferences!!.getString(Local_IPPort, "")
        set(localIPPort) {
            val editor = sPreferences!!.edit()
            editor.putString(Local_IPPort, localIPPort)
            editor.apply()
        }


    init {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        initData()
    }

    fun clearLoginMsg() {
        val burl = sPreferences!!.getString(WebServiceUrl, DEFAULT_WEB_SERVICE)
        //        String aftUrl = sPreferences.getString(this.PROJECT_PATH, DEFAULT_PROJECT_PATH_STR  );
        val editor = sPreferences!!.edit()
        editor.clear().commit()
        webServiceUrl = burl
    }

    /**
     * 获取完整项目路径
     */
    fun getImageURL(imageId: String): String {
        val burl = sPreferences!!.getString(WebServiceUrl, DEFAULT_WEB_SERVICE)
        return burl + imagePath + imageId
    }


    /**
     * 设置角色代码 ，并且确认是否是管理员
     */
    fun setCodeId(codeId: String) {
        val editor = sPreferences!!.edit()
        editor.putString(CODEID, codeId)
        if (ADMIN_CODE == codeId)
            editor.putBoolean(IS_ADMIN, true)
        else
            editor.putBoolean(IS_ADMIN, false)
        if (Super_ADMIN_CODE == codeId)
            editor.putBoolean(IS_Super_ADMIN, true)
        else
            editor.putBoolean(IS_Super_ADMIN, false)
        editor.apply()
    }

    /**
     * 设置用户名
     */
    fun setUseName(userName: String) {
        val editor = sPreferences!!.edit()
        editor.putString(USER_NAME, userName)
        editor.apply()
    }


    fun setCommonValue(key: String, value: String) {
        val editor = sPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getCommonValue(key: String): String? {
        return sPreferences!!.getString(key, null)
    }


    fun setIsLocal(isLocal: Boolean?) {
        val editor = sPreferences!!.edit()
        editor.putBoolean(Local_isLocal, isLocal!!)
        editor.apply()
    }

    var localUserName: String
        set (localUserName: String) {
            val editor = sPreferences!!.edit()
            editor.putString(Local_UserName, localUserName)
            editor.apply()
        }
        get () {
            return sPreferences!!.getString(Local_UserName, "")
        }
    var localPassword: String
        set(localPassword: String) {
            val editor = sPreferences!!.edit()
            editor.putString(Local_Password, localPassword)
            editor.apply()
        }
        get() {
            return sPreferences!!.getString(Local_Password, "")
        }
    var scanBarcodeUpdateIP:String
        set(ip:String){
            val editor = sPreferences!!.edit()
            editor.putString(ScanBarcodeUpdate_IP, ip)
            editor.apply()
        }
        get(){
            return sPreferences!!.getString(ScanBarcodeUpdate_IP,"47.100.50.216");
        }
    var scanBarcodeUpdatePort:String
        set(ip:String){
            val editor = sPreferences!!.edit()
            editor.putString(ScanBarcodeUpdate_Port, ip)
            editor.apply()
        }
        get(){
            return sPreferences!!.getString(ScanBarcodeUpdate_Port,"35005");
        }

    var editable:Boolean
        set(editable:Boolean){
            val editor = sPreferences!!.edit()
            editor.putBoolean(EDITABLE, editable)
            editor.apply()
        }
        get(){
            return sPreferences!!.getBoolean(EDITABLE,false)
        }
    var lastLogin:Int
        set(login:Int){
            var editor =  sPreferences!!.edit()
            editor.putInt(currentSystem,login)
            editor.apply()
        }
        get(){
            return sPreferences!!.getInt(currentSystem, OnlineSystem)
        }
    fun pushOnlineAccount() {
        addOnlineAccount(loginid, password)
    }
    fun pushLocalAccount() {
        addLocalAccount(localUserName,localPassword)
    }
    fun addOnlineAccount(loginid: String?, password: String?) {
        if (!TextUtils.isEmpty(loginid) && !TextUtils.isEmpty(password)) {
            val acounts = sPreferences!!.getStringSet(Onling_Accounts, HashSet<String>())
            removeOnLineAccount(loginid)
            var account = StringBuffer(loginid).append(cut_cut_cut).append(password).toString()
            acounts!!.add(RSAUtil.encryptionPub(account))
            val editor = sPreferences!!.edit()
            editor.putStringSet(Onling_Accounts, acounts)
            editor.apply()
        }
    }

    fun addLocalAccount(loginid: String?, password: String?) {
        if (!TextUtils.isEmpty(loginid) && !TextUtils.isEmpty(password)) {
            val acounts = sPreferences!!.getStringSet(Local_Accounts, HashSet<String>())
            removeOnLineAccount(loginid)
            var account = StringBuffer(loginid).append(cut_cut_cut).append(password).toString()
            acounts!!.add(RSAUtil.encryptionPub(account))
            val editor = sPreferences!!.edit()
            editor.putStringSet(Local_Accounts, acounts)
            editor.apply()
        }
    }

    fun getOnlineAccounts(): Array<Array<String>> {
        val acounts = sPreferences!!.getStringSet(Onling_Accounts, HashSet())
        val list = ArrayList<Array<String>>()
        val iterator = acounts.iterator()
        for (str in iterator) {
            val str1 = RSAUtil.decryPri(str)
            val account = str1.split(cut_cut_cut)
            if (account.size == 2) {
                list.add(account.toTypedArray())
            }
        }
        return list.toTypedArray()
    }

    fun getLocalAccounts(): Array<Array<String>> {
        val acounts = sPreferences!!.getStringSet(Local_Accounts, HashSet())
        val list = ArrayList<Array<String>>()
        val iterator = acounts.iterator()
        for (str in iterator) {
            val str1 = RSAUtil.decryPri(str)
            val account = str1.split(cut_cut_cut)
            if (account.size == 2) {
                list.add(account.toTypedArray())
            }
        }
        return list.toTypedArray()
    }

    fun removeOnLineAccount(loginId: String?) {
        val acounts = sPreferences!!.getStringSet(Onling_Accounts, HashSet())
        var beremove: String? = null
        val iterator = acounts.iterator()
        for (str in iterator) {
            val str1 = RSAUtil.decryPri(str)
            val account = str1.split(cut_cut_cut)
            if (loginId.equals(account[0])) beremove = str
        }
        acounts.remove(beremove)
    }

    fun removeLocalAccount(loginId: String?) {
        val acounts = sPreferences!!.getStringSet(Local_Accounts, HashSet())
        var beremove: String? = null
        val iterator = acounts.iterator()
        for (str in iterator) {
            val str1 = RSAUtil.decryPri(str)
            val account = str1.split(cut_cut_cut)
            if (loginId.equals(account[0])) beremove = str
        }
        acounts.remove(beremove)
    }
    companion object {
        private var sPreferences: SharedPreferences? = null
        private var sBLOZIPerferenceManager: BLOZIPreferenceManager? = null
        private const val currentSystem = "CurrentSystem"
        const val OnlineSystem = 0
        const val LocalSystem = 1
        private const val PASSWORD = "password"
        private const val CODEID = "codeId"
        private const val IS_ADMIN = "isAdmin"
        private const val ADMIN_CODE = "7002"
        private const val Super_ADMIN_CODE = "7001"
        private const val IS_Super_ADMIN = "isSuperAdmin"
        private const val USER_NAME = "user_name"
        private const val LOGINID = "loginId"
        private const val USERID = "userId"
        private const val WebServiceUrl = "webServiceUrl"
        private const val PROJECT_PATH = "projectPath"
        private const val DEFAULT_WEB_SERVICE = "http://4.1.5.6:3500"
        private const val DEFAULT_PROJECT_PATH_STR = "/MarketWebService/marketController/appService.do"
        const val APK_Path = "/MarketWebService/APK/"
        private const val imagePath = "/MarketWebService/rackController/executeAjax.do?action=getGoodsImage&original=true&goodsImageId="
        public const val key_app_UpdateTime = "UpdateTime"
        const val key_app_UpdateContent = "UpdateContent"
        const val key_app_UpdateContentCNS = "UpdateContentCNS"
        const val key_app_UpdateContentCNT = "UpdateContentCNT"
        const val key_app_APKFileName = "APKFileName"
        const val key_app_VersionName = "VersionName"
        const val key_app_VersionCode = "VersionCode"

        private const val Local_isLocal = "isLocal"
        private const val Local_IPPort = "LocalIPPort"
        private const val Local_UserName = "LocalUserName"
        private const val Local_Password = "LocalPassword"
        private const val cut_cut_cut = "(￣ε(#￣)☆╰╮o(￣▽￣///)"
        //        private final const  val 公钥="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJQWrOaSn1AYbfHhkXn1Bubv+4WQuPeu/irqscRULJlweypMOaRx7Va3UbKq72rBLsEEpSnC4gVP8n00t4SASUB0oxiYSLIv2XneWUj3jTPrn7Pnfir9uhAITkcX8WZcTgGpi3rupZlqnt+J92m5Q6grmKsjZ8koHKp4O9j8f8WQIDAQAB"
//        private final const val 私钥="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIlBas5pKfUBht8eGRefUG5u/7hZC4967+KuqxxFQsmXB7Kkw5pHHtVrdRsqrvasEuwQSlKcLiBU/yfTS3hIBJQHSjGJhIsi/Zed5ZSPeNM+ufs+d+Kv26EAhORxfxZlxOAamLeu6lmWqe34n3ablDqCuYqyNnySgcqng72Px/xZAgMBAAECgYAf0jTK6gK6NMJqE/ZJTbw9/in3OV2+xAe9jDVLdptHVWaJej2HFRoG7MTXqYuq0LYKWLSCdocTI+GMACatGjVFLI2+XReLAJDJUpMNMBjsqbW9R7v9MZAF6Cpz7XnbTMUXapGeQoUYvX7lcrFyCs6W9XX6GB7ot+lWS+MRaIR/SQJBANnKadCR3+AKBobW9K1zXOIzgoYOhMTHcB2wObInkfix08m4HdNe7SDLZK5ghQcy6KchaDUxAJJdA0kfg88PpL8CQQChVfIzcKDywUIdJmtDADn2/ObRqdCH9DiQ31BWlHaIOl62ShgNbYNDf2RujoZZkT3XMqUzCdnVlTCvv32GhqznAkALm5s6L5w0D7S7WRefawenUsGBLaLr7glOu8bHdQ51p9y24qZMTsHqvox2MtTERnVZ+xDXzO3P3z8+lXxr+9R3AkBrm6ewJ68yOzSdvMJOLhh7ZLOPpHQyOfKArnInTV7B/iyt6T2htymWG6IhG28Azp8wqdcF88wXaSrQDijrUWiBAkBZiZ4KnTH1CvyEJMZ1AQHccqUzGxIB/9Alee0r7UKiNYCHqT1pMHqEoxxjNF8H7l/v9g7EltQTdF1rp6Kc2MvN"
        const val Onling_Account_LoginIds = "Onling_Account_LoginIds"
        const val Onling_Account_Passwords = "Onling_Account_Passwords"
        private const val Onling_Accounts = "Onling_Accounts"
        private const val Local_Accounts = "Local_Accounts"

        private const val ScanBarcodeUpdate_IP = "ScanBarcodeUpdate_IP"
        private const val ScanBarcodeUpdate_Port = "ScanBarcodeUpdate_Port"
        private const val EDITABLE = "Editable"
        private const val AUTOBIND = "AUTOBIND"
        private const val AUTOIMPORT = "AUTOIMPORT"
        private const val ImportTagForceScanTags = "ImportTagForceScanTags"
        private const val ImportTagActionSelection = "ImportTagActionSelection"
        private const val AutoLogin ="AutoLogin"
        fun getInstance(context: Context): BLOZIPreferenceManager? {
            if (sBLOZIPerferenceManager == null) {
                sBLOZIPerferenceManager = BLOZIPreferenceManager(context)
                initData()
            }
            return sBLOZIPerferenceManager
        }
        /**
         * @authon 骆长涛
         * @description 初始化数据
         */
        private fun initData() {
            val editor = sPreferences!!.edit()
            editor.putString(PROJECT_PATH, DEFAULT_PROJECT_PATH_STR)
            editor.apply()
        }


    }


}
