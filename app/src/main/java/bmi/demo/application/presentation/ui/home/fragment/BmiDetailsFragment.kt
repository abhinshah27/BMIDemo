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
import bmi.demo.application.databinding.FragmentBmiDetailsBinding
import bmi.demo.application.databinding.LayoutNativeAdBinding
import bmi.demo.application.presentation.ui.home.viewmodel.BmiViewModel
import bmi.demo.application.utils.clickWithDebounce
import bmi.demo.application.utils.diffFontSize
import bmi.demo.application.utils.openPlayStoreApp
import bmi.demo.application.utils.saveScreenshotToCache
import bmi.demo.application.utils.shareScreenshot
import bmi.demo.application.utils.show
import bmi.demo.application.utils.takeScreenshot
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

/**
 * This class represents a fragment for displaying BMI (Body Mass Index) details.
 * It is responsible for showing the calculated BMI value and related information, as well as handling ad loading,
 * user interactions, and navigation within the app.
 */
class BmiDetailsFragment : Fragment() {

    private val viewModel: BmiViewModel by activityViewModels()
    private lateinit var mBinding: FragmentBmiDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentBmiDetailsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        loadAnAds()
        backPress()
        clickEvent()
        observeValue()
    }

    /**
     * Initialize the toolbar title in the layout
     */
    private fun initData() {
        mBinding.apply {
            toolbar.tvToolbarTitle.text = getText(R.string.bmi_details)
        }
    }

    /**
     * Observe changes in the BMI value and update the UI accordingly.
     */
    private fun observeValue() {
        viewModel.bmiLiveData.observe(viewLifecycleOwner) {
            it?.let { bmiResultData ->
                val value = bmiResultData.bmiValue.split(".")
                if (value.isNotEmpty() && value.size == 2){
                    mBinding.tvBimValue.text = bmiResultData.bmiValue.diffFontSize(".${value[1]}",80)
                }
                mBinding.tvBimResult.text = bmiResultData.resultText
            }
        }
    }

    /**
     * Load native ads and display them in the UI.
     */
    private fun loadAnAds() {
        val adLoader = AdLoader.Builder(requireActivity(), BuildConfig.NATIVE_AD_ID)
            .forNativeAd { ad: NativeAd ->
                val binding = LayoutNativeAdBinding.inflate(layoutInflater)
                binding.adTitleTextView.text = ad.headline
                binding.adBodyTextView.text = ad.body
                binding.adIconImageView.setImageDrawable(ad.icon?.drawable)
                mBinding.clNativeAdd.addView(binding.llLayoutLoadsAds.rootView)
                mBinding.clNativeAdd.show()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle ad loading failure
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    /**
     * Set up click event handlers for UI elements such as back navigation, sharing, and app rating.
     */
    private fun clickEvent() {
        mBinding.apply {
            toolbar.ivBack.clickWithDebounce {
                findNavController().popBackStack()
            }
            tvShare.clickWithDebounce {
                val screenshotBitmap = root.rootView.takeScreenshot()
                val screenshotFile = requireContext().saveScreenshotToCache(screenshotBitmap)
                requireContext().shareScreenshot(screenshotFile)
            }
            tvRate.clickWithDebounce {
                requireActivity().openPlayStoreApp()
            }
        }
    }

    /**
     * Handle the back press action by navigating back within the fragment.
     */
    private fun backPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }
}
