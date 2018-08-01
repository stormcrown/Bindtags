package com.blozi.bindtags.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.content.Context
import android.os.Bundle

class BloziAccountAuthenticator : AbstractAccountAuthenticator {

    private var context :Context?=null
    private constructor(context: Context?) : super(context) {
        this.context = context
    }
    companion object {
        private var bloziAccountAuthenticator:BloziAccountAuthenticator?=null
        fun getInstance(context: Context?) :BloziAccountAuthenticator{
            return bloziAccountAuthenticator ?:BloziAccountAuthenticator(context)
        }
    }
    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startAddAccountSession(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        return super.startAddAccountSession(response, accountType, authTokenType, requiredFeatures, options)
    }

    override fun startUpdateCredentialsSession(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        return super.startUpdateCredentialsSession(response, account, authTokenType, options)
    }

    override fun isCredentialsUpdateSuggested(response: AccountAuthenticatorResponse?, account: Account?, statusToken: String?): Bundle {
        return super.isCredentialsUpdateSuggested(response, account, statusToken)
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAccountFromCredentials(response: AccountAuthenticatorResponse?, account: Account?, accountCredentials: Bundle?): Bundle {
        return super.addAccountFromCredentials(response, account, accountCredentials)
    }

    override fun getAccountCredentialsForCloning(response: AccountAuthenticatorResponse?, account: Account?): Bundle {
        return super.getAccountCredentialsForCloning(response, account)
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccountRemovalAllowed(response: AccountAuthenticatorResponse?, account: Account?): Bundle {
        return super.getAccountRemovalAllowed(response, account)
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun finishSession(response: AccountAuthenticatorResponse?, accountType: String?, sessionBundle: Bundle?): Bundle {
        return super.finishSession(response, accountType, sessionBundle)
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}