/*
 * *
 *  * Created by Prady on 4/8/23, 10:52 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 4/7/23, 3:05 PM
 *
 */

package com.app.ulife.creatoron.ui.userTypeActivities.customer.cartActivity.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.ulife.creatoron.R
import com.app.ulife.creatoron.adapters.ecom.AddressList2Adapter
import com.app.ulife.creatoron.databinding.FragmentCheckoutBinding
import com.app.ulife.creatoron.factories.SharedVMF
import com.app.ulife.creatoron.helpers.Constants
import com.app.ulife.creatoron.helpers.PreferenceManager
import com.app.ulife.creatoron.models.CommonUserIdReq
import com.app.ulife.creatoron.models.EmptyRequest
import com.app.ulife.creatoron.models.Obj
import com.app.ulife.creatoron.models.UserIdObj
import com.app.ulife.creatoron.models.checkout.CheckoutOrderReq
import com.app.ulife.creatoron.models.checkout.CheckoutOrderRes
import com.app.ulife.creatoron.models.getCity.GetCityReq
import com.app.ulife.creatoron.models.getCity.GetCityRes
import com.app.ulife.creatoron.models.getState.GetStateRes
import com.app.ulife.creatoron.models.shippingDetails.add.AddShippiDetailsReq
import com.app.ulife.creatoron.models.shippingDetails.add.AddShippiDetailsRes
import com.app.ulife.creatoron.models.shippingDetails.get.Data
import com.app.ulife.creatoron.models.shippingDetails.get.ShippDetailsRes
import com.app.ulife.creatoron.models.wallet.WalletReq
import com.app.ulife.creatoron.models.wallet.balance.GetWalletBalRes
import com.app.ulife.creatoron.ui.userTypeActivities.customer.cartActivity.BagActivity
import com.app.ulife.creatoron.ui.userTypeActivities.customer.mainActivity.MainActivity
import com.app.ulife.creatoron.utils.onDebouncedListener
import com.app.ulife.creatoron.utils.snackbar
import com.app.ulife.creatoron.utils.toast
import com.app.ulife.creatoron.viewModels.SharedVM
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class CheckoutFragment : Fragment(), KodeinAware, AddressList2Adapter.OnItemClickListener {
    private lateinit var binding: FragmentCheckoutBinding
    override val kodeinContext = kcontext<Fragment>(this)
    override val kodein: Kodein by kodein()
    private val factory: SharedVMF by instance()
    private lateinit var viewModel: SharedVM
    private lateinit var preferenceManager: PreferenceManager
    var stateId = ""
    var cityId = ""
    var addressId = ""
    var paymentMode = ""
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var addAddressSheetDialog: BottomSheetDialog
    private val addressList = mutableListOf<Data>()
    val stateListName = mutableListOf<String>()
    val stateListId = mutableListOf<String>()
    val cityListName = mutableListOf<String>()
    val cityListId = mutableListOf<String>()
    lateinit var actCity: AutoCompleteTextView
    lateinit var cityAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager = PreferenceManager.instance
        viewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
        setupViews()
        listeners()
        hitApi()
    }

    private fun setupViews() {
        binding.apply {
            tvDiscount.text = "" + arguments?.getString("discount")
            tvMrp.text = "" + arguments?.getString("mrp")
            tvFinalPrice.text = "" + arguments?.getString("finalAmt")

            rgPayModes.setOnCheckedChangeListener { radioGroup, i ->
                paymentMode = when (radioGroup.checkedRadioButtonId) {
                    R.id.rb_online -> "Online"
                    R.id.rb_cod -> "COD"
                    R.id.rb_wallet -> "Wallet"
                    else -> ""
                }
            }
        }
    }

    private fun listeners() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }
            btnPickAddr.setOnClickListener {
                openAddressPicker()
            }
            btnRemovePickupAddr.setOnClickListener {
                btnPickAddr.visibility = View.VISIBLE
                cardPickContainer.visibility = View.GONE
                addressId = ""
            }

            btnConfirmOrder.onDebouncedListener {
                when {
                    addressId.isNullOrEmpty() -> root.snackbar("Please select address")
                    paymentMode.isNullOrEmpty() -> root.snackbar("Please select payment method")
                    else -> checkoutOrder(
                        CheckoutOrderReq(
                            apiname = "PlaceOrder",
                            obj = com.app.ulife.creatoron.models.checkout.Obj(
                                BillingAddress = "$addressId",
                                PayMode = "$paymentMode",
                                UserId = "" + preferenceManager.userid
                            )
                        )
                    )
                }
            }
        }
    }

    private fun checkoutOrder(req: CheckoutOrderReq) {
        viewModel.checkoutOrder = MutableLiveData()
        viewModel.checkoutOrder.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, CheckoutOrderRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            when (response.data[0].id) {
                                1 -> {
                                    val dialog = Dialog(requireContext())
                                    dialog.setCancelable(false)
                                    dialog.setContentView(R.layout.dialog_header_success)
                                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                                    val lp = WindowManager.LayoutParams()
                                    lp.copyFrom(dialog.window?.attributes)
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT

                                    dialog.findViewById<MaterialButton>(R.id.btn_close)
                                        .setOnClickListener {
                                            activity?.finish()
                                            dialog.dismiss()
                                        }

                                    dialog.findViewById<TextView>(R.id.tv_amount).text =
                                        "Order successfully placed of ₹ " + tvFinalPrice.text
                                    dialog.show()
                                    dialog.window?.attributes = lp

                                }

                                else -> context?.toast("${response.data[0]?.msg}")
                            }
                        }
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.checkoutOrder(req)
    }

    private fun hitApi() {
        getShippingDetails(
            CommonUserIdReq(
                apiname = "GetShippingDetails",
                obj = UserIdObj(userId = "" + preferenceManager.userid)
            )
        )
        getStateList(EmptyRequest(apiname = "Proc_GetAllStateList", obj = Obj()))

        getWalletBalance(
            WalletReq(
                apiname = "GetWalletBalance", obj = com.app.ulife.creatoron.models.wallet.Obj(
                    datatype = "T",
                    transactiontype = "",
                    userid = "" + preferenceManager.userid,
                    wallettype = "F"
                )
            )
        )
    }

    private fun getShippingDetails(req: CommonUserIdReq) {
        viewModel.userIdRequestCommon = MutableLiveData()
        viewModel.userIdRequestCommon.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, ShippDetailsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            addressList.addAll(response.data)
                        }
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.userIdRequestCommon(req)
    }

    private fun openAddressPicker() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_select_address)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val btnClose = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_close)
        btnClose?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        val btnAddAddr = bottomSheetDialog.findViewById<MaterialButton>(R.id.btn_add_address)
        btnAddAddr?.setOnClickListener {
            bottomSheetDialog.dismiss()
            openAddAddressSheet()
        }

        val rvAddrList = bottomSheetDialog.findViewById<RecyclerView>(R.id.rv_addr_list)
        rvAddrList?.apply {
            adapter = AddressList2Adapter(context, addressList, this@CheckoutFragment)
        }
        bottomSheetDialog.show()
    }

    override fun onItemClick(response: Data) {
        bottomSheetDialog.dismiss()
        binding.apply {
            btnPickAddr.visibility = View.GONE
            cardPickContainer.visibility = View.VISIBLE

            addressId = "" + response.Id
            tvPickupAddress.text =
                "" + response.FullAddress + ", " + response.StateId + ", " + response.CityId
            tvPickupPincode.text = getString(R.string.pincode) + " : " + response.PinCode
            tvPickupMobile.text = "Mobile : +91-" + response.Phone
            tvPickupName.text = "${response.FirstName} ${response.LastName}"
        }
    }

    private fun openAddAddressSheet() {
        addAddressSheetDialog = BottomSheetDialog(requireContext())
        addAddressSheetDialog.setContentView(R.layout.bottom_sheet_add_address)
        addAddressSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        addAddressSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val bottomSheet: FrameLayout =
            addAddressSheetDialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        // Height of the view
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        addAddressSheetDialog.findViewById<MaterialButton>(R.id.btn_close)?.setOnClickListener {
            addAddressSheetDialog.dismiss()
        }

        val actState = addAddressSheetDialog.findViewById<AutoCompleteTextView>(R.id.act_state)
        actCity = addAddressSheetDialog.findViewById(R.id.act_city)!!

        // state adapter
        val stateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            stateListName
        )
        actState?.setAdapter(stateAdapter)
        actState?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                stateId = stateListId[position]
                getCityList(
                    GetCityReq(
                        apiname = "BindCity",
                        obj = com.app.ulife.creatoron.models.getCity.Obj(StateId = "$stateId")
                    )
                )
            }

        // city adapter
        cityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            cityListName
        )
        actCity?.setAdapter(cityAdapter)
        actCity?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                cityId = cityListId[position]
            }

        val etFName = addAddressSheetDialog.findViewById<EditText>(R.id.et_first_name)
        val etLName = addAddressSheetDialog.findViewById<EditText>(R.id.et_last_name)
        val etNumber = addAddressSheetDialog.findViewById<EditText>(R.id.et_number)
        val etAddr = addAddressSheetDialog.findViewById<EditText>(R.id.et_address)
        val etPincode = addAddressSheetDialog.findViewById<EditText>(R.id.et_pincode)

        addAddressSheetDialog.findViewById<MaterialButton>(R.id.btn_save)?.setOnClickListener {
            when {
                etFName?.text.toString().isEmpty() -> etFName?.error = "Please enter first name"
                etLName?.text.toString().isEmpty() -> etLName?.error = "Please enter first name"
                etNumber?.text.toString().isEmpty() -> etNumber?.error = "Please enter number"
                etAddr?.text.toString().isEmpty() -> etAddr?.error = "Please enter address"
                actState?.text.toString().isEmpty() -> actState?.error = "Please select state"
                actCity?.text.toString().isEmpty() -> actCity?.error = "Please select state"
                etPincode?.text.toString().isEmpty() -> etPincode?.error = "Please enter pincode"
                else -> {
                    addShippingDetails(
                        AddShippiDetailsReq(
                            apiname = "InsertShippingDetails",
                            obj = com.app.ulife.creatoron.models.shippingDetails.add.Obj(
                                cityid = cityId,
                                companyname = "",
                                email = "",
                                firstname = "" + etFName?.text,
                                fulladdress = "" + etAddr?.text,
                                lastname = "" + etLName?.text,
                                phone = "" + etNumber?.text,
                                pincode = "" + etPincode?.text,
                                stateid = stateId,
                                streetadd = "",
                                townadd = "",
                                userid = "" + preferenceManager.userid
                            )
                        )
                    )
                }
            }
        }

        addAddressSheetDialog.show()
    }

    private fun addShippingDetails(req: AddShippiDetailsReq) {
        viewModel.addShippingDetails = MutableLiveData()
        viewModel.addShippingDetails.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, AddShippiDetailsRes::class.java)
                if (response != null) {
                    if (response.status) {
                        when (response.data[0].id) {
                            1 -> {
                                addAddressSheetDialog.dismiss()
                                context?.toast("${response.data[0]?.msg}")
                                getShippingDetails(
                                    CommonUserIdReq(
                                        apiname = "GetShippingDetails",
                                        obj = UserIdObj(userId = "" + preferenceManager.userid)
                                    )
                                )
                            }

                            else -> context?.toast("${response.data[0]?.msg}")
                        }
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.addShippingDetails(req)
    }

    private fun getStateList(req: EmptyRequest) {
        viewModel.getStateList = MutableLiveData()
        viewModel.getStateList.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetStateRes::class.java)
                if (response != null) {
                    if (response.status) {
                        stateListName.clear()
                        stateListId.clear()

                        for (i in response.data.indices) {
                            stateListName.add("" + response.data[i].StateName)
                            stateListId.add("" + response.data[i].Id)
                        }
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getStateList(req)
    }

    private fun getCityList(req: GetCityReq) {
        viewModel.getCityList = MutableLiveData()
        viewModel.getCityList.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetCityRes::class.java)
                if (response != null) {
                    if (response.status) {
                        cityListName.clear()
                        cityListId.clear()

                        for (i in response.data.indices) {
                            cityListName.add("" + response.data[i].Text)
                            cityListId.add("" + response.data[i].Value)
                        }

                        cityAdapter.setNotifyOnChange(true)
                        cityAdapter.notifyDataSetChanged()
                    } else {
                        (activity as BagActivity).apiErrorDialog(response.message)
                    }
                } else {
                    (activity as BagActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                (activity as BagActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getCityList(req)
    }

    private fun getWalletBalance(req: WalletReq) {
        viewModel.getWalletBalance = MutableLiveData()
        viewModel.getWalletBalance.observe(requireActivity()) {
            try {
                val response = Gson().fromJson(it, GetWalletBalRes::class.java)
                if (response != null) {
                    if (response.status) {
                        binding.apply {
                            rbWallet.text = "Wallet (\u20B9${response.data[0]?.Balance})"
                        }
                    } else {
                        binding.apply {
                            rbWallet.text = "Wallet (\u20B90.0)"
                        }
                        (activity as MainActivity).apiErrorDialog(response.message)
                    }
                } else {
                    binding.apply {
                        rbWallet.text = "Wallet (\u20B90.0)"
                    }
                    (activity as MainActivity).apiErrorDialog(Constants.apiErrors)
                }
            } catch (e: Exception) {
                binding.apply {
                    rbWallet.text = "Wallet (\u20B90.0)"
                }
                (activity as MainActivity).apiErrorDialog("$it\n$e")
            }
        }
        viewModel.getWalletBalance(req)
    }
}