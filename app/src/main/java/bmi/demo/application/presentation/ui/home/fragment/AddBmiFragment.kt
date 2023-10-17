package bmi.demo.application.presentation.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import bmi.demo.application.BuildConfig
import bmi.demo.application.R
import bmi.demo.application.databinding.FragmentAddBmiBinding
import bmi.demo.application.presentation.ui.home.viewmodel.BmiViewModel
import bmi.demo.application.utils.Constants.MAX_VALUE_0
import bmi.demo.application.utils.Constants.MAX_VALUE_1
import bmi.demo.application.utils.Constants.MAX_VALUE_100
import bmi.demo.application.utils.Constants.MAX_VALUE_200
import bmi.demo.application.utils.clickWithDebounce
import bmi.demo.application.utils.snack
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * This class represents a fragment for adding BMI (Body Mass Index) details.
 * It allows users to input information such as name, weight, height, and gender to calculate their BMI.
 * Additionally, it displays an interstitial ad and provides navigation functionality.
 */
class AddBmiFragment : Fragment() {

    private val bmiViewModel: BmiViewModel by activityViewModels()
    private lateinit var mBinding: FragmentAddBmiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentAddBmiBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        initInterstitialAd()
        backPress()
        clickEvent()
        initNumberPicker()
    }

    /**
     * Initialize the toolbar title in the layout
     */
    private fun initData() {
        mBinding.apply {
            toolbar.tvToolbarTitle.text = getText(R.string.add_bmi_details)
        }
    }

    /**
     * Initialize an interstitial ad for display
     */
    private fun initInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireActivity(),
            BuildConfig.INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    bmiViewModel.mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    bmiViewModel.mInterstitialAd = interstitialAd
                }
            },
        )
    }

    /**
     * Set up click events for the Calculate button and Back button
     */
    private fun clickEvent() {
        mBinding.apply {
            btnCalculate.clickWithDebounce {
                // Get the name from the input field
                bmiViewModel.name = edName.text.toString()
                if (bmiViewModel.name.isNotEmpty()) {
                    // Show the interstitial ad and calculate BMI
                    showInterstitialAd()
                    bmiViewModel.calculateBmi()
                } else {
                    mBinding.root.snack(getString(R.string.enter_your_name))
                }
            }
            toolbar.ivBack.clickWithDebounce {
                // Finish the current activity
                requireActivity().finish()
            }
        }
    }

    /**
     * Initialize the number pickers for weight, height, and gender
     */
    private fun initNumberPicker() {
        mBinding.apply {
            npWeight.apply {
                minValue = MAX_VALUE_1
                maxValue = MAX_VALUE_100
                wrapSelectorWheel = false
                displayedValues = bmiViewModel.weightHeightList()
                npWeight.value = bmiViewModel.weight.toInt()
            }
            npHeight.apply {
                minValue = MAX_VALUE_1
                maxValue = MAX_VALUE_200
                wrapSelectorWheel = false
                displayedValues = bmiViewModel.weightHeightList()
                npHeight.value = bmiViewModel.height.toInt()
            }
            npGender.apply {
                minValue = MAX_VALUE_0
                maxValue = MAX_VALUE_1
                wrapSelectorWheel = false
                displayedValues = bmiViewModel.pickerGender
            }
            npWeight.setOnValueChangedListener { picker, _, _ ->
                bmiViewModel.weight = bmiViewModel.weightHeightList()[picker.value]
            }
            npHeight.setOnValueChangedListener { picker, _, _ ->
                bmiViewModel.height = bmiViewModel.weightHeightList()[picker.value]
            }
            npGender.setOnValueChangedListener { picker, _, _ ->
                bmiViewModel.gender = bmiViewModel.pickerGender[picker.value]
            }
        }
    }

    /**
     * Handle the back button press to navigate back
     */
    private fun backPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }

    /**
     * Show the interstitial ad and handle its events
     */
    private fun showInterstitialAd() {
        bmiViewModel.mInterstitialAd?.show(requireActivity())
        bmiViewModel.mInterstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    findNavController().navigate(R.id.bmiDetailsFragment)
                }

                override fun onAdDismissedFullScreenContent() {
                    bmiViewModel.mInterstitialAd = null
                    findNavController().navigate(R.id.bmiDetailsFragment)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    bmiViewModel.mInterstitialAd = null
                    findNavController().navigate(R.id.bmiDetailsFragment)
                }

                override fun onAdImpression() {
                }

                override fun onAdShowedFullScreenContent() {
                }
            }
    }
}
