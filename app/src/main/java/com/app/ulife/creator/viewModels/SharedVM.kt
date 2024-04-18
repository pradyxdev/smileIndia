/*
 * *
 *  * Created by Prady on 3/24/23, 10:37 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/23/23, 6:55 PM
 *
 */

package com.app.ulife.creator.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.ulife.creator.helpers.Coroutines
import com.app.ulife.creator.models.CommonUserIdReq
import com.app.ulife.creator.models.EmptyRequest
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
import com.app.ulife.creator.models.productList.GetProductListReq
import com.app.ulife.creator.models.recharge.DoRechargeNewReq
import com.app.ulife.creator.models.shippingDetails.add.AddShippiDetailsReq
import com.app.ulife.creator.models.updateProfile.UpdateProfileReq
import com.app.ulife.creator.models.wallet.WalletReq
import com.app.ulife.creator.models.withdrawManage.withdrawReport.WithdrawReportReq
import com.app.ulife.creator.models.withdrawManage.withdrawalReq.StartWithdrawalReq
import com.app.ulife.creator.repositories.SharedRepo

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
    fun getOrderHistory(request: OrderHistoryReq) {
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
    fun getOrderItems(request: OrderItemsReq) {
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
}