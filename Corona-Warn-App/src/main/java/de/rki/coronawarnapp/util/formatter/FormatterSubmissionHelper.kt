@file:JvmName("FormatterSubmissionHelper")

package de.rki.coronawarnapp.util.formatter

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import de.rki.coronawarnapp.CoronaWarnApplication
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.submission.StartOfSymptoms
import de.rki.coronawarnapp.submission.SymptomIndication
import de.rki.coronawarnapp.ui.submission.ApiRequestState
import de.rki.coronawarnapp.util.DeviceUIState
import de.rki.coronawarnapp.util.TimeAndDateExtensions.toUIFormat
import java.util.Date

fun formatButtonStyleByState(currentState: SymptomIndication?, state: SymptomIndication?): Int =
    formatColor(currentState == state, R.color.colorTextSixteenWhite, R.color.colorStableDark)

fun formatBackgroundButtonStyleByState(currentState: SymptomIndication?, state: SymptomIndication?): Int =
    formatColor(currentState == state, R.color.colorTextSemanticNeutral, R.color.colorSurface2)

fun formatCalendarButtonStyleByState(currentState: StartOfSymptoms?, state: StartOfSymptoms?): Int =
    formatColor(currentState == state, R.color.colorTextSixteenWhite, R.color.colorStableDark)

fun formatCalendarBackgroundButtonStyleByState(currentState: StartOfSymptoms?, state: StartOfSymptoms?): Int =
    formatColor(currentState == state, R.color.colorTextSemanticNeutral, R.color.colorSurface2)

fun isEnableButtonByState(currentState: SymptomIndication?): Boolean {
    return currentState != null
}

fun formatTestResultSpinnerVisible(uiStateState: ApiRequestState?): Int =
    formatVisibility(uiStateState != ApiRequestState.SUCCESS)

fun formatTestResultVisible(uiStateState: ApiRequestState?): Int =
    formatVisibility(uiStateState == ApiRequestState.SUCCESS)

fun formatSubmitButtonEnabled(apiRequestState: ApiRequestState) =
    apiRequestState == ApiRequestState.IDLE || apiRequestState == ApiRequestState.FAILED

fun formatSubmitSpinnerVisible(apiRequestState: ApiRequestState) =
    formatVisibility(apiRequestState == ApiRequestState.STARTED)

fun formatTestResultStatusText(uiState: DeviceUIState?): String {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getString(R.string.test_result_card_status_negative)
        DeviceUIState.PAIRED_POSITIVE,
        DeviceUIState.PAIRED_POSITIVE_TELETAN -> appContext.getString(R.string.test_result_card_status_positive)
        else -> appContext.getString(R.string.test_result_card_status_invalid)
    }
}

fun formatTestResultStatusColor(uiState: DeviceUIState?): Int {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getColor(R.color.colorTextSemanticGreen)
        DeviceUIState.PAIRED_POSITIVE,
        DeviceUIState.PAIRED_POSITIVE_TELETAN -> appContext.getColor(R.color.colorTextSemanticRed)
        else -> appContext.getColor(R.color.colorTextSemanticRed)
    }
}

fun formatTestResult(uiState: DeviceUIState?): Spannable {
    val appContext = CoronaWarnApplication.getAppContext()
    return SpannableStringBuilder()
        .append(appContext.getString(R.string.test_result_card_virus_name_text))
        .append("\n")
        .append(
            formatTestResultStatusText(uiState),
            ForegroundColorSpan(formatTestResultStatusColor(uiState)),
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
}

fun formatTestResultCardContent(uiState: DeviceUIState?): Spannable {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NO_RESULT ->
            SpannableString(appContext.getString(R.string.test_result_card_status_pending))
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED ->
            SpannableString(appContext.getString(R.string.test_result_card_status_invalid))

        DeviceUIState.PAIRED_POSITIVE,
        DeviceUIState.PAIRED_POSITIVE_TELETAN,
        DeviceUIState.PAIRED_NEGATIVE -> formatTestResult(uiState)
        else -> SpannableString("")
    }
}

fun formatTestStatusIcon(uiState: DeviceUIState?): Drawable? {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NO_RESULT -> appContext.getDrawable(R.drawable.ic_test_result_illustration_pending)
        DeviceUIState.PAIRED_POSITIVE_TELETAN,
        DeviceUIState.PAIRED_POSITIVE -> appContext.getDrawable(R.drawable.ic_test_result_illustration_positive)
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getDrawable(R.drawable.ic_test_result_illustration_negative)
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED -> appContext.getDrawable(R.drawable.ic_test_result_illustration_invalid)
        else -> appContext.getDrawable(R.drawable.ic_test_result_illustration_invalid)
    }
}

fun formatTestResultRegisteredAtText(registeredAt: Date?): String {
    val appContext = CoronaWarnApplication.getAppContext()
    return appContext.getString(R.string.test_result_card_registered_at_text)
        .format(registeredAt?.toUIFormat(appContext))
}

fun formatTestResultPendingStepsVisible(uiState: DeviceUIState?): Int =
    formatVisibility(uiState == DeviceUIState.PAIRED_NO_RESULT)

