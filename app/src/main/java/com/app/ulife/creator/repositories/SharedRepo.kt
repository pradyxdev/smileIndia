/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/24/23, 10:36 AM
 *
 */

package com.app.ulife.creator.repositories

import com.app.ulife.creator.data.remote.MyApi
import com.app.ulife.creator.data.remote.SafeApiRequest
import com.app.ulife.creator.models.CommonUserIdReq
import com.app.ulife.creator.models.EmptyRequest
import com.app.ulife.creator.models.UserIdRequest
import com.app.ulife.creator.models.addToCart.AddToCartReq
import com.app.ulife.creator.models.bbpsRecharge.mobPlan.GetPlanListReq
import com.app.ulife.creator.models.bbpsRecharge.mobileLookup.GetMobileLookupReq
import com.app.ulife.creator.models.bbpsRecharge.operator.GetOperatorReq
import com.app.ulife.creator.models.bbpsRecharge.operatorLocal.GetOperatorLocalReq
import com.app.ulife.creator.models.checkout.CheckoutOrderReq
import com.app.ulife.creator.models.deleteCart.DeleteCartReq
import com.app.ulife.creator.models.epinManage.epinGenerate.EpinGenerateReq
import com.app.ulife.creator.models.epinManage.epinReport.EPinReportReq
import com.app.ulife.creator.models.epinManage.epinReqReport.EpinReqReportReq
import com.app.ulife.creator.models.epinManage.epinRequest.EpinRequestReq
import com.app.ulife.creator.models.epinManage.epinTopup.EpinTopupReq
import com.app.ulife.creator.models.epinManage.epinTransfer.EpinTransferReq
import com.app.ulife.creator.models.epinManage.epinTransferReport.EpinTransferReportReq
import com.app.ulife.creator.models.fcm.SaveFcmReq
import com.app.ulife.creator.models.fundsManage.add.AddFundsReq
import com.app.ulife.creator.models.fundsManage.addFundReport.AddFundsReportReq
import com.app.ulife.creator.models.fundsManage.transferFund.GetTransferFundsReq
import com.app.ulife.creator.models.fundsManage.transferFundReport.GetTransFundReportReq
import com.app.ulife.creator.models.getCat.GetCategoryReq
import com.app.ulife.creator.models.getCity.GetCityReq
import com.app.ulife.creator.models.networkManage.directTeam.GetDirectTeamReq
import com.app.ulife.creator.models.networkManage.downline.GetDownlineReq
import com.app.ulife.creator.models.networkManage.levelWise.GetLevelWiseReq
import com.app.ulife.creator.models.networkManage.teamBv.GetTeamBvReq
import com.app.ulife.creator.models.orderHistory.OrderHistoryReq
import com.app.ulife.creator.models.orderItems.OrderItemsReq
import com.app.ulife.creator.models.passwordManage.accPassword.AccountPasswordReq
import com.app.ulife.creator.models.passwordManage.changePassword.ChangePasswordReq
import com.app.ulife.creator.models.payoutManage.directIncome.GetDirectIncomeReq
import com.app.ulife.creator.models.payoutManage.ldc.report.GetLdcReportReq
import com.app.ulife.creator.models.payoutManage.rechargeIncome.GetRechargeIncomeReq
import com.app.ulife.creator.models.payoutManage.shoppingIncome.GetShoppingIncomeReq
import com.app.ulife.creator.models.paysprint.bbpsFetchBill.GetPsFetchBbpsReq
import com.app.ulife.creator.models.paysprint.bbpsPayBill.GetPsBbpsPayBillReq
import com.app.ulife.creator.models.paysprint.doPsRecharge.DoPsRechargeReq
import com.app.ulife.creator.models.paysprint.dthHlr.GetHlrDthInfoReq
import com.app.ulife.creator.models.paysprint.fastag.psFastagRecharge.DoPsFastagRechargeReq
import com.app.ulife.creator.models.paysprint.fastag.psFetchFastag.GetPsFetchFastagReq
import com.app.ulife.creator.models.paysprint.history.mobHistory.GetMobRechargeHistReq
import com.app.ulife.creator.models.paysprint.hlrCheck.HlrCheckReq
import com.app.ulife.creator.models.paysprint.psMobPlanList.GetPsMobPlanListReq
import com.app.ulife.creator.models.productList.GetProductListReq
import com.app.ulife.creator.models.recharge.DoRechargeNewReq
import com.app.ulife.creator.models.shippingDetails.add.AddShippiDetailsReq
import com.app.ulife.creator.models.updateProfile.UpdateProfileReq
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.withdrawManage.withdrawReport.WithdrawReportReq
import com.app.ulife.creator.models.withdrawManage.withdrawalReq.StartWithdrawalReq
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SharedRepo(
    private val api: MyApi
) : SafeApiRequest() {
    suspend fun saveFCM(request: SaveFcmReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, SaveFcmReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.saveFCM(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getUserDetails(request: CommonUserIdReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, CommonUserIdReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserDetails(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getBanner(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getBanner(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getCategories(request: GetCategoryReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetCategoryReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun emptyRequestCommon(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun userIdRequestCommon(request: CommonUserIdReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, CommonUserIdReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getProductListReq(request: GetProductListReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetProductListReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun addToCart(request: AddToCartReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, AddToCartReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getCart(request: CommonUserIdReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, CommonUserIdReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun deleteCart(request: DeleteCartReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, DeleteCartReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getStateList(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getCityList(request: GetCityReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetCityReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun addShippingDetails(request: AddShippiDetailsReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, AddShippiDetailsReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun checkoutOrder(request: CheckoutOrderReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, CheckoutOrderReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getOrderHistory(request: OrderHistoryReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, OrderHistoryReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getWalletBalance(request: WalletReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, WalletReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getWalletHistory(request: WalletReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, WalletReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getOrderItems(request: OrderItemsReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, OrderItemsReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getOperator(request: GetOperatorReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetOperatorReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getOperatorLocal(request: GetOperatorLocalReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetOperatorLocalReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun doRecharge(request: DoRechargeNewReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, DoRechargeNewReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.doRecharge(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun fetchBill(request: DoRechargeNewReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, DoRechargeNewReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.doRecharge(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getTransactionType(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getCircle(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getCircle(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPlansList(request: GetPlanListReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPlanListReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPlansList(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getDthPlansList(request: GetPlanListReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPlanListReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getDthPlansList(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun doMobileLookUp(request: GetMobileLookupReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetMobileLookupReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.doMobileLookUp(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getDirectTeamHistory(request: GetDirectTeamReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetDirectTeamReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getDownlineHistory(request: GetDownlineReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetDownlineReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getLevelWiseHistory(request: GetLevelWiseReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetLevelWiseReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getTeamBvHistory(request: GetTeamBvReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetTeamBvReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun addFunds(request: AddFundsReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, AddFundsReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getAddFundsReport(request: AddFundsReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, AddFundsReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getTransFundReport(request: GetTransFundReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetTransFundReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun startFundTransfer(request: GetTransferFundsReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetTransferFundsReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getEpinReport(request: EPinReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EPinReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun epinTopup(request: EpinTopupReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EpinTopupReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun epinGenerate(request: EpinGenerateReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EpinGenerateReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getTransferPinReport(request: EpinTransferReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EpinTransferReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun epinTransfer(request: EpinTransferReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EpinTransferReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getDeduction(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun startWithdrawal(request: StartWithdrawalReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, StartWithdrawalReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getWithdrawReport(request: WithdrawReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, WithdrawReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getDirectIncomeHistory(request: GetDirectIncomeReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetDirectIncomeReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getShoppingIncomeHistory(request: GetShoppingIncomeReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetShoppingIncomeReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getRechargeIncomeHistory(request: GetRechargeIncomeReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetRechargeIncomeReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getCompanyDetails(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun epinRequest(request: EpinRequestReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EpinRequestReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getEpinReqReport(request: EpinReqReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EpinReqReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun changePassword(request: AccountPasswordReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, AccountPasswordReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun changeTransPassword(request: ChangePasswordReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, ChangePasswordReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun updateUserDetails(request: UpdateProfileReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UpdateProfileReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getLdcDates(request: EmptyRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, EmptyRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getLdcReport(request: GetLdcReportReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetLdcReportReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    // paysprint api's
    suspend fun getPsOperator(request: UserIdRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UserIdRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPsOperator(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun hlrCheck(request: HlrCheckReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, HlrCheckReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.hlrCheck(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPsMobPlanList(request: GetPsMobPlanListReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPsMobPlanListReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPsMobPlanList(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun doPsMobRecharge(request: DoPsRechargeReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, DoPsRechargeReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.doPsMobRecharge(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPsBbpsOperator(request: UserIdRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UserIdRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPsBbpsOperator(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPsFetchBbpsOperator(request: GetPsFetchBbpsReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPsFetchBbpsReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPsFetchBbpsOperator(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPsFastagOperator(request: UserIdRequest): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, UserIdRequest::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPsFastagOperator(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getPsFetchFastagOperator(request: GetPsFetchFastagReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPsFetchFastagReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getPsFetchFastagOperator(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun doPsFastagRecharge(request: DoPsFastagRechargeReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, DoPsFastagRechargeReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.doPsFastagRecharge(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getMobRechargeHistory(request: GetMobRechargeHistReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetMobRechargeHistReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getUserRequest(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getHlrDthInfo(request: GetHlrDthInfoReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetHlrDthInfoReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.getHlrDthInfo(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun doPsBbpsBillPay(request: GetPsBbpsPayBillReq): String {
        var response = String()
        try {
            val jsonObject = Gson().toJson(request, GetPsBbpsPayBillReq::class.java)
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            response = apiRequest { api.doPsBbpsBillPay(body) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }
}