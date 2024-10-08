/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 6:55 PM
 *
 */

package com.app.smile.india.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.smile.india.helpers.Coroutines
import com.app.smile.india.models.CommonUserIdReq
import com.app.smile.india.models.EmptyRequest
import com.app.smile.india.models.UserIdRequest
import com.app.smile.india.models.addToCart.AddToCartReq
import com.app.smile.india.models.affiliates.details.GetAffiliatesDetailsReq
import com.app.smile.india.models.affiliates.list.GetAffiliatesReq
import com.app.smile.india.models.bbpsRecharge.mobPlan.GetPlanListReq
import com.app.smile.india.models.bbpsRecharge.mobileLookup.GetMobileLookupReq
import com.app.smile.india.models.bbpsRecharge.operator.GetOperatorReq
import com.app.smile.india.models.bbpsRecharge.operatorLocal.GetOperatorLocalReq
import com.app.smile.india.models.checkout.CheckoutOrderReq
import com.app.smile.india.models.deleteCart.DeleteCartReq
import com.app.smile.india.models.epinManage.epinGenerate.EpinGenerateReq
import com.app.smile.india.models.epinManage.epinReport.EPinReportReq
import com.app.smile.india.models.epinManage.epinReqReport.EpinReqReportReq
import com.app.smile.india.models.epinManage.epinRequest.EpinRequestReq
import com.app.smile.india.models.epinManage.epinTopup.EpinTopupReq
import com.app.smile.india.models.epinManage.epinTransfer.EpinTransferReq
import com.app.smile.india.models.epinManage.epinTransferReport.EpinTransferReportReq
import com.app.smile.india.models.fcm.SaveFcmReq
import com.app.smile.india.models.fundsManage.add.AddFundsReq
import com.app.smile.india.models.fundsManage.addFundReport.AddFundsReportReq
import com.app.smile.india.models.fundsManage.transferFund.GetTransferFundsReq
import com.app.smile.india.models.fundsManage.transferFundReport.GetTransFundReportReq
import com.app.smile.india.models.getCat.GetCategoryReq
import com.app.smile.india.models.getCity.GetCityReq
import com.app.smile.india.models.getSaleItems.GetSaleItemsReq
import com.app.smile.india.models.getSaleReport.GetSaleReportReq
import com.app.smile.india.models.getSubCategory.GetSubCategoryReq
import com.app.smile.india.models.networkManage.directTeam.GetDirectTeamReq
import com.app.smile.india.models.networkManage.downline.GetDownlineReq
import com.app.smile.india.models.networkManage.levelWise.GetLevelWiseReq
import com.app.smile.india.models.networkManage.teamBv.GetTeamBvReq
import com.app.smile.india.models.orderHistory.OrderHistoryReq
import com.app.smile.india.models.orderItems.OrderItemsReq
import com.app.smile.india.models.passwordManage.accPassword.AccountPasswordReq
import com.app.smile.india.models.passwordManage.changePassword.ChangePasswordReq
import com.app.smile.india.models.payoutManage.directIncome.GetDirectIncomeReq
import com.app.smile.india.models.payoutManage.ldc.report.GetLdcReportReq
import com.app.smile.india.models.payoutManage.rechargeIncome.GetRechargeIncomeReq
import com.app.smile.india.models.payoutManage.shoppingIncome.GetShoppingIncomeReq
import com.app.smile.india.models.paysprint.bbpsFetchBill.GetPsFetchBbpsReq
import com.app.smile.india.models.paysprint.bbpsPayBill.GetPsBbpsPayBillReq
import com.app.smile.india.models.paysprint.doPsRecharge.DoPsRechargeReq
import com.app.smile.india.models.paysprint.dthHlr.GetHlrDthInfoReq
import com.app.smile.india.models.paysprint.fastag.psFastagRecharge.DoPsFastagRechargeReq
import com.app.smile.india.models.paysprint.fastag.psFetchFastag.GetPsFetchFastagReq
import com.app.smile.india.models.paysprint.history.mobHistory.GetMobRechargeHistReq
import com.app.smile.india.models.paysprint.hlrCheck.HlrCheckReq
import com.app.smile.india.models.paysprint.lpg.fetchBill.GetPsFetchLpgBillReq
import com.app.smile.india.models.paysprint.lpg.fetchDistrict.GetLpgDistrictReq
import com.app.smile.india.models.paysprint.lpg.payLpgBill.GetPsLpgPayBillReq
import com.app.smile.india.models.paysprint.psMobPlanList.GetPsMobPlanListReq
import com.app.smile.india.models.productList.GetProductListReq
import com.app.smile.india.models.recharge.DoRechargeNewReq
import com.app.smile.india.models.shippingDetails.add.AddShippiDetailsReq
import com.app.smile.india.models.updateProfile.UpdateProfileReq
import com.app.smile.india.models.wallet.WalletReq
import com.app.smile.india.models.withdrawManage.withdrawReport.WithdrawReportReq
import com.app.smile.india.models.withdrawManage.withdrawalReq.StartWithdrawalReq
import com.app.smile.india.repositories.SharedRepo