fun formatTestResultNegativeStepsVisible(uiState: DeviceUIState?): Int =
    formatVisibility(uiState == DeviceUIState.PAIRED_NEGATIVE)

fun formatTestResultPositiveStepsVisible(uiState: DeviceUIState?): Int =
    formatVisibility(uiState == DeviceUIState.PAIRED_POSITIVE || uiState == DeviceUIState.PAIRED_POSITIVE_TELETAN)

fun formatTestResultInvalidStepsVisible(uiState: DeviceUIState?): Int =
    formatVisibility(uiState == DeviceUIState.PAIRED_ERROR || uiState == DeviceUIState.PAIRED_REDEEMED)

fun formatSubmissionStatusCardSubtitleColor(uiState: DeviceUIState?): Int {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getColor(R.color.colorTextSemanticGreen)
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED -> appContext.getColor(R.color.colorTextSemanticNeutral)
        else -> appContext.getColor(R.color.colorTextPrimary1)
    }
}

fun formatSubmissionStatusCardSubtitleText(uiState: DeviceUIState?): String {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getString(R.string.submission_status_card_subtitle_negative)
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED -> appContext.getString(R.string.submission_status_card_subtitle_invalid)
        else -> ""
    }
}

fun formatSubmissionStatusCardContentTitleText(uiState: DeviceUIState?): String {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED,
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getString(R.string.submission_status_card_title_available)
        DeviceUIState.PAIRED_NO_RESULT -> appContext.getString(R.string.submission_status_card_title_pending)
        else -> appContext.getString(R.string.submission_status_card_title_pending)
    }
}

fun formatSubmissionStatusCardContentBodyText(uiState: DeviceUIState?): String {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED -> appContext.getString(R.string.submission_status_card_body_invalid)
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getString(R.string.submission_status_card_body_negative)
        DeviceUIState.PAIRED_NO_RESULT -> appContext.getString(R.string.submission_status_card_body_pending)
        else -> appContext.getString(R.string.submission_status_card_body_pending)
    }
}

fun formatSubmissionStatusCardContentStatusTextVisible(uiState: DeviceUIState?): Int {
    return when (uiState) {
        DeviceUIState.PAIRED_NEGATIVE,
        DeviceUIState.PAIRED_REDEEMED,
        DeviceUIState.PAIRED_ERROR -> View.VISIBLE
        else -> View.GONE
    }
}

fun formatSubmissionStatusCardContentIcon(uiState: DeviceUIState?): Drawable? {
    val appContext = CoronaWarnApplication.getAppContext()
    return when (uiState) {
        DeviceUIState.PAIRED_NO_RESULT -> appContext.getDrawable(R.drawable.ic_main_illustration_pending)
        DeviceUIState.PAIRED_POSITIVE,
        DeviceUIState.PAIRED_POSITIVE_TELETAN -> appContext.getDrawable(R.drawable.ic_main_illustration_pending)
        DeviceUIState.PAIRED_NEGATIVE -> appContext.getDrawable(R.drawable.ic_main_illustration_negative)
        DeviceUIState.PAIRED_ERROR,
        DeviceUIState.PAIRED_REDEEMED -> appContext.getDrawable(R.drawable.ic_main_illustration_invalid)
        else -> appContext.getDrawable(R.drawable.ic_main_illustration_invalid)
    }
}

fun formatSubmissionStatusCardFetchingVisible(
    deviceRegistered: Boolean?,
    uiStateState: ApiRequestState?
): Int = formatVisibility(
    deviceRegistered == true && (
            uiStateState == ApiRequestState.STARTED ||
                    uiStateState == ApiRequestState.FAILED)
)

fun formatSubmissionStatusCardUnregisteredVisible(
    deviceRegistered: Boolean?
): Int = formatVisibility(deviceRegistered == false)

fun formatSubmissionStatusCardContentVisible(
    deviceUiState: DeviceUIState?
): Int = formatVisibility(
    deviceUiState == DeviceUIState.PAIRED_ERROR ||
            deviceUiState == DeviceUIState.PAIRED_NEGATIVE ||
            deviceUiState == DeviceUIState.PAIRED_NO_RESULT ||
            deviceUiState == DeviceUIState.PAIRED_REDEEMED
)

fun formatShowSubmissionStatusPositiveCard(deviceUiState: DeviceUIState?): Int =
    formatVisibility(
        deviceUiState == DeviceUIState.PAIRED_POSITIVE ||
                deviceUiState == DeviceUIState.PAIRED_POSITIVE_TELETAN
    )

fun formatShowSubmissionDoneCard(deviceUiState: DeviceUIState?): Int =
    formatVisibility(deviceUiState == DeviceUIState.SUBMITTED_FINAL)

fun formatShowRiskStatusCard(deviceUiState: DeviceUIState?): Int =
    formatVisibility(
        deviceUiState != DeviceUIState.PAIRED_POSITIVE &&
                deviceUiState != DeviceUIState.PAIRED_POSITIVE_TELETAN &&
                deviceUiState != DeviceUIState.SUBMITTED_FINAL
    )