class SharedVM(private val repository: SharedRepo) : ViewModel() {

    var latitude: MutableLiveData<Double> = MutableLiveData()
    var logitude: MutableLiveData<Double> = MutableLiveData()
    var fullAddress: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        error.value = Error()
    }

    fun setLatitude(latitude: Double) {
        this.latitude.postValue(latitude)
    }

    fun setLongitude(longitude: Double) {
        logitude.postValue(longitude)
    }

    fun setFullAddress(fullAddress: String) {
        this.fullAddress.postValue(fullAddress)
    }

    data class Error(var error: String? = null)

    // Rest APIS
    var saveFCM = MutableLiveData<String>()
    fun getSaveFCM(request: SaveFcmReq) {
        Coroutines.main {
            saveFCM.postValue(repository.saveFCM(request))
        }
    }

    var getUserDetails = MutableLiveData<String>()
    fun getUserDetails(request: CommonUserIdReq) {
        Coroutines.main {
            getUserDetails.postValue(repository.getUserDetails(request))
        }
    }

    var getBanner = MutableLiveData<String>()
    fun getBanner(request: EmptyRequest) {
        Coroutines.main {
            getBanner.postValue(repository.getBanner(request))
        }
    }

    var getCategories = MutableLiveData<String>()
    fun getCategories(request: GetCategoryReq) {
        Coroutines.main {
            getCategories.postValue(repository.getCategories(request))
        }
    }

    var emptyRequestCommon = MutableLiveData<String>()
    fun emptyRequestCommon(request: EmptyRequest) {
        Coroutines.main {
            emptyRequestCommon.postValue(repository.emptyRequestCommon(request))
        }
    }

    var userIdRequestCommon = MutableLiveData<String>()
    fun userIdRequestCommon(request: CommonUserIdReq) {
        Coroutines.main {
            userIdRequestCommon.postValue(repository.userIdRequestCommon(request))
        }
    }

    var getProductListReq = MutableLiveData<String>()
    fun getProductListReq(request: GetProductListReq) {
        Coroutines.main {
            getProductListReq.postValue(repository.getProductListReq(request))
        }
    }

    var getSubCategory = MutableLiveData<String>()
    fun getSubCategory(request: GetSubCategoryReq) {
        Coroutines.main {
            getSubCategory.postValue(repository.getSubCategory(request))
        }
    }

    var addToCart = MutableLiveData<String>()
    fun addToCart(request: AddToCartReq) {
        Coroutines.main {
            addToCart.postValue(repository.addToCart(request))
        }
    }

    var getCart = MutableLiveData<String>()
    fun getCart(request: CommonUserIdReq) {
        Coroutines.main {
            getCart.postValue(repository.getCart(request))
        }
    }

    var deleteCart = MutableLiveData<String>()
    fun deleteCart(request: DeleteCartReq) {
        Coroutines.main {
            deleteCart.postValue(repository.deleteCart(request))
        }
    }

    var getStateList = MutableLiveData<String>()
    fun getStateList(request: EmptyRequest) {
        Coroutines.main {
            getStateList.postValue(repository.getStateList(request))
        }
    }

    var getCityList = MutableLiveData<String>()
    fun getCityList(request: GetCityReq) {
        Coroutines.main {
            getCityList.postValue(repository.getCityList(request))
        }
    }

    var addShippingDetails = MutableLiveData<String>()
    fun addShippingDetails(request: AddShippiDetailsReq) {
        Coroutines.main {
            addShippingDetails.postValue(repository.addShippingDetails(request))
        }
    }

    var checkoutOrder = MutableLiveData<String>()
    fun checkoutOrder(request: CheckoutOrderReq) {
        Coroutines.main {
            checkoutOrder.postValue(repository.checkoutOrder(request))
        }
    }

    var getOrderHistory = MutableLiveData<String>()
    fun getOrderHistory(request: GetSaleReportReq) {
        Coroutines.main {
            getOrderHistory.postValue(repository.getOrderHistory(request))
        }
    }

    var getWalletBalance = MutableLiveData<String>()
    fun getWalletBalance(request: WalletReq) {
        Coroutines.main {
            getWalletBalance.postValue(repository.getWalletBalance(request))
        }
    }

    var getWalletHistory = MutableLiveData<String>()
    fun getWalletHistory(request: WalletReq) {
        Coroutines.main {
            getWalletHistory.postValue(repository.getWalletHistory(request))
        }
    }

    var getOrderItems = MutableLiveData<String>()
    fun getOrderItems(request: GetSaleItemsReq) {
        Coroutines.main {
            getOrderItems.postValue(repository.getOrderItems(request))
        }
    }

    var getOperator = MutableLiveData<String>()
    fun getOperator(request: GetOperatorReq) {
        Coroutines.main {
            getOperator.postValue(repository.getOperator(request))
        }
    }

    var getOperatorLocal = MutableLiveData<String>()
    fun getOperatorLocal(request: GetOperatorLocalReq) {
        Coroutines.main {
            getOperatorLocal.postValue(repository.getOperatorLocal(request))
        }
    }

    var doRecharge = MutableLiveData<String>()
    fun doRecharge(request: DoRechargeNewReq) {
        Coroutines.main {
            doRecharge.postValue(repository.doRecharge(request))
        }
    }

    var fetchBill = MutableLiveData<String>()
    fun fetchBill(request: DoRechargeNewReq) {
        Coroutines.main {
            fetchBill.postValue(repository.fetchBill(request))
        }
    }

    var getTransactionType = MutableLiveData<String>()
    fun getTransactionType(request: EmptyRequest) {
        Coroutines.main {
            getTransactionType.postValue(repository.getTransactionType(request))
        }
    }

    var getCircle = MutableLiveData<String>()
    fun getCircle(request: EmptyRequest) {
        Coroutines.main {
            getCircle.postValue(repository.getCircle(request))
        }
    }

    var getPlansList = MutableLiveData<String>()
    fun getPlansList(request: GetPlanListReq) {
        Coroutines.main {
            getPlansList.postValue(repository.getPlansList(request))
        }
    }

    var getDthPlansList = MutableLiveData<String>()
    fun getDthPlansList(request: GetPlanListReq) {
        Coroutines.main {
            getDthPlansList.postValue(repository.getDthPlansList(request))
        }
    }

    var doMobileLookUp = MutableLiveData<String>()
    fun doMobileLookUp(request: GetMobileLookupReq) {
        Coroutines.main {
            doMobileLookUp.postValue(repository.doMobileLookUp(request))
        }
    }

    var getDirectTeamHistory = MutableLiveData<String>()
    fun getDirectTeamHistory(request: GetDirectTeamReq) {
        Coroutines.main {
            getDirectTeamHistory.postValue(repository.getDirectTeamHistory(request))
        }
    }

    var getDownlineHistory = MutableLiveData<String>()
    fun getDownlineHistory(request: GetDownlineReq) {
        Coroutines.main {
            getDownlineHistory.postValue(repository.getDownlineHistory(request))
        }
    }

    var getLevelWiseHistory = MutableLiveData<String>()
    fun getLevelWiseHistory(request: GetLevelWiseReq) {
        Coroutines.main {
            getLevelWiseHistory.postValue(repository.getLevelWiseHistory(request))
        }
    }

    var getTeamBvHistory = MutableLiveData<String>()
    fun getTeamBvHistory(request: GetTeamBvReq) {
        Coroutines.main {
            getTeamBvHistory.postValue(repository.getTeamBvHistory(request))
        }
    }

    var addFunds = MutableLiveData<String>()
    fun addFunds(request: AddFundsReq) {
        Coroutines.main {
            addFunds.postValue(repository.addFunds(request))
        }
    }

    var getAddFundsReport = MutableLiveData<String>()
    fun getAddFundsReport(request: AddFundsReportReq) {
        Coroutines.main {
            getAddFundsReport.postValue(repository.getAddFundsReport(request))
        }
    }

    var getTransFundReport = MutableLiveData<String>()
    fun getTransFundReport(request: GetTransFundReportReq) {
        Coroutines.main {
            getTransFundReport.postValue(repository.getTransFundReport(request))
        }
    }

    var startFundTransfer = MutableLiveData<String>()
    fun startFundTransfer(request: GetTransferFundsReq) {
        Coroutines.main {
            startFundTransfer.postValue(repository.startFundTransfer(request))
        }
    }

    var getEpinReport = MutableLiveData<String>()
    fun getEpinReport(request: EPinReportReq) {
        Coroutines.main {
            getEpinReport.postValue(repository.getEpinReport(request))
        }
    }

    var epinTopup = MutableLiveData<String>()
    fun epinTopup(request: EpinTopupReq) {
        Coroutines.main {
            epinTopup.postValue(repository.epinTopup(request))
        }
    }

    var epinGenerate = MutableLiveData<String>()
    fun epinGenerate(request: EpinGenerateReq) {
        Coroutines.main {
            epinGenerate.postValue(repository.epinGenerate(request))
        }
    }

    var getTransferPinReport = MutableLiveData<String>()
    fun getTransferPinReport(request: EpinTransferReportReq) {
        Coroutines.main {
            getTransferPinReport.postValue(repository.getTransferPinReport(request))
        }
    }

    var epinTransfer = MutableLiveData<String>()
    fun epinTransfer(request: EpinTransferReq) {
        Coroutines.main {
            epinTransfer.postValue(repository.epinTransfer(request))
        }
    }

    var getDeduction = MutableLiveData<String>()
    fun getDeduction(request: EmptyRequest) {
        Coroutines.main {
            getDeduction.postValue(repository.getDeduction(request))
        }
    }

    var startWithdrawal = MutableLiveData<String>()
    fun startWithdrawal(request: StartWithdrawalReq) {
        Coroutines.main {
            startWithdrawal.postValue(repository.startWithdrawal(request))
        }
    }

    var getWithdrawReport = MutableLiveData<String>()
    fun getWithdrawReport(request: WithdrawReportReq) {
        Coroutines.main {
            getWithdrawReport.postValue(repository.getWithdrawReport(request))
        }
    }

    var getDirectIncomeHistory = MutableLiveData<String>()
    fun getDirectIncomeHistory(request: GetDirectIncomeReq) {
        Coroutines.main {
            getDirectIncomeHistory.postValue(repository.getDirectIncomeHistory(request))
        }
    }

    var getShoppingIncomeHistory = MutableLiveData<String>()
    fun getShoppingIncomeHistory(request: GetShoppingIncomeReq) {
        Coroutines.main {
            getShoppingIncomeHistory.postValue(repository.getShoppingIncomeHistory(request))
        }
    }

    var getRechargeIncomeHistory = MutableLiveData<String>()
    fun getRechargeIncomeHistory(request: GetRechargeIncomeReq) {
        Coroutines.main {
            getRechargeIncomeHistory.postValue(repository.getRechargeIncomeHistory(request))
        }
    }

    var getCompanyDetails = MutableLiveData<String>()
    fun getCompanyDetails(request: EmptyRequest) {
        Coroutines.main {
            getCompanyDetails.postValue(repository.getCompanyDetails(request))
        }
    }

    var epinRequest = MutableLiveData<String>()
    fun epinRequest(request: EpinRequestReq) {
        Coroutines.main {
            epinRequest.postValue(repository.epinRequest(request))
        }
    }

    var getEpinReqReport = MutableLiveData<String>()
    fun getEpinReqReport(request: EpinReqReportReq) {
        Coroutines.main {
            getEpinReqReport.postValue(repository.getEpinReqReport(request))
        }
    }

    var changePassword = MutableLiveData<String>()
    fun changePassword(request: AccountPasswordReq) {
        Coroutines.main {
            changePassword.postValue(repository.changePassword(request))
        }
    }

    var changeTransPassword = MutableLiveData<String>()
    fun changeTransPassword(request: ChangePasswordReq) {
        Coroutines.main {
            changeTransPassword.postValue(repository.changeTransPassword(request))
        }
    }

    var updateUserDetails = MutableLiveData<String>()
    fun updateUserDetails(request: UpdateProfileReq) {
        Coroutines.main {
            updateUserDetails.postValue(repository.updateUserDetails(request))
        }
    }

    var getLdcDates = MutableLiveData<String>()
    fun getLdcDates(request: EmptyRequest) {
        Coroutines.main {
            getLdcDates.postValue(repository.getLdcDates(request))
        }
    }

    var getLdcReport = MutableLiveData<String>()
    fun getLdcReport(request: GetLdcReportReq) {
        Coroutines.main {
            getLdcReport.postValue(repository.getLdcReport(request))
        }
    }

    // paysprint api's
    var getPsOperator = MutableLiveData<String>()
    fun getPsOperator(request: UserIdRequest) {
        Coroutines.main {
            getPsOperator.postValue(repository.getPsOperator(request))
        }
    }

    var hlrCheck = MutableLiveData<String>()
    fun hlrCheck(request: HlrCheckReq) {
        Coroutines.main {
            hlrCheck.postValue(repository.hlrCheck(request))
        }
    }

    var getPsMobPlanList = MutableLiveData<String>()
    fun getPsMobPlanList(request: GetPsMobPlanListReq) {
        Coroutines.main {
            getPsMobPlanList.postValue(repository.getPsMobPlanList(request))
        }
    }

    var doPsMobRecharge = MutableLiveData<String>()
    fun doPsMobRecharge(request: DoPsRechargeReq) {
        Coroutines.main {
            doPsMobRecharge.postValue(repository.doPsMobRecharge(request))
        }
    }

    var getPsBbpsOperator = MutableLiveData<String>()
    fun getPsBbpsOperator(request: UserIdRequest) {
        Coroutines.main {
            getPsBbpsOperator.postValue(repository.getPsBbpsOperator(request))
        }
    }

    var getLpgBbpsOperator = MutableLiveData<String>()
    fun getLpgBbpsOperator(request: UserIdRequest) {
        Coroutines.main {
            getLpgBbpsOperator.postValue(repository.getLpgBbpsOperator(request))
        }
    }

    var getLpgStateList = MutableLiveData<String>()
    fun getLpgStateList(request: EmptyRequest) {
        Coroutines.main {
            getLpgStateList.postValue(repository.getLpgStateList(request))
        }
    }

    var getLpgDistrictList = MutableLiveData<String>()
    fun getLpgDistrictList(request: GetLpgDistrictReq) {
        Coroutines.main {
            getLpgDistrictList.postValue(repository.getLpgDistrictList(request))
        }
    }

    var getPsFetchLpgOperator = MutableLiveData<String>()
    fun getPsFetchLpgOperator(request: GetPsFetchLpgBillReq) {
        Coroutines.main {
            getPsFetchLpgOperator.postValue(repository.getPsFetchLpgOperator(request))
        }
    }

    var getPsFetchBbpsOperator = MutableLiveData<String>()
    fun getPsFetchBbpsOperator(request: GetPsFetchBbpsReq) {
        Coroutines.main {
            getPsFetchBbpsOperator.postValue(repository.getPsFetchBbpsOperator(request))
        }
    }

    var getPsFastagOperator = MutableLiveData<String>()
    fun getPsFastagOperator(request: UserIdRequest) {
        Coroutines.main {
            getPsFastagOperator.postValue(repository.getPsFastagOperator(request))
        }
    }

    var getPsFetchFastagOperator = MutableLiveData<String>()
    fun getPsFetchFastagOperator(request: GetPsFetchFastagReq) {
        Coroutines.main {
            getPsFetchFastagOperator.postValue(repository.getPsFetchFastagOperator(request))
        }
    }

    var doPsFastagRecharge = MutableLiveData<String>()
    fun doPsFastagRecharge(request: DoPsFastagRechargeReq) {
        Coroutines.main {
            doPsFastagRecharge.postValue(repository.doPsFastagRecharge(request))
        }
    }

    var getMobRechargeHistory = MutableLiveData<String>()
    fun getMobRechargeHistory(request: GetMobRechargeHistReq) {
        Coroutines.main {
            getMobRechargeHistory.postValue(repository.getMobRechargeHistory(request))
        }
    }

    var getHlrDthInfo = MutableLiveData<String>()
    fun getHlrDthInfo(request: GetHlrDthInfoReq) {
        Coroutines.main {
            getHlrDthInfo.postValue(repository.getHlrDthInfo(request))
        }
    }

    var doPsBbpsBillPay = MutableLiveData<String>()
    fun doPsBbpsBillPay(request: GetPsBbpsPayBillReq) {
        Coroutines.main {
            doPsBbpsBillPay.postValue(repository.doPsBbpsBillPay(request))
        }
    }

    var doPsLpgBillPay = MutableLiveData<String>()
    fun doPsLpgBillPay(request: GetPsLpgPayBillReq) {
        Coroutines.main {
            doPsLpgBillPay.postValue(repository.doPsLpgBillPay(request))
        }
    }

    var getAffiliatesList = MutableLiveData<String>()
    fun getAffiliatesList(request: GetAffiliatesReq) {
        Coroutines.main {
            getAffiliatesList.postValue(repository.getAffiliatesList(request))
        }
    }

    var getAffiliatesDetails = MutableLiveData<String>()
    fun getAffiliatesDetails(request: GetAffiliatesDetailsReq) {
        Coroutines.main {
            getAffiliatesDetails.postValue(repository.getAffiliatesDetails(request))
        }
    }

    var passwordRecovery = MutableLiveData<String>()
    fun passwordRecovery(request: UserIdRequest) {
        Coroutines.main {
            passwordRecovery.postValue(repository.passwordRecovery(request))
        }
    }
}